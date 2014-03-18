package com.zachlowe.android.fitnessplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RoutineDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "RoutineDatabaseHelper";

	private static final String DB_NAME = "routines.sqlite";
	private static final int VERSION = 1;
	
	private static final String TABLE_ROUTINE = "routine";
	private static final String COLUMN_ROUTINE_ID = "_id";
	private static final String COLUMN_ROUTINE_TITLE = "title";
	private static final String COLUMN_ROUTINE_DESCRIPTION = "description";
	private static final String COLUMN_PLACEHOLDER = " ";
	
	public RoutineDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the 'routine' table
		db.execSQL("create table routine (" +
			"_id integer primary key autoincrement, title text, description text)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	// Insert newly created empty routine
	public long insertRoutine(Routine routine) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ROUTINE_TITLE, COLUMN_PLACEHOLDER);
		cv.put(COLUMN_ROUTINE_DESCRIPTION, COLUMN_PLACEHOLDER);
		
		Log.d(TAG, "Inserting new routine into database");
		
		return getWritableDatabase().insert(TABLE_ROUTINE, null, cv);
	}
	
	// Update an existing routine
	public int updateRoutine(Routine routine ) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ROUTINE_TITLE, routine.getTitle());
		cv.put(COLUMN_ROUTINE_DESCRIPTION, routine.getDescription());
		
		Log.d(TAG, "Updating existing routine in database");
		
		return getWritableDatabase().update(TABLE_ROUTINE, cv, "_id=?",
				new String[]{Long.toString(routine.getId())});
	}
	
	// Delete a routine by removing it from the database
	public int deleteRoutine(Routine routine) {
		long id = routine.getId();
		return getWritableDatabase().delete(TABLE_ROUTINE, 
				COLUMN_ROUTINE_ID + " = ?",
				new String[]{ String.valueOf(id) });
	}
	
	public RoutineCursor queryRoutines() {
		// Equivalent to "select * from routine order by title"
		Cursor wrapped = getReadableDatabase().query(TABLE_ROUTINE,
				null, null, null, null, null, COLUMN_ROUTINE_TITLE + " asc");
		return new RoutineCursor(wrapped);
	}
	
	public RoutineCursor queryRoutine(long id) {
		Cursor wrapped = getReadableDatabase().query(TABLE_ROUTINE,
				null, // All columns
				COLUMN_ROUTINE_ID + " = ?", // Look for a routine ID
				new String[]{ String.valueOf(id) }, // with this value
				null,	// group by
				null,	// order by
				null,	// having
				"1");	// limit one row
		return new RoutineCursor(wrapped);
	}
	
	/**
	 * A convenience class to wrap a cursor that returns rows from the routine table.
	 */
	public static class RoutineCursor extends CursorWrapper {
		
		public RoutineCursor(Cursor c) {
			super(c);
		}
		
		/**
		 * Returns an Exercise object configured for the current row,
		 * or null if the row is invalid
		 */
		public Routine getRoutine() {
			if (isBeforeFirst() || isAfterLast())
				return null;
			
			Routine routine = new Routine();
			long routineId = getLong(getColumnIndex(COLUMN_ROUTINE_ID));
			routine.setId(routineId);
			
			String routineTitle = getString(getColumnIndex(COLUMN_ROUTINE_TITLE));
			routine.setTitle(routineTitle);
			
			String routineDescription = getString(getColumnIndex(COLUMN_ROUTINE_DESCRIPTION));
			routine.setDescription(routineDescription);
			
			return routine;
		}
	}
}
