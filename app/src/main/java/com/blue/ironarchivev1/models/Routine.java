package com.blue.ironarchivev1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Routine {
	private String name;
	private int id;
	private Set<Integer> linkedRoutines;

	public Set<Integer> getLinkedRoutines() {
		return linkedRoutines;
	}

	public void setLinkedRoutines(Set<Integer> linkedRoutines) {
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

	@Override
	protected Routine clone() throws CloneNotSupportedException{
		return(Routine) super.clone();
	}
}
