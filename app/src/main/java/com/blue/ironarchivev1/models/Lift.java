package com.blue.ironarchivev1.models;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class Lift extends WorkoutItem {
	private String name;
	private int hasDelay, id, olympicBar, reps, routineId, set;

	long restTime, time;
	double weight;
	SharedPreferences settings;

	public Lift() {
		this.hasDelay = 0;
		this.olympicBar = 0;
		this.reps = 0;
		this.restTime = 0;
		this.time = 0;
		this.weight = 0.0;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getUsesOlympicBar() {
		return olympicBar;
	}

	public void setUsesOlympicBar(int olympicBar) {
		this.olympicBar = olympicBar;
	}

	public int getReps() {
		return reps;
	}
	
	public void setReps(int rep) {
		reps = rep;
	}
	
	public long getRestTime() {
		return restTime;
	}
	
	public void setRestTime(long restTime) {
		this.restTime = restTime;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
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
	
	public List<String> getAttributes(){
		List<String> attributes = new ArrayList<String>();
		//attributes.add(String.valueOf(this.id));
		//attributes.add(this.name);
		attributes.add(String.valueOf(this.weight));	
		attributes.add(String.valueOf(this.reps));
		attributes.add(String.valueOf(this.time));
		attributes.add(String.valueOf(this.restTime));
		//attributes.add(String.valueOf(this.routineId));
		return attributes;
	}
	
	public CharSequence calculateImperialPlatesBar(){
		double platesWeightO1, platesWeightO2;
		
		platesWeightO1 = weight-45;
		platesWeightO2 = weight-45;
		
		int fortyFive = 0, thirtyFive = 0, twentyFive = 0, fifteen = 0, ten = 0, five = 0, twoHalf = 0;
		CharSequence weightsMessage = "";
		
		if(weight != 0){
			while (platesWeightO1 >= 5.0){
				if (platesWeightO1 >= 90) {
			    	fortyFive+=2;
			    	platesWeightO1 -= 90;
				}
				else if (platesWeightO1 >= 50) {
					twentyFive+=2;
					platesWeightO1 -= 50;
				}
				else if (platesWeightO1 >= 30) {
					fifteen+=2;
					platesWeightO1 -= 30;
				}
				else if (platesWeightO1 >= 20) {
					ten+=2;
					platesWeightO1 -= 20;
				}
				else if (platesWeightO1 >= 10) {
					five+=2;
					platesWeightO1 -= 10;
				}
				else if (platesWeightO1 >= 5) {
					twoHalf+=2;
					platesWeightO1 -= 5;
				}
			}
			
			if(fortyFive > 0){
				weightsMessage = fortyFive + "x45, ";
			}
			
			if(twentyFive > 0){
				weightsMessage = TextUtils.concat(weightsMessage, twentyFive + "x25, ");
			}
			
			if(fifteen > 0){
				weightsMessage = TextUtils.concat(weightsMessage, fifteen + "x15, ");
			}
			
			if(ten > 0){
				weightsMessage = TextUtils.concat(weightsMessage, ten + "x10, ");
			}
			
			if(five > 0){
				weightsMessage = TextUtils.concat(weightsMessage, five + "x5, ");
			}
			
			if(twoHalf > 0){
				weightsMessage = TextUtils.concat(weightsMessage, twoHalf + "x2.5, ");
			}
			
			weightsMessage = weightsMessage.subSequence(0, weightsMessage.length()-2);
			
			if(ten == 2 && twentyFive == 2){
				
				weightsMessage = TextUtils.concat(weightsMessage, " or\n");
				
				fortyFive = 0;
				thirtyFive = 0;
				twentyFive = 0;
				fifteen = 0;
				ten = 0;
				five = 0;
				twoHalf = 0;
				
				while (platesWeightO2 >= 5.0){
					if (platesWeightO2 >= 90) {
				    	fortyFive+=2;
				    	platesWeightO2 -= 90;
					}
					else if (platesWeightO2 >= 70) {
						thirtyFive+=2;
						platesWeightO2 -= 70;
					}
					else if (platesWeightO2 >= 50) {
						twentyFive+=2;
						platesWeightO2 -= 50;
					}
					else if (platesWeightO2 >= 30) {
						fifteen+=2;
						platesWeightO2 -= 30;
					}
					else if (platesWeightO2 >= 20) {
						ten+=2;
						platesWeightO2 -= 20;
					}
					else if (platesWeightO2 >= 10) {
						five+=2;
						platesWeightO2 -= 10;
					}
					else if (platesWeightO2 >= 5) {
						twoHalf+=2;
						platesWeightO2 -= 5;
					}
				}
				
				if(fortyFive > 0){
					weightsMessage = TextUtils.concat(weightsMessage, fortyFive + "x45, ");
				}
				
				if(thirtyFive > 0){
					weightsMessage = TextUtils.concat(weightsMessage, thirtyFive + "x35, ");
				}
				
				if(twentyFive > 0){
					weightsMessage = TextUtils.concat(weightsMessage, twentyFive + "x25, ");
				}
				
				if(fifteen > 0){
					weightsMessage = TextUtils.concat(weightsMessage, fifteen + "x15, ");
				}
				
				if(ten > 0){
					weightsMessage = TextUtils.concat(weightsMessage, ten + "x10, ");
				}
				
				if(five > 0){
					weightsMessage = TextUtils.concat(weightsMessage, five + "x5, ");
				}
				
				if(twoHalf > 0){
					weightsMessage = TextUtils.concat(weightsMessage, twoHalf + "x2.5, ");
				}
				
				weightsMessage = weightsMessage.subSequence(0, weightsMessage.length()-2);
				
			}
		}
		
		else{
			weightsMessage = "Invalid Plate Weight";
		}
		
		return weightsMessage;
	}
	
	public CharSequence calculateMetricPlatesBar(){
		double platesWeightO1;
		
		platesWeightO1 = weight-20;
		
		int twenty = 0, fifteen = 0, ten = 0, five = 0, twoHalf = 0, one = 0;
		CharSequence weightsMessage = "";
		
		if(weight != 0){
			while (platesWeightO1 >= 2.0){
				if (platesWeightO1 >= 40) {
					twenty+=2;
			    	platesWeightO1 -= 40;
				}
				else if (platesWeightO1 >= 30) {
					fifteen+=2;
					platesWeightO1 -= 30;
				}
				else if (platesWeightO1 >= 20) {
					ten+=2;
					platesWeightO1 -= 20;
				}
				else if (platesWeightO1 >= 10) {
					five+=2;
					platesWeightO1 -= 10;
				}
				else if (platesWeightO1 >= 5) {
					twoHalf+=2;
					platesWeightO1 -= 5;
				}
				else if (platesWeightO1 >= 2) {
					one+=2;
					platesWeightO1 -= 2;
				}
			}
			
			if(twenty > 0){
				weightsMessage = twenty + "x20, ";
			}
			
			if(fifteen > 0){
				weightsMessage = TextUtils.concat(weightsMessage, fifteen + "x15, ");
			}
			
			if(ten > 0){
				weightsMessage = TextUtils.concat(weightsMessage, ten + "x10, ");
			}
			
			if(five > 0){
				weightsMessage = TextUtils.concat(weightsMessage, five + "x5, ");
			}
			
			if(twoHalf > 0){
				weightsMessage = TextUtils.concat(weightsMessage, twoHalf + "x2.5, ");
			}
			
			if(one > 0){
				weightsMessage = TextUtils.concat(weightsMessage, one + "x1, ");
		}
		
		weightsMessage = weightsMessage.subSequence(0, weightsMessage.length()-2);
		}
		return weightsMessage;
	}
	
	public CharSequence calculateImperialPlatesNoBar(){
		double platesWeightO1, platesWeightO2;
		
		platesWeightO1 = weight;
		platesWeightO2 = weight;
		
		int fortyFive = 0, thirtyFive = 0, twentyFive = 0, fifteen = 0, ten = 0, five = 0, twoHalf = 0;
		CharSequence weightsMessage = "";
		
		if(weight != 0){
			while (platesWeightO1 >= 5.0){
				if (platesWeightO1 >= 45) {
			    	fortyFive++;
			    	platesWeightO1 -= 45;
				}
				else if (platesWeightO1 >= 25) {
					twentyFive++;
					platesWeightO1 -= 25;
				}
				else if (platesWeightO1 >= 15) {
					fifteen++;
					platesWeightO1 -= 15;
				}
				else if (platesWeightO1 >= 10) {
					ten++;
					platesWeightO1 -= 10;
				}
				else if (platesWeightO1 >= 5) {
					five++;
					platesWeightO1 -= 5;
				}
				else if (platesWeightO1 >= 2.5) {
					twoHalf++;
					platesWeightO1 -= 2.5;
				}
			}
			
			if(fortyFive > 0){
				weightsMessage = fortyFive + "x45, ";
			}
			
			if(twentyFive > 0){
				weightsMessage = TextUtils.concat(weightsMessage, twentyFive + "x25, ");
			}
			
			if(fifteen > 0){
				weightsMessage = TextUtils.concat(weightsMessage, fifteen + "x15, ");
			}
			
			if(ten > 0){
				weightsMessage = TextUtils.concat(weightsMessage, ten + "x10, ");
			}
			
			if(five > 0){
				weightsMessage = TextUtils.concat(weightsMessage, five + "x5, ");
			}
			
			if(twoHalf > 0){
				weightsMessage = TextUtils.concat(weightsMessage, twoHalf + "x2.5, ");
			}
			
			weightsMessage = weightsMessage.subSequence(0, weightsMessage.length()-2);
			
			if(ten == 2 && twentyFive == 2){
				
				weightsMessage = TextUtils.concat(weightsMessage, " or\n");
				
				fortyFive = 0;
				thirtyFive = 0;
				twentyFive = 0;
				fifteen = 0;
				ten = 0;
				five = 0;
				twoHalf = 0;
				
				while (platesWeightO2 >= 5.0){
					if (platesWeightO2 >= 45) {
				    	fortyFive++;
				    	platesWeightO2 -= 45;
					}
					else if (platesWeightO2 >= 35) {
						thirtyFive++;
						platesWeightO2 -= 35;
					}
					else if (platesWeightO2 >= 25) {
						twentyFive++;
						platesWeightO2 -= 25;
					}
					else if (platesWeightO2 >= 15) {
						fifteen++;
						platesWeightO2 -= 15;
					}
					else if (platesWeightO2 >= 10) {
						ten++;
						platesWeightO2 -= 10;
					}
					else if (platesWeightO2 >= 5) {
						five++;
						platesWeightO2 -= 5;
					}
					else if (platesWeightO2 >= 2.5) {
						twoHalf++;
						platesWeightO2 -= 2.5;
					}
				}
				
				if(fortyFive > 0){
					weightsMessage = TextUtils.concat(weightsMessage, fortyFive + "x45, ");
				}
				
				if(thirtyFive > 0){
					weightsMessage = TextUtils.concat(weightsMessage, thirtyFive + "x35, ");
				}
				
				if(twentyFive > 0){
					weightsMessage = TextUtils.concat(weightsMessage, twentyFive + "x25, ");
				}
				
				if(fifteen > 0){
					weightsMessage = TextUtils.concat(weightsMessage, fifteen + "x15, ");
				}
				
				if(ten > 0){
					weightsMessage = TextUtils.concat(weightsMessage, ten + "x10, ");
				}
				
				if(five > 0){
					weightsMessage = TextUtils.concat(weightsMessage, five + "x5, ");
				}
				
				if(twoHalf > 0){
					weightsMessage = TextUtils.concat(weightsMessage, twoHalf + "x2.5, ");
				}
				
				weightsMessage = weightsMessage.subSequence(0, weightsMessage.length()-2);
				
			}
		}
		
		else{
			weightsMessage = "Invalid Plate Weight";
		}
		
		return weightsMessage;
	}
	
	public CharSequence calculateMetricPlatesNoBar(){
		double platesWeightO1;
		
		platesWeightO1 = weight;
		
		int twenty = 0, fifteen = 0, ten = 0, five = 0, twoHalf = 0, one = 0;
		CharSequence weightsMessage = "";
		
		if(weight != 0){
			while (platesWeightO1 >= 2.0){
				if (platesWeightO1 >= 20) {
					twenty++;
			    	platesWeightO1 -= 20;
				}
				else if (platesWeightO1 >= 15) {
					fifteen++;
					platesWeightO1 -= 15;
				}
				else if (platesWeightO1 >= 10) {
					ten++;
					platesWeightO1 -= 10;
				}
				else if (platesWeightO1 >= 5) {
					five++;
					platesWeightO1 -= 5;
				}
				else if (platesWeightO1 >= 2.5) {
					twoHalf++;
					platesWeightO1 -= 2.5;
				}
				else if (platesWeightO1 >= 1) {
					one++;
					platesWeightO1--;
				}
			}
			
			if(twenty > 0){
				weightsMessage = twenty + "x20, ";
			}
			
			if(fifteen > 0){
				weightsMessage = TextUtils.concat(weightsMessage, fifteen + "x15, ");
			}
			
			if(ten > 0){
				weightsMessage = TextUtils.concat(weightsMessage, ten + "x10, ");
			}
			
			if(five > 0){
				weightsMessage = TextUtils.concat(weightsMessage, five + "x5, ");
			}
			
			if(twoHalf > 0){
				weightsMessage = TextUtils.concat(weightsMessage, twoHalf + "x2.5, ");
			}
			
			if(one > 0){
				weightsMessage = TextUtils.concat(weightsMessage, one + "x1, ");
		}
		
		weightsMessage = weightsMessage.subSequence(0, weightsMessage.length()-2);
		}
		return weightsMessage;
	}

	@Override
	public int compareTo(WorkoutItem i) {
		if(this.getTime() == ((Lift) i).getTime() &&
				this.getHasDelay() == ((Lift)i).getHasDelay() &&
				this.getReps() == ((Lift)i).getReps() &&
				this.getWeight() == ((Lift)i).getWeight() &&
				this.getUsesOlympicBar() == ((Lift)i).getUsesOlympicBar()){
			return 0;
		}
		else if(this.getTime() != ((Lift) i).getTime() ||
				this.getHasDelay() != ((Lift)i).getHasDelay() ||
				this.getReps() != ((Lift)i).getReps() ||
				this.getWeight() != ((Lift)i).getWeight() ||
				this.getUsesOlympicBar() != ((Lift)i).getUsesOlympicBar()){
			return -1;
		}
		else {
			return 1;
		}
	}
}
