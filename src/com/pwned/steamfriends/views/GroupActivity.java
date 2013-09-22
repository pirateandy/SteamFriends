/*	SteamFriends
*	Copyright (C) 2008-2013  Pwned, LLC
*
*	This program is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	This program is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.pwned.steamfriends.views;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.adapters.GroupActivityAdapter;
import com.pwned.steamfriends.item.GroupDetail;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;

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
import android.widget.TextView;

public class GroupActivity extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<GroupDetail> m_groupdetails = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private GroupActivityAdapter m_adapter;
    private Runnable viewGroupActivity;
    private static final String BREAK_TAG = "group";
    private String groupid;
    private ImageView steamHeader;
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
    	tracker.trackPageView("/GroupActivity");
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			groupid = extras.getString("groupid");
		}
        
        setContentView(R.layout.group);
        m_groupdetails = new ArrayList<GroupDetail>();
        this.m_adapter = new GroupActivityAdapter(this, R.layout.row, m_groupdetails);
        setListAdapter(this.m_adapter);
       
        viewGroupActivity = new Thread(){
            @Override
            public void run() {
                getAchievements();
            }
        };
        
        Animation a = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setRepeatMode(Animation.RESTART);
        a.setRepeatCount(Animation.INFINITE);
        a.setDuration(750);
        
        a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.linear_interpolator));
        ivLoad = (ImageView) findViewById(R.id.loading_spinner);
        ivLoad.startAnimation(a);
        
        Thread thread =  new Thread(null, viewGroupActivity, "MagentoBackground");
        thread.start();
        //m_ProgressDialog = ProgressDialog.show(GroupActivity.this,"","Getting Group's Announcements ...", true);
        
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
    private Thread returnRes = new Thread() {

        @Override
        public void run() {
            if(m_groupdetails != null && m_groupdetails.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_groupdetails.size();i++)
                m_adapter.add(m_groupdetails.get(i));
            } else {
            	TextView tv = (TextView)findViewById(android.R.id.empty);
            	tv.setText(R.string.groupsactivity_no_items);
            }
            ivLoad.clearAnimation();
            ivLoad.setVisibility(View.GONE);
            m_adapter.notifyDataSetChanged();
        }
    };
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
    
    private void getAchievements(){
          try{
              m_groupdetails = new ArrayList<GroupDetail>();              
              try {
      			String url = Constants.GROUP_ACTIVITY_URL + "?steam="+groupid;
      			//BufferedInputStream response = BufferedURL.getDataFromURL(url,Constants.XML_GROUPS_TIMEOUT);
      			BufferedInputStream response = BufferedURL.getDataFromURLBufferedInputStream(url);

      			if (response != null) {
      				try {

      					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      					factory.setNamespaceAware(true);
      					XmlPullParser xpp = factory.newPullParser();

      					xpp.setInput(response, null);
      					int eventType = xpp.getEventType();
      					while (eventType != XmlPullParser.END_DOCUMENT) {

      						if (eventType == XmlPullParser.START_TAG) {
      							String key = xpp.getName();
      							eventType = xpp.next();
      							if (eventType == XmlPullParser.TEXT) {
      								String value = xpp.getText();
      								tempArray.put(key, value);
      								// Toast.makeText(this, key +" => "+
      								// value,Toast.LENGTH_LONG).show();

      							}
      						}
      						if (eventType == XmlPullParser.END_TAG) {
      							String endTag = xpp.getName();
      							// Toast.makeText(this,
      							// BREAK_TAG.equalsIgnoreCase(endTag),Toast.LENGTH_LONG).show();
      							if (endTag.equalsIgnoreCase(BREAK_TAG)) {

      								GroupDetail o = new GroupDetail();
      								o.setTitle(tempArray.get("title"));
      				              	o.setDescription(tempArray.get("description"));
      				              	o.setDate(tempArray.get("date"));
      				              
      				              	m_groupdetails.add(o);

      							}

      						}
      						eventType = xpp.next();

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
              
              
              
              Logger.log("ARRAY", ""+ m_groupdetails.size());
            } catch (Exception e) {
              Logger.log("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
    

}