package com.zachlowe.android.fitnessplanner;

import android.content.Context;

import com.zachlowe.android.fitnessplanner.DatabaseHelper.ExerciseCursor;

public class ExerciseCatalog {
	private static final String TAG = "ExcerciseCatalog";
	
	private static ExerciseCatalog sExerciseCatalog;
	private Context mAppContext;
	private DatabaseHelper mHelper;
	
	private ExerciseCatalog(Context appContext) {
		mAppContext = appContext;
		mHelper = new DatabaseHelper(mAppContext);
	}
	
	public static ExerciseCatalog get(Context c) {
		if (sExerciseCatalog == null) {
			sExerciseCatalog = new ExerciseCatalog(c.getApplicationContext());
		}
		return sExerciseCatalog;
	}
	
	// Insert a new exercise into the database
	public Exercise insertExercise() {
		Exercise exercise = new Exercise();
		exercise.setId(mHelper.insertExercise(exercise));
		return exercise;
	}
	
	// Update the values for the exercise given
	public int updateExercise(Exercise exercise) {
		return mHelper.updateExercise(exercise);
	}
	
	public ExerciseCursor queryExercises() {
		return mHelper.queryExercises();
	}
	
	public void deleteExercise(Exercise e) {
		mHelper.deleteExercise(e);
	}
	
	public Exercise getExercise(long id) {
		Exercise exercise = null;
		ExerciseCursor cursor = mHelper.queryExercise(id);
		cursor.moveToFirst();
		// If you got a row, get an exercise
		if (!cursor.isAfterLast())
			exercise = cursor.getExercise();
		cursor.close();
		return exercise;
	}
}