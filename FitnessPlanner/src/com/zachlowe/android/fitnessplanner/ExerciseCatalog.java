package com.zachlowe.android.fitnessplanner;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class ExerciseCatalog {
	private static final String TAG = "ExcerciseCatalog";
	
	private ArrayList<Exercise> mExercises;
	
	private static ExerciseCatalog sExerciseCatalog;
	private Context mAppContext;
	
	private ExerciseCatalog(Context appContext) {
		mAppContext = appContext;
		
		mExercises = new ArrayList<Exercise>();
		for (int i = 0; i < 10; i++) {
			Exercise e = new Exercise();
			e.setTitle("Exercise #" + String.valueOf(i));
			e.setDescription("Description #" + String.valueOf(i));
			mExercises.add(e);
		}
		
		Log.d(TAG, "ExerciseCatalog constructor called");
	}
	
	public static ExerciseCatalog get(Context c) {
		if (sExerciseCatalog == null) {
			sExerciseCatalog = new ExerciseCatalog(c.getApplicationContext());
		}
		return sExerciseCatalog;
	}
	
	public void addExercise(Exercise e) {
		mExercises.add(e);
	}
	
	public void deleteExercise(Exercise e) {
		mExercises.remove(e);
	}
	
	public ArrayList<Exercise> getExercises() {
		return mExercises;
	}
	
	public Exercise getExercise(UUID id) {
		for (Exercise e : mExercises) {
			if (e.getId().equals(id))
				return e;
		}
		return null;
	}
}
