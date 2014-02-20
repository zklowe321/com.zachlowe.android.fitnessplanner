package com.zachlowe.android.fitnessplanner;

import java.util.ArrayList;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ExerciseListFragment extends ListFragment {
	private static final String TAG = "ExerciseListFragment";
	
	private ArrayList<Exercise> mExercises;
	private Callbacks mCallbacks;
	
	/**
	 * Required interface for hosting activities
	 */
	public interface Callbacks {
		void onExerciseSelected(Exercise exercise);
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		getActivity().setTitle(R.string.exercises_title);
		mExercises = ExerciseCatalog.get(getActivity()).getExercises();
		
		ExerciseAdapter adapter = new ExerciseAdapter(mExercises);
		setListAdapter(adapter);
		
		setRetainInstance(true);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Use floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);
		} else {
			// Use contextual action bar on Honeycomb and higher
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_exercise:
						ExerciseAdapter adapter = (ExerciseAdapter)getListAdapter();
						ExerciseCatalog catalog = ExerciseCatalog.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								catalog.deleteExercise(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default: return false;
				}
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.exercise_list_item_context, menu);
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) { }

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

				@Override
				public void onItemCheckedStateChanged(ActionMode mode,int position,
						long id, boolean checked) { }
			});
			
		}
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_exercise_list, menu);
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_exercise:
				Exercise exercise = new Exercise();
				ExerciseCatalog.get(getActivity()).addExercise(exercise);
				((ExerciseAdapter)getListAdapter()).notifyDataSetChanged();
				mCallbacks.onExerciseSelected(exercise);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	// Inflate context menu for Froyo and Gingerbread
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.exercise_list_item_context, menu);
	}
	
	// Handle item selection in Froyo and Gingerbread
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		ExerciseAdapter adapter = (ExerciseAdapter)getListAdapter();
		Exercise exercise = adapter.getItem(position);
		
		switch (item.getItemId()) {
			case R.id.menu_item_delete_exercise:
				ExerciseCatalog.get(getActivity()).deleteExercise(exercise);
				adapter.notifyDataSetChanged();
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Get the Exercise from the adapter
		Exercise e = ((ExerciseAdapter)getListAdapter()).getItem(position);
		
		mCallbacks.onExerciseSelected(e);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
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
	
	public void updateUI() {
		((ExerciseAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	
	private class ExerciseAdapter extends ArrayAdapter<Exercise> {
		
		public ExerciseAdapter(ArrayList<Exercise> exercises) {
			super(getActivity(), 0, exercises);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// If we weren't given a View, inflate one
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_exercise, null);
			}
			
			// Configure the view for this exercise
			Exercise e = getItem(position);
			
			TextView titleTextView = 
					(TextView)convertView.findViewById(R.id.exercise_list_item_titleTextView);
			titleTextView.setText(e.getTitle());
			TextView descriptionTextView = 
					(TextView)convertView.findViewById(R.id.exercise_list_item_descriptionTextView);
			descriptionTextView.setText(e.getDescription());
			
			return convertView;
		}
		
	}	
}