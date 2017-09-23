package com.blue.ironarchivev1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public static final String KEY_ID = "_ID";
	public static final String KEY_ROUTINEID = "Routine_ID";
	
	public static final String TABLE_WARMUP = "Warmup";
	public static final String TABLE_MOBILITY = "Mobility";
	public static final String TABLE_STRETCH = "Stretches";
	public static final String TABLE_LIFT = "Lifts";
	public static final String TABLE_ROUTINE = "Routine";

	public static final String KEY_DELAY = "Delay";
	public static final String KEY_LINKEDROUTINEID = "LinkedRoutine_ID";
	public static final String KEY_NAME = "Name";
	public static final String KEY_OLYMPICBAR = "OlympicBar";
	public static final String KEY_TIME = "Time";
	public static final String KEY_REPS = "Reps";
	public static final String KEY_RESTTIME = "RestTime";
	public static final String KEY_SETNUMBER = "SetNumber";
	public static final String KEY_WEIGHT = "Weight";
	
	private static final String DATABASE_NAME = "myWorkout";
	private static final int DATABASE_VERSION = 1;
	private static final String CREATE_ROUTINE_TABLE =
	        "CREATE TABLE IF NOT EXISTS " + TABLE_ROUTINE + " (" + KEY_ID + " INTEGER PRIMARY KEY"
			+ KEY_NAME + " TEXT, "
	        + KEY_LINKEDROUTINEID + " INTEGER);";
	
	private static final String CREATE_WARMUP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WARMUP + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
			+ KEY_NAME + " TEXT, "
			+ KEY_REPS + " INTEGER, "
	        + KEY_TIME + " INTEGER, "
			+ KEY_DELAY + " INTEGER, "
			+ KEY_SETNUMBER + " INTEGER, "
	        + KEY_ROUTINEID + " INTEGER"
	        + ", FOREIGN KEY(" + KEY_ROUTINEID + ") REFERENCES "+ TABLE_ROUTINE +"(" + KEY_ID + "));";
	
	private static final String CREATE_MOBILITY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOBILITY + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
	        + KEY_NAME + " TEXT, "
			+ KEY_REPS + " INTEGER, "
	        + KEY_TIME + " INTEGER, "
			+ KEY_DELAY + " INTEGER, "
			+ KEY_SETNUMBER + " INTEGER, "
			+ KEY_ROUTINEID + " INTEGER"
	        + ", FOREIGN KEY(" + KEY_ROUTINEID + ") REFERENCES "+ TABLE_ROUTINE +"(" + KEY_ID + "));";
	
	private static final String CREATE_STRETCHES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STRETCH + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
	        + KEY_NAME + " TEXT, "
			+ KEY_TIME + " INTEGER, "
	        + KEY_DELAY + " INTEGER, "
			+ KEY_SETNUMBER + " INTEGER, "
			+ KEY_ROUTINEID + " INTEGER"
	        + ", FOREIGN KEY(" + KEY_ROUTINEID + ") REFERENCES "+ TABLE_ROUTINE +"(" + KEY_ID + "));";
	
	//SQLite only takes 1 or 0 for boolean that's why I'm using it this way
	private static final String CREATE_LIFTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LIFT + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
	        + KEY_NAME + " TEXT, "
			+ KEY_WEIGHT + " REAL, "
	        + KEY_REPS + " INTEGER, "
			+ KEY_RESTTIME + " INTEGER, "
	        + KEY_TIME + " INTEGER, "
			+ KEY_DELAY + " INTEGER, "
			+ KEY_SETNUMBER + " INTEGER, "
			+ KEY_OLYMPICBAR + " INTEGER, "
			+ KEY_ROUTINEID + " INTEGER"
	        + ", FOREIGN KEY(" + KEY_ROUTINEID + ") REFERENCES "+ TABLE_ROUTINE +"(" + KEY_ID + "));";
	
	private static DatabaseHelper instance;
	
	public static synchronized DatabaseHelper getHelper(Context context) {
		if (instance == null)
			instance = new DatabaseHelper(context);
		return instance;
	}
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ROUTINE_TABLE);		
		db.execSQL(CREATE_WARMUP_TABLE);		
		db.execSQL(CREATE_MOBILITY_TABLE);
		db.execSQL(CREATE_STRETCHES_TABLE);	
		db.execSQL(CREATE_LIFTS_TABLE);
		
		ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.KEY_NAME, "Default");
        db.insert(TABLE_ROUTINE, null, initialValues);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("DBManager", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WARMUP + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOBILITY + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRETCH + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIFT + ";");
        onCreate(db);
	}
}
