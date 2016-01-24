package com.blue.ironarchivev1;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blue.ironarchivev1.util.ItemCountDownTimer;

public class TimerActivity extends Activity{
	
	private TextView mTextField;
	private CountDown myCounter; 
	private long mTime, countDownTime, timeLeft;
	private int hasDelay, currentProgress, maxProgress, progress;
	private Vibrator v;
	private SharedPreferences settings;
	private String hms;
	private ProgressBar mProgress;
	private Intent intent;
	
	private static final String TAG = "Timer";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "onCreateView called");
		setContentView(R.layout.timer);
		mTextField = (TextView) findViewById(R.id.timerText);
		mProgress = (ProgressBar) findViewById(R.id.ringProgressbar);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		intent = getIntent();
		
		if(savedInstanceState!=null){
			mTime = savedInstanceState.getLong("current_time", 0);
			countDownTime = savedInstanceState.getLong("start_time", 0);
			maxProgress = savedInstanceState.getInt("max_progress", 0);
			mProgress.setMax(maxProgress);
			myCounter = new CountDown(mTime, 10, false);
		}
		else{
			mTime = intent.getLongExtra("time", 0);
			maxProgress = (int) intent.getLongExtra("time", 0) * 1000;
			hasDelay = intent.getIntExtra("delay", 0);
			if(hasDelay == 1){
				mTime = (mTime * 1000) + 10000;
				//maxProgress -= 10000;
				mProgress.setMax((int) (mTime - 10000));
				myCounter = new CountDown(mTime, 10, true);
			}else{
				mTime = mTime * 1000;
				mProgress.setMax((int) (mTime));
				myCounter = new CountDown(mTime, 10, false);	
			}
			
		}

		myCounter.start();
	}

	/*public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View myTimer = inflater.inflate(R.layout.timer, container);
		mTextField = (TextView) myTimer.findViewById(R.id.timerText);
		mProgress = (ProgressBar) myTimer.findViewById(R.id.ringProgressbar);
				
		if(savedInstanceState!=null){
				myCounter = new CountDown(mTime, 1000, false);
		}
		else{
			if(hasDelay == 1){
				mTime = (mTime * 1000) + 10000;
				myCounter = new CountDown(mTime, 1000, true);
			}
			else{
				mTime = mTime * 1000;
				myCounter = new CountDown(mTime, 1000, false);
			}
		}
		myCounter.start();
		return myTimer;		
	}*/	
		
	public void onDestroyView() {
		//Log.d(TAG, "onDestroyView called");
		if(timeLeft > 250){
			myCounter.cancel();
		}
		onDestroyView();
	}

	/*TimerActivity newInstance(long num, int delay){
		TimerActivity f = new TimerActivity();
		
		Bundle args = new Bundle();
		args.putLong("time", num);
		args.putInt("delay", delay);
		//f.setArguments(args);
		
		return f;
	}*/
	
	public void onSaveInstanceState(Bundle outState) {
//		Log.d(TAG, "onSaveInstanceState called");

		outState.putLong("current_time", mTime);
		outState.putLong("start_time", countDownTime);
		outState.putInt("current_progress", currentProgress);
		outState.putInt("max_progress", maxProgress);

		super.onSaveInstanceState(outState);
	}

	private class CountDown extends ItemCountDownTimer{
		private long currentSecond, delay;
		private String beginTime;
		
		public CountDown(long startTime, long interval, boolean hasDelay) {
			super(startTime, interval);
			
			this.currentSecond = TimeUnit.MILLISECONDS.toSeconds(startTime + 1000);
			
			if(hasDelay){
				delay = 10000;
				countDownTime = startTime - delay;
			}
			
		}
		
		public void onTick(long millisUntilFinished) {
			timeLeft = millisUntilFinished;
			hms = String.format("%02d:%02d",  
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished + 1000) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished + 1000)),  
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished + 1000) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished + 1000)));  
            
			beginTime = String.format("%02d:%02d",  
                    TimeUnit.MILLISECONDS.toMinutes(countDownTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(countDownTime)),  
                    TimeUnit.MILLISECONDS.toSeconds(countDownTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(countDownTime)));
				
			if(TimeUnit.MILLISECONDS.toSeconds(mTime) < TimeUnit.MILLISECONDS.toSeconds(countDownTime) || countDownTime == 0){
				progress = (int) (millisUntilFinished);
				mTextField.setText(hms);
			}
			else{
				progress = (int) (countDownTime);
				mTextField.setText(beginTime);
			}
			
			mProgress.setProgress(progress);

			if(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished + 1000) < currentSecond){
				mTime -= 1000;
				--currentSecond;
			}
			
	     }

	     public void onFinish() {
	    	 mProgress.setProgress(0);
	    	 mTextField.setText("Done!");
	    	 if(settings.getBoolean("PREF_TIME_VIBRATE", false)){
	    		 v.vibrate(500);
	    	 }
	    	 mProgress.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
	    		 
	    	 });
	     }

		@Override
		public void onFinalTick() {
			mProgress.setProgress(progress);
			mTextField.setText("00:01");			
		}
	  }
	
}
