package com.zachlowe.android.fitnessplanner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class RoutineListActivity extends SingleFragmentActivity
	implements RoutineListFragment.Callbacks {
	
	private static final String TAG = "RoutineListActivity";
	public static final String EXTRA_ROUTINE_ID =
			"com.zachlowe.android.fitnessplanner.routine_id";

	@Override
	protected Fragment createFragment() {
		return new RoutineListFragment();
	}
	
	public void onRoutineSelected(Routine routine) {
		// Start an instance of RoutineActivity
		Intent i = new Intent(this, RoutineActivity.class);
		i.putExtra(EXTRA_ROUTINE_ID, routine.getId());
		startActivity(i);
	}

	public void onRoutineUpdated(Routine routine) {
		RoutineCatalog.get(this).updateRoutine(routine);
	
		FragmentManager fm = getSupportFragmentManager();
		RoutineListFragment listFragment = (RoutineListFragment)
			fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}

}
