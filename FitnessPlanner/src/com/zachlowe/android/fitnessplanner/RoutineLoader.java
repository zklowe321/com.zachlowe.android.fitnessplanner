package com.zachlowe.android.fitnessplanner;

import android.content.Context;

public class RoutineLoader extends DataLoader<Routine> {

	private long mRoutineId;
	
	public RoutineLoader(Context context, long routineId) {
		super(context);
		mRoutineId = routineId;
	}

	@Override
	public Routine loadInBackground() {
		return RoutineCatalog.get(getContext()).getRoutine(mRoutineId);
	}
	
}
