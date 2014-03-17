package com.zachlowe.android.fitnessplanner;

public class RoutineExercise extends Exercise {

	private int mSets;
	private int mReps;
	
	public RoutineExercise() {
		super();
		mSets = 0;
		mReps = 0;
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
}
