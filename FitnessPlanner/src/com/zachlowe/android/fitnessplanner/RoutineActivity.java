package com.zachlowe.android.fitnessplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class RoutineActivity extends SingleFragmentActivity
	implements RoutineFragment.Callbacks, RoutineDetailFragment.Callbacks {
	
	private static final String TAG = "RoutineActivity";
	
	public static final String EXTRA_ROUTINE_ID =
			"com.zachlowe.android.fitnessplanner.routine_id";
	public static final String EXTRA_ADD_EXERCISE =
			"com.zachlowe.android.fitnessplanner.add_exercise";
	private static final int REQUEST_EXERCISE = 1;

	@Override
	protected Fragment createFragment() {
		return RoutineDetailFragment.newInstance( getIntent().getLongExtra(EXTRA_ROUTINE_ID, -1) );
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.activity_twopane;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		ft.add(R.id.detailFragmentContainer, RoutineFragment.newInstance( getIntent().getLongExtra(EXTRA_ROUTINE_ID, -1)) );
		ft.commit();
	}
	
	public void onRoutineExerciseSelected(RoutineExercise routineExercise) {
		
	}

	public void onAddExerciseSelected(long routineId) {
		Intent i = new Intent(this, ExerciseListActivity.class);
		i.putExtra(EXTRA_ROUTINE_ID, routineId);
		i.putExtra(EXTRA_ADD_EXERCISE, 1);
		startActivityForResult(i, REQUEST_EXERCISE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_EXERCISE) {
			long exerciseId = data.getLongExtra(
					ExerciseListActivity.EXTRA_EXERCISE_ID,-1);
			
			long routineId = data.getLongExtra(EXTRA_ROUTINE_ID, -1);
			
			Log.d(TAG, "routineId: " + routineId);
			
			RoutineExerciseCatalog.get(this).insertRoutineExercise(routineId, exerciseId);
		} 
	}
	
	public void onRoutineUpdated(Routine routine) {
		
	}
	

}
