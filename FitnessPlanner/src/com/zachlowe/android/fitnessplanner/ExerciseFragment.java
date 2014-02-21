package com.zachlowe.android.fitnessplanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ExerciseFragment extends Fragment {
	private static final String TAG = "ExerciseFragment";
	
	public static final String ARG_EXERCISE_ID = "EXERCISE_ID";
	
	private Exercise mExercise;
	private ExerciseCatalog mCatalog;
	private EditText mTitleField;
	private EditText mDescriptionField;
	private Callbacks mCallbacks;
	
	/**
	 * Required interface for hosting activities
	 */
	public interface Callbacks {
		void onExerciseUpdated(Exercise exercise);
	}
	
	public static ExerciseFragment newInstance(long exerciseId) {
		Bundle args = new Bundle();
		args.putLong(ARG_EXERCISE_ID, exerciseId);
		
		ExerciseFragment fragment = new ExerciseFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCatalog = ExerciseCatalog.get(getActivity());
		
		Bundle args = getArguments();
		if (args != null) {
			Log.d(TAG, "Args are not null");
			long exerciseId = args.getLong(ARG_EXERCISE_ID, -1);
			if (exerciseId != -1) {
				Log.d(TAG, "exerciseId is not -1");
				mExercise = mCatalog.getExercise(exerciseId);
			}
			Log.d(TAG, "exerciseId is -1");
		}
		Log.d(TAG, "Args are null");
		
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_exercise, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText)v.findViewById(R.id.exercise_title);
		mTitleField.setText(mExercise.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start,
					int count, int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mExercise.setTitle(c.toString());
				
				mCallbacks.onExerciseUpdated(mExercise);
				Log.d(TAG, "passed onExerciseUpdated. trying activity setTitle");
				
				getActivity().setTitle(mExercise.getTitle());
				Log.d(TAG, "passed activity setTitle");
			}
		});
		
		mDescriptionField = (EditText)v.findViewById(R.id.exercise_description);
		mDescriptionField.setText(mExercise.getDescription());
		mDescriptionField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start,
					int count, int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				Log.d(TAG, "onTextChanged mDescriptionField called");
				
				mExercise.setDescription(c.toString());
				mCallbacks.onExerciseUpdated(mExercise);
			}
		});
		
		return v;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		ExerciseCatalog.get(getActivity()).updateExercise(mExercise);
		
		Log.d(TAG, "updateExercise");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}	
}