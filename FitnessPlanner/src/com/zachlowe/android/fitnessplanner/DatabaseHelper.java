package com.zachlowe.android.fitnessplanner;

import com.zachlowe.android.fitnessplanner.ExerciseDatabaseHelper.ExerciseCursor;
import com.zachlowe.android.fitnessplanner.RoutineDatabaseHelper.RoutineCursor;
import com.zachlowe.android.fitnessplanner.RoutineExerciseDatabaseHelper.RoutineExerciseCursor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";

	private static final String DB_NAME = "fitnessplanner.sqlite";
	private static final int VERSION = 1;
	
	/** 	Exercise table constants */
	private static final String TABLE_EXERCISE = "exercise";
	private static final String COLUMN_EXERCISE_ID = "_id";
	private static final String COLUMN_EXERCISE_TITLE = "title";
	private static final String COLUMN_EXERCISE_DESCRIPTION = "description";
	private static final String COLUMN_PLACEHOLDER = " ";
	
	/** 	Routine table constants		*/
	private static final String TABLE_ROUTINE = "routine";
	private static final String COLUMN_ROUTINE_ID = "_id";
	private static final String COLUMN_ROUTINE_TITLE = "title";
	private static final String COLUMN_ROUTINE_DESCRIPTION = "description";
	
	/**		RoutineExercise table constants		*/
	private static final String TABLE_ROUTINE_EXERCISE = "routine_exercise";
	private static final String COLUMN_PARENT_ROUTINE_ID = "r_id";
	private static final String COLUMN_PARENT_EXERCISE_ID = "e_id";
	private static final String COLUMN_SETS = "sets";
	private static final String COLUMN_REPS = "reps";
	
	private final String RE_QUERY = "select * from exercise inner join routine_exercise " +
			"on routine_exercise._id = ?;";
	private final String RE_QUERY_ALL = 
			"select * from routine_exercise left join exercise " +
			"union all " +
			"select * from exercise left join routine_exercise";
	private final String RE_QUERY_ROUTINE =
			"select * from (" + RE_QUERY_ALL +
			") where r_id = ?;";
	
	private final String QUERY =
			"select routine_exercise._id, e_id, r_id, sets, reps, exercise.title, exercise.description " +
			"from exercise, routine_exercise " +
			"where r_id = ?;";
	
	private final String TEST_Q =
			"select * from routine_exercise inner join exercise " +
			"on routine_exercise.e_id = exercise._id " +
			"and routine_exercise.r_id = ?";
	
	
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the 'exercise' table
		db.execSQL("create table exercise (" +
			"_id integer primary key autoincrement, title text, description text)");
		
		// Create the 'routine' table
		db.execSQL("create table routine (" +
			"_id integer primary key autoincrement, title text, description text)");
		
		// Create the 'routine_exercise' table
		db.execSQL("create table routine_exercise (" +
			"r_id integer references routine(_id)," +
				"e_id integer references exercise(_id), sets integer, reps integer)");
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
	
	// Insert newly created empty routine
	public long insertRoutine(Routine routine) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ROUTINE_TITLE, COLUMN_PLACEHOLDER);
		cv.put(COLUMN_ROUTINE_DESCRIPTION, COLUMN_PLACEHOLDER);
			
		Log.d(TAG, "Inserting new routine into database");
			
		return getWritableDatabase().insert(TABLE_ROUTINE, null, cv);
	}
		
	// Update an existing routine
	public int updateRoutine(Routine routine) {
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
		
	// Insert new entry into routine_exercise database
	public long insertRoutineExercise(RoutineExercise routineExercise) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_PARENT_ROUTINE_ID, routineExercise.getRoutineId());
		cv.put(COLUMN_PARENT_EXERCISE_ID, routineExercise.getId());
		cv.put(COLUMN_SETS, routineExercise.getSets());
		cv.put(COLUMN_REPS, routineExercise.getReps());
			
		Log.d(TAG, "Inserting new routine exercise into database");
		Log.d(TAG, "RoutineID = " + routineExercise.getRoutineId());
			
		return getWritableDatabase().insert(TABLE_ROUTINE_EXERCISE, null, cv);
	}
		
	// Update an existing routineExercise
	public int updateRoutineExercise(RoutineExercise routineExercise) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_PARENT_ROUTINE_ID, routineExercise.getRoutineId());
		cv.put(COLUMN_PARENT_EXERCISE_ID, routineExercise.getId());
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
		
	public RoutineExerciseCursor queryRoutineExercises(long routineId) {
		//Cursor wrapped = getReadableDatabase().rawQuery(QUERY, new String[]{ String.valueOf(routineId) });
		Cursor wrapped = getReadableDatabase().rawQuery(TEST_Q, new String[]{ String.valueOf(routineId) });
		
		Log.d(TAG, "query routineId = " + routineId);
			
		return new RoutineExerciseCursor(wrapped);
			
		/**
		// Equivalent to "select * from routine_exercises order by title"
		Cursor wrapped = getReadableDatabase().query(TABLE_ROUTINE_EXERCISE,
				null, null, null, null, null, COLUMN_ROUTINE_ID + " asc");
		return new RoutineExerciseCursor(wrapped);
		*/
	}
		
	public RoutineExerciseCursor queryRoutineExercise(long _id) {
		Cursor wrapped = getReadableDatabase().rawQuery(RE_QUERY, new String[]{ String.valueOf(_id) });
			
		return new RoutineExerciseCursor(wrapped);
	}
		
	/**
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
	}*/
	
	
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
					getLong(getColumnIndex(COLUMN_PARENT_ROUTINE_ID)), 
					getLong(getColumnIndex(COLUMN_PARENT_EXERCISE_ID)) );
			
			String title = getString(getColumnIndex(COLUMN_EXERCISE_TITLE));
			routineExercise.setTitle(title);
			
			String description = getString(getColumnIndex(COLUMN_EXERCISE_DESCRIPTION));
			routineExercise.setDescription(description);
			
			int sets = getInt(getColumnIndex(COLUMN_SETS));
			routineExercise.setSets(sets);
			
			int reps = getInt(getColumnIndex(COLUMN_REPS));
			routineExercise.setReps(reps);
			
			
			return routineExercise;
		}
	}

}
