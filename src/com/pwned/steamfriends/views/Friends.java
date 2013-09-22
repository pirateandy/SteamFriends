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
import com.pwned.steamfriends.adapters.FriendsAdapter;
import com.pwned.steamfriends.item.Friend;
import com.pwned.steamfriends.service.SteamService;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Friends extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Friend> m_friends = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private FriendsAdapter m_adapter;
    private Runnable viewFriends;
    private static final String BREAK_TAG = "user";
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
    	tracker.trackPageView("/Friends");
        
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(APP_ID);
        
        

        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// uArray = extras.get("userArray");
			steamid = extras.getString("steamid");
		}
        
        setContentView(R.layout.main);
        m_friends = new ArrayList<Friend>();
        this.m_adapter = new FriendsAdapter(this, R.layout.row, m_friends);
        setListAdapter(this.m_adapter);
       
        viewFriends = new Thread(){
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
        
        Thread thread =  new Thread(null, viewFriends, "MagentoBackground");
        thread.start();
        //m_ProgressDialog = ProgressDialog.show(Friends.this,"","Getting Friends ...", true);
        
        steamHeader=(ImageView)findViewById(R.id.headerimage);
        steamHeader.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HEADER_URL)); 
				  startActivity(myIntent);
			  }
			});
    }
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	menu.add(1,0,0, "Refresh");
    	menu.add(1,1,1, "Start Service");
    	menu.add(1,2,2, "Kill Service");
    	
    	
    	//DEFINE MENU ICONS
    	MenuItem refresh = menu.getItem(0);
    	refresh.setIcon(android.R.drawable.ic_menu_rotate);
    	
    	MenuItem start = menu.getItem(2);
    	start.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	
    	MenuItem kill = menu.getItem(1);
    	kill.setIcon(android.R.drawable.ic_menu_info_details);
    	
    	return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
        case 0:
        	Intent myIntent = new Intent(Friends.this, Friends.class);
        	myIntent.putExtra("steamid",steamid);
			startActivity(myIntent);
            return true;
        case 1:
        	try {

                // setup and start SteamService
                {
                  SteamService.setMainActivity(this);
                  Intent svc = new Intent(this, SteamService.class);
                  startService(svc);
                }

              }
              catch (Exception e) {
                //Log.i(Global.TAG, "ui creation problem");
              }
        	
            return true;
        case 2:
        	try {

                // setup and start SteamService
                {
                  SteamService.setMainActivity(this);
                  Intent svc = new Intent(this, SteamService.class);
                  stopService(svc);
                }

              }
              catch (Exception e) {
                //Log.i(Global.TAG, "ui creation problem");
              }
            return true;
            
        }
        return false;
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
		 Intent myIntent = new Intent(Friends.this, Games.class);
		 myIntent.putExtra("steamid", o.getSteamID());
		 startActivity(myIntent);
		 try {
			TransitionHelper.overridePendingTransition(this,R.anim.slide_left_in,R.anim.slide_left_out);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		 
	 }
	 
    private Thread returnRes = new Thread() {

        @Override
        public void run() {
        	
            if(m_friends != null && m_friends.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_friends.size();i++)
                m_adapter.add(m_friends.get(i));
            } else {
            	TextView tv = (TextView)findViewById(android.R.id.empty);
            	tv.setText(R.string.main_no_items);
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
              m_friends = new ArrayList<Friend>();              
              try {
      			String url = Constants.FRIENDS_URL + "?steam="+steamid;
      			Logger.log("url",url);
      			//BufferedInputStream response = BufferedURL.getDataFromURL(url,Constants.XML_FRIENDS_TIMEOUT);
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
      								// Toast.makeText(this,
      								// "add row",Toast.LENGTH_LONG).show();
      								//mStrings[] = tempArray.get("feed");
      								String gamestatus = "";
      								if(tempArray.get("gamestatus") == null) {
      									gamestatus = "Online";
      								} else {
      									gamestatus = tempArray.get("gamestatus");
      								}
      								Friend o = new Friend();
      								o.setOrderName(tempArray.get("name"));
      				              	o.setOrderStatus(gamestatus);
      				              	o.setImage(tempArray.get("image"));
      				              	o.setSteamID(tempArray.get("steamid"));
      				              	o.setInGame(new Boolean(tempArray.get("ingame")));
      				              	o.setLastOnline(tempArray.get("lastonline"));
      				              
      				              	m_friends.add(o);
      								//addToStringArray(friendArray, tempArray.get("name") + "\n" + tempArray.get("gamestatus"));
      								//addToStringArray(imageArray, tempArray.get("image"));
      								//new ImageLoader(imgView, tempArray.get("image"));
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
              
              
              
              Logger.log("ARRAY", ""+ m_friends.size());
            } catch (Exception e) {
              Logger.log("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
}