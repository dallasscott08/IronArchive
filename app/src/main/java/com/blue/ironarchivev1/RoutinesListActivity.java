package com.blue.ironarchivev1;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blue.ironarchivev1.db.RoutineDAO;
import com.blue.ironarchivev1.dialogfragments.AddRoutineDialog;
import com.blue.ironarchivev1.dialogfragments.UpdateRoutineDialog;
import com.blue.ironarchivev1.models.Routine;

public class RoutinesListActivity extends FragmentActivity {

	private RoutineDAO routineDAO;
	private List<Routine> routineList;
	private int routineID;
	private static int blue, white, transparent;
	private RoutineArrayAdapter adapter;
	private ListView routineListView;
	private boolean linkingRoutines;
	private Routine parentRoutine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routine_list);
		Bundle extras = getIntent().getExtras();
		blue = getResources().getColor(R.color.blue);
		white = getResources().getColor(R.color.white);
		transparent = getResources().getColor(android.R.color.transparent);

		routineID = extras.getInt("routineID", 1);
		
		routineDAO = new RoutineDAO(this);
		parentRoutine = routineDAO.getWorkoutItem(routineID);
		
		if(!routineDAO.getWorkoutItem(routineID).getName().equals("Default")){
			setTitle(getResources().getString(R.string.app_name) + "   -   " + routineDAO.getWorkoutItem(routineID).getName());
		}
		
		routineList = routineDAO.getAllItems();
		
		routineListView = (ListView) findViewById(R.id.routineList);

		Button addItem = (Button) findViewById(R.id.addItem_button);
		addItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment mFragment = new AddRoutineDialog();
				mFragment.show(getSupportFragmentManager(), "add_routine_item");
			}
		});

		Button linkRoutines = (Button) findViewById(R.id.linkRoutines_button);
		linkRoutines.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(linkingRoutines){
					linkingRoutines = false;
					clearListItemColors();
				}
				else {
					linkingRoutines = true;
					setLinkedColors(parentRoutine);
				}
			}
		});
		
		adapter = new RoutineArrayAdapter(this,
		        R.layout.item_routine_name, routineList);
		
		routineListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
				DialogFragment mFragment = null;
				
				Bundle args = new Bundle();
				args.putInt("id", routineList.get(position).getId());

				mFragment = new UpdateRoutineDialog();
				mFragment.setArguments(args);
				mFragment.show(getSupportFragmentManager(), "update_routine_item");
				
				//setTitle(getResources().getString(R.string.app_name) + "   -   " + routineDAO.getWorkoutItem(routineID).getName());
				
				return false;
			}
		});
		
		routineListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(linkingRoutines){
					Routine selectedRoutine = routineList.get(position);

					if(selectedRoutine.getId() != parentRoutine.getId()) {
						changeRoutineColor(view, selectedRoutine);
					}

					routineDAO.updateWorkoutItem(selectedRoutine);
				}
				else {
					routineID = routineList.get(position).getId();
					parentRoutine = routineDAO.getWorkoutItem(routineID);
					setTitle(getResources().getString(R.string.app_name) + "   -   " + routineDAO.getWorkoutItem(routineID).getName());
				}
			}
			
		});
		
		routineListView.setAdapter(adapter);
	}
	
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("routineID", routineID);
		setResult(RESULT_OK, data);
		super.finish();
	}

	public void refreshList(boolean updateRoutineID){
		adapter.clear();
		routineList = routineDAO.getAllItems();
		adapter.addAll(routineList);
		adapter.notifyDataSetChanged();
		routineListView.setAdapter(adapter);
		if(updateRoutineID){
		routineID = routineList.get(routineList.size()-1).getId();
		setTitle(getResources().getString(R.string.app_name) + "   -   " + routineDAO.getWorkoutItem(routineID).getName());
		}
	}

	public void setRoutineAfterEndOfListDelete(){
		Routine lastItem = routineDAO.getAllItems().get(routineDAO.getAllItems().size()-1);
		setTitle(getResources().getString(R.string.app_name) + "   -   " + lastItem.getName());
		routineID = lastItem.getId();
	}
	
	public void replaceRoutineAfterDelete(){
		List<Routine> routineList = routineDAO.getAllItems();
		for(int i = 0; i < routineList.size(); i++){
			if(routineList.get(i).getId() > routineID){
				setTitle(getResources().getString(R.string.app_name) + "   -   " + routineList.get(i).getName());
				routineID = routineList.get(i).getId();
				break;
			}
		}
	}

	private void setLinkedColors(Routine parentRoutine){
		for(int i = 0; i < routineListView.getCount();i++) {
			if(routineList.get(i).getId() == parentRoutine.getId()){
				toggleColor(routineListView.getChildAt(i), white, blue);
			}
			else if(routineList.get(i).getLinkedRoutineId() == parentRoutine.getLinkedRoutineId()) {
				toggleColor(routineListView.getChildAt(i), white, blue);
			}
		}
	}

	private void clearListItemColors(){
		for(int i = 0; i < routineListView.getCount();i++) {
			toggleColor(routineListView.getChildAt(i), blue, transparent);
		}
	}

	private void toggleColor(View v, int txtColor, int bgColor){
		TextView text = (TextView) v.findViewById(R.id.routineItemTextView);
		text.setBackgroundColor(bgColor);
		text.setTextColor(txtColor);
	}

	private void changeRoutineColor(View view, Routine routine){
		if(parentRoutine.getLinkedRoutineId() == 0){
			toggleColor(view, white, blue);
			routine.setLinkedRoutineId(parentRoutine.getId());
		}
		else if(routine.getLinkedRoutineId() == parentRoutine.getId() ||
				routine.getLinkedRoutineId() == parentRoutine.getLinkedRoutineId()){
			toggleColor(view, blue, transparent);
			routine.setLinkedRoutineId(0);
		}
		else{
			toggleColor(view, white, blue);
			routine.setLinkedRoutineId(parentRoutine.getLinkedRoutineId());
		}
	}

	@Override
	protected void onResume() {
		refreshList(false);
		super.onResume();
	}

	private class RoutineArrayAdapter extends ArrayAdapter<Routine>{
		
		Activity context;
	    int layoutResourceId; 
	    List<Routine> routines;
	    
		public RoutineArrayAdapter(Context context, int resource,
				List<Routine> objects) {
			super(context, resource, objects);
			this.context = (Activity) context;
			this.layoutResourceId = resource;
			this.routines = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater inflater = context.getLayoutInflater();
				convertView = inflater.inflate(layoutResourceId, parent, false);
				
				TextView routineName = (TextView) convertView.findViewById(R.id.routineItemTextView);
				routineName.setText(routines.get(position).getName());
			}
			
			return convertView;
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
	}
	
}
