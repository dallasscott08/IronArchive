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
import android.widget.TextView;

import com.blue.ironarchivev1.ItemsListFragment;
import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.db.WarmupDAO;
import com.blue.ironarchivev1.models.Warmup;

public class UpdateWarmupItemDialog extends DialogFragment {

	private Button mCommitButton, mDeleteButton;
	private TextView iCurrentName, iCurrentTime, iCurrentReps;
	private EditText iName, iTime, iReps;
	private CheckBox iDelay;
	private WarmupDAO warmupDAO;
	private Warmup mWarmup, initialWarmup;
	private int mID;
	
	UpdateWarmupItemDialog newInstance(int num, int routineID){
		UpdateWarmupItemDialog f = new UpdateWarmupItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("id", num);
		args.putInt("routineId", routineID);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		warmupDAO = new WarmupDAO(getActivity(), getArguments().getInt("routineId"));
		mID = getArguments().getInt("id");
		mWarmup = warmupDAO.getWorkoutItem(mID);
		initialWarmup = new Warmup(mWarmup);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		View myWarmupItem = inflater.inflate(R.layout.fragment_update_mob_or_wa_item, container, false);

		mCommitButton = (Button) myWarmupItem.findViewById(R.id.button_commit_mob_or_wa_update);
		getDialog().setTitle("Update Warm-Up Item");
		
		iCurrentName = (TextView) myWarmupItem.findViewById(R.id.mwcurrentname);
		iCurrentName.setText(mWarmup.getName());
		
		iName = (EditText) myWarmupItem.findViewById(R.id.mweditname);
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
		
		iCurrentTime = (TextView) myWarmupItem.findViewById(R.id.mwcurrenttime);
		iCurrentTime.setText(String.valueOf(mWarmup.getTime()));
		
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
		
		iCurrentReps = (TextView) myWarmupItem.findViewById(R.id.mwcurrentreps);
		iCurrentReps.setText(String.valueOf(mWarmup.getReps()));
		
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

		iDelay = (CheckBox) myWarmupItem.findViewById(R.id.mwdelaytime);
		if(mWarmup.getHasDelay() == 1){
			iDelay.setChecked(true);
		}
			
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(iDelay.isChecked()){
					mWarmup.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mWarmup.setHasDelay(0);
				}
				warmupDAO.updateWorkoutItem(mWarmup);
				if(!(initialWarmup.getName().equals(mWarmup.getName()))){
					warmupDAO.increaseListSetNumbersAfterModify(mWarmup);
					warmupDAO.decreaseListSetNumbersAfterModify(initialWarmup);
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						warmupDAO.updateLinkedItems(initialWarmup, mWarmup);
					}
				}).start();
				dismiss();
			}
		});
		
		mDeleteButton = (Button) myWarmupItem.findViewById(R.id.button_commit_mob_or_wa_delete);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				warmupDAO.deleteWorkoutItem(mWarmup);
				warmupDAO.decreaseListSetNumbersAfterModify(mWarmup);
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
