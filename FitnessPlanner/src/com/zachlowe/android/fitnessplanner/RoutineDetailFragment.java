package com.zachlowe.android.fitnessplanner;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;

public class RoutineDetailFragment extends Fragment {

	private static final String TAG = "RoutineDetailFragment";
	private static final String ARG_ROUTINE_ID = "ROUTINE_ID";
	private static final int LOAD_ROUTINE = 0;
	
	private Routine mRoutine;
	private EditText mTitleField;
	private EditText mDescriptionField;
	private Button mSerializeButton;
	
	/**
	 * Required interface for hosting activities
	 */
	public interface Callbacks {
		void onRoutineUpdated(Routine routine);
	}
	
	public static RoutineDetailFragment newInstance(long routineId) {
		Bundle args = new Bundle();
		args.putLong(ARG_ROUTINE_ID, routineId);
		
		RoutineDetailFragment fragment = new RoutineDetailFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if (args != null) {
			long routineId = args.getLong(ARG_ROUTINE_ID, -1);
			if (routineId != -1) {
				LoaderManager lm = getLoaderManager();
				lm.initLoader(LOAD_ROUTINE, args, new RoutineLoaderCallbacks());
			}
		}
			
		setHasOptionsMenu(true);
		setRetainInstance(true);
	
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_routine, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText)v.findViewById(R.id.routine_title);
		mTitleField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start,
					int count, int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mRoutine.setTitle(c.toString());
				getActivity().setTitle(mRoutine.getTitle());
			}
		});
		
		mDescriptionField = (EditText)v.findViewById(R.id.routine_description);
		mDescriptionField.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable e) { }

			@Override
			public void beforeTextChanged(CharSequence c, int start,
					int count, int after) { }

			@Override
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mRoutine.setDescription(c.toString());
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
		RoutineCatalog.get(getActivity()).updateRoutine(mRoutine);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	
	public void updateUI() {
		if (mRoutine != null && !isEmpty(mRoutine)) {
			//getActivity().setTitle(mExercise.getTitle());
			mTitleField.setText(mRoutine.getTitle());
			mDescriptionField.setText(mRoutine.getDescription());
		}
	}
	
	public boolean isEmpty(Routine routine) {
		return (mRoutine.getTitle().equals(" ") &&
				mRoutine.getDescription().equals(" "));
	}
	
	private class RoutineLoaderCallbacks implements LoaderCallbacks<Routine> {
		
		@Override
		public Loader<Routine> onCreateLoader(int id, Bundle args) {
			return new RoutineLoader(getActivity(), args.getLong(ARG_ROUTINE_ID));
		}
		
		@Override
		public void onLoadFinished(Loader<Routine> loader, Routine routine) {
			mRoutine = routine;
			updateUI();
		}
		
		@Override
		public void onLoaderReset(Loader<Routine> loader) {
			// Do nothing
		}
	}
	
}
