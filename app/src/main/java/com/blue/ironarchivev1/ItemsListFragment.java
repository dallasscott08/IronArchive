package com.blue.ironarchivev1;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.blue.ironarchivev1.db.LiftDAO;
import com.blue.ironarchivev1.db.MobilityDAO;
import com.blue.ironarchivev1.db.StretchDAO;
import com.blue.ironarchivev1.db.WarmupDAO;
import com.blue.ironarchivev1.dialogfragments.AddLiftItemDialog;
import com.blue.ironarchivev1.dialogfragments.AddMobilityItemDialog;
import com.blue.ironarchivev1.dialogfragments.AddStretchItemDialog;
import com.blue.ironarchivev1.dialogfragments.AddWarmupItemDialog;
import com.blue.ironarchivev1.dialogfragments.UpdateLiftItemDialog;
import com.blue.ironarchivev1.dialogfragments.UpdateMobilityItemDialog;
import com.blue.ironarchivev1.dialogfragments.UpdateStretchItemDialog;
import com.blue.ironarchivev1.dialogfragments.UpdateWarmupItemDialog;
import com.blue.ironarchivev1.models.Lift;
import com.blue.ironarchivev1.models.Mobility;
import com.blue.ironarchivev1.models.Stretch;
import com.blue.ironarchivev1.models.Warmup;
import com.blue.ironarchivev1.models.WorkoutItem;
//import com.blue.ironarchivev1.util.TimerFragment;
import com.blue.ironarchivev1.util.WorkoutListAdapter;

public class ItemsListFragment extends Fragment{
	
		private int listType = 0, routineId = 1;
		private Button addItem;
		private ExpandableListView expandList;
		Activity activity;
		private LiftDAO liftDAO;
		private MobilityDAO mobilityDAO;
		private StretchDAO stretchDAO;
		private WarmupDAO warmupDAO;
		private List<WorkoutItem> itemsList;
		private WorkoutListAdapter adapter;
		private ItemsListFragment thisFrag = this;
		private SharedPreferences settings;
		private String system;
		
		private static final String TAG = "Item List";

		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
			settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
			if (savedInstanceState != null) {
		    	listType = savedInstanceState.getInt("list_type");
		    	routineId = savedInstanceState.getInt("routineId");
		    	activity = getActivity();
		    	liftDAO = new LiftDAO(activity, routineId);
		    	mobilityDAO = new MobilityDAO(activity, routineId);
		    	stretchDAO = new StretchDAO(activity, routineId);
		    	warmupDAO = new WarmupDAO(activity, routineId);
		    }
		    else{
		    	activity = getActivity();
		    	liftDAO = new LiftDAO(activity, routineId);
		    	mobilityDAO = new MobilityDAO(activity, routineId);
		    	stretchDAO = new StretchDAO(activity, routineId);
		    	warmupDAO = new WarmupDAO(activity, routineId);
		    }
		  }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View mList = inflater.inflate(R.layout.fragment_workout_item_list, container, false);

			expandList = (ExpandableListView) mList.findViewById(R.id.expListView);
			addItem = (Button) mList.findViewById(R.id.addItem_button);
			
			return mList;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        	        	        
	        getItemsList();
	        
	        adapter = new WorkoutListAdapter(getActivity(), itemsList, listType);
			expandList.setAdapter(adapter);
			
	        addItem.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DialogFragment mFragment = null;
					
					Bundle args = new Bundle();
					args.putInt("routineId", routineId);
					
					switch(listType){
					case 0:
						 mFragment = new AddStretchItemDialog();
						 mFragment.setArguments(args);
						 mFragment.setTargetFragment(thisFrag, 0);
						 mFragment.show(getFragmentManager(), "add_stretch_item");
				         break;
				    case 1:
				    	 mFragment = new AddMobilityItemDialog();
				    	 mFragment.setArguments(args);
				    	 mFragment.setTargetFragment(thisFrag, 0);
				    	 mFragment.show(getFragmentManager(), "add_mob_or_wa_item");
				         break;
				    case 2:
				    	 mFragment = new AddWarmupItemDialog();
				    	 mFragment.setArguments(args);
				    	 mFragment.setTargetFragment(thisFrag, 0);
				    	 mFragment.show(getFragmentManager(), "add_mob_or_wa_item");
				         break;
				    case 3:
				    	 mFragment = new AddLiftItemDialog();
				    	 mFragment.setArguments(args);
				    	 mFragment.setTargetFragment(thisFrag, 0);
				    	 mFragment.show(getFragmentManager(), "add_lift_item");
				         break;
					}
				}
			});	        
	        
	        expandList.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					
					int itemType = ExpandableListView.getPackedPositionType(id);
					
					if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
						DialogFragment mFragment = null;
					
						Bundle args = new Bundle();
						args.putInt("id", itemsList.get(ExpandableListView.getPackedPositionGroup(id)).getId());
						args.putInt("routineId", routineId);
					
						switch(listType){
						case 0:
							mFragment = new UpdateStretchItemDialog();
							mFragment.setArguments(args);
							mFragment.setTargetFragment(thisFrag, 0);
							mFragment.show(getFragmentManager(), "update_stretch_item");
							break;
						case 1:
							mFragment = new UpdateMobilityItemDialog();
							mFragment.setArguments(args);
							mFragment.setTargetFragment(thisFrag, 0);
							mFragment.show(getFragmentManager(), "update_mob_or_wa_item");
							break;
						case 2:
							mFragment = new UpdateWarmupItemDialog();
							mFragment.setArguments(args);
							mFragment.setTargetFragment(thisFrag, 0);
							mFragment.show(getFragmentManager(), "update_mob_or_wa_item");
							break;
						case 3:
							mFragment = new UpdateLiftItemDialog();
							mFragment.setArguments(args);
							mFragment.setTargetFragment(thisFrag, 0);
							mFragment.show(getFragmentManager(), "update_lift_item");
							break;
						}
						return true;
					}
					else{
						return false;
					}
				}
			});
	        
	        expandList.setOnChildClickListener(new OnChildClickListener(){
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					Intent intent = new Intent(activity, TimerActivity.class);
					
					//Bundle args = new Bundle();
					if(itemsList.get(groupPosition) instanceof Stretch){
						Stretch stretch = (Stretch) itemsList.get(groupPosition);
						if(stretch.getTime() > 0){
							intent.putExtra("time", stretch.getTime());
							intent.putExtra("delay", stretch.getHasDelay());
							//args.putLong("time", stretch.getTime());
							//args.putInt("delay", stretch.getHasDelay());
						}
					}
					else if(itemsList.get(groupPosition) instanceof Mobility){
						Mobility mobility = (Mobility) itemsList.get(groupPosition);
						if(childPosition == 0 && mobility.getTime() > 0){
							intent.putExtra("time", mobility.getTime());
							intent.putExtra("delay", mobility.getHasDelay());
							//args.putLong("time", mobility.getTime());
							//args.putInt("delay", mobility.getHasDelay());
						}
					}
					else if(itemsList.get(groupPosition) instanceof Warmup){
						Warmup warmup = (Warmup) itemsList.get(groupPosition);
						if(childPosition == 0 && warmup.getTime() > 0){
							intent.putExtra("time", warmup.getTime());
							intent.putExtra("delay", warmup.getHasDelay());
							//args.putLong("time", warmup.getTime());
							//args.putInt("delay", warmup.getHasDelay());
						}
					}
					else if(itemsList.get(groupPosition) instanceof Lift){
						Lift lift = (Lift) itemsList.get(groupPosition);
						system = settings.getString("PREF_MEASUREMENT_SYSTEM", "imp");
						if(childPosition == 0 && lift.getWeight() > 0.0){
							if(system.equals("imp")){
								CharSequence calculatedWeight = 
										(lift.getUsesOlympicBar() == 1)? 
												lift.calculateImperialPlatesBar():lift.calculateImperialPlatesNoBar();
								Toast toast = Toast.makeText(getActivity(), 
										calculatedWeight, 
										Toast.LENGTH_LONG);
								toast.show();
							}
							else if(system.equals("met")){	
								CharSequence calculatedWeight = 
										(lift.getUsesOlympicBar() == 1)? 
												lift.calculateMetricPlatesBar():lift.calculateMetricPlatesNoBar();
								Toast toast = Toast.makeText(getActivity(), 
										calculatedWeight,
										Toast.LENGTH_LONG);
								toast.show();
							}
						}
						else if(childPosition == 2 && lift.getTime() > 0){
							intent.putExtra("time", lift.getTime());
							intent.putExtra("delay", lift.getHasDelay());
							//args.putLong("time", lift.getTime());
							//args.putInt("delay", lift.getHasDelay());
						}
						else if(childPosition == 3 && lift.getRestTime() > 0){							
							intent.putExtra("time", lift.getRestTime());
							//args.putLong("time", lift.getRestTime());
						}
					}
										
					//if(args.getLong("time") > 0){
					if(intent.getLongExtra("time", 0) > 0){
						intent.putExtra("routineID", routineId);
						startActivity(intent);
						//mTimerFragment.setArguments(args);
						//mTimerFragment.show(getFragmentManager(), "timer");
					}
					return false;
				}
				
			});
		}

		private void getItemsList(){
			
			switch(listType){
			case 0:
				itemsList = stretchDAO.getAllItems();
		         break;
		    case 1:
		    	itemsList = mobilityDAO.getAllItems();
		         break;
		    case 2:
		    	itemsList = warmupDAO.getAllItems();
		         break;
		    case 3:
		    	itemsList = liftDAO.getAllItems();
		         break;
			}
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			outState.putInt("list_type", listType);
			outState.putInt("routineId", routineId);
			super.onSaveInstanceState(outState);
		}

		@Override
		public void onPause() {
//			Log.d(TAG, "onPause called");
			super.onPause();
		}

		@Override
		public void onStop() {
//			Log.d(TAG, "onStop called");
		    Fragment timer = getFragmentManager().findFragmentByTag("timer");
		    if (timer != null) {
		    	getFragmentManager().beginTransaction().remove(timer).commitAllowingStateLoss();
		    }
			super.onStop();
		}

		@Override
		public void onResume() {
//			Log.d(TAG, "onResume called");
			refreshList();
			super.onResume();
		}

		public void refreshList(){
			getItemsList();
			adapter.updateData(itemsList);
			adapter.notifyDataSetChanged();
		}
		
		public int getListType() {
			return listType;
		}

		public void setListType(int listType) {
			this.listType = listType;
		}
		
		public int getRoutineId() {
			return routineId;
		}

		public void setRoutineId(int routineId) {
			this.routineId = routineId;
		}

}
