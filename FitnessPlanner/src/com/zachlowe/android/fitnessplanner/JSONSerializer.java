package com.zachlowe.android.fitnessplanner;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zachlowe.android.fitnessplanner.DatabaseHelper.RoutineCursor;
import com.zachlowe.android.fitnessplanner.DatabaseHelper.RoutineExerciseCursor;

public class JSONSerializer {
	private static final String TAG = "JSONSerializer";
	
	private long mRoutineId;
	private Context mContext;
	
// /data/data/<Your-Application-Package-Name>/databases/<your-database-name>
	
	@SuppressLint("SdCardPath")
	private static final String DB_NAME = "fitnessplanner.sqlite";
	private static final String ROUTINE_QUERY = "select * from routine where _id = ?";
	
	private final String RE_QUERY_ROUTINE =
			"select * from routine_exercise inner join exercise " +
			"on routine_exercise.e_id = exercise._id " +
			"where routine_exercise.r_id = ?";

	public JSONSerializer(long routineId, Context context) {
		mRoutineId = routineId;
		mContext = context;
		Log.d(TAG, "routineId: " + mRoutineId);
	}
	
	public String toJSON() {
		try {
			String path = mContext.getDatabasePath(DB_NAME).getAbsolutePath();
			
			// Open the fitnessplanner database
			SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			
			if (db.isOpen())
				Log.d(TAG, "database is open");
		
			Cursor cursor = db.query("routine",
					null, // All columns
					"_id = ?", // Look for a routine ID
					new String[]{ String.valueOf(mRoutineId) }, // with this value
					null,	// group by
					null,	// order by
					null,	// having
					"1");	// limit one row
			
			// Get the routine from the database
			//Cursor cursor = db.rawQuery(ROUTINE_QUERY, new String[]{ String.valueOf(mRoutineId) });
			RoutineCursor routineCursor = new RoutineCursor(cursor);
			
			Log.d(TAG, "count: " + routineCursor.getCount());
			if (routineCursor.isBeforeFirst() || routineCursor.isAfterLast())
				routineCursor.moveToFirst();
			
			Routine routine = routineCursor.getRoutine();
		
			if (routine == null)
				Log.d(TAG, "routine null");
			
			// Add routine values to the JSONObject
			JSONObject returnObject = new JSONObject();
			returnObject.put("routineTitle", routine.getTitle());
			returnObject.put("routineDescription", routine.getDescription());
			
			// Create an array to hold routineExercies associated with the routine
			JSONArray routineExercises = new JSONArray();
			cursor = db.rawQuery(RE_QUERY_ROUTINE, new String[]{ String.valueOf(mRoutineId) });
			RoutineExerciseCursor routineExerciseCursor = new RoutineExerciseCursor(cursor);
			
			routineExerciseCursor.moveToFirst();
			while (!routineExerciseCursor.isAfterLast()) {
				JSONObject currentRow = new JSONObject();
				RoutineExercise routineExercise = routineExerciseCursor.getRoutineExercise();
				
				// Make sure the routineExercise isn't null
				if (routineExercise == null) {
					Log.d(TAG, "routineExercise null");
					break;
				} else {
					Log.d(TAG, "routineExercise not null");
				}
				
				// Insert values into object for the current routineExercise
				currentRow.put("re_title", routineExercise.getTitle());
				currentRow.put("re_description", routineExercise.getDescription());
				currentRow.put("re_sets", routineExercise.getSets());
				currentRow.put("re_reps", routineExercise.getReps());
				
				// Add the row to the array and move cursor forward
				routineExercises.put(currentRow);
				routineExerciseCursor.moveToNext();
			}
			
			// Add routineExercise array to the JSONObject
			returnObject.put("routineExercises", routineExercises);
			
			// Close cursors
			routineCursor.close();
			routineExerciseCursor.close();
			
			// Return JSON formatted String to calling function
			return returnObject.toString();
		} catch (NullPointerException npe) {
			Log.d(TAG, npe.toString());
			return null;
		} catch (Exception e) {
			Log.d(TAG, e.toString());
			return null;
		} 
	}
	
}
