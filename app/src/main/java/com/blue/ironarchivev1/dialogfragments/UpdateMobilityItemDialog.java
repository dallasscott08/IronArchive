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
import com.blue.ironarchivev1.db.MobilityDAO;
import com.blue.ironarchivev1.models.Mobility;

public class UpdateMobilityItemDialog extends DialogFragment {
	
	private Button mCommitButton, mDeleteButton;
	private TextView iCurrentName, iCurrentTime, iCurrentReps;
	private EditText iName, iTime, iReps;
	private CheckBox iDelay;
	private MobilityDAO mobilityDAO;
	private Mobility mMobility;
	private int mID;

	UpdateMobilityItemDialog newInstance(int num, int routineID){
		UpdateMobilityItemDialog f = new UpdateMobilityItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("id", num);
		args.putInt("routineId", routineID);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mobilityDAO = new MobilityDAO(getActivity(), getArguments().getInt("routineId"));
		mID = getArguments().getInt("id");
		mMobility = mobilityDAO.getWorkoutItem(mID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myMobilityItem = inflater.inflate(R.layout.fragment_update_mob_or_wa_item, container, false);
		final Mobility original = mobilityDAO.getWorkoutItem(mID);

		mCommitButton = (Button) myMobilityItem.findViewById(R.id.button_commit_mob_or_wa_update);
		getDialog().setTitle("Update Mobility Item");
		
		iCurrentName = (TextView) myMobilityItem.findViewById(R.id.mwcurrentname);
		iCurrentName.setText(mMobility.getName());
		
		iName = (EditText) myMobilityItem.findViewById(R.id.mweditname);
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
		
		iCurrentTime = (TextView) myMobilityItem.findViewById(R.id.mwcurrenttime);
		iCurrentTime.setText(String.valueOf(mMobility.getTime()));
		
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
		
		iCurrentReps = (TextView) myMobilityItem.findViewById(R.id.mwcurrentreps);
		iCurrentReps.setText(String.valueOf(mMobility.getReps()));
		
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

		iDelay = (CheckBox) myMobilityItem.findViewById(R.id.mwdelaytime);
		if(mMobility.getHasDelay() == 1){
			iDelay.setChecked(true);
		}
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(iDelay.isChecked()){
					mMobility.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mMobility.setHasDelay(0);
				}
				mobilityDAO.updateWorkoutItem(mMobility);
				if(!(original.getName().equals(mMobility.getName()))){
					mobilityDAO.increaseListSetNumbersAfterModify(mMobility);
					mobilityDAO.decreaseListSetNumbersAfterModify(original);
				}
				dismiss();
			}
		});
		
		mDeleteButton = (Button) myMobilityItem.findViewById(R.id.button_commit_mob_or_wa_delete);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mobilityDAO.deleteWorkoutItem(mMobility);
				mobilityDAO.decreaseListSetNumbersAfterModify(mMobility);
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
