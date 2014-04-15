package com.zachlowe.android.fitnessplanner;

public class Routine {

	private long mId;
	private String mTitle;
	private String mDescription;
	
	public Routine() {
		mId = -1;
		mTitle = null;
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

}
