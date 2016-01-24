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
import com.blue.ironarchivev1.db.WarmupDAO;
import com.blue.ironarchivev1.models.Warmup;

public class AddWarmupItemDialog extends DialogFragment {

	private Button mCommitButton;
	private EditText iName, iTime, iReps, iSets;
	private CheckBox iDelay;
	private WarmupDAO warmupDAO;
	private Warmup mWarmup;
	private int numSets;
	
	AddWarmupItemDialog newInstance(int num){
		AddWarmupItemDialog f = new AddWarmupItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("routineId", num);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		warmupDAO = new WarmupDAO(getActivity(), getArguments().getInt("routineId"));
		mWarmup = new Warmup();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		View myWarmupItem = inflater.inflate(R.layout.fragment_add_mob_or_wa_item, container, false);
		mCommitButton = (Button) myWarmupItem.findViewById(R.id.button_commit_mob_or_wa);
		getDialog().setTitle("Add Warm-Up Item");
		
		iName = (EditText) myWarmupItem.findViewById(R.id.mweditname);
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
				mWarmup.setName(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
			
		});
		
		iTime = (EditText) myWarmupItem.findViewById(R.id.mwedittime);
		iTime.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mWarmup.setTime(Long.parseLong(s.toString()));
				}catch (NumberFormatException e){
					mWarmup.setTime(0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		iReps = (EditText) myWarmupItem.findViewById(R.id.mweditreps);
		iReps.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					mWarmup.setReps(Integer.parseInt(s.toString()));
				}catch (NumberFormatException e){
					mWarmup.setReps(0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
			
		});
		
		iSets = (EditText) myWarmupItem.findViewById(R.id.mweditset);
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
		
		iDelay = (CheckBox) myWarmupItem.findViewById(R.id.mwdelaytime);
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(numSets == 0){
					numSets = 1;
				}
				if(iDelay.isChecked()){
					mWarmup.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mWarmup.setHasDelay(0);
				}
				for(int i = 0; i < numSets; i++){
					mWarmup.setSet(i+1);
					warmupDAO.addWarmupItem(mWarmup);
				}
				dismiss();
			}
		});
		return myWarmupItem;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ItemsListFragment) getTargetFragment()).refreshList();
	}
	
}
