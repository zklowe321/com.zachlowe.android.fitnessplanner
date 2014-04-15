package com.zachlowe.android.fitnessplanner;

import android.content.Context;

public class RoutineExerciseLoader extends DataLoader<RoutineExercise> {
	private static final String TAG = "RoutineExerciseLoader";

	private long mRoutineId;
	private long mExerciseId;
	
	public RoutineExerciseLoader(Context context, long exerciseId, long routineId) {
		super(context);
		mRoutineId = routineId;
		mExerciseId = exerciseId;
	}

	@Override
	public RoutineExercise loadInBackground() {
		return RoutineExerciseCatalog.get(getContext()).getRoutineExercise(mExerciseId, mRoutineId);
	}
}
