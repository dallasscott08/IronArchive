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

import com.blue.ironarchivev1.ItemsListFragment;
import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.db.LiftDAO;
import com.blue.ironarchivev1.models.Lift;

public class UpdateLiftItemDialog extends DialogFragment {

	private Button mCommitButton, mDeleteButton;
	private TextView iCurrentName, iCurrentReps, iCurrentRestTime, iCurrentTime, iWeightHeading, iCurrentWeight;
	private EditText iName, iReps, iRestTime, iTime, iWeight;
	private CheckBox iDelay;
	private LiftDAO liftDAO;
	private Lift mLift, initialLift;
	private int mID;
	private SharedPreferences settings;

	UpdateLiftItemDialog newInstance(int num, int routineID){
		UpdateLiftItemDialog f = new UpdateLiftItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("id", num);
		args.putInt("routineId", routineID);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		liftDAO = new LiftDAO(getActivity(), getArguments().getInt("routineId"));
		mID = getArguments().getInt("id");
		mLift = liftDAO.getWorkoutItem(mID);
		initialLift = new Lift(mLift);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myLiftItem = inflater.inflate(R.layout.fragment_update_lift_item, container, false);

		mCommitButton = (Button) myLiftItem.findViewById(R.id.button_commit_lift_update);
		getDialog().setTitle("Update Lift Item");
		
		iCurrentName = (TextView) myLiftItem.findViewById(R.id.lcurrentname);
		iCurrentName.setText(mLift.getName());
		
		iName = (EditText) myLiftItem.findViewById(R.id.leditname);
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
		
		iCurrentWeight = (TextView) myLiftItem.findViewById(R.id.lcurrentweight);
		iCurrentWeight.setText(String.valueOf(mLift.getWeight()));
		
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
		
		iCurrentReps = (TextView) myLiftItem.findViewById(R.id.lcurrentreps);
		iCurrentReps.setText(String.valueOf(mLift.getReps()));
		
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
		
		iCurrentTime = (TextView) myLiftItem.findViewById(R.id.lcurrenttime);
		iCurrentTime.setText(String.valueOf(mLift.getTime()));
		
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
		
		iCurrentRestTime = (TextView) myLiftItem.findViewById(R.id.lcurrentresttime);
		iCurrentRestTime.setText(String.valueOf(mLift.getRestTime()));
		
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

		iDelay = (CheckBox) myLiftItem.findViewById(R.id.ldelaytime);
		if(mLift.getHasDelay() == 1){
			iDelay.setChecked(true);
		}
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(iDelay.isChecked()){
					mLift.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mLift.setHasDelay(0);
				}
				liftDAO.updateWorkoutItem(mLift);
				if(!(initialLift.getName().equals(mLift.getName()))){
					liftDAO.increaseListSetNumbersAfterModify(mLift);
					liftDAO.decreaseListSetNumbersAfterModify(initialLift);
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						liftDAO.updateLinkedItems(initialLift, mLift);
					}
				}).start();
				
				dismiss();
			}
		});
		
		mDeleteButton = (Button) myLiftItem.findViewById(R.id.button_commit_lift_delete);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				liftDAO.deleteWorkoutItem(mLift);
				liftDAO.decreaseListSetNumbersAfterModify(mLift);
				dismiss();
			}
		});
		return myLiftItem;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ItemsListFragment) getTargetFragment()).refreshList();
	}
	
}
