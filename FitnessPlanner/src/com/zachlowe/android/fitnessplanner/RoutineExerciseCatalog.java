package com.zachlowe.android.fitnessplanner;

import android.content.Context;

import com.zachlowe.android.fitnessplanner.RoutineExerciseDatabaseHelper.RoutineExerciseCursor;

public class RoutineExerciseCatalog {

	private static RoutineExerciseCatalog sRoutineExerciseCatalog;
	private Context mAppContext;
	private RoutineExerciseDatabaseHelper mHelper;
	
	private RoutineExerciseCatalog(Context appContext) {
		mAppContext = appContext;
		mHelper = new RoutineExerciseDatabaseHelper(mAppContext);
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
	
	public RoutineExercise getRoutineExercise(long r_id, long e_id) {
		RoutineExercise routineExercise = null;
		RoutineExerciseCursor cursor = mHelper.queryRoutineExercise(r_id, e_id);
		cursor.moveToFirst();
		// If you got a row, get a routine
		if (!cursor.isAfterLast())
			routineExercise = cursor.getRoutineExercise();
		cursor.close();
		return routineExercise;
	}
	
}
