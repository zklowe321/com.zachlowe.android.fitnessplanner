/**
 *	ListFragment that displays all the exercises in the database
 */
package com.zachlowe.android.fitnessplanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.zachlowe.android.fitnessplanner.DatabaseHelper.ExerciseCursor;

public class ExerciseListFragment extends ListFragment
		implements LoaderCallbacks<Cursor> {
	private static final String TAG = "ExerciseListFragment";
	
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
		
		// Initialize the loader to load the list of exercises
		getLoaderManager().initLoader(0, null, this);
		
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
							ExerciseCursorAdapter adapter = (ExerciseCursorAdapter)getListAdapter();
							ExerciseCatalog catalog = ExerciseCatalog.get(getActivity());
							for (int i = adapter.getCount() - 1; i >= 0; i--) {
								if (getListView().isItemChecked(i)) {
									ExerciseCursor cursor = (ExerciseCursor)adapter.getItem(i);
									Exercise exercise = cursor.getExercise();
									catalog.deleteExercise(exercise);
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							updateUI();
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
				Exercise exercise = ExerciseCatalog.get(getActivity()).insertExercise();
				((ExerciseCursorAdapter)getListAdapter()).notifyDataSetChanged();
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
		ExerciseCursorAdapter adapter = (ExerciseCursorAdapter)getListAdapter();
		Exercise exercise = (Exercise)adapter.getItem(position);
		
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
		Exercise exercise = ExerciseCatalog.get(getActivity()).getExercise(id);
		
		mCallbacks.onExerciseSelected(exercise);
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
		// Reset the loader to get any new exercise available 
		getLoaderManager().restartLoader(0, null, this);
	}
	
	/**
	 * LoaderCallbacks interface methods
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// You only ever load the exercises, so assume this is the case
		return new ExerciseListCursorLoader(getActivity());
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// Create an adapter to point at this cursor
		ExerciseCursorAdapter adapter =
				new ExerciseCursorAdapter(getActivity(), (ExerciseCursor)cursor);
		setListAdapter(adapter);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// Stop using the cursor (via the adapter)
		setListAdapter(null);
	}
	
	private static class ExerciseListCursorLoader extends SQLiteCursorLoader {
		
		public ExerciseListCursorLoader(Context context) {
			super(context);
		}
		
		@Override
		protected Cursor loadCursor() {
			// Query the list of exercises
			return ExerciseCatalog.get(getContext()).queryExercises();
		}
	}
	
	private static class ExerciseCursorAdapter extends CursorAdapter {
		
		private ExerciseCursor mExerciseCursor;
		
		public ExerciseCursorAdapter(Context context, ExerciseCursor cursor) {
			super(context, cursor, 0);
			mExerciseCursor = cursor;
		}
	
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// Use a layout inflater to get a row view
			LayoutInflater inflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater
					.inflate(R.layout.list_item_exercise, parent, false);
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// Get the exercise for the current row
			Exercise exercise = mExerciseCursor.getExercise();
			
			TextView titleTextView = 
				(TextView)view.findViewById(R.id.exercise_list_item_titleTextView);
			titleTextView.setText(exercise.getTitle());
		}
	}
}