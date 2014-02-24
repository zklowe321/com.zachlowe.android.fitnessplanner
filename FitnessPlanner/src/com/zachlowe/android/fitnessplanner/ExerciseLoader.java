package com.zachlowe.android.fitnessplanner;

import android.content.Context;

public class ExerciseLoader extends DataLoader<Exercise> {
	
	private long mExerciseId;
	
	public ExerciseLoader(Context context, long exerciseId) {
		super(context);
		mExerciseId = exerciseId;
	}

	@Override
	public Exercise loadInBackground() {
		return ExerciseCatalog.get(getContext()).getExercise(mExerciseId);
	}
}
