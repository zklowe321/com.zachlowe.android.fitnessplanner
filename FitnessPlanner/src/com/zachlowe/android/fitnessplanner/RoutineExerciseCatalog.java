package com.zachlowe.android.fitnessplanner;

import android.content.Context;
import android.util.Log;

import com.zachlowe.android.fitnessplanner.RoutineExerciseDatabaseHelper.RoutineExerciseCursor;

public class RoutineExerciseCatalog {
	private static final String TAG = "RoutineExerciseCatalog";

	private static RoutineExerciseCatalog sRoutineExerciseCatalog;
	private Context mAppContext;
	private RoutineExerciseDatabaseHelper mHelper;
	private ExerciseDatabaseHelper mExerciseHelper;
	
	private RoutineExerciseCatalog(Context appContext) {
		mAppContext = appContext;
		mHelper = new RoutineExerciseDatabaseHelper(mAppContext);
		mExerciseHelper = new ExerciseDatabaseHelper(mAppContext);
	}
	
	public static RoutineExerciseCatalog get(Context c) {
		if (sRoutineExerciseCatalog == null) {
			sRoutineExerciseCatalog = new RoutineExerciseCatalog(c.getApplicationContext());
		}
		return sRoutineExerciseCatalog;
	}
	
	// Insert a routineExercise into the database
	public RoutineExercise insertRoutineExercise(long routineId, long exerciseId) {
		RoutineExercise routineExercise = new RoutineExercise(routineId, exerciseId);
		
		routineExercise.setREId(mHelper.insertRoutineExercise(routineExercise));
		
		Exercise exercise = ExerciseCatalog.get(mAppContext).getExercise(exerciseId);
		
		routineExercise.setTitle(exercise.getTitle());
		routineExercise.setDescription(exercise.getDescription());
		
		return routineExercise;
	}
	
	// Update the values for the routineExercise given
	public int updateRoutineExercise(RoutineExercise routineExercise) {
		return mHelper.updateRoutineExercise(routineExercise);
	}
	
	public RoutineExerciseCursor queryRoutineExercises() {
		return mHelper.queryRoutineExercises();
	}
	
	public void deleteRoutineExercise(RoutineExercise routineExercise) {
		mHelper.deleteRoutineExercise(routineExercise);
	}
	
	public RoutineExercise getRoutineExercise(long routineExerciseId) {
		RoutineExercise routineExercise = null;
		RoutineExerciseCursor cursor = mHelper.queryRoutineExercise(routineExerciseId);
		cursor.moveToFirst();
		// If you got a row, get a routine
		if (!cursor.isAfterLast())
			routineExercise = cursor.getRoutineExercise();
		cursor.close();
		return routineExercise;
	}
	
}
