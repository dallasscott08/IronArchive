package com.blue.ironarchivev1.util;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.models.Lift;
import com.blue.ironarchivev1.models.WorkoutItem;

public class WorkoutListAdapter extends BaseExpandableListAdapter {

	private List<WorkoutItem> groupItemList;
	private HashMap<WorkoutItem, List<String>> groupItemMap;
	private Activity activity;
	private int listType;
	private SharedPreferences settings;
	
	public WorkoutListAdapter(Activity context, List<WorkoutItem> itemList, int listType){
		activity = context;
		groupItemList = itemList;
		this.listType = listType;
		groupItemMap = new HashMap<WorkoutItem, List<String>>();
		for(WorkoutItem wi: groupItemList){
			groupItemMap.put(wi, wi.getAttributes());
		}
		settings = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	@Override
	public int getGroupCount() {
		return groupItemList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupItemList.get(groupPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupItemMap.get(groupItemList.get(groupPosition))
                .get(childPosition);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return groupItemMap.get(groupItemList.get(groupPosition))
                .size();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(R.layout.item_list_group_header, null);
		}
		
		String olympic = "";

		if(groupItemList.get(groupPosition) instanceof Lift){			
			Lift currentLift = (Lift) groupItemList.get(groupPosition);
			int usesOlympicBar = currentLift.getUsesOlympicBar();
			if(usesOlympicBar == 1){
				olympic = " - Olympic";
			}
		}
		
		TextView itemName = (TextView) convertView.findViewById(R.id.listItemTextView);
		itemName.setText(groupItemList.get(groupPosition).getName()
				+" - ("+ groupItemList.get(groupPosition).getSet() + ")" + olympic);
				
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		String childAttribute = (String) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(R.layout.item_expanded_group, parent, false);
		}
				
		TextView name = (TextView) convertView.findViewById(R.id.childitemname);
		name.setFocusable(false);
		TextView value = (TextView) convertView.findViewById(R.id.childitemvalue);
		value.setFocusable(false);
		
		if(listType == 0){
			name.setText("Time (sec)");
		}
		
		if(listType == 1 || listType == 2){
			if(childPosition == 0)
				name.setText("Time (sec)");
			else
				name.setText("Repetitions");
		}
		if(listType == 3){									
			if(childPosition == 0){
				if(settings.getString("PREF_MEASUREMENT_SYSTEM", "imp").equals("imp")){
					name.setText("Weight (lbs)");
				}
				else{
					name.setText("Weight (kgs)");
				}
			}
			else if(childPosition == 1)
				name.setText("Repetitions");
			else if(childPosition == 2)
				name.setText("Time (sec)");
			else if(childPosition == 3)
				name.setText("Rest Time (sec)");
		}
		
		value.setText(childAttribute);
		convertView.setClickable(false);
		return convertView;	
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	public void updateData(List<WorkoutItem> itemList){
		groupItemList = itemList;
		groupItemMap = new HashMap<WorkoutItem, List<String>>();
		for(WorkoutItem wi: groupItemList){
			groupItemMap.put(wi, wi.getAttributes());
		}
		notifyDataSetChanged();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

			 /*if(listType == 0 && childPosition == 0){
				 return true;
			 }

	    	 if(listType == 1 && childPosition == 0){
	    		 return true;
			 }

	    	 if(listType == 2 && childPosition == 0){
	    		 return true;
			 }

	    	 if(listType == 3 && childPosition == 2){
	    		 return true;
			 }
	    	 
	    	 if(listType == 3 && childPosition == 3){
	    		 return true;
			 }*/
	    	 
		return true;
	}
				
}