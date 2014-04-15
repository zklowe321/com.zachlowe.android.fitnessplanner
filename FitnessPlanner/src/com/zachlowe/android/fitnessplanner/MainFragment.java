/**
 * 	Main screen of the Application. Allows the user to choose which feature they want to use
 */
package com.zachlowe.android.fitnessplanner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MainFragment extends Fragment {
	
	private ImageButton mCalendarImageButton;
	private ImageButton mExerciseImageButton;
	private ImageButton mRoutineImageButton;
	private ImageButton mRunningImageButton;
	
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
				// Intent i = new Intent(getActivity(), CalendarActivity.class);
				// startActivity(i);
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
		
		mRunningImageButton = (ImageButton)v.findViewById(R.id.fragment_main_running_ImageButton);
		mRunningImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent i = new Intent(getActivity(), RunningActivity.class);
				// startActivity(i);
			}
		});
		
		return v;
	}
}






















