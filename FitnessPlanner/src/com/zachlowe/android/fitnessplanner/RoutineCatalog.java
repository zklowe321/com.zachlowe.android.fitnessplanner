package com.zachlowe.android.fitnessplanner;

import android.content.Context;

import com.zachlowe.android.fitnessplanner.ExerciseDatabaseHelper.ExerciseCursor;

public class RoutineCatalog {

	private static RoutineCatalog sRoutineCatalog;
	private Context mAppContext;
	private RoutineDatabaseHelper mHelper;
	
	private RoutineCatalog(Context appContext) {
		mAppContext = appContext;
		
		mHelper = new RoutineDatabaseHelper(mAppContext);
	}
	
	public static RoutineCatalog get(Context c) {
		if (sRoutineCatalog == null) {
			sRoutineCatalog = new RoutineCatalog(c.getApplicationContext());
		}
		return sRoutineCatalog;
	}
	
	// Insert a new exercise into the database
	public Exercise insertRoutine() {
		Routine routine = new Routine();
		routine.setId(mHelper.insertRoutine(routine));
		return routine;
	}
	
	// Update the values for the exercise given
	public int updateRoutine(Routine routine) {
		return mHelper.updateRoutine(routine);
	}
	
	public RoutineCursor queryRoutines() {
		return mHelper.queryRoutines();
	}
	
	public void deleteRoutine(Routine r) {
		mHelper.deleteRoutine(r);
	}
	
	public Routine getRoutine(long id) {
		Routine routine = null;
		RoutineCursor cursor = mHelper.queryRoutine(id);
		cursor.moveToFirst();
		// If you got a row, get a routine
		if (!cursor.isAfterLast())
			routine = cursor.getRoutine();
		cursor.close();
		return routine;
	}
	
}
