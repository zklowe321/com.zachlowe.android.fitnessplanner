package com.zachlowe.android.fitnessplanner;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.zachlowe.android.fitnessplanner.DatabaseHelper.ExerciseCursor;

public class ExercisePagerActivity extends FragmentActivity
	implements ExerciseFragment.Callbacks, LoaderCallbacks<Cursor> {
	
	private static final String TAG = "ExercisePagerActivity";
	
	/** A key for passing an exercise ID as a long */
	public static final String EXTRA_EXERCISE_ID =
			"com.zachlowe.android.fitnessplanner.exercise_id";

	private ViewPager mViewPager;
	private ExerciseCursor mCursor;
	
	public void onExerciseUpdated(Exercise exercise) { }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize the loader to load the ExerciseCursor
		getSupportLoaderManager().initLoader(0, null, this).forceLoad();
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop called");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart called");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSupportLoaderManager().restartLoader(0, null, this);
		Log.d(TAG, "onResume called");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause called");
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart called");
	}
		
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(TAG, "onCreateLoader called");
		return new ExercisePagerCursorLoader(this);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.d(TAG, "onLoadFinished called");
		mCursor = (ExerciseCursor)cursor;
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
				Log.d(TAG, "getItem() called");
				updateUI(pos);
				return ExerciseFragment.newInstance(mCursor.getExercise().getId());
			}

			@Override
			public int getCount() {
				return mCursor.getCount();
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				Log.d(TAG, "onPageSelected called");
				updateUI(pos);
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
				Log.d(TAG, "onPageScrolled called");
				updateUI(pos);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				Log.d(TAG, "onPageScrollStateChanged called");
			}
		});
		
		long exerciseId = (long)getIntent()
				.getLongExtra(EXTRA_EXERCISE_ID, -1);
		mCursor.moveToFirst();
		
		for (int i = 0; i < mCursor.getCount(); i++) {
			if (mCursor.getExercise().getId() == exerciseId) {
				mViewPager.setCurrentItem(i);
				break;
			} else {
				mCursor.moveToNext();
			}
		} 
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "onLoaderReset called");
		// Do nothing
	}
	
	public void updateUI(int pos) {
		mCursor.moveToPosition(pos);
		Exercise exercise = mCursor.getExercise();
		if (exercise.getTitle() != null)
			setTitle(exercise.getTitle());
	}
	
	/**
	 * Inner class to load cursor from SQLiteDatabase
	 */
	private static class ExercisePagerCursorLoader extends SQLiteCursorLoader {
		
		public ExercisePagerCursorLoader(Context context) {
			super(context);
		}
		
		@Override
		protected Cursor loadCursor() {
			Log.d(TAG, "loadCursor() called");
			// Query list of exercises
			return ExerciseCatalog.get(getContext()).queryExercises();
		}
	}
}