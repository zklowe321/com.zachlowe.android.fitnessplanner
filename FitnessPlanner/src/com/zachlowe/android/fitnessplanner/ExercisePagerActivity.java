package com.zachlowe.android.fitnessplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.zachlowe.android.fitnessplanner.ExerciseDatabaseHelper.ExerciseCursor;

public class ExercisePagerActivity extends FragmentActivity
	implements ExerciseFragment.Callbacks {
	
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
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mCursor = ExerciseCatalog.get(this).queryExercises();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
				mCursor.moveToPosition(pos);
				Exercise exercise = mCursor.getExercise();
				return ExerciseFragment.newInstance(exercise.getId());
			}

			@Override
			public int getCount() {
				return mCursor.getCount();
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				mCursor.moveToPosition(pos);
				Exercise exercise = mCursor.getExercise();
				if (exercise.getTitle() != null)
					setTitle(exercise.getTitle());
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
			
			@Override
			public void onPageScrollStateChanged(int state) { }
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
}