package com.zachlowe.android.fitnessplanner;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor> {
	private static final String TAG = "SQLiteCursorLoader";
	
	private Cursor mCursor;
	
	public SQLiteCursorLoader(Context context) {
		super(context);
	}
	
	protected abstract Cursor loadCursor();
	
	@Override
	public Cursor loadInBackground() {
		Log.d(TAG, "loadInBackground() called");
		
		Cursor cursor = loadCursor();
		if (cursor != null) {
			Log.d(TAG, "cursor is not null");
			// Ensure that the content window is filled
			cursor.getCount();
		}
		return cursor;
	}
	
	@Override
	public void deliverResult(Cursor data) {
		Log.d(TAG, "deliverResult called");
		
		Cursor oldCursor = mCursor;
		mCursor = data;
		if (isStarted()) {
			Log.d(TAG, "isStarted");
			super.deliverResult(data);
		}
		
		if (oldCursor != null && oldCursor != data && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}
	
	@Override
	protected void onStartLoading() {
		Log.d(TAG, "onStartLoading() called");
		
		if (mCursor != null) {
			Log.d(TAG, "cursor is not null");
			deliverResult(mCursor);
		}
		
		if (takeContentChanged() || mCursor == null) {
			Log.d(TAG, "forceload() called");
			forceLoad();
		}
	}
	
	@Override
	protected void onStopLoading() {
		Log.d(TAG, "onStopLoading() called");
		// Attempt to cancel the current load task if possible
		cancelLoad();
	}
	
	@Override
	public void onCanceled(Cursor cursor) {
		Log.d(TAG, "onCanceled called");
		
		if (cursor != null && !cursor.isClosed()) {
			Log.d(TAG, "passed cursor is not null and not closed");
			cursor.close();
		}
	}
	
	@Override
	protected void onReset() {
		Log.d(TAG, "onReset() called");
		
		super.onReset();
		
		// Ensure the loader is stopped
		onStopLoading();
		
		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		
		mCursor = null;
	}
}