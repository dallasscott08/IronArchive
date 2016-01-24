package com.blue.ironarchivev1.models;

import java.util.ArrayList;
import java.util.List;

public class Stretch extends WorkoutItem {

	String name;
	long time;
	int hasDelay, id, routineId, set;
	
	public Stretch() {
		this.time = 0;
		this.hasDelay = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getHasDelay() {
		return hasDelay;
	}

	public void setHasDelay(int delay) {
		this.hasDelay = delay;
	}
		
	public int getRoutineId() {
		return routineId;
	}

	public void setRoutineId(int routineId) {
		this.routineId = routineId;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	@Override
	public List<String> getAttributes() {
		List<String> attributes = new ArrayList<String>();
		//attributes.add(String.valueOf(this.id));
		//attributes.add(this.name);
		attributes.add(String.valueOf(this.time));
		//attributes.add(String.valueOf(this.routineId));
		return attributes;
	}
	
}
