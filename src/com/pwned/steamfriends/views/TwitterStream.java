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

package com.pwned.steamfriends.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.adapters.TwitterStreamAdapter;
import com.pwned.steamfriends.item.Stream;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;

public class TwitterStream extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
	private ImageView steamHeader;
	private Runnable viewStream;
	private TwitterStreamAdapter m_adapter;
	private ArrayList<Stream> m_streams = null;
	private ImageView ivLoad;
	
	private NotificationManager mManager;
    private static int APP_ID = R.layout.main;
    GoogleAnalyticsTracker tracker;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 setMyTheme();
	        super.onCreate(savedInstanceState);
	        tracker = GoogleAnalyticsTracker.getInstance();
        	tracker.start(Constants.UACODE,20, this);
        	tracker.trackPageView("/TwitterStream");
	        
	        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        mManager.cancel(APP_ID);
	        
	        setContentView(R.layout.twitter);
	        m_streams = new ArrayList<Stream>();
	        this.m_adapter = new TwitterStreamAdapter(this, R.layout.row, m_streams);
	        setListAdapter(this.m_adapter);
	       
	        viewStream = new Thread(){
	            @Override
	            public void run() {
	                getStream();
	            }
	        };
	        
	        Animation a = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	        a.setRepeatMode(Animation.RESTART);
	        a.setRepeatCount(Animation.INFINITE);
	        a.setDuration(750);
	        
	        a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.linear_interpolator));
	        ivLoad = (ImageView) findViewById(R.id.loading_spinner);
	        ivLoad.startAnimation(a);
	        
	        Thread thread =  new Thread(null, viewStream, "SpecialsBackground");
	        thread.start();
	        //m_ProgressDialog = ProgressDialog.show(TwitterStream.this,"","Loading Twitter Stream...", true);
	        
	        steamHeader=(ImageView)findViewById(R.id.headerimage);
	        steamHeader.setOnClickListener(new View.OnClickListener() {
				  public void onClick(View v) {
					  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HEADER_URL)); 
					  startActivity(myIntent);
				  }
				});
	    }
	    
	    
	    @Override
		protected void onSaveInstanceState(Bundle outState) 
	    {
			super.onSaveInstanceState(outState); // the UI component values are saved here.
			
		}
		
		@Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState)
	    {
	        super.onRestoreInstanceState(savedInstanceState);
	    }
		
		 @Override
		 protected void onStop() 
		 {
			 super.onStop();
		 }
		 
		 @Override
		 protected void onStart() 
		 {
			 super.onStart();
		 }
		 
		 @Override
		 protected void onPause() 
		 {
			 //m_ProgressDialog.dismiss();
			 super.onPause();
		 }
		 
		 @Override
		 protected void onResume() 
		 {
			 setMyTheme();
			 super.onResume();
			 mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		     mManager.cancel(APP_ID);
		 }
		 @Override
		  protected void onDestroy() {
		    super.onDestroy();
		    // Stop the tracker when it is no longer needed.
		    tracker.stop();
		  }
		 
		 private void setMyTheme(){
				SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		        String themeInfo = myPrefs.getString("displayTheme", "dark");
		        int SDK_INT = android.os.Build.VERSION.SDK_INT;
		        if(themeInfo.equalsIgnoreCase("dark")){
		        	if(SDK_INT >= 11) {
		        		setTheme(android.R.style.Theme_Holo_NoActionBar); 
		        	} else {
		        		setTheme(android.R.style.Theme_Black_NoTitleBar); 
		        	}
		        } else {
		        	if(SDK_INT >= 11) {
		        		setTheme(android.R.style.Theme_Holo_Light_NoActionBar); 
		        	} else {
		        		setTheme(android.R.style.Theme_Light_NoTitleBar); 
		        	}
		        }
			}
		  
		 @Override
		 protected void onListItemClick(ListView listView, View view, int position, long id) {
			 super.onListItemClick(listView, view, position, id);
			 
			 Stream s = m_adapter.getItem(position);
			 Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getLink())); 
			 startActivity(myIntent);
			 
		 }
		 
		 private Thread returnRes = new Thread() {

		        @Override
		        public void run() {		        	
		        	Logger.log("size",Integer.toString(m_streams.size()));
		        	if(m_streams != null && m_streams.size() > 0){
		                m_adapter.notifyDataSetChanged();
		                for(int i=0;i<m_streams.size();i++)
		                m_adapter.add(m_streams.get(i));
		            } else {
		            	TextView tv = (TextView)findViewById(android.R.id.empty);
		            	tv.setText(R.string.no_items);
		            }
		            ivLoad.clearAnimation();
		            ivLoad.setVisibility(View.GONE);
		            m_adapter.notifyDataSetChanged();

		        }
		    };
		 
		 private void getStream(){
	          try{
	              m_streams = new ArrayList<Stream>();    
	              try {
	      			String url = Constants.TWITTER_URL;
	      			BufferedReader response = BufferedURL.getDataFromURLBufferedReader(url);
	      			if (response != null) {
	      				try {
	      					String line;
	      					while ((line = response.readLine()) != null) {
	      						JSONArray ja = new JSONArray(line);
	      		 
	      						for (int i = 0; i < ja.length(); i++) {
	      							JSONObject jo = (JSONObject) ja.get(i);
	      							Stream s = new Stream();
	      							s.setStream(jo.getString("text"));
	      							s.setDate(jo.getString("created_at"));
	      							//Log.i("STREAM",jo.getString("text"));
	      							String [] parts = jo.getString("text").split("\\s");
	      							for( String item : parts ) {
	      								try {
	      									URL urlLink = new URL(item);
	      					            	s.setLink(urlLink.toString());   
	      								} catch (Exception e) {

	      								}
	      							}
	      							
	      							m_streams.add(s);
	      							Logger.log("stream add",jo.getString("text"));
	      						}
	      						
	      					}

	      				} catch (Exception e) {
	      				}

	      			} else {
	      				//Toast.makeText(this, "response bad", Toast.LENGTH_LONG).show();
	      			}
	      		} catch (IOException e) {
	      			//String mess = e.getMessage();
	      			//Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
	      		}
	              
	              
	              
	              Logger.log("ARRAY", ""+ m_streams.size());
	            } catch (Exception e) {
	              Logger.log("BACKGROUND_PROC", e.getMessage());
	            }
	            runOnUiThread(returnRes);
	        }

}
