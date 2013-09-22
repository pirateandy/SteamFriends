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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.adapters.SpecialsAdapter;
import com.pwned.steamfriends.item.Special;
import com.pwned.utils.BufferedURL;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Specials extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Special> m_specials = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private SpecialsAdapter m_adapter;
    private Runnable viewSpecials;
    private static final String BREAK_TAG = "game";
    private ImageView steamHeader;
    private String cc;
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
    	tracker.trackPageView("/Specials");
        
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(APP_ID);
        setContentView(R.layout.specials);
        m_specials = new ArrayList<Special>();
        this.m_adapter = new SpecialsAdapter(this, R.layout.row, m_specials);
        setListAdapter(this.m_adapter);
        
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        cc = myPrefs.getString("countryCode", null);
        
        String[] c = Locale.getISOCountries();
        int ctr ; // loop index
        for (ctr=0; ctr < c.length; ctr++){
        	//String name = Locale.getDisplayCountry(new Locale(c[ctr]));
        	//Logger.log("ISO Countries: ",name);
        }
        
        viewSpecials = new Thread(){
            @Override
            public void run() {
                getSpecials();
            }
        }; 
        
        Animation a = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setRepeatMode(Animation.RESTART);
        a.setRepeatCount(Animation.INFINITE);
        a.setDuration(750);
        
        a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.linear_interpolator));
        ivLoad = (ImageView) findViewById(R.id.loading_spinner);
        ivLoad.startAnimation(a);
        
        Thread thread =  new Thread(null, viewSpecials, "SpecialsBackground");
        thread.start();
        //m_ProgressDialog = ProgressDialog.show(Specials.this,"","Grabbing some sick deals ...", true);
        
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
		 
		 Special o = m_adapter.getItem(position);
		 Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(o.getLink())); 
		 startActivity(myIntent);
		 
	 }
	 
    private Thread returnRes = new Thread() {

        @Override
        public void run() {
        	if(m_specials != null && m_specials.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_specials.size();i++)
                m_adapter.add(m_specials.get(i));
            } else {
            	TextView tv = (TextView)findViewById(android.R.id.empty);
            	tv.setText(R.string.specials_no_items);
            }
            ivLoad.clearAnimation();
            ivLoad.setVisibility(View.GONE);
            m_adapter.notifyDataSetChanged();
            

        }
    }; 
     
    private void getSpecials(){
    	
          try{
              m_specials = new ArrayList<Special>();              
              try {
      			
            	String url = Constants.SPECIALS_URL + "?cc="+cc;
      			
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
      							}
      						}
      						if (eventType == XmlPullParser.END_TAG) {
      							String endTag = xpp.getName();
      							
      							if (endTag.equalsIgnoreCase(BREAK_TAG)) {

      								Special o = new Special();
      								o.setTitle(tempArray.get("title"));
      				              	o.setImage(tempArray.get("image"));
      				              	o.setLink(tempArray.get("link"));
      				              	o.setRelease(tempArray.get("release"));
      				              	o.setNew(tempArray.get("new"));
      				              	o.setOld(tempArray.get("old"));
      				              	o.setMeta(tempArray.get("meta"));
      				              	o.setPlatforms(tempArray.get("platforms"));      				              
      				              	m_specials.add(o);
      				              
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
              
              
              
              //Logger.log("ARRAY", ""+ m_specials.size());
            } catch (Exception e) {
              //Logger.log("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
}