package com.blue.ironarchivev1.models;

import java.util.List;

public abstract class WorkoutItem {

	public abstract List<String> getAttributes();
	
	public abstract String getName();
	public abstract void setId(int id);
	public abstract int getId();
	public abstract void setSet(int set);
	public abstract int getSet();
}
