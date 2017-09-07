package com.blue.ironarchivev1.models;

import java.util.List;

public abstract class WorkoutItem  implements Comparable<WorkoutItem>, Cloneable{

	public abstract List<String> getAttributes();

	public abstract int getSet();
	public abstract void setSet(int set);
	public abstract String getName();
	public abstract void setName(String name);
	public abstract int getId();
	public abstract void setId(int id);
	public abstract int getRoutineId();
	public abstract int compareTo(WorkoutItem s);
}
