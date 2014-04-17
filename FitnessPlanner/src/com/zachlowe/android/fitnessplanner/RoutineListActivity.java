package com.zachlowe.android.fitnessplanner;

import org.apache.http.protocol.HTTP;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class RoutineListActivity extends SingleFragmentActivity
	implements RoutineListFragment.Callbacks {
	
	private static final String TAG = "RoutineListActivity";
	public static final String EXTRA_ROUTINE_ID =
			"com.zachlowe.android.fitnessplanner.routine_id";
	public static final String EXTRA_SHARE_ROUTINE =
			"com.zachlowe.android.fitnessplanner.share_routine";

	@Override
	protected Fragment createFragment() {
		return new RoutineListFragment();
	}
	
	/**
	 *	Callback for when a routine is selected. Two possible actions:
	 *		1. If a routine is not being shared, launch the RoutineActivity
	 *		2. If a routine is being shared:
	 *			a. Put the routine into readable form
	 *			b. Send the String to the email application of the user's choosing
	 */
	public void onRoutineSelected(Routine routine) {
		int share = getIntent().getIntExtra(EXTRA_SHARE_ROUTINE, -1);
		if ( share != 1 ) {
			// Start an instance of RoutineActivity
			Intent i = new Intent(this, RoutineActivity.class);
			i.putExtra(EXTRA_ROUTINE_ID, routine.getId());
			startActivity(i);
		} else {
			long routineId = routine.getId();
			
			// Put the routine into readable String form
			Serializer serializer = new Serializer(routineId, this);
            String str = serializer.toString();
            
            // Create an implicit intent and send the routine as an email
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/text");
            email.putExtra(Intent.EXTRA_SUBJECT, "Check out my workout routine!");
            email.putExtra(Intent.EXTRA_TEXT, str);
            startActivity(Intent.createChooser(email, "Choose email application"));
		}
	}

	public void onRoutineUpdated(Routine routine) {
		RoutineCatalog.get(this).updateRoutine(routine);
	
		FragmentManager fm = getSupportFragmentManager();
		RoutineListFragment listFragment = (RoutineListFragment)
			fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}

}
