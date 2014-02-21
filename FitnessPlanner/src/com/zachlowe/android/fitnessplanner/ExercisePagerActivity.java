package com.zachlowe.android.fitnessplanner;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class ExercisePagerActivity extends FragmentActivity
	implements ExerciseFragment.Callbacks {
	
	private static final String TAG = "ExercisePagerActivity";

	private ViewPager mViewPager;
	private ArrayList<Exercise> mExercises;
	
	public void onExerciseUpdated(Exercise exercise) { }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mExercises = ExerciseCatalog.get(this).getExercises();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public Fragment getItem(int pos) {
				Exercise exercise = mExercises.get(pos);
				return ExerciseFragment.newInstance(exercise.getId());
			}

			@Override
			public int getCount() {
				return mExercises.size();
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				Exercise exercise = mExercises.get(pos);
				if (exercise.getTitle() != null)
					setTitle(exercise.getTitle());
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
			
			@Override
			public void onPageScrollStateChanged(int state) { }
		});
		
		long exerciseId = (long)getIntent()
				.getLongExtra(ExerciseFragment.EXTRA_EXERCISE_ID, -1);
		for (int i = 0; i < mExercises.size(); i++) {
			if (mExercises.get(i).getId() == exerciseId) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}