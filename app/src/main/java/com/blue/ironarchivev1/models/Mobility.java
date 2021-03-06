package com.blue.ironarchivev1.models;

import java.util.ArrayList;
import java.util.List;

public class Mobility extends WorkoutItem {

	private String name;
	private int hasDelay, id, reps, routineId, set;
	private long time;
	
	public Mobility() {
		this.reps = 0;
		this.time = 0;
		this.hasDelay = 0;
	}

	public Mobility(Mobility original) {
		this.name = original.getName();
		this.time = original.getTime();
		this.hasDelay = original.getHasDelay();
		this.id = original.getId();
		this.routineId = original.getRoutineId();
		this.reps = original.getReps();
		this.set = original.getSet();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getReps() {
		return reps;
	}
	
	public void setReps(int reps) {
		this.reps = reps;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
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
		attributes.add(String.valueOf(this.reps));
		//attributes.add(String.valueOf(this.routineId));
		return attributes;
	}

	@Override
	public int compareTo(WorkoutItem i) {
		if(this.getTime() == ((Mobility) i).getTime() &&
				this.getHasDelay() == ((Mobility)i).getHasDelay() &&
				this.getReps() == ((Mobility)i).getReps()){
			return 0;
		}
		else if(this.getTime() != ((Mobility) i).getTime() ||
				this.getHasDelay() != ((Mobility)i).getHasDelay() ||
				this.getReps() != ((Mobility)i).getReps()){
			return -1;
		}
		else {
			return 1;
		}
	}
}
