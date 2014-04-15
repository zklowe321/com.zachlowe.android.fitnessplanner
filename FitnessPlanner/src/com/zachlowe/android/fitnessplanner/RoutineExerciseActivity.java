package com.zachlowe.android.fitnessplanner;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class RoutineExerciseActivity extends SingleFragmentActivity {

	private static final String TAG = "RoutineExerciseActivity";
	
	public static final String EXTRA_ROUTINE_ID =
			"com.zachlowe.android.fitnessplanner.routine_id";
	public static final String EXTRA_EXERCISE_ID =
			"com.zachlowe.android.fitnessplanner.exercise_id";

	@Override
	protected Fragment createFragment() {
		Intent i = getIntent();
		long exerciseId = i.getLongExtra(EXTRA_EXERCISE_ID, -1);
		long routineId = i.getLongExtra(EXTRA_ROUTINE_ID, -1);
		
		return RoutineExerciseFragment.newInstance(exerciseId, routineId);
	}

}
