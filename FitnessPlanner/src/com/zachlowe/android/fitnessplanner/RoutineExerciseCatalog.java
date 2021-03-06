package com.zachlowe.android.fitnessplanner;

import android.content.Context;
import android.util.Log;

import com.zachlowe.android.fitnessplanner.DatabaseHelper.RoutineExerciseCursor;

public class RoutineExerciseCatalog {
	private static final String TAG = "RoutineExerciseCatalog";

	private static RoutineExerciseCatalog sRoutineExerciseCatalog;
	private Context mAppContext;
	private DatabaseHelper mHelper;
	
	private RoutineExerciseCatalog(Context appContext) {
		mAppContext = appContext;
		mHelper = new DatabaseHelper(mAppContext);

	}
	
	public static RoutineExerciseCatalog get(Context c) {
		if (sRoutineExerciseCatalog == null) {
			sRoutineExerciseCatalog = new RoutineExerciseCatalog(c.getApplicationContext());
		}
		return sRoutineExerciseCatalog;
	}
	
	// Insert a routineExercise into the database
	public RoutineExercise insertRoutineExercise(long exerciseId, long routineId) {
		RoutineExercise routineExercise = new RoutineExercise(exerciseId, routineId);
		
		mHelper.insertRoutineExercise(routineExercise);
		
		return routineExercise;
	}
	
	// Update the values for the routineExercise given
	public int updateRoutineExercise(RoutineExercise routineExercise) {
		return mHelper.updateRoutineExercise(routineExercise);
	}
	
	public RoutineExerciseCursor queryRoutineExercises(long routineId) {
		return mHelper.queryRoutineExercises(routineId);
	}
	
	public void deleteRoutineExercise(RoutineExercise routineExercise) {
		mHelper.deleteRoutineExercise(routineExercise);
	}
	
	public RoutineExercise getRoutineExercise(long exerciseId, long routineId) {
		RoutineExercise routineExercise = null;
		RoutineExerciseCursor cursor = mHelper.queryRoutineExercise(exerciseId, routineId);
		cursor.moveToFirst();
		// If you got a row, get a routine
		if (!cursor.isAfterLast())
			routineExercise = cursor.getRoutineExercise();
		cursor.close();
		return routineExercise;
	}
}
