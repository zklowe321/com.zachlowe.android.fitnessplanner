/**
 *	FragmentActivity that shows specific ExerciseFragments and lets users
 *	swipe through them
 */
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
	public void onResume() {
		super.onResume();
		getSupportLoaderManager().restartLoader(0, null, this);
	}
		
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new ExercisePagerCursorLoader(this);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mCursor = (ExerciseCursor)cursor;
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
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
				updateUI(pos);
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
				updateUI(pos);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) { }
		});
		
		long exerciseId = (long)getIntent()
				.getLongExtra(EXTRA_EXERCISE_ID, -1);
		mCursor.moveToFirst();
		
		for (int i = 0; i < mCursor.getCount(); i++) {
			try {
				if (mCursor.getExercise().getId() == exerciseId) {
					mViewPager.setCurrentItem(i);
					break;
				} else {
					mCursor.moveToNext();
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
		} 
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
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
			// Query list of exercises
			return ExerciseCatalog.get(getContext()).queryExercises();
		}
	}
}