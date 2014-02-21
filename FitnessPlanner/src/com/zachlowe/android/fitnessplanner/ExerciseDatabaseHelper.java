package com.zachlowe.android.fitnessplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExerciseDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "ExerciseDatabaseHelper";
	
	private static final String DB_NAME = "exercises.sqlite";
	private static final int VERSION = 1;
	
	private static final String TABLE_EXERCISE = "exercise";
	private static final String COLUMN_EXERCISE_ID = "_id";
	private static final String COLUMN_EXERCISE_TITLE = "title";
	private static final String COLUMN_EXERCISE_DESCRIPTION = "description";
	private static final String COLUMN_PLACEHOLDER = " ";
	
	public ExerciseDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the 'exercise' table
		db.execSQL("create table exercise (" +
			"_id integer primary key autoincrement, title text, description text)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	// Insert newly created empty exercise
	public long insertExercise(Exercise exercise) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_EXERCISE_TITLE, COLUMN_PLACEHOLDER);
		cv.put(COLUMN_EXERCISE_DESCRIPTION, COLUMN_PLACEHOLDER);
		
		Log.d(TAG, "Inserting new exercise into database");
		
		return getWritableDatabase().insert(TABLE_EXERCISE, null, cv);
	}
	
	// Update an existing exercise
	public int updateExercise(Exercise exercise) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_EXERCISE_TITLE, exercise.getTitle());
		cv.put(COLUMN_EXERCISE_DESCRIPTION, exercise.getDescription());
		
		Log.d(TAG, "Updating existing exercise in database");
		
		return getWritableDatabase().update(TABLE_EXERCISE, cv, "_id=?",
				new String[]{Long.toString(exercise.getId())});
	}
	
	// Delete an exercise by removing it from the database
	public int deleteExercise(Exercise exercise) {
		long id = exercise.getId();
		return getWritableDatabase().delete(TABLE_EXERCISE, 
				COLUMN_EXERCISE_ID + " = ?",
				new String[]{ String.valueOf(id) });
	}
	
	public ExerciseCursor queryExercises() {
		// Equivalent to "select * from exercise order by title"
		Cursor wrapped = getReadableDatabase().query(TABLE_EXERCISE,
				null, null, null, null, null, COLUMN_EXERCISE_TITLE + " asc");
		return new ExerciseCursor(wrapped);
	}
	
	public ExerciseCursor queryExercise(long id) {
		Cursor wrapped = getReadableDatabase().query(TABLE_EXERCISE,
				null, // All columns
				COLUMN_EXERCISE_ID + " = ?", // Look for an exercise ID
				new String[]{ String.valueOf(id) }, // with this value
				null,	// group by
				null,	// order by
				null,	// having
				"1");	// limit one row
		return new ExerciseCursor(wrapped);
	}
	
	/**
	 * A convenience class to wrap a cursor that returns rows from the exercise table.
	 */
	public static class ExerciseCursor extends CursorWrapper {
		
		public ExerciseCursor(Cursor c) {
			super(c);
		}
		
		/**
		 * Returns an Exercise object configured for the current row,
		 * or null if the row is invalid
		 */
		public Exercise getExercise() {
			if (isBeforeFirst() || isAfterLast())
				return null;
			
			Exercise exercise = new Exercise();
			long exerciseId = getLong(getColumnIndex(COLUMN_EXERCISE_ID));
			exercise.setId(exerciseId);
			
			String exerciseTitle = getString(getColumnIndex(COLUMN_EXERCISE_TITLE));
			exercise.setTitle(exerciseTitle);
			
			String exerciseDescription = getString(getColumnIndex(COLUMN_EXERCISE_DESCRIPTION));
			exercise.setDescription(exerciseDescription);
			
			return exercise;
		}
	}
}