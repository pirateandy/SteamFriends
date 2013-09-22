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

package com.pwned.steamfriends.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.DataHelper;
import com.pwned.steamfriends.R;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;
import com.pwned.steamfriends.views.Friends;
import com.pwned.steamfriends.views.Settings;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SteamService extends Service {

	private static Activity MAIN_ACTIVITY;

	private Timer timer = new Timer();
	private long UPDATE_INTERVAL;
	private static final String BREAK_TAG = "user";
	private int count = 0;
	private int onlinecount = 0;
	private int ingamecount = 0;


	private NotificationManager mManager;
	private static int APP_ID = R.layout.main;
	
	public static void setMainActivity(Activity activity) {
	  MAIN_ACTIVITY = activity;
	}


	@Override
	public IBinder onBind(Intent intent) {
	  return null;
	}

	@Override public void onCreate() {
	  super.onCreate();

	  // init the service here
	  _startService(0);

	  if (MAIN_ACTIVITY != null) Toast.makeText(MAIN_ACTIVITY, "Friends Service Started",Toast.LENGTH_LONG).show();
	}

	@Override public void onDestroy() {
	  super.onDestroy();

	  _shutdownService();

	  if (MAIN_ACTIVITY != null) Toast.makeText(MAIN_ACTIVITY, "Friends Service Stopped",Toast.LENGTH_LONG).show();
	}
	
	public void destroyService() {
		_shutdownService();

		  if (MAIN_ACTIVITY != null) Toast.makeText(MAIN_ACTIVITY, "Friends Service Stopped",Toast.LENGTH_LONG).show();
		
	}

	private void _startService(long d) {
	  SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	  UPDATE_INTERVAL = Long.parseLong(myPrefs.getString("refreshInterval", "1800000").trim());
	  timer.scheduleAtFixedRate(
	      new TimerTask() {
	        @Override
			public void run() {
	        	_getUpdate();
	        }
	      },
	      d,
	      UPDATE_INTERVAL);
	  //Log.i(getClass().getSimpleName(), "Timer started!!!");
	}

	private void _getUpdate() {
		count = 0;
		onlinecount = 0;
		ingamecount = 0;
		final HashMap<String, String> tempArray = new HashMap<String, String>();
		
		//SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        //final String userID = myPrefs.getString("steamID", "");
		String userID = "";
		
		DataHelper dh = new DataHelper(this);
	    List<String> names = dh.selectAll();
	    dh.close();
	    for (String name : names) { userID = name; }
		
        if(!userID.equalsIgnoreCase("")){
                      
           mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           mManager.cancel(APP_ID);
           
           String url = Constants.SERVICE_URL + "?steam="+userID+"&offline=false";
           Logger.log("url",url);
           BufferedInputStream response = null;

           try {
        	   //response = BufferedURL.getDataFromURL(url,Constants.XML_FRIENDS_TIMEOUT);
        	   response = BufferedURL.getDataFromURLBufferedInputStream(url);
           } catch (Exception e1) {
        	   // TODO Auto-generated catch block
        	   e1.printStackTrace();
           }

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
								if(new Boolean(tempArray.get("ingame"))) {
  									ingamecount++;
  								} else {
  									onlinecount++;
  								}
								
								count++;
							}

						}
						eventType = xpp.next();

					}

				} catch (Exception e) {
				}

			} else {
				//Toast.makeText(this, "response bad", Toast.LENGTH_LONG).show();
			}
						
			Intent intent = new Intent(this,Friends.class);
			intent.putExtra("steamid",userID);
			String friends_str = "Friend";
			if((ingamecount >= 2) || (ingamecount == 0)) { friends_str="Friends"; }
	        Notification notification = new Notification(R.drawable.tinyicon,ingamecount + " "+ friends_str +" In-Game and "+ onlinecount +" Online", System.currentTimeMillis());
	    	notification.setLatestEventInfo(this,"SteamFriends",ingamecount + " "+ friends_str +" In-Game and "+ onlinecount +" Online",PendingIntent.getActivity(this.getBaseContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT));
	    	long[] vibrate = {0,100,200,300};
	    	notification.vibrate = vibrate;
	    	mManager.notify(APP_ID, notification);
	    	
           
        } else {
        	Intent intent = new Intent(this,Settings.class);
	        Notification notification = new Notification(R.drawable.tinyicon,"Oh no, we have an error :( Make sure your Steam ID is set in the Settings section", System.currentTimeMillis());
	    	notification.setLatestEventInfo(this,"SteamFriends","Oh no, we have an error :( Make sure your Steam ID is set in the Settings section",PendingIntent.getActivity(this.getBaseContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT));
	    	long[] vibrate = {0,100,200,300};
	    	notification.vibrate = vibrate;
	    	mManager.notify(APP_ID, notification);
        }
        
        
	  
	}

	private void _shutdownService() {
	  if (timer != null) timer.cancel();
	  //Log.i(getClass().getSimpleName(), "Timer stopped!!!");
	}

	}//end class SteamService
