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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.adapters.FriendsAdapter;
import com.pwned.steamfriends.item.Friend;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;
import com.pwned.utils.TransitionHelper;

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

public class Groups extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Friend> m_groups = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private FriendsAdapter m_adapter;
    private Runnable viewGroups;
    private static final String BREAK_TAG = "group";
    private String steamid;
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
    	tracker.trackPageView("/Groups");
        
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(APP_ID);

        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// uArray = extras.get("userArray");
			steamid = extras.getString("steamid");
		}
        
        setContentView(R.layout.groups);
        m_groups = new ArrayList<Friend>();
        this.m_adapter = new FriendsAdapter(this, R.layout.row, m_groups);
        setListAdapter(this.m_adapter);
       
        viewGroups = new Thread(){
            @Override
            public void run() {
                getFriends();
            }
        }; 
        
        Animation a = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setRepeatMode(Animation.RESTART);
        a.setRepeatCount(Animation.INFINITE);
        a.setDuration(750);
        
        a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.linear_interpolator));
        ivLoad = (ImageView) findViewById(R.id.loading_spinner);
        ivLoad.startAnimation(a);
        
        Thread thread =  new Thread(null, viewGroups, "GroupsBackground");
        thread.start();
        //m_ProgressDialog = ProgressDialog.show(Groups.this,"","Getting Groups ...", true);
        
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

	 @Override
	 protected void onListItemClick(ListView listView, View view, int position, long id) {
		 super.onListItemClick(listView, view, position, id);
		 Friend o = m_adapter.getItem(position);
		 Intent myIntent = new Intent(Groups.this, GroupActivity.class);
		 myIntent.putExtra("groupid", o.getSteamID());
		 startActivity(myIntent);
		 try {
			 TransitionHelper.overridePendingTransition(this,R.anim.slide_left_in,R.anim.slide_left_out);
		 } catch(NoSuchMethodException e){
			 //e.printStackTrace();
		 }
	 }
	 
    private Thread returnRes = new Thread() {

        @Override
        public void run() {
        	if(m_groups != null && m_groups.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_groups.size();i++)
                m_adapter.add(m_groups.get(i));
            } else {
            	TextView tv = (TextView)findViewById(android.R.id.empty);
            	tv.setText(R.string.groups_no_items);
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
    
    private void getFriends(){
          try{
              m_groups = new ArrayList<Friend>();              
              try {
      			String url = Constants.GROUPS_URL + "?steam="+steamid;
      			//BufferedInputStream response = BufferedURL.getDataFromURL(url,Constants.XML_GROUPS_TIMEOUT);
      			BufferedInputStream response = BufferedURL.getDataFromURLBufferedInputStream(url);

      			if (response != null) {
      				try {

      					XmlPullParserFactory factory = XmlPullParserFactory
      							.newInstance();
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
      								
      								Friend o = new Friend();
      								o.setOrderName(tempArray.get("title"));
      				              	o.setOrderStatus("Online");
      				              	o.setImage(tempArray.get("image"));
      				              	o.setSteamID(tempArray.get("link"));
      				              	o.setInGame(false);
      				              	o.setLastOnline(tempArray.get("membercount") + " Members");
      				              
      				              	m_groups.add(o);
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
              
              
              
              Logger.log("ARRAY", ""+ m_groups.size());
            } catch (Exception e) {
              Logger.log("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
}