package com.blue.ironarchivev1.dialogfragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.ItemsListFragment;
import com.blue.ironarchivev1.db.LiftDAO;
import com.blue.ironarchivev1.models.Lift;

public class AddLiftItemDialog extends DialogFragment {

	private Button mCommitButton;
	private TextView iWeightHeading;
	private EditText iName, iReps, iRestTime, iSets, iTime, iWeight;
	private CheckBox iDelay, iOlympicBar;
	private LiftDAO liftDAO;
	private Lift mLift;
	private SharedPreferences settings;
	private int numSets;

	AddLiftItemDialog newInstance(int num){
		AddLiftItemDialog f = new AddLiftItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("routineId", num);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		liftDAO = new LiftDAO(getActivity(), getArguments().getInt("routineId"));
		mLift = new Lift();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myLiftItem = inflater.inflate(R.layout.fragment_add_lift_item, container, false);
		mCommitButton = (Button) myLiftItem.findViewById(R.id.button_commit_lift);
		getDialog().setTitle("Add Lift Item");
		
		iName = (EditText) myLiftItem.findViewById(R.id.leditname);
		iName.requestFocus();	
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		iName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {	
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mLift.setName(s.toString());				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
			
		});		
		
		iWeightHeading = (TextView) myLiftItem.findViewById(R.id.lweightHeading);
		if(settings.getString("PREF_MEASUREMENT_SYSTEM", "imp").equals("imp")){
			iWeightHeading.setText("Weight (lbs)");
		}
		else{
			iWeightHeading.setText("Weight (kgs)");
		}	
		
		iWeight = (EditText) myLiftItem.findViewById(R.id.leditweight);
		iWeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mLift.setWeight(Double.parseDouble(s.toString()));
				}catch (NumberFormatException e){
					mLift.setWeight(0.0);
				}				
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
			
		});
		
		iReps = (EditText) myLiftItem.findViewById(R.id.leditreps);
		iReps.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mLift.setReps(Integer.parseInt(s.toString()));
				}catch (NumberFormatException e){
					mLift.setReps(0);
				}				
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
			
		});
		
		iTime = (EditText) myLiftItem.findViewById(R.id.ledittime);
		iTime.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mLift.setTime(Long.parseLong(s.toString()));
				}catch (NumberFormatException e){
					mLift.setTime(0);
				}								
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
			
		});
		
		iRestTime = (EditText) myLiftItem.findViewById(R.id.leditresttime);
		iRestTime.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mLift.setRestTime(Long.parseLong(s.toString()));
				}catch (NumberFormatException e){
					mLift.setRestTime(0);
				}				
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
			
		});
		
		iSets = (EditText) myLiftItem.findViewById(R.id.leditset);
		iSets.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					numSets = Integer.parseInt(s.toString());
				}catch (NumberFormatException e){
					numSets = 1;
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		iDelay = (CheckBox) myLiftItem.findViewById(R.id.ldelaytime);
		iOlympicBar = (CheckBox) myLiftItem.findViewById(R.id.lolympicbar);
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(numSets == 0){
					numSets = 1;
				}
				if(iDelay.isChecked()){
					mLift.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mLift.setHasDelay(0);
				}
				if(iOlympicBar.isChecked()){
					mLift.setUsesOlympicBar(1);
				}
				else if(!iOlympicBar.isChecked()){
					mLift.setUsesOlympicBar(0);
				}
				for(int i = 0; i < numSets; i++){
					mLift.setSet(i+1);
					liftDAO.addLiftItem(mLift);
				}
				dismiss();
			}
		});
		return myLiftItem;
	}

	@Override
	public void onResume() {
		//getDialog().getWindow().setLayout(fragmentWidth, fragmentHeight);
		super.onResume();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ItemsListFragment) getTargetFragment()).refreshList();
	}
	
}
