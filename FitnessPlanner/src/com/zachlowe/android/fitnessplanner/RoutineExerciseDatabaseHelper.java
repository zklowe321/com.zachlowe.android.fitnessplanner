package com.zachlowe.android.fitnessplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RoutineExerciseDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "RoutineExerciseDatabaseHelper";

	private static final String DB_NAME = "routine_exercises.sqlite";
	private static final int VERSION = 1;
	
	private static final String TABLE_ROUTINE_EXERCISE = "routine_exercises";
	private static final String COLUMN_ROUTINE_ID = "r_id";
	private static final String COLUMN_EXERCISE_ID = "e_id";
	private static final String COLUMN_SETS = "sets";
	private static final String COLUMN_REPS = "reps";
	
	public RoutineExerciseDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the 'routine_exercise' table
		db.execSQL("create table routine_exercises (" +
			"_id integer primary key, r_id integer, e_id integer, sets integer, reps integer)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	// Insert new entry into routine_exercise database
	public long insertRoutineExercise(RoutineExercise routineExercise) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ROUTINE_ID, routineExercise.getRoutineId());
		cv.put(COLUMN_EXERCISE_ID, routineExercise.getId());
		cv.put(COLUMN_SETS, routineExercise.getSets());
		cv.put(COLUMN_REPS, routineExercise.getReps());
		
		Log.d(TAG, "Inserting new routine exercise into database");
		
		return getWritableDatabase().insert(TABLE_ROUTINE_EXERCISE, null, cv);
	}
	
	// Update an existing routineExercise
	public int updateRoutineExercise(RoutineExercise routineExercise) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ROUTINE_ID, routineExercise.getRoutineId());
		cv.put(COLUMN_EXERCISE_ID, routineExercise.getId());
		cv.put(COLUMN_SETS, routineExercise.getSets());
		cv.put(COLUMN_REPS, routineExercise.getReps());
		
		Log.d(TAG, "Updating routine exercise in database");
		
		return getWritableDatabase().update(TABLE_ROUTINE_EXERCISE, cv, "r_id=? and e_id=?",
				new String[]{ Long.toString(routineExercise.getRoutineId()),
					Long.toString(routineExercise.getId()) });
	}
	
	// Delete a routineExercise by removing it from the database
	public int deleteRoutineExercise(RoutineExercise routineExercise) {
		long r_id = routineExercise.getRoutineId();
		long e_id = routineExercise.getId();
		return getWritableDatabase().delete(TABLE_ROUTINE_EXERCISE, 
				"r_id=? and e_id=?",
				new String[]{ String.valueOf(r_id), String.valueOf(e_id) });
	}
	
	public RoutineExerciseCursor queryRoutineExercises() {
		// Equivalent to "select * from routine_exercises order by title"
		Cursor wrapped = getReadableDatabase().query(TABLE_ROUTINE_EXERCISE,
				null, null, null, null, null, COLUMN_ROUTINE_ID + " asc");
		return new RoutineExerciseCursor(wrapped);
	}
	
	public RoutineExerciseCursor queryRoutineExercise(long _id) {
		Cursor wrapped = getReadableDatabase().query(TABLE_ROUTINE_EXERCISE,
				null, // All columns
				"_id = ?", // Look for a routine and exercise ID
				new String[]{ String.valueOf(_id) }, // with this value
				null,	// group by
				null,	// order by
				null,	// having
				"1");	// limit one row
		return new RoutineExerciseCursor(wrapped);
	}
	
	/**
	 * A convenience class to wrap a cursor that returns rows from the routine_exercise table.
	 */
	public static class RoutineExerciseCursor extends CursorWrapper {
		
		public RoutineExerciseCursor(Cursor c) {
			super(c);
		}
		
		/**
		 * Returns a RoutineExercise object configured for the current row,
		 * or null if the row is invalid
		 */
		public RoutineExercise getRoutineExercise() {
			if (isBeforeFirst() || isAfterLast())
				return null;
			
			RoutineExercise routineExercise = new RoutineExercise( 
					getLong(getColumnIndex(COLUMN_ROUTINE_ID)), 
					getLong(getColumnIndex(COLUMN_EXERCISE_ID)) );
			//routineExercise.setId( exercise.getId());
			
			//routineExercise.setTitle( exercise.getTitle() );
			//routineExercise.setDescription( exercise.getDescription() );
			
			int sets = getInt(getColumnIndex(COLUMN_SETS));
			routineExercise.setSets(sets);
			
			int reps = getInt(getColumnIndex(COLUMN_REPS));
			routineExercise.setReps(reps);
			
			return routineExercise;
		}
	}

}
