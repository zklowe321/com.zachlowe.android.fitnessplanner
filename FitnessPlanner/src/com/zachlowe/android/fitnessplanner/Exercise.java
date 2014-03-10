package com.zachlowe.android.fitnessplanner;

public class Exercise {
	
	private long mId;
	private String mTitle;
	private String mDescription;
	
	public Exercise() {
		mId = -1;
		mTitle = null;
		mDescription = null;
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
	
	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}
}
