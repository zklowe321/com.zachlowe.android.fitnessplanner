/**
 *	View class to display and edit the information of a given exercise
 */
package com.zachlowe.android.fitnessplanner;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ExerciseFragment extends Fragment {
	private static final String ARG_EXERCISE_ID = "EXERCISE_ID";
	private static final int LOAD_EXERCISE = 0;
	
	private Exercise mExercise;
	private EditText mTitleField;
	private EditText mDescriptionField;
	
	/**
	 * Required interface for hosting activities
	 */
	public interface Callbacks {
		void onExerciseUpdated(Exercise exercise);
	}
	
	/**		Return a fragment with the correct exerciseId attached	*/
	public static ExerciseFragment newInstance(long exerciseId) {
		Bundle args = new Bundle();
		args.putLong(ARG_EXERCISE_ID, exerciseId);
		
		ExerciseFragment fragment = new ExerciseFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Get the exerciseId and initialize the loader to load data
		Bundle args = getArguments();
		if (args != null) {
			long exerciseId = args.getLong(ARG_EXERCISE_ID, -1);
			if (exerciseId != -1) {
				LoaderManager lm = getLoaderManager();
				lm.initLoader(LOAD_EXERCISE, args, new ExerciseLoaderCallbacks());
			}
		}
		
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
		
		// Connect the titleField EditText
		mTitleField = (EditText)v.findViewById(R.id.exercise_title);
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
				getActivity().setTitle(mExercise.getTitle());
			}
		});
		
		// Connect the descriptionField EditText
		mDescriptionField = (EditText)v.findViewById(R.id.exercise_description);
		mDescriptionField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start,
					int count, int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mExercise.setDescription(c.toString());
			}
		});
		updateUI();
		
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
		
		// Update the exercise
		ExerciseCatalog.get(getActivity()).updateExercise(mExercise);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	
	/**		Get new values from database and put them on screen		*/
	public void updateUI() {
		if (mExercise != null) {
			getActivity().setTitle(mExercise.getTitle());
			mTitleField.setText(mExercise.getTitle());
			mDescriptionField.setText(mExercise.getDescription());
		}
	}
	
	/**
	 *	Private inner class to handle callback events when loading data from the database.
	 */
	private class ExerciseLoaderCallbacks implements LoaderCallbacks<Exercise> {
		
		@Override
		public Loader<Exercise> onCreateLoader(int id, Bundle args) {
			return new ExerciseLoader(getActivity(), args.getLong(ARG_EXERCISE_ID));
		}
		
		@Override
		public void onLoadFinished(Loader<Exercise> loader, Exercise exercise) {
			mExercise = exercise;
			updateUI();
		}
		
		@Override
		public void onLoaderReset(Loader<Exercise> loader) {
			// Do nothing
		}
	}	
}