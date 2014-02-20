package com.zachlowe.android.fitnessplanner;

import java.util.UUID;

public class Exercise {
	
	private UUID mId;
	private String mTitle;
	private String mDescription;
	
	public Exercise() {
		mId = UUID.randomUUID();
		mTitle = null;
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
	
	public UUID getId() {
		return mId;
	}
}
