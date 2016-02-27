package com.blue.ironarchivev1.models;

import java.util.ArrayList;
import java.util.List;

public class Routine{
	private String linkedRoutines, name;
	private int id;

	public String getLinkedRoutines() {
		return linkedRoutines;
	}

	public void setLinkedRoutines(String linkedRoutines) {
		this.linkedRoutines = linkedRoutines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public List<String> getAttributes() {
		List<String> attributes = new ArrayList<String>();
		attributes.add(String.valueOf(this.id));
		attributes.add(this.name);
		return attributes;
	}

	public void setSet(int set) {
		// TODO Auto-generated method stub
		
	}

	public int getSet() {
		// TODO Auto-generated method stub
		return 0;
	}
}
