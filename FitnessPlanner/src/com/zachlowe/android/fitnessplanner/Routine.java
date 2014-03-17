package com.zachlowe.android.fitnessplanner;

import java.util.ArrayList;

public class Routine {

	private long mId;
	private String mTitle;
	private String mDescription;
	private ArrayList<RoutineExercise> mExercises;
	
	public Routine() {
		mId = -1;
		mTitle = null;
		mExercises = new ArrayList<RoutineExercise>();
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public ArrayList<RoutineExercise> getExercises() {
		return mExercises;
	}

	public void setExercises(ArrayList<RoutineExercise> exercises) {
		mExercises = exercises;
	}
}
