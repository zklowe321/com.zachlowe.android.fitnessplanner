/**
 * 	Main screen of the Application. Allows the user to choose which feature they want to use
 */
package com.zachlowe.android.fitnessplanner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainFragment extends Fragment {
	private static final String TAG = "MainFragment";
	
	public static final String EXTRA_PERFORM_ACTION =
			"com.zachlowe.android.fitnessplanner.perform_action";
	
	private static final int REQUEST_SHARE = 1;
	private static final int REQUEST_CALENDAR = 2;
	
	private ImageButton mCalendarImageButton;
	private ImageButton mExerciseImageButton;
	private ImageButton mRoutineImageButton;
	private ImageButton mShareImageButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@SuppressLint("NewApi")
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mCalendarImageButton = (ImageButton)v.findViewById(R.id.fragment_main_calendar_ImageButton);
		mCalendarImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), RoutineListActivity.class);
				i.putExtra(EXTRA_PERFORM_ACTION, REQUEST_CALENDAR);
				startActivity(i);
			}
		});
		
		mExerciseImageButton = (ImageButton)v.findViewById(R.id.fragment_main_exercise_ImageButton);
		mExerciseImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), ExerciseListActivity.class);
				startActivity(i);
			}
		});
		
		mRoutineImageButton = (ImageButton)v.findViewById(R.id.fragment_main_routine_ImageButton);
		mRoutineImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), RoutineListActivity.class);
				startActivity(i);
			}
		});
		
		mShareImageButton = (ImageButton)v.findViewById(R.id.fragment_main_share_ImageButton);
		mShareImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), RoutineListActivity.class);
				i.putExtra(EXTRA_PERFORM_ACTION, REQUEST_SHARE);
				startActivity(i);
			}
		});
		
		return v;
	}
	
}






















