package com.zachlowe.android.fitnessplanner;

import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import com.zachlowe.android.fitnessplanner.ExerciseDatabaseHelper.ExerciseCursor;

public class ExerciseCatalog {
	private static final String TAG = "ExcerciseCatalog";
	
	private static final String PREFS_FILE = "exercises";
	private static final String PREF_CURRENT_EXERCISE_ID =
			"ExerciseCatalog.currentExerciseId";
	
	private ArrayList<Exercise> mExercises;
	
	private static ExerciseCatalog sExerciseCatalog;
	private Context mAppContext;
	private ExerciseDatabaseHelper mHelper;
	private SharedPreferences mPrefs;
	private long mCurrentExerciseId;
	
	private ExerciseCatalog(Context appContext) {
		mAppContext = appContext;
		
		mExercises = new ArrayList<Exercise>();
		
		mHelper = new ExerciseDatabaseHelper(mAppContext);
		mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		mCurrentExerciseId = mPrefs.getLong(PREF_CURRENT_EXERCISE_ID, -1);
	}
	
	public static ExerciseCatalog get(Context c) {
		if (sExerciseCatalog == null) {
			sExerciseCatalog = new ExerciseCatalog(c.getApplicationContext());
		}
		return sExerciseCatalog;
	}
	
	public void addExercise(Exercise e) {
		e.setId(mHelper.insertExercise(e));
		mExercises.add(e);
	}
	
	public ExerciseCursor queryExercises() {
		return mHelper.queryExercises();
	}
	
	public void deleteExercise(Exercise e) {
		mExercises.remove(e);
	}
	
	public ArrayList<Exercise> getExercises() {
		return mExercises;
	}
	
	public Exercise getExercise(long id) {
		for (Exercise e : mExercises) {
			if (e.getId() == id)
				return e;
		}
		return null;
	}
}
