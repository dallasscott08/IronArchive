package com.blue.ironarchivev1.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.blue.ironarchivev1.models.Mobility;
import com.blue.ironarchivev1.models.WorkoutItem;

public class MobilityDAO extends DBManager implements WorkoutItemDAO{

	int routineId = 1;
	static String[] allColumns = new String[] {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME,
			DatabaseHelper.KEY_REPS, DatabaseHelper.KEY_TIME,
			DatabaseHelper.KEY_DELAY, DatabaseHelper.KEY_SETNUMBER, DatabaseHelper.KEY_ROUTINEID};
	static String likeItemsQuery = "SELECT * FROM " + DatabaseHelper.TABLE_MOBILITY +
			" WHERE " + DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_ID +
			" IN (SELECT " + DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_ID + " FROM " + DatabaseHelper.TABLE_MOBILITY +
			" INNER JOIN " + DatabaseHelper.TABLE_ROUTINE +
			" ON " + DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_ROUTINEID + "=" + DatabaseHelper.TABLE_ROUTINE + "." + DatabaseHelper.KEY_ID +
			" WHERE " + DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_ID + "!=? AND " +
			DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_NAME + "=? AND " +
			DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_TIME + "=? AND " +
			DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_DELAY + "=? AND " +
			DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_REPS + "=? AND " +
			DatabaseHelper.TABLE_MOBILITY + "." + DatabaseHelper.KEY_SETNUMBER + "=?)";

	public MobilityDAO(Context ctx, int rID) {
		super(ctx);
		routineId = rID;
	}

	public MobilityDAO(Context ctx) {
		super(ctx);
	}
	
	public long addMobilityItem(Mobility m) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(DatabaseHelper.KEY_NAME, m.getName());
        initialValues.put(DatabaseHelper.KEY_REPS, m.getReps());
        initialValues.put(DatabaseHelper.KEY_TIME, m.getTime());
        initialValues.put(DatabaseHelper.KEY_DELAY, m.getHasDelay());
        initialValues.put(DatabaseHelper.KEY_SETNUMBER, this.getLikeItemCount(m.getName()));
        initialValues.put(DatabaseHelper.KEY_ROUTINEID, routineId);
        
        return mDb.insert(DatabaseHelper.TABLE_MOBILITY, null, initialValues);
    }
	
	public Mobility getWorkoutItem(int rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_MOBILITY, allColumns, DatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(rowId)},
                null, null, null, null);
		if (mCursor != null && mCursor.getCount() >= 1) {
            mCursor.moveToFirst();
            Mobility mobility = cursorToItem(mCursor);
            return mobility;
        }
		
        return new Mobility();
	}

	public boolean updateWorkoutItem(WorkoutItem m) {
        ContentValues args = new ContentValues();
        
        args.put(DatabaseHelper.KEY_NAME, m.getName());
        args.put(DatabaseHelper.KEY_REPS, ((Mobility) m).getReps());
        args.put(DatabaseHelper.KEY_TIME, ((Mobility) m).getTime());
        args.put(DatabaseHelper.KEY_DELAY, ((Mobility) m).getHasDelay());
        args.put(DatabaseHelper.KEY_SETNUMBER, ((Mobility) m).getSet());
        
        return mDb.update(DatabaseHelper.TABLE_MOBILITY, args, DatabaseHelper.KEY_ID + "=" + ((Mobility) m).getId(), null) > 0;
    }
	
	public boolean deleteWorkoutItem(WorkoutItem m) {
		return mDb.delete(DatabaseHelper.TABLE_MOBILITY, DatabaseHelper.KEY_ID + "=" + ((Mobility) m).getId(), null) > 0;
	}
	
	public List<WorkoutItem> getAllItems() {
    	List<WorkoutItem> items = new ArrayList<WorkoutItem>();
    	
    	Cursor mCursor = mDb.query(DatabaseHelper.TABLE_MOBILITY, allColumns, DatabaseHelper.KEY_ROUTINEID + "=?", new String[] {String.valueOf(routineId)}, null, null, null);
    			
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Mobility item = cursorToItem(mCursor);
          items.add(item);
          mCursor.moveToNext();
        }

        mCursor.close();
        return items;
    }
	
	private Mobility cursorToItem(Cursor cursor) {
		Mobility item = new Mobility();
    	
		if(cursor.getInt(0) != 0)
			item.setId(cursor.getInt(0));
		
		if(cursor.getString(1) != null)
			item.setName(cursor.getString(1));
		
		if(cursor.getInt(2) != 0)
			item.setReps(cursor.getInt(2));
		
		if(cursor.getLong(3) != 0)
			item.setTime(cursor.getLong(3));
		
		item.setHasDelay(cursor.getInt(4));
		
		item.setSet(cursor.getInt(5));
		
		if(cursor.getInt(6) != 0)
			item.setRoutineId(cursor.getInt(6));
    	
        return item;
    }
	
	private int getLikeItemCount(String itemName) throws SQLException {
		Cursor mCursor = mDb.query(DatabaseHelper.TABLE_MOBILITY
			, new String[] {DatabaseHelper.KEY_NAME}
			, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=?"
			, new String[] {itemName, String.valueOf(routineId)}
			, null, null, null);
        
        return mCursor.getCount()+1;
	}

	@Override
	public List<WorkoutItem> getRoutineDuplicates(WorkoutItem oldValues) {
		List<WorkoutItem> items = new ArrayList<WorkoutItem>();
		Cursor mCursor = mDb.rawQuery(likeItemsQuery,
				new String[]{String.valueOf(oldValues.getId()),
						oldValues.getName(),
						String.valueOf(((Mobility) oldValues).getTime()),
						String.valueOf(((Mobility) oldValues).getHasDelay()),
						String.valueOf(((Mobility) oldValues).getReps()),
						String.valueOf(oldValues.getSet())});

		if (mCursor != null && mCursor.getCount() >= 1) {
			while (mCursor.moveToNext()) {
				Mobility item = cursorToItem(mCursor);
				items.add(item);
			}
		}

		mCursor.close();
		return items;
	}

	public void updateLinkedItems(WorkoutItem oldItem, WorkoutItem newItem)
	{
		List<WorkoutItem> linkedStretches = getRoutineDuplicates(oldItem);
		for(WorkoutItem mobility: linkedStretches){
			((Mobility) mobility).setReps(((Mobility) newItem).getReps());
			((Mobility) mobility).setHasDelay(((Mobility) newItem).getHasDelay());
			((Mobility) mobility).setTime(((Mobility) newItem).getTime());
			mobility.setSet(newItem.getSet());
			updateWorkoutItem(mobility);
		}
	}
	
	public void decreaseListSetNumbersAfterModify(WorkoutItem i){
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_MOBILITY
				, allColumns
				, DatabaseHelper.KEY_NAME + "=? AND " + DatabaseHelper.KEY_ROUTINEID + "=? AND " + DatabaseHelper.KEY_SETNUMBER + ">?"
				, new String[] {i.getName(), String.valueOf(routineId), String.valueOf(i.getSet())}
				, null, null, null, null);
		
		if (mCursor != null && mCursor.getCount() >= 1) {
			while(mCursor.moveToNext()){
				WorkoutItem item = cursorToItem(mCursor);
				item.setSet(item.getSet()-1);
				this.updateWorkoutItem(item);
			}
        }
	}
	
	public void increaseListSetNumbersAfterModify(WorkoutItem i){
		Cursor itemsAboveThisOne = mDb.query(DatabaseHelper.TABLE_MOBILITY
				, new String[] {DatabaseHelper.KEY_NAME}
				, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=? AND " + DatabaseHelper.KEY_ID + "<?"
				, new String[] {i.getName(), String.valueOf(routineId), String.valueOf(i.getId())}
				, null, null, null);
		
		i.setSet(itemsAboveThisOne.getCount()+1);
		this.updateWorkoutItem(i);
		
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_MOBILITY
				, allColumns
				, DatabaseHelper.KEY_NAME + "=? AND " + DatabaseHelper.KEY_ROUTINEID + "=? AND " + DatabaseHelper.KEY_ID +">?"
				, new String[] {i.getName(), String.valueOf(routineId), String.valueOf(i.getId())}
				, null, null, null, null);
		
		if (mCursor != null && mCursor.getCount() >= 1) {
			while(mCursor.moveToNext()){
				WorkoutItem item = cursorToItem(mCursor);
				item.setSet(item.getSet()+1);
				this.updateWorkoutItem(item);
			}
        }
	}
}
