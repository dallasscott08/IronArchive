package com.blue.ironarchivev1.db;

import com.blue.ironarchivev1.models.WorkoutItem;

public interface WorkoutItemDAO {

	abstract WorkoutItem getWorkoutItem(int rowId);
	abstract boolean updateWorkoutItem(WorkoutItem rowId);
	abstract boolean deleteWorkoutItem(WorkoutItem rowId);
}
