package com.blue.ironarchivev1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FrontPageActivity extends Activity {

	private int routineID = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()){
		case R.id.action_settings:
			intent = new Intent(FrontPageActivity.this, SettingsActivity.class);
		    startActivity(intent);
		    return true;
		case R.id.action_help:
			intent = new Intent(FrontPageActivity.this, TutorialActivity.class);
		    startActivity(intent);
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_front_page);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}
	
	public void openRoutine(View view){
	     Intent intent = new Intent(FrontPageActivity.this, RoutineFragmentContainer.class);
	     intent.putExtra("routineID", routineID);
	     startActivity(intent);
	}
	
	public void openSelectRoutine(View view){
		Intent intent = new Intent(FrontPageActivity.this, RoutinesListActivity.class);
		intent.putExtra("routineID", routineID);
		startActivityForResult(intent, 0);
	}
	
	public void openTutorial(View view){
		Intent intent = new Intent(FrontPageActivity.this, TutorialActivity.class);
	    startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		routineID = data.getIntExtra("routineID", 1);
	}
}
