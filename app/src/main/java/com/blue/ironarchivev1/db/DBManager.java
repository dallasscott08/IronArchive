package com.blue.ironarchivev1.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {	
	
	private final Context mContext;
	private DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;
	
	public DBManager(Context ctx){
		this.mContext = ctx;
		mDbHelper = DatabaseHelper.getHelper(mContext);
		open();
	}
	
	public void open() throws SQLException {
		if(mDbHelper == null)
			mDbHelper = DatabaseHelper.getHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }
    
}
