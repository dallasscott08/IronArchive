package com.blue.ironarchivev1.db;

import com.blue.ironarchivev1.models.WorkoutItem;
import java.util.List;

public interface WorkoutItemDAO {

	abstract WorkoutItem getWorkoutItem(int rowId);
	abstract boolean updateWorkoutItem(WorkoutItem rowId);
	abstract boolean deleteWorkoutItem(WorkoutItem rowId);
	abstract List<WorkoutItem> getRoutineDuplicates(WorkoutItem oldValues);
	abstract void updateLinkedItems(WorkoutItem oldItem, WorkoutItem newItem);
}
