/*
*	Copyright 2008 Pwned, LLC Licensed under the
*	Educational Community License, Version 2.0 (the "License"); you may
*	not use this file except in compliance with the License. You may
*	obtain a copy of the License at
*
*	http://www.osedu.org/licenses/ECL-2.0
*
*	Unless required by applicable law or agreed to in writing,
*	software distributed under the License is distributed on an "AS IS"
*	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
*	or implied. See the License for the specific language governing
*	permissions and limitations under the License.
*/

package com.pwned.steamfriends;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.pwned.steamfriends.service.WishlistService;
import com.pwned.steamfriends.views.Selector;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class StartPage extends Activity {
 
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    private EditText steamid;
	private Button getSteamFriends;
	private String steamIDInfo;
	private DataHelper dh;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_one);
        viewFlipper = (ViewFlipper)findViewById(R.id.flipper);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        
        steamid=(EditText)findViewById(R.id.steam_enter_text);
        getSteamFriends=(Button)findViewById(R.id.enterid);
         
        
        checkSteamLoggedIn();
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
        
        getSteamFriends.setOnClickListener(new Button.OnClickListener() {
			  public void onClick(View v) {
				  
				  
				  if(!(steamid.getText().toString().equals(""))) { 
					SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		  
					SharedPreferences.Editor editor = myPrefs.edit();
					editor.putString("steamID", steamid.getText().toString());
					editor.commit();
					  
					dh = new DataHelper(StartPage.this);  
					dh.insert(steamid.getText().toString());
					dh.close();
					
					Intent myIntent = new Intent(StartPage.this, Selector.class);
			    	myIntent.putExtra("steamid",steamid.getText().toString());
			    	startActivity(myIntent);
				} else {
					Toast.makeText(StartPage.this, "Please enter your Steam Community ID / Profile Number to proceed",Toast.LENGTH_LONG).show();
				}
				  
			  }
			});
        
    }
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                	viewFlipper.showNext();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation(slideRightIn);
                    viewFlipper.setOutAnimation(slideRightOut);
                	viewFlipper.showPrevious();
                }
                //Logger.log("current",viewFlipper.getCurrentView().get);
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
	        return true;
	    else
	    	return false;
    }
     
    @Override
	 protected void onResume() 
	 {
		 super.onResume();
	     checkSteamLoggedIn();
	     
	 }
   
   @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    //tracker.stop();
	  }
    
    
    private void checkSteamLoggedIn() {
    	SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	     steamIDInfo = myPrefs.getString("steamID", "");
	     steamid.setText(steamIDInfo);
	      if(!steamIDInfo.equalsIgnoreCase("")){
	    	  
	    	  	dh = new DataHelper(StartPage.this);  
				dh.insert(steamIDInfo);
				dh.close();
				
	            Intent myIntentSet = new Intent(this, Selector.class);
				startActivity(myIntentSet);
				finish();
	        }
    	
    }
}