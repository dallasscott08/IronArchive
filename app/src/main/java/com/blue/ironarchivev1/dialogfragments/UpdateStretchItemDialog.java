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

import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.ItemsListFragment;
import com.blue.ironarchivev1.db.StretchDAO;
import com.blue.ironarchivev1.models.Stretch;

public class UpdateStretchItemDialog extends DialogFragment {
	
	private Button mCommitButton, mDeleteButton;
	private TextView iCurrentName, iCurrentTime;
	private EditText iName, iTime;
	private CheckBox iDelay;
	private StretchDAO stretchDAO;
	private Stretch mStretch;
	private int mID;
	
	UpdateStretchItemDialog newInstance(int num, int routineID){
		UpdateStretchItemDialog f = new UpdateStretchItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("id", num);
		args.putInt("routineId", routineID);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stretchDAO = new StretchDAO(getActivity(), getArguments().getInt("routineId"));
		mID = getArguments().getInt("id");
		mStretch = stretchDAO.getWorkoutItem(mID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
				
		View myStretchItem = inflater.inflate(R.layout.fragment_update_stretch_item, container, false);
		final Stretch original = stretchDAO.getWorkoutItem(mID);
				
		mCommitButton = (Button) myStretchItem.findViewById(R.id.button_commit_stretch_update);
		getDialog().setTitle("Update Stretch Item");
		
		iCurrentName = (TextView) myStretchItem.findViewById(R.id.scurrentname);
		iCurrentName.setText(mStretch.getName());
		
		iName = (EditText) myStretchItem.findViewById(R.id.seditname);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		iName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {	
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mStretch.setName(s.toString());				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		iCurrentTime = (TextView) myStretchItem.findViewById(R.id.scurrenttime);
		iCurrentTime.setText(String.valueOf(mStretch.getTime()));
		
		iTime = (EditText) myStretchItem.findViewById(R.id.sedittime);
		iTime.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
				mStretch.setTime(Long.parseLong(s.toString()));
				}catch (NumberFormatException e){
					mStretch.setTime(0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {				
			}
			
		});

		iDelay = (CheckBox) myStretchItem.findViewById(R.id.sdelaytime);
		if(mStretch.getHasDelay() == 1){
			iDelay.setChecked(true);
		}
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(iDelay.isChecked()){
					mStretch.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mStretch.setHasDelay(0);
				}
				
				stretchDAO.updateWorkoutItem(mStretch);
				if(!(original.getName().equals(mStretch.getName()))){
					stretchDAO.increaseListSetNumbersAfterModify(mStretch);
					stretchDAO.decreaseListSetNumbersAfterModify(original);
				}
					
				dismiss();
			}
		});
		
		mDeleteButton = (Button) myStretchItem.findViewById(R.id.button_commit_stretch_delete);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stretchDAO.deleteWorkoutItem(mStretch);
				stretchDAO.decreaseListSetNumbersAfterModify(mStretch);
				dismiss();
			}
		});
		
		return myStretchItem;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((ItemsListFragment) getTargetFragment()).refreshList();
	}
	
}
