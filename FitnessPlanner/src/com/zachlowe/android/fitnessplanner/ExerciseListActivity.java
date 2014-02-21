package com.zachlowe.android.fitnessplanner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ExerciseListActivity extends SingleFragmentActivity
	implements ExerciseListFragment.Callbacks, ExerciseFragment.Callbacks {

	@Override
	protected Fragment createFragment() {
		return new ExerciseListFragment();
	}

	public void onExerciseSelected(Exercise exercise) {
			// Start an instance of ExercisePagerActivity
			Intent i = new Intent(this, ExercisePagerActivity.class);
			i.putExtra(ExerciseFragment.EXTRA_EXERCISE_ID, exercise.getId());
			startActivity(i);
	}
	
	public void onExerciseUpdated(Exercise exercise) {
		FragmentManager fm = getSupportFragmentManager();
		ExerciseListFragment listFragment = (ExerciseListFragment)
				fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}
	
}
