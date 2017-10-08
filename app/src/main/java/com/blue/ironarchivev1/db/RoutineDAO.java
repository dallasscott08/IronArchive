package com.blue.ironarchivev1.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.blue.ironarchivev1.models.Routine;

public class RoutineDAO extends DBManager {
	
	String[] allColumns = new String[] {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_LINKEDROUTINEID};

	public RoutineDAO(Context ctx){
		super(ctx);
	}
	
	public long addRoutineItem(Routine s) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(DatabaseHelper.KEY_NAME, s.getName());

        return mDb.insert(DatabaseHelper.TABLE_ROUTINE, null, initialValues);
    }
	
	public Routine getWorkoutItem(int rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DatabaseHelper.TABLE_ROUTINE, allColumns, DatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(rowId)},
                null, null, null, null);
		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
		Routine routine = cursorToItem(mCursor);
		
        return routine;
	}
	
	public boolean updateWorkoutItem(Routine rt) {
        ContentValues args = new ContentValues();

        args.put(DatabaseHelper.KEY_NAME, rt.getName());
		args.put(DatabaseHelper.KEY_LINKEDROUTINEID, rt.getLinkedRoutineId());

        return mDb.update(DatabaseHelper.TABLE_ROUTINE, args, DatabaseHelper.KEY_ID + "=" +  rt.getId(), null) > 0;
    }
	
	public boolean deleteWorkoutItem(Routine rt) {
		mDb.delete(DatabaseHelper.TABLE_WARMUP, DatabaseHelper.KEY_ROUTINEID + "=" + rt.getId(), null);
		mDb.delete(DatabaseHelper.TABLE_MOBILITY, DatabaseHelper.KEY_ROUTINEID + "=" + rt.getId(), null);
		mDb.delete(DatabaseHelper.TABLE_STRETCH, DatabaseHelper.KEY_ROUTINEID + "=" + rt.getId(), null);
		mDb.delete(DatabaseHelper.TABLE_LIFT, DatabaseHelper.KEY_ROUTINEID + "=" + rt.getId(), null);
        return mDb.delete(DatabaseHelper.TABLE_ROUTINE, DatabaseHelper.KEY_ID + "=" + rt.getId(), null) > 0;
    }
	
	public List<Routine> getAllItems() {
    	List<Routine> routines = new ArrayList<Routine>();
    	
    	Cursor mCursor = mDb.query(DatabaseHelper.TABLE_ROUTINE, allColumns, null, null, null, null, null);
    	
    	mCursor.moveToFirst();
    	
    	while (!mCursor.isAfterLast()) {
    		Routine item = cursorToItem(mCursor);
    		routines.add(item);
    		mCursor.moveToNext();
    	}    	

        mCursor.close();
        return routines;
    }
	
	private Routine cursorToItem(Cursor cursor) {
		Routine routine = new Routine();
    	
		routine.setId(cursor.getInt(0));
		routine.setName(cursor.getString(1));
		routine.setLinkedRoutineId(cursor.getInt(2));
        
        return routine;
    }

}
