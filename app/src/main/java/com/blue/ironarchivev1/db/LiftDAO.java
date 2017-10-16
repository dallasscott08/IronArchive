package com.blue.ironarchivev1.db;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.preference.PreferenceManager;

import com.blue.ironarchivev1.models.Lift;
import com.blue.ironarchivev1.models.WorkoutItem;

public class LiftDAO extends DBManager implements WorkoutItemDAO{

	int routineId = 1;
	static String[] allColumns = new String[] {DatabaseHelper.KEY_ID,
			DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_WEIGHT,
			DatabaseHelper.KEY_REPS, DatabaseHelper.KEY_TIME, 
			DatabaseHelper.KEY_RESTTIME, DatabaseHelper.KEY_DELAY, 
			DatabaseHelper.KEY_SETNUMBER, DatabaseHelper.KEY_OLYMPICBAR, DatabaseHelper.KEY_ROUTINEID};
	static String likeItemsQuery = "SELECT * FROM " + DatabaseHelper.TABLE_LIFT +
			" WHERE " + DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_ID +
			" IN (SELECT " + DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_ID + " FROM " + DatabaseHelper.TABLE_LIFT +
			" INNER JOIN " + DatabaseHelper.TABLE_ROUTINE +
			" ON " + DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_ROUTINEID + "=" + DatabaseHelper.TABLE_ROUTINE + "." + DatabaseHelper.KEY_ID +
			" WHERE " + DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_ID + "!=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_NAME + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_TIME + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_DELAY + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_REPS + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_WEIGHT + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_RESTTIME + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_OLYMPICBAR + "=? AND " +
			DatabaseHelper.TABLE_LIFT + "." + DatabaseHelper.KEY_SETNUMBER + "=?)";
	SharedPreferences settings;
	String system;
	
	public LiftDAO(Context ctx, int rID) {
		super(ctx);
		settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		routineId = rID;
	}

	public LiftDAO(Context ctx) {
		super(ctx);
	}
	
	public long addLiftItem(Lift l) {
        ContentValues initialValues = new ContentValues();
               
        system = settings.getString("PREF_MEASUREMENT_SYSTEM", "imp");
        
        initialValues.put(DatabaseHelper.KEY_NAME, l.getName());
        if(system.equals("met")){
        	initialValues.put(DatabaseHelper.KEY_WEIGHT, ((Lift) l).getWeight() * 2.2046);
        }
        else{
        	initialValues.put(DatabaseHelper.KEY_WEIGHT, l.getWeight());
        }
        initialValues.put(DatabaseHelper.KEY_REPS, l.getReps());
        initialValues.put(DatabaseHelper.KEY_TIME, l.getTime());
        initialValues.put(DatabaseHelper.KEY_RESTTIME, l.getRestTime());
        initialValues.put(DatabaseHelper.KEY_DELAY, l.getHasDelay());
        initialValues.put(DatabaseHelper.KEY_SETNUMBER, this.getLikeItemCount(l.getName()));
        initialValues.put(DatabaseHelper.KEY_OLYMPICBAR, l.getUsesOlympicBar());
        initialValues.put(DatabaseHelper.KEY_ROUTINEID, routineId);
       
        return mDb.insert(DatabaseHelper.TABLE_LIFT, null, initialValues);
    }
	
	public Lift getWorkoutItem(int rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_LIFT, allColumns, DatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(rowId)},
                null, null, null, null);
		if (mCursor != null && mCursor.getCount() >= 1) {
            mCursor.moveToFirst();
            Lift lift = cursorToItem(mCursor);
            return lift;
        }	
		
        return new Lift();
	}
	
	public boolean updateWorkoutItem(WorkoutItem l) {
        ContentValues args = new ContentValues();
        
        system = settings.getString("PREF_MEASUREMENT_SYSTEM", "imp");
               
        args.put(DatabaseHelper.KEY_NAME, l.getName());
        if(system.equals("met")){
        	args.put(DatabaseHelper.KEY_WEIGHT, ((Lift) l).getWeight() * 2.2046);
        }
        else{
            args.put(DatabaseHelper.KEY_WEIGHT, ((Lift) l).getWeight());
        }
        args.put(DatabaseHelper.KEY_REPS, ((Lift) l).getReps());
        args.put(DatabaseHelper.KEY_TIME, ((Lift) l).getTime());
        args.put(DatabaseHelper.KEY_RESTTIME, ((Lift) l).getRestTime());
        args.put(DatabaseHelper.KEY_DELAY, ((Lift) l).getHasDelay());
        args.put(DatabaseHelper.KEY_SETNUMBER, ((Lift) l).getSet());

        return mDb.update(DatabaseHelper.TABLE_LIFT, args, DatabaseHelper.KEY_ID + "=" + ((Lift) l).getId(), null) > 0;
    }
	
	public boolean deleteWorkoutItem(WorkoutItem l) {
		return mDb.delete(DatabaseHelper.TABLE_LIFT, DatabaseHelper.KEY_ID + "=" + ((Lift) l).getId(), null) > 0;		
    }
	
	public List<WorkoutItem> getAllItems() {
    	List<WorkoutItem> items = new ArrayList<WorkoutItem>();
    		       
    	Cursor mCursor = mDb.query(DatabaseHelper.TABLE_LIFT, allColumns, DatabaseHelper.KEY_ROUTINEID + "=?", new String[] {String.valueOf(routineId)}, null, null, null);
    			
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Lift item = cursorToItem(mCursor);
          items.add(item);
          mCursor.moveToNext();
        }

        mCursor.close();
        return items;
    }
	
	private Lift cursorToItem(Cursor cursor) {
		Lift lift = new Lift();
        system = settings.getString("PREF_MEASUREMENT_SYSTEM", "imp");
    	
       	if(cursor.getInt(0) != 0)
       		lift.setId(cursor.getInt(0));
       	
       	if(cursor.getString(1) != null)
       		lift.setName(cursor.getString(1));
       	
       	if(cursor.getDouble(2) >= 1){
       		DecimalFormat df = new DecimalFormat("###.#");
       		if(system.equals("met")){          	
            	lift.setWeight(Double.parseDouble(df.format(cursor.getDouble(2) / 2.2046)));
            }
            else{
            	lift.setWeight(Double.parseDouble(df.format(cursor.getDouble(2))));
            }
       	}
       	
       	if(cursor.getInt(3) != 0)
       		lift.setReps(cursor.getInt(3));
       	
       	if(cursor.getLong(4) != 0)
       		lift.setTime(cursor.getLong(4));
       	
       	if(cursor.getLong(5) != 0)
       		lift.setRestTime(cursor.getLong(5));
   		
       	lift.setHasDelay(cursor.getInt(6));
        
       	lift.setSet(cursor.getInt(7));
       	
       	lift.setUsesOlympicBar(cursor.getInt(8));
       	
       	if(cursor.getInt(9) != 0)
        	lift.setRoutineId(cursor.getInt(9));
    	
        return lift;
    }
	
	private int getLikeItemCount(String itemName) throws SQLException {
		Cursor mCursor = mDb.query(DatabaseHelper.TABLE_LIFT
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
						String.valueOf(((Lift) oldValues).getTime()),
						String.valueOf(((Lift) oldValues).getHasDelay()),
						String.valueOf(((Lift) oldValues).getReps()),
						String.valueOf(((Lift) oldValues).getWeight()),
						String.valueOf(((Lift) oldValues).getRestTime()),
						String.valueOf(((Lift) oldValues).getUsesOlympicBar()),
						String.valueOf((oldValues).getSet())});

		if (mCursor != null && mCursor.getCount() >= 1) {
			while (mCursor.moveToNext()) {
				Lift item = cursorToItem(mCursor);
				items.add(item);
			}
		}

		mCursor.close();
		return items;
	}

	public void updateLinkedItems(WorkoutItem oldItem, WorkoutItem newItem)
	{
		List<WorkoutItem> linkedStretches = getRoutineDuplicates(oldItem);
		for(WorkoutItem lift: linkedStretches){
			((Lift) lift).setWeight(((Lift) newItem).getWeight());
			((Lift) lift).setReps(((Lift) newItem).getReps());
			((Lift) lift).setHasDelay(((Lift) newItem).getHasDelay());
			((Lift) lift).setTime(((Lift) newItem).getTime());
			((Lift) lift).setRestTime(((Lift) newItem).getRestTime());
			((Lift) lift).setUsesOlympicBar(((Lift) newItem).getUsesOlympicBar());
			lift.setSet(newItem.getSet());
			updateWorkoutItem(lift);
		}
	}
	
	public void decreaseListSetNumbersAfterModify(WorkoutItem i){
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_LIFT
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
		Cursor itemsAboveThisOne = mDb.query(DatabaseHelper.TABLE_LIFT
				, new String[] {DatabaseHelper.KEY_NAME}
				, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=? AND " + DatabaseHelper.KEY_ID + "<?"
				, new String[] {i.getName(), String.valueOf(routineId), String.valueOf(i.getId())}
				, null, null, null);
		
		i.setSet(itemsAboveThisOne.getCount()+1);
		this.updateWorkoutItem(i);
		
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_LIFT
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