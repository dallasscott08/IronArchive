package com.blue.ironarchivev1.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.blue.ironarchivev1.models.Stretch;
import com.blue.ironarchivev1.models.WorkoutItem;
//import com.blue.ironarchivev1.util.WorkoutItemUtils;

public class StretchDAO extends DBManager implements WorkoutItemDAO{

	int routineId;
	static String[] allColumns = new String[] {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME,
			DatabaseHelper.KEY_TIME, DatabaseHelper.KEY_DELAY, 
			DatabaseHelper.KEY_SETNUMBER, DatabaseHelper.KEY_ROUTINEID};
	static String likeItemsQuery = "SELECT * FROM " + DatabaseHelper.TABLE_STRETCH +
			" WHERE " + DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_ID +
			" IN (SELECT " + DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_ID + " FROM " + DatabaseHelper.TABLE_STRETCH +
			" INNER JOIN " + DatabaseHelper.TABLE_ROUTINE +
			" ON " + DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_ROUTINEID + "=" + DatabaseHelper.TABLE_ROUTINE + "." + DatabaseHelper.KEY_ID +
			" WHERE " + DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_ID + "!=? AND " +
	DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_NAME + "=? AND " +
	DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_TIME + "=? AND " +
	DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_DELAY + "=? AND " +
	DatabaseHelper.TABLE_STRETCH + "." + DatabaseHelper.KEY_SETNUMBER + "=?)";

	public StretchDAO(Context ctx, int rID){
		super(ctx);
		routineId = rID;
	}
	
	public StretchDAO(Context ctx){
		super(ctx);
	}
	
	public long addStretchItem(Stretch s) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(DatabaseHelper.KEY_NAME, s.getName());
        initialValues.put(DatabaseHelper.KEY_TIME, s.getTime());
        initialValues.put(DatabaseHelper.KEY_DELAY, s.getHasDelay());
        initialValues.put(DatabaseHelper.KEY_SETNUMBER, this.getLikeItemCount(s.getName()));
        initialValues.put(DatabaseHelper.KEY_ROUTINEID, routineId);

        return mDb.insert(DatabaseHelper.TABLE_STRETCH, null, initialValues);
    }
	
	public Stretch getWorkoutItem(int rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_STRETCH, allColumns, DatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(rowId)}, null,
                null, null, null);
		if (mCursor != null && mCursor.getCount() >= 1) {
            mCursor.moveToFirst();
            Stretch stretch = cursorToItem(mCursor);
            return stretch;
        }
        
        return new Stretch();
	}
	
	public boolean updateWorkoutItem(WorkoutItem s) {
        ContentValues args = new ContentValues();
                
        args.put(DatabaseHelper.KEY_NAME, s.getName());
        args.put(DatabaseHelper.KEY_TIME, ((Stretch) s).getTime());
        args.put(DatabaseHelper.KEY_DELAY, ((Stretch) s).getHasDelay());
        args.put(DatabaseHelper.KEY_SETNUMBER, ((Stretch) s).getSet());
                
        return mDb.update(DatabaseHelper.TABLE_STRETCH, args, DatabaseHelper.KEY_ID + "=" + ((Stretch) s).getId(), null) > 0;
    }
	
	public boolean deleteWorkoutItem(WorkoutItem s) {
		return mDb.delete(DatabaseHelper.TABLE_STRETCH, DatabaseHelper.KEY_ID + "=" + ((Stretch) s).getId(), null) > 0;
	}
	
	public List<WorkoutItem> getAllItems() {		
    	List<WorkoutItem> stretches = new ArrayList<WorkoutItem>();
    	
    	Cursor mCursor = mDb.query(DatabaseHelper.TABLE_STRETCH, allColumns, DatabaseHelper.KEY_ROUTINEID + "=?", new String[] {String.valueOf(routineId)}, null, null, null);
    	
    	mCursor.moveToFirst();
    	
    	while (!mCursor.isAfterLast()) {
    		Stretch item = cursorToItem(mCursor);
    		stretches.add(item);
    		mCursor.moveToNext();
    	}    	

        mCursor.close();
        return stretches;
    }
	
	private Stretch cursorToItem(Cursor cursor) {
		Stretch stretch = new Stretch();
    	
		if(cursor.getInt(0) != 0)
			stretch.setId(cursor.getInt(0));
		
		if(cursor.getString(1) != null)
			stretch.setName(cursor.getString(1));
		
		if(cursor.getLong(2) != 0)
			stretch.setTime(cursor.getLong(2));
		
		stretch.setHasDelay(cursor.getInt(3));
        
		stretch.setSet(cursor.getInt(4));
		
		if(cursor.getInt(5) != 0)
			stretch.setRoutineId(cursor.getInt(5));
        
        return stretch;
    }
	
	private int getLikeItemCount(String itemName) throws SQLException {
		Cursor mCursor = mDb.query(DatabaseHelper.TABLE_STRETCH
			, new String[] {DatabaseHelper.KEY_NAME}
			, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=?"
			, new String[] {itemName, String.valueOf(routineId)}
			, null, null, null);
        
        return mCursor.getCount()+1;
	}

	public List<WorkoutItem> getRoutineDuplicates(WorkoutItem oldValues) throws SQLException {
		List<WorkoutItem> items = new ArrayList<WorkoutItem>();
		Cursor mCursor = mDb.rawQuery(likeItemsQuery,
				new String[]{String.valueOf(oldValues.getId()),
						oldValues.getName(),
				String.valueOf(((Stretch) oldValues).getTime()),
				String.valueOf(((Stretch) oldValues).getHasDelay()),
				String.valueOf(oldValues.getSet())});

		if (mCursor != null && mCursor.getCount() >= 1) {
			while (mCursor.moveToNext()) {
				Stretch item = cursorToItem(mCursor);
				items.add(item);
			}
		}

		mCursor.close();
		return items;
	}

	public void updateLinkedItems(WorkoutItem oldItem, WorkoutItem newItem)
	{
		List<WorkoutItem> linkedStretches = getRoutineDuplicates(oldItem);
		for(WorkoutItem stretch: linkedStretches){
			((Stretch) stretch).setHasDelay(((Stretch) newItem).getHasDelay());
			((Stretch) stretch).setTime(((Stretch) newItem).getTime());
			stretch.setSet(newItem.getSet());
			updateWorkoutItem(stretch);
		}
	}
	
	public void decreaseListSetNumbersAfterModify(WorkoutItem i){
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_STRETCH
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
		Cursor itemsAboveThisOne = mDb.query(DatabaseHelper.TABLE_STRETCH
				, new String[] {DatabaseHelper.KEY_NAME}
				, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=? AND " + DatabaseHelper.KEY_ID + "<?"
				, new String[] {i.getName(), String.valueOf(routineId), String.valueOf(i.getId())}
				, null, null, null);
		
		i.setSet(itemsAboveThisOne.getCount()+1);
		this.updateWorkoutItem(i);
		
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_STRETCH
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