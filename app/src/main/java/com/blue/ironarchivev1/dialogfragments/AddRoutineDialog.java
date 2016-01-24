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

import com.blue.ironarchivev1.R;
import com.blue.ironarchivev1.RoutinesListActivity;
import com.blue.ironarchivev1.db.RoutineDAO;
import com.blue.ironarchivev1.models.Routine;

public class AddRoutineDialog extends DialogFragment {
	
	private Button mCommitButton;
	private EditText iName;
	private RoutineDAO routineDAO;
	private Routine mRoutine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		routineDAO = new RoutineDAO(getActivity());
		mRoutine = new Routine();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View myRoutineItem = inflater.inflate(R.layout.fragment_add_routine_item, container, false);
		mCommitButton = (Button) myRoutineItem.findViewById(R.id.button_commit_routine);
		getDialog().setTitle("Add Routine");

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
				routineDAO.addRoutineItem(mRoutine);
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
