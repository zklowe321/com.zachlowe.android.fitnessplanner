package com.zachlowe.android.fitnessplanner;

public class RoutineExercise extends Exercise {

	private int mSets;
	private int mReps;
	private long mRoutineId;
	
	public RoutineExercise(long routineId) {
		super();
		mSets = 0;
		mReps = 0;
		mRoutineId = routineId;
	}

	public int getSets() {
		return mSets;
	}

	public void setSets(int sets) {
		mSets = sets;
	}

	public int getReps() {
		return mReps;
	}

	public void setReps(int reps) {
		mReps = reps;
	}
	
	public long getRoutineId() {
		return mRoutineId;
	}
	
	public void setRoutineId(int routineId) {
		mRoutineId = routineId;
	}
}
