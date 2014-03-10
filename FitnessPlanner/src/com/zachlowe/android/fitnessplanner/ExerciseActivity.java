package com.zachlowe.android.fitnessplanner;

import android.support.v4.app.Fragment;

public class ExerciseActivity extends SingleFragmentActivity {
	/** A key for passing a run ID as a long */
	public static final String EXTRA_EXERCISE_ID =
			"com.zachlowe.android.fitnessplanner";

	@Override
	protected Fragment createFragment() {
		long exerciseId = getIntent().getLongExtra(EXTRA_EXERCISE_ID, -1);
		if (exerciseId != -1) {
			return ExerciseFragment.newInstance(exerciseId);
		} else {
			return new ExerciseFragment();
		}
	}
}
