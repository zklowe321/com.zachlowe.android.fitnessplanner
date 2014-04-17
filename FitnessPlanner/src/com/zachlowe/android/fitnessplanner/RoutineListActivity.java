package com.zachlowe.android.fitnessplanner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class RoutineListActivity extends SingleFragmentActivity
	implements RoutineListFragment.Callbacks {
	
	private static final String TAG = "RoutineListActivity";
	public static final String EXTRA_ROUTINE_ID =
			"com.zachlowe.android.fitnessplanner.routine_id";
	public static final String EXTRA_PERFORM_ACTION =
			"com.zachlowe.android.fitnessplanner.perform_action";
	public static final String EXTRA_DATE =
			"com.zachlowe.android.fitnessplanner.date";
	
	private static final int REQUEST_SHARE = 1;
	private static final int REQUEST_CALENDAR = 2;

	@Override
	protected Fragment createFragment() {
		return new RoutineListFragment();
	}
	
	/**
	 *	Callback for when a routine is selected. Two possible actions:
	 *		1. If a routine is not being shared or scheduled, launch the RoutineActivity
	 *		2. If a routine is being shared:
	 *			a. Put the routine into readable form
	 *			b. Send the String to the email application of the user's choosing
	 *		3. If a routine is being scheduled:
	 *			a. Present user with date picker dialog
	 *			b. Create event from chosen date
	 */
	public void onRoutineSelected(Routine routine) {
		int action = getIntent().getIntExtra(EXTRA_PERFORM_ACTION, -1);
		if ( action == REQUEST_SHARE ) {
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
            
		} else if ( action == REQUEST_CALENDAR ) {

			Date date = (Date)getIntent().getSerializableExtra(EXTRA_DATE);
			long startTime = date.getTime();

		    Intent intent = new Intent(Intent.ACTION_EDIT);
		    intent.setType("vnd.android.cursor.item/event");
		    intent.putExtra("beginTime",startTime);
		    intent.putExtra("allDay", true);
		    intent.putExtra("title", routine.getTitle());
		    startActivity(intent);
		    
		} else {
			// Start an instance of RoutineActivity
			Intent i = new Intent(this, RoutineActivity.class);
			i.putExtra(EXTRA_ROUTINE_ID, routine.getId());
			startActivity(i);
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
