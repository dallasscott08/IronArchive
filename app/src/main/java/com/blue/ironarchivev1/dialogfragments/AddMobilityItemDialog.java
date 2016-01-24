package com.blue.ironarchivev1.dialogfragments;

import android.content.DialogInterface;
import android.os.Bundle;
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

import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.ItemsListFragment;
import com.blue.ironarchivev1.db.MobilityDAO;
import com.blue.ironarchivev1.models.Mobility;

public class AddMobilityItemDialog extends DialogFragment {
	
	private Button mCommitButton;
	private EditText iName, iTime, iReps, iSets;
	private CheckBox iDelay;
	private MobilityDAO mobilityDAO;
	private Mobility mMobility;
	private int numSets;

	AddMobilityItemDialog newInstance(int num){
		AddMobilityItemDialog f = new AddMobilityItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("routineId", num);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mobilityDAO = new MobilityDAO(getActivity(), getArguments().getInt("routineId"));
		mMobility = new Mobility();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myMobilityItem = inflater.inflate(R.layout.fragment_add_mob_or_wa_item, container, false);
		mCommitButton = (Button) myMobilityItem.findViewById(R.id.button_commit_mob_or_wa);
		getDialog().setTitle("Add Mobility Item");
		
		iName = (EditText) myMobilityItem.findViewById(R.id.mweditname);
		iName.requestFocus();
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		iName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mMobility.setName(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		iTime = (EditText) myMobilityItem.findViewById(R.id.mwedittime);
		iTime.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mMobility.setTime(Long.parseLong(s.toString()));
				}catch (NumberFormatException e){
					mMobility.setTime(0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		iReps = (EditText) myMobilityItem.findViewById(R.id.mweditreps);
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
					mMobility.setReps(Integer.parseInt(s.toString()));
				}catch (NumberFormatException e){
					mMobility.setReps(0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		iSets = (EditText) myMobilityItem.findViewById(R.id.mweditset);
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
		
		iDelay = (CheckBox) myMobilityItem.findViewById(R.id.mwdelaytime);
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(numSets == 0){
					numSets = 1;
				}
				if(iDelay.isChecked()){
					mMobility.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mMobility.setHasDelay(0);
				}
				for(int i = 0; i < numSets; i++){
					mMobility.setSet(i+1);
					mobilityDAO.addMobilityItem(mMobility);
				}
				dismiss();
			}
		});
		return myMobilityItem;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ItemsListFragment) getTargetFragment()).refreshList();
	}
	
}
