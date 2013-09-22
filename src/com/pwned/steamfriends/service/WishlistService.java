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
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.DataHelper;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.views.Selector;
import com.pwned.steamfriends.views.Wishlist;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;

public class WishlistService extends Service {
	
	private static Activity MAIN_ACTIVITY;
	
	private Timer timer = new Timer();
	private HashMap<String, String> tempArray = new HashMap<String, String>();
	private int updatesReceived = 0;
	private final static long fONCE_PER_DAY = 24*60*60*1000;
	private final static int fONE_DAY = 1;
	private final static int fSIX_PM = 18;
	private final static int fZERO_MINUTES = 0;
	private Boolean runOnceBoolean = false;
	private String cc;


	private NotificationManager mManager;
	private static int APP_ID = R.layout.wishlist;



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
		if (MAIN_ACTIVITY != null) Toast.makeText(MAIN_ACTIVITY, "Wishlist Service Started",Toast.LENGTH_LONG).show();
	}

	@Override public void onDestroy() {
		super.onDestroy();
		_shutdownService();
		if (MAIN_ACTIVITY != null) Toast.makeText(MAIN_ACTIVITY, "Wishlist Service Stopped",Toast.LENGTH_LONG).show();
	}
		
	public void destroyService() {
		_shutdownService();
		if (MAIN_ACTIVITY != null) Toast.makeText(MAIN_ACTIVITY, "Wishlist Service Stopped",Toast.LENGTH_LONG).show();		
	}
	

	private void _startService(long d) {
		//Logger.log("service","start");
		//lets run this once to start
		/*
		if(!runOnceBoolean){
			Thread runOnce = new Thread(){
            	@Override
            	public void run() {
            		Logger.log("wishlist service update","get initial update");
            		try {
    	        		_getUpdate();
    	        	} catch(Exception e){
    	        		
    	        	}
            	}
        	};
        	runOnce.start();
        	Logger.log("boot", "not from boot");
		} else {
			Logger.log("boot", "FROM BOOT");
			
		}
		*/
		
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, fONE_DAY);
        Calendar result = new GregorianCalendar(
          tomorrow.get(Calendar.YEAR),
          tomorrow.get(Calendar.MONTH),
          tomorrow.get(Calendar.DATE),
          fSIX_PM,
          fZERO_MINUTES
        );
		
		timer.scheduleAtFixedRate(
	      new TimerTask() {
	        @Override
			public void run() {
	        	//Logger.log("wishlist service update","running");
	        	try {
	        		_getUpdate();
	        	} catch(Exception e){
	        		
	        	}
	        }
	      },
	      0,
	      43200000);
	}
	 
	private void _shutdownService() {
		if (timer != null) timer.cancel(); 
	}
	 
	private void _getUpdate() throws Exception {
		//Logger.log("getting update","from steam wishlist service");
		SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	
		String userID = "";
		
		DataHelper dh = new DataHelper(this);
	    List<String> names = dh.selectAll();
	    dh.close();
	    for (String name : names) { userID = name; }
		
        cc = myPrefs.getString("countryCode", null);
		
    	String url = Constants.WISHLIST_SERVICE_URL + "?steam=" + userID + "&cc=" + cc;
		Logger.log("URL",url);
		
		BufferedInputStream response = null;
		try {
			//response = BufferedURL.getDataFromURL(url,0);
			response = BufferedURL.getDataFromURLBufferedInputStream(url);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (response != null) {
			try {
				//Logger.log("getting","finding deals on your wishlist");
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
							//Logger.log("Key",key);
						}
					}
					eventType = xpp.next();

				}
			
			} catch (Exception e) {
			}

		} else {
			//Toast.makeText(this, "response bad", Toast.LENGTH_LONG).show();
		}
		updatesReceived++;
		
		if(tempArray.get("notify").equals("true")){
			
		
			Intent intent = new Intent(this,Wishlist.class);
			intent.putExtra("steamid",userID);

        	Notification notification = new Notification(R.drawable.tinyicon,tempArray.get("notificationtext"), System.currentTimeMillis());
    		notification.setLatestEventInfo(this,"Steam Friends Wishlist Sale!",tempArray.get("notificationtext"),PendingIntent.getActivity(this.getBaseContext(), 0, intent,PendingIntent.FLAG_CANCEL_CURRENT));
    		long[] vibrate = {0,100,200,300};
    		notification.vibrate = vibrate;
    		mManager.notify(APP_ID, notification);
		}
    	
		
	}
	
}
