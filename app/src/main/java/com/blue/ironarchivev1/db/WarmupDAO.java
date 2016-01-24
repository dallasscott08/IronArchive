package com.blue.ironarchivev1.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.blue.ironarchivev1.models.Warmup;
import com.blue.ironarchivev1.models.WorkoutItem;

public class WarmupDAO extends DBManager implements WorkoutItemDAO{

	int routineId = 1;
	String[] allColumns = new String[] {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME, 
			DatabaseHelper.KEY_REPS, DatabaseHelper.KEY_TIME, 
			DatabaseHelper.KEY_DELAY, DatabaseHelper.KEY_SETNUMBER, DatabaseHelper.KEY_ROUTINEID};

	public WarmupDAO(Context ctx, int rID){
		super(ctx);
		routineId = rID;
	}
	
	public WarmupDAO(Context ctx){
		super(ctx);
	}
	
	public long addWarmupItem(Warmup w) {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(DatabaseHelper.KEY_NAME, w.getName());
        initialValues.put(DatabaseHelper.KEY_REPS, w.getReps());
        initialValues.put(DatabaseHelper.KEY_TIME, w.getTime());
        initialValues.put(DatabaseHelper.KEY_DELAY, w.getHasDelay());
        initialValues.put(DatabaseHelper.KEY_SETNUMBER, this.getLikeItemCount(w.getName()));
        initialValues.put(DatabaseHelper.KEY_ROUTINEID, routineId);
       
        return mDb.insert(DatabaseHelper.TABLE_WARMUP, null, initialValues);
    }
	
	public Warmup getWorkoutItem(int rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_WARMUP, allColumns, DatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(rowId)},
                null, null, null, null);
		if (mCursor != null && mCursor.getCount() >= 1) {
            mCursor.moveToFirst();
            Warmup warmup = cursorToItem(mCursor);
            return warmup;
        }
       
		return new Warmup();        
	}

	public boolean updateWorkoutItem(WorkoutItem w) {
        ContentValues args = new ContentValues();
        
        args.put(DatabaseHelper.KEY_NAME, w.getName());
        args.put(DatabaseHelper.KEY_REPS, ((Warmup) w).getReps());
        args.put(DatabaseHelper.KEY_TIME, ((Warmup) w).getTime());
        args.put(DatabaseHelper.KEY_DELAY, ((Warmup) w).getHasDelay());
        args.put(DatabaseHelper.KEY_SETNUMBER, ((Warmup) w).getSet());

        return mDb.update(DatabaseHelper.TABLE_WARMUP, args, DatabaseHelper.KEY_ID + "=" + ((Warmup) w).getId(), null) > 0;
    }
	
	public boolean deleteWorkoutItem(WorkoutItem w) {
		return mDb.delete(DatabaseHelper.TABLE_WARMUP, DatabaseHelper.KEY_ID + "=" + ((Warmup) w).getId(), null) > 0;
    }
	
	public List<WorkoutItem> getAllItems() {
    	List<WorkoutItem> items = new ArrayList<WorkoutItem>();
            
    	Cursor mCursor = mDb.query(DatabaseHelper.TABLE_WARMUP, allColumns, DatabaseHelper.KEY_ROUTINEID + "=?", new String[] {String.valueOf(routineId)}, null, null, null);
    			
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Warmup item = cursorToItem(mCursor);
          items.add(item);
          mCursor.moveToNext();
        }

        mCursor.close();
        return items;
    }
	
	private Warmup cursorToItem(Cursor cursor) {
		Warmup item = new Warmup();
    	
		if(cursor.getInt(0) != 0)
			item.setId(cursor.getInt(0));
		
		if(cursor.getString(1) != null)
			item.setName(cursor.getString(1));
		
		if(cursor.getInt(2) != 0)
			item.setReps(cursor.getInt(2));
		
		if(cursor.getLong(3) >= 1)
			item.setTime(cursor.getLong(3));
		
		item.setHasDelay(cursor.getInt(4));
        
		item.setSet(cursor.getInt(5));
		
		if(cursor.getInt(6) != 0)
			item.setRoutineId(cursor.getInt(6));
    	
        return item;
    }
	
	private int getLikeItemCount(String itemName) throws SQLException {
		Cursor mCursor = mDb.query(DatabaseHelper.TABLE_WARMUP
			, new String[] {DatabaseHelper.KEY_NAME}
			, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=?"
			, new String[] {itemName, String.valueOf(routineId)}
			, null, null, null);
        
        return mCursor.getCount()+1;
	}
	
	public void decreaseListSetNumbersAfterModify(WorkoutItem i){
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_WARMUP
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
		Cursor itemsAboveThisOne = mDb.query(DatabaseHelper.TABLE_WARMUP
				, new String[] {DatabaseHelper.KEY_NAME}
				, DatabaseHelper.KEY_NAME + " =? AND " + DatabaseHelper.KEY_ROUTINEID + "=? AND " + DatabaseHelper.KEY_ID + "<?"
				, new String[] {i.getName(), String.valueOf(routineId), String.valueOf(i.getId())}
				, null, null, null);
		
		i.setSet(itemsAboveThisOne.getCount()+1);
		this.updateWorkoutItem(i);
		
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_WARMUP
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