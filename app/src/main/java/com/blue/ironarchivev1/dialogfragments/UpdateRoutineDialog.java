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
import android.widget.EditText;
import android.widget.TextView;

import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.RoutinesListActivity;
import com.blue.ironarchivev1.db.RoutineDAO;
import com.blue.ironarchivev1.models.Routine;

public class UpdateRoutineDialog extends DialogFragment {
	
	private Button mCommitButton, mDeleteButton;
	private EditText iName;
	private TextView iCurrentName;
	private RoutineDAO routineDAO;
	private Routine mRoutine;
	private int mID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		routineDAO = new RoutineDAO(getActivity());
		mID = getArguments().getInt("id");
		mRoutine = routineDAO.getWorkoutItem(mID);	
	}
	
	UpdateRoutineDialog newInstance(int num){
		UpdateRoutineDialog f = new UpdateRoutineDialog();
		
		Bundle args = new Bundle();
		args.putInt("id", num);
		f.setArguments(args);
		
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myRoutineItem = inflater.inflate(R.layout.fragment_update_routine_item, container, false);
		mCommitButton = (Button) myRoutineItem.findViewById(R.id.button_commit_routine_update);
		getDialog().setTitle("Update Routine");

		iCurrentName = (TextView) myRoutineItem.findViewById(R.id.rcurrentname);
		iCurrentName.setText(mRoutine.getName());
		
		iName = (EditText) myRoutineItem.findViewById(R.id.reditname);
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
				mRoutine.setName(s.toString());				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		mCommitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				routineDAO.updateWorkoutItem(mRoutine);
				dismiss();
			}
		});
		
		
		mDeleteButton = (Button) myRoutineItem.findViewById(R.id.button_commit_routine_delete);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mID != 1){
					Routine routine = (Routine) routineDAO.getAllItems().get(routineDAO.getAllItems().size()-1);
					
					routineDAO.deleteWorkoutItem(mRoutine);
					
					if(routine.getId() == mRoutine.getId()){
						((RoutinesListActivity) getActivity()).setRoutineAfterEndOfListDelete();
					}else{
						((RoutinesListActivity) getActivity()).replaceRoutineAfterDelete();
					}
					
					mID--;
				}				
				dismiss();
			}
		});
				
		return myRoutineItem;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		((RoutinesListActivity) getActivity()).refreshList(true);
	}
	
}
