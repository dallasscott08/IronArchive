package com.blue.ironarchivev1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class Stretch extends WorkoutItem {

	private String name;
	private long time;
	private int hasDelay, id, routineId, set;
	
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

	@Override
	public int compareTo(WorkoutItem s){
		if(this.getTime() == ((Stretch) s).getTime() &&
				this.getHasDelay() == ((Stretch)s).getHasDelay()) {
			return 0;
		}
		else if(this.getTime() != ((Stretch) s).getTime() ||
				this.getHasDelay() != ((Stretch)s).getHasDelay()) {
			return -1;
		}else{
			return 1;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
