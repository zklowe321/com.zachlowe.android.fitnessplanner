package com.zachlowe.android.fitnessplanner;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ExerciseListActivity extends SingleFragmentActivity
	implements ExerciseListFragment.Callbacks, ExerciseFragment.Callbacks {
	
	private static final String TAG = "ExerciseListActivity";
	public static final String EXTRA_EXERCISE_ID =
			"com.zachlowe.android.fitnessplanner.exercise_id";
	public static final String EXTRA_ADD_EXERCISE =
			"com.zachlowe.android.fitnessplanner.add_exercise";
	public static final String EXTRA_ROUTINE_ID =
			"com.zachlowe.android.fitnessplanner.routine_id";

	@Override
	protected Fragment createFragment() {
		return new ExerciseListFragment();
	}

	public void onExerciseSelected(Exercise exercise) {
		if ( getIntent().getIntExtra(EXTRA_ADD_EXERCISE, -1) != 1 ) {
			// Start an instance of ExercisePagerActivity
			Intent i = new Intent(this, ExercisePagerActivity.class);
			i.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
			startActivity(i);
		} else {
			// Set result as the exerciseId
			Intent i = new Intent();
			i.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
			i.putExtra(EXTRA_ROUTINE_ID,
					getIntent().getLongExtra(EXTRA_ROUTINE_ID, -1));
			setResult(Activity.RESULT_OK, i);
			finish();
		}
	}
	
	public void onExerciseUpdated(Exercise exercise) {
		ExerciseCatalog.get(this).updateExercise(exercise);
		
		FragmentManager fm = getSupportFragmentManager();
		ExerciseListFragment listFragment = (ExerciseListFragment)
				fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}
}