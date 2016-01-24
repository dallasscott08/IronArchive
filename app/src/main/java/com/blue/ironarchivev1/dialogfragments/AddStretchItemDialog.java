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
import com.blue.ironarchivev1.db.StretchDAO;
import com.blue.ironarchivev1.models.Stretch;

public class AddStretchItemDialog extends DialogFragment {
	
	private Button mCommitButton;
	private EditText iName, iSets, iTime;
	private CheckBox iDelay;
	private StretchDAO stretchDAO;
	private Stretch mStretch;
	private int numSets;
	
	AddStretchItemDialog newInstance(int num){
		AddStretchItemDialog f = new AddStretchItemDialog();
		
		Bundle args = new Bundle();
		args.putInt("routineId", num);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stretchDAO = new StretchDAO(getActivity(), getArguments().getInt("routineId"));
		mStretch = new Stretch();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myStretchItem = inflater.inflate(R.layout.fragment_add_stretch_item, container, false);
		mCommitButton = (Button) myStretchItem.findViewById(R.id.button_commit_stretch);
		getDialog().setTitle("Add Stretch Item");

		iName = (EditText) myStretchItem.findViewById(R.id.seditname);
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
				mStretch.setName(s.toString());				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
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
		
		iSets = (EditText) myStretchItem.findViewById(R.id.seditset);
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
		
		iDelay = (CheckBox) myStretchItem.findViewById(R.id.sdelaytime);
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(numSets == 0){
					numSets = 1;
				}
				if(iDelay.isChecked()){
					mStretch.setHasDelay(1);
				}
				else if(!iDelay.isChecked()){
					mStretch.setHasDelay(0);
				}
				for(int i = 0; i < numSets; i++){
					mStretch.setSet(i+1);
					stretchDAO.addStretchItem(mStretch);
				}
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
