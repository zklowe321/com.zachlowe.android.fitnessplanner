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
		//if (findViewById(R.id.fragmentContainer) == null) {
			// Start an instance of CrimePagerActivity
			Intent i = new Intent(this, ExercisePagerActivity.class);
			i.putExtra(ExerciseFragment.EXTRA_EXERCISE_ID, exercise.getId());
			startActivity(i);
		/**} else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment currFragment = fm.findFragmentById(R.id.fragmentContainer);
			Fragment newFragment = ExerciseFragment.newInstance(exercise.getId());
			
			if (currFragment != null) {
				ft.remove(currFragment);
			}
			
			ft.add(R.id.fragmentContainer, newFragment);
			ft.commit();
		} */
	}
	
	public void onExerciseUpdated(Exercise exercise) {
		FragmentManager fm = getSupportFragmentManager();
		ExerciseListFragment listFragment = (ExerciseListFragment)
				fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}
	
}
