package com.zachlowe.android.fitnessplanner;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RoutineExerciseFragment extends Fragment
	implements LoaderCallbacks<RoutineExercise> {

	private static final String TAG = "RoutineExerciseFragment";
	private static final String ARG_ROUTINE_ID = "ROUTINE_ID";
	private static final String ARG_EXERCISE_ID = "EXERCISE_ID";
	private static final int LOAD_ROUTINE_EXERCISE = 0;
	
	private RoutineExercise mRoutineExercise;
	private TextView mTitleField;
	private TextView mDescriptionField;
	private EditText mSetsField;
	private EditText mRepsField;
	
	public static RoutineExerciseFragment newInstance(long exerciseId, long routineId) {
		Bundle args = new Bundle();
		args.putLong(ARG_ROUTINE_ID, routineId);
		args.putLong(ARG_EXERCISE_ID, exerciseId);
		
		RoutineExerciseFragment fragment = new RoutineExerciseFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if (args != null) {
			long routineId = args.getLong(ARG_ROUTINE_ID, -1);
			long exerciseId = args.getLong(ARG_EXERCISE_ID, -1);
			if (routineId != -1 && exerciseId != -1) {
				LoaderManager lm = getLoaderManager();
				lm.initLoader(LOAD_ROUTINE_EXERCISE, args, this).forceLoad();
			}
		}
			
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_routine_exercise, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (TextView)v.findViewById(R.id.exercise_title);
		
		mDescriptionField = (TextView)v.findViewById(R.id.exercise_description);
		
		mSetsField = (EditText)v.findViewById(R.id.routine_exercise_setsEditText);
		mSetsField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				String s = String.valueOf(c);
				
				if (!s.equals(""))
					mRoutineExercise.setSets( Integer.valueOf(s) );
			}
		});
		
		mRepsField = (EditText)v.findViewById(R.id.routine_exercise_repsEditText);
		mRepsField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				String s = String.valueOf(c);
				
				if (!s.equals(""))
					mRoutineExercise.setReps( Integer.valueOf(s) );
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

		int result = RoutineExerciseCatalog.get(getActivity()).updateRoutineExercise(mRoutineExercise);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(0, getArguments(), this);
		updateUI();
	}
	
	public void updateUI() {
		if (mRoutineExercise != null) {
			mTitleField.setText(mRoutineExercise.getTitle());
			mDescriptionField.setText(mRoutineExercise.getDescription());
			mSetsField.setText(
					String.valueOf( mRoutineExercise.getSets() ));
			mRepsField.setText(
					String.valueOf( mRoutineExercise.getReps() ));
		}
	}
	
	public boolean isEmpty(RoutineExercise routineExercise) {
		return (mRoutineExercise.getTitle().equals(" ") &&
				mRoutineExercise.getDescription().equals(" "));
	}
	
	@Override
	public Loader<RoutineExercise> onCreateLoader(int id, Bundle args) {
		long routineId = args.getLong(ARG_ROUTINE_ID);
		long exerciseId = args.getLong(ARG_EXERCISE_ID);
		
		return new RoutineExerciseLoader(getActivity(), exerciseId, routineId);
	}
	
	@Override
	public void onLoadFinished(Loader<RoutineExercise> loader, RoutineExercise routineExercise) {
		mRoutineExercise = routineExercise;
		updateUI();
	}
	
	@Override
	public void onLoaderReset(Loader<RoutineExercise> loader) {
		// Do nothing
	}
}
