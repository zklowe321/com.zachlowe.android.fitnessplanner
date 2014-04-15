/**
 *	Activity to hold listFragment of exercises
 */
package com.zachlowe.android.fitnessplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class ExerciseListActivity extends SingleFragmentActivity
	implements ExerciseListFragment.Callbacks, ExerciseFragment.Callbacks {
	
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

	/**
	 *	Callback for when an exercise is selected. Two possible actions:
	 *		1. If an exercise is not being added to a routine, launch the ExercisePagerActivity
	 *		2. If an exercise is being added to a routine:
	 *			a. Set result for RoutineActivity
	 *			b. Finish() to send result to querying activity
	 */
	public void onExerciseSelected(Exercise exercise) {
		int add = getIntent().getIntExtra(EXTRA_ADD_EXERCISE, -1);
		if ( add != 1 ) {
			// Start an instance of ExercisePagerActivity
			Intent i = new Intent(this, ExercisePagerActivity.class);
			i.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
			startActivity(i);
		} else {
			long routineId = getIntent().getLongExtra(EXTRA_ROUTINE_ID, -1);
			// Set result as the exerciseId
			Intent i = new Intent();
			i.putExtra(EXTRA_EXERCISE_ID, exercise.getId());
			i.putExtra(EXTRA_ROUTINE_ID, routineId);
			setResult(Activity.RESULT_OK, i);
			finish();
		}
	}
	
	/**
	 *	When called, update the list of Exercises
	 */
	public void onExerciseUpdated(Exercise exercise) {
		ExerciseCatalog.get(this).updateExercise(exercise);
		
		FragmentManager fm = getSupportFragmentManager();
		ExerciseListFragment listFragment = (ExerciseListFragment)
				fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}	
}