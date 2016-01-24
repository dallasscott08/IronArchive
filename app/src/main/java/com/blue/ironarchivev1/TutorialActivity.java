package com.blue.ironarchivev1;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class TutorialActivity extends Activity {

	 //private ViewFlipper tutorialFlipper;
	 //private float initialX;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_tutorial);
		 /*tutorialFlipper = (ViewFlipper) findViewById(R.id.tutorialViewFlipper);
		 tutorialFlipper.setInAnimation(this, android.R.anim.fade_in);
		 tutorialFlipper.setOutAnimation(this, android.R.anim.fade_out);*/
	 }
	 
	 /*@Override
	 public boolean onTouchEvent(MotionEvent touchevent) {
		 switch (touchevent.getAction()) {
			 case MotionEvent.ACTION_DOWN:
				 initialX = touchevent.getX();
				 break;
			 case MotionEvent.ACTION_UP:
				 float finalX = touchevent.getX();
				 if (initialX > finalX) {
					 if (tutorialFlipper.getDisplayedChild() == 1)
					 break;
					 
					 tutorialFlipper.showNext();
				 } else {
					 if (tutorialFlipper.getDisplayedChild() == 0)
					 break;
					 
					 tutorialFlipper.showPrevious();
				 }
				 break;
		 }
		 return false;
	 }*/
	 
}
