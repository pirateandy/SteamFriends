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
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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
import com.pwned.steamfriends.adapters.WishlistAdapter;
import com.pwned.steamfriends.item.WishlistItem;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;

public class Wishlist extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<WishlistItem> m_wishlist = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private WishlistAdapter m_adapter;
    private Runnable viewWishlist;
    private static final String BREAK_TAG = "game";
    private String steamid;
    private String cc;
    private ImageView steamHeader;
    private ImageView ivLoad;
    
    private NotificationManager mManager;
    private static int APP_ID = R.layout.wishlist;
    GoogleAnalyticsTracker tracker;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setMyTheme();
        super.onCreate(savedInstanceState);
        tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.start(Constants.UACODE,20, this);
    	tracker.trackPageView("/Wishlist");
        
    	Bundle extras = getIntent().getExtras();
		if (extras != null) {
			steamid = extras.getString("steamid");
		}
    	
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(APP_ID);
        
        setContentView(R.layout.wishlist);
        m_wishlist = new ArrayList<WishlistItem>();
        this.m_adapter = new WishlistAdapter(this, R.layout.row, m_wishlist);
        setListAdapter(this.m_adapter);
        
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        cc = myPrefs.getString("countryCode", null);
                
        String[] c = Locale.getISOCountries();
        int ctr ; // loop index
        for (ctr=0; ctr < c.length; ctr++){
        	//String name = Locale.getDisplayCountry(new Locale(c[ctr]));
        	//Logger.log("ISO Countries: ",name);
        }
        
        viewWishlist = new Thread(){
            @Override
            public void run() {
                getWishlist();
            }
        }; 
        
        Animation a = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setRepeatMode(Animation.RESTART);
        a.setRepeatCount(Animation.INFINITE);
        a.setDuration(750);
        
        a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.linear_interpolator));
        ivLoad = (ImageView) findViewById(R.id.loading_spinner);
        ivLoad.startAnimation(a);
        
        Thread thread =  new Thread(null, viewWishlist, "WishlistBackground");
        thread.start();
       // m_ProgressDialog = ProgressDialog.show(Wishlist.this,"","Grabbing the wishlist ...", true);
        
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
		 
		 WishlistItem o = m_adapter.getItem(position);
		 Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(o.getLink())); 
		 startActivity(myIntent);
		 
	 }
	 
    private Thread returnRes = new Thread() {

        @Override
        public void run() {
        	if(m_wishlist != null && m_wishlist.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_wishlist.size();i++)
                m_adapter.add(m_wishlist.get(i));
            } else {
            	TextView tv = (TextView)findViewById(android.R.id.empty);
            	tv.setText(R.string.wishlist_no_items);
            }
            ivLoad.clearAnimation();
            ivLoad.setVisibility(View.GONE);
            m_adapter.notifyDataSetChanged();
            

        }
    }; 
	 
	 private void getWishlist(){
         try{
             m_wishlist = new ArrayList<WishlistItem>();              
             try {
     			String url = Constants.WISHLIST_URL + "?steam="+steamid+"&cc="+cc;
     			//BufferedInputStream response = BufferedURL.getDataFromURL(url,Constants.XML_WISHLIST_TIMEOUT);
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
     								WishlistItem o = new WishlistItem();
     								o.setTitle(tempArray.get("title"));
     				              	o.setImage(tempArray.get("image"));
     				              	o.setLink(tempArray.get("link"));
     				              	o.setAdded(tempArray.get("added"));
     				              	o.setNewPrice(tempArray.get("new"));
     				              	o.setOldPrice(tempArray.get("old"));
     				              	o.setDiscount(tempArray.get("discount"));
     				              
     				              	m_wishlist.add(o);
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
             
             
             
             Logger.log("ARRAY", ""+ m_wishlist.size());
           } catch (Exception e) {
             Logger.log("BACKGROUND_PROC", e.getMessage());
           }
           runOnUiThread(returnRes);
       }
}
