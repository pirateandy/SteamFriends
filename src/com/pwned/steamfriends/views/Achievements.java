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

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.AsyncImageLoader;
import com.pwned.utils.Logger;
import com.pwned.utils.AsyncImageLoader.ImageCallback;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.adapters.AchievementAdapter;
import com.pwned.steamfriends.item.Achievement;

public class Achievements extends ListActivity {
	private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Achievement> m_achievements = null;
    private ArrayList<Achievement> m_achievementsunlocked = null;
    private HashMap<String, String> tempArray = new HashMap<String, String>();
    private AchievementAdapter m_adapter;
    private Runnable viewAchievements;
    private static final String BREAK_TAG = "user";
    private String gameName;
    private String gameLogo;
    private String steamid;
    private String gameid;
    private ImageView steamHeader;
    private ImageView ivLoad;
     
    private NotificationManager mManager;
    private static int APP_ID = R.layout.main;
    
    private String lockedVars = "";
    
    private LinearLayout slideHandleButton;
    private SlidingDrawer slidingDrawer;
    private ListView listView;
    private ProgressBar myProgress;
    private Boolean getLocked;
    
    int myProgressLocked = 0;
    int myProgressUnLocked = 0;
    int myProgressAll = 0;
    
    GoogleAnalyticsTracker tracker;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setMyTheme();
        super.onCreate(savedInstanceState);
        tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.start(Constants.UACODE,20, this);
    	tracker.trackPageView("/Achievements");
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			steamid = extras.getString("steamid");
			gameid = extras.getString("gameid");
			gameLogo = extras.getString("gamelogo");
			
		}
        
        setContentView(R.layout.achievements);
        m_achievements = new ArrayList<Achievement>();
        m_achievementsunlocked = new ArrayList<Achievement>();
        this.m_adapter = new AchievementAdapter(this, R.layout.row, m_achievements);
        setListAdapter(this.m_adapter);
       
        viewAchievements = new Thread(){
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
        
        Thread thread =  new Thread(null, viewAchievements, "MagentoBackground");
        thread.start();
        
        //m_ProgressDialog = ProgressDialog.show(Achievements.this,"","Getting Achievements ...", true);
        
		SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        getLocked = myPrefs.getBoolean("locked", true);
        
        listView = getListView();
        
        myProgress = (ProgressBar)findViewById(R.id.progressbar_Horizontal);

        steamHeader=(ImageView)findViewById(R.id.headerimage);
        steamHeader.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HEADER_URL)); 
				  startActivity(myIntent);
			  }
			});
        
        
        slideHandleButton = (LinearLayout) findViewById(R.id.slideHandleButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
        slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
        	public void onDrawerOpened() {
        		 //listView.setVisibility(ListView.GONE);
        		//slideHandleButton.setBackgroundResource(R.drawable.gameinfotab);
        		double totalProgressDouble = myProgressAll * 1.0;
        		double percent = (myProgressUnLocked/totalProgressDouble)*100;
        		
        		//Logger.log("total",t.toString());
        		//Toast.makeText(Achievements.this, Long.toString(t),Toast.LENGTH_LONG).show();
        		//Toast.makeText(Achievements.this, Double.toString(percent),Toast.LENGTH_LONG).show();
        		
        		
        		
        		TextView progressText = (TextView)findViewById(R.id.progress_text);
        		progressText.setText(Integer.toString((int)percent) + "% achieved ( "+Integer.toString(myProgressUnLocked)+ " of "+Integer.toString(myProgressAll)+" )");
        		myProgress.setProgress((int)percent);
        	}
        });

        slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
        	public void onDrawerClosed() {
        			//listView.setVisibility(ListView.VISIBLE);
        			//slideHandleButton.setBackgroundResource(R.drawable.gameinfotab);
        		myProgress.setProgress(0);
        	}
        });
        loadGameLogo(gameLogo);
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
           
            
            if(m_achievements != null && m_achievements.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_achievements.size();i++)
                m_adapter.add(m_achievements.get(i));
            } else {
            	TextView tv = (TextView)findViewById(android.R.id.empty);
            	tv.setText(R.string.achievements_no_items);
            }
            ivLoad.clearAnimation();
            ivLoad.setVisibility(View.GONE);
            m_adapter.notifyDataSetChanged();
           
        }
    };
    
    private void loadGameLogo(String url) {
    	
    	final ImageView imageView = (ImageView)findViewById(R.id.gameimage);
    	final AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
        BitmapDrawable cachedImage = asyncImageLoader.loadDrawable(url, new ImageCallback() {
            public void imageLoaded(BitmapDrawable imageDrawable, String imageUrl) {
            	try {
					imageView.setImageDrawable(imageDrawable);
				} catch (Exception e) {
					imageView.setImageResource(R.drawable.uiholder);
				}
            }
        });
        try {
			imageView.setImageDrawable(cachedImage);
		} catch (Exception e) {
			imageView.setImageResource(R.drawable.uiholder);
		}
    	
    	/*
    	final ImageView gameLogoView = (ImageView)findViewById(R.id.gameimage);
    	Drawable drawable = LazyLoadImage.loadImageFromUrl(url);
    	gameLogoView.setImageDrawable(drawable);
    	*/
    	
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
    
    
    private void getAchievements(){
          try{
              m_achievements = new ArrayList<Achievement>();              
              try {
      			String url = Constants.ACHIEVEMENTS_URL + "?steam="+steamid+"&game="+gameid;

      			//BufferedInputStream response = BufferedURL.getDataFromURL(url,Constants.XML_ACHIEVEMENTS_TIMEOUT);
      			BufferedInputStream response = BufferedURL.getDataFromURLBufferedInputStream(url);
      			Logger.log("games url",url);

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
      								
      				              	
      				              	gameName = tempArray.get("game").toString();
      				              	//Logger.log("status",tempArray.get("status"));
      				              	if(tempArray.get("status").equalsIgnoreCase("locked")){
      				              		myProgressLocked++;
      				              		
      				              		if(getLocked){
      				              			Achievement o = new Achievement();
      				              			o.setTitle(tempArray.get("title"));
      				              			o.setDescription(tempArray.get("description"));
      				              			o.setImage(tempArray.get("image"));
      				              			o.setLink(tempArray.get("link"));
      				              			o.setGame(tempArray.get("status"));
      				              			m_achievements.add(o);
      				              		}
      				              		
      				              	} else {
      				              		myProgressUnLocked++;
      				              		
      				              		Achievement o = new Achievement();
      				              		o.setTitle(tempArray.get("title"));
      				              		o.setDescription(tempArray.get("description"));
      				              		o.setImage(tempArray.get("image"));
      				              		o.setLink(tempArray.get("link"));
      				              		o.setGame(tempArray.get("status"));
      				              
      				              		m_achievements.add(o);
      				              		
      				              	}
      				              	myProgressAll++;
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
              
              
              
              //Log.i("ARRAY", ""+ m_achievements.size());
            } catch (Exception e) {
              //Logger.log("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
    

}