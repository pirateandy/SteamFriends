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

import java.util.List;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.DataHelper;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.SteamFriends;
import com.pwned.steamfriends.service.SteamService;
import com.pwned.steamfriends.service.WishlistService;
import com.pwned.utils.Logger;
import com.pwned.utils.VersionCheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Selector extends Activity {
	
	private String steamid;
	private ImageView getSteamFriends;
	private ImageView getMyStuff;
	private ImageView steamHeader;
	private ImageView getSpecials;
	private ImageView getGroups;
	private ImageView getTwitter;
	private ImageView getSettings;
	private ImageView getWishlist;
	private Button supportSteamFriends;
	
	private NotificationManager mManager;
    private static int APP_ID = R.layout.main;
    GoogleAnalyticsTracker tracker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setMyTheme();
        
    	SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        steamid = myPrefs.getString("steamID", "");
        
        tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.start(Constants.UACODE,20, this);
    	tracker.trackPageView("/Selector");
        
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(APP_ID);
        
        setContentView(R.layout.selector);
        
        getSteamFriends = (ImageView)findViewById(R.id.yourguysbtn);
        getMyStuff = (ImageView)findViewById(R.id.friendguysbtn);
        getSpecials = (ImageView)findViewById(R.id.specialsbtn);
        getGroups = (ImageView)findViewById(R.id.groupsbtn);
        getTwitter = (ImageView)findViewById(R.id.twitterbtn);
        getSettings = (ImageView)findViewById(R.id.settingsbtn);
        getWishlist = (ImageView)findViewById(R.id.wishlistbtn);
        supportSteamFriends = (Button)findViewById(R.id.supportsteamfriends);
        //startChat = (ImageView)findViewById(R.id.chatbtn);
        
        super.onCreate(savedInstanceState);
        
        
        Intent intent = getIntent(); 
        Uri returned_uri = intent.getData();
 
        if (returned_uri == null) {
            //Don't do anything
        } else {
        	//Let's remove the register banner :D
        	SharedPreferences.Editor editor = myPrefs.edit();
			editor.putString("pwnedAccount", "true");
			editor.putBoolean("pwnedAccount", true);
			editor.commit();
        	supportSteamFriends.setVisibility(View.GONE);
        }
        
     // set background
        BitmapDrawable background =(BitmapDrawable)getResources().getDrawable(R.drawable.globalheaderbg);
        background.setTileModeXY(TileMode.REPEAT,TileMode.CLAMP);
        findViewById(R.id.relativerepeat).setBackgroundDrawable(background);
        
        //check to see if they are a pwned member
	    if(myPrefs.getBoolean("pwnedAccount", false)){
	    	supportSteamFriends.setVisibility(View.GONE);
	    }
	    
	    /*
	    try {
	    	Intent serviceIntent = new Intent();
	    	serviceIntent.setAction("com.pwned.steamfriends.service.WishlistService");
	    	startService(serviceIntent);
	    } catch(Exception e){
	    	Logger.log("Service Error","Cannot Start Steam Friends Wishlist Serivce");
	    }
	    */
		
        steamHeader=(ImageView)findViewById(R.id.headerimage);
        steamHeader.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HEADER_URL)); 
				  startActivity(myIntent);
			  }
			});
        getGroups.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  getSteamGroups();
			  }
			});
        
        getSpecials.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  getSteamSpecials();
			  }
			});
        
        getSteamFriends.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				getFriends();
			  }
			}); 
        getMyStuff.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				getPersonal();
			  }
			});
        getTwitter.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				getTwitterStream();
			  }
			});
        getSettings.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  getSettings();
			  }
			});
        getWishlist.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  getWishlist();
			  }
			});
        /*
        startChat.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  Intent settingsIntent = new Intent(Selector.this, LoginActivity.class);
      				startActivity(settingsIntent);
			  }
			});
        */
        supportSteamFriends.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SUPPORT_REGISTER_URL)); 
				  startActivity(myIntent);
			  }
			});
        
        if((steamid.toString().equals(""))) { 
        	Intent myIntentSet = new Intent(this, SteamFriends.class);
			startActivity(myIntentSet);
        }
        
        try {
			VersionCheck.getVersion(this);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
		}
         
    }
    
    @Override
	 protected void onResume() 
	 {
    	setMyTheme();
		 super.onResume();
		 mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	     mManager.cancel(APP_ID);
	     SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	     steamid = myPrefs.getString("steamID", "");
	     if(myPrefs.getBoolean("pwnedAccount", false)){
		 	supportSteamFriends.setVisibility(View.GONE);
		 }
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
	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,0,0, "Change ID");
    	menu.add(0,1,1, "Start Service");
    	menu.add(0,2,2, "Kill Service");
    	menu.add(0,3,3, "Settings");
    	menu.add(0,4,4, "Start Wishlist");
    	menu.add(0,5,5, "Kill Wishlist");
    	
    	
    	//DEFINE MENU ICONS
    	MenuItem refresh = menu.getItem(0);
    	
    	MenuItem start = menu.getItem(2);
    	//start.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	
    	MenuItem kill = menu.getItem(1);
    	//kill.setIcon(android.R.drawable.ic_menu_info_details);
    	
    	MenuItem settings = menu.getItem(3);
    	//settings.setIcon(android.R.drawable.ic_menu_manage);
    	
    	return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
        case 0:
        	try {

                // setup and start SteamService
                {
                	Intent settingsIntent = new Intent(this, Settings.class);
        			startActivity(settingsIntent);
                }

              }
              catch (Exception e) {
                //Log.i(Global.TAG, "ui creation problem");
              }
            return true;
        case 1:
        	try {

                // setup and start SteamService
                {
                  if(!checkID()){
                	  showpopup("Steam ID is blank!", "Your Steam ID seems to be blank. Don't worry, you can set it in the Settings section :D");
                  } else {                	  
                	  SteamService.setMainActivity(this);
                	  Intent svc = new Intent(this, SteamService.class);
                	  startService(svc);
                  }
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
        case 3:
        	try {

                // setup and start SteamService
                {
                	Intent settingsIntent = new Intent(this, Settings.class);
        			startActivity(settingsIntent);
                }

              }
              catch (Exception e) {
                //Log.i(Global.TAG, "ui creation problem");
              }
            return true;
        case 4:
        	try {

                // setup and start SteamService
                {
                	if(!checkID()){
                  	  	showpopup("Steam ID is blank!", "Your Steam ID seems to be blank. Don't worry, you can set it in the Settings section :D");
                    } else { 
                    	WishlistService.setMainActivity(this);
                  		Intent svc = new Intent(this, WishlistService.class);
                  		startService(svc);
                    }
                }

              }
              catch (Exception e) {
                //Log.i(Global.TAG, "ui creation problem");
              }
            return true;
        case 5:
        	try {

                // setup and start SteamService
                {
                  
                  WishlistService.setMainActivity(this);
                  Intent svc = new Intent(this, WishlistService.class);
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
    
    private void getFriends() {
    	if(!checkID()){
    		showpopup("Steam ID is blank!", "Your Steam ID seems to be blank. Don't worry, you can set it in the Settings section :D");
    	} else {
    		Intent myIntent = new Intent(this, Friends.class);
    		myIntent.putExtra("steamid",steamid);
    		startActivity(myIntent);
    		
    	}
    }
    
    private void getPersonal(){		
    	if(!checkID()){
    		showpopup("Steam ID is blank!", "Your Steam ID seems to be blank. Don't worry, you can set it in the Settings section :D");
    	} else {
    		Intent myIntent = new Intent(this, Games.class);
    		myIntent.putExtra("steamid", steamid);
    		startActivity(myIntent);
    	}
    }
    
    private void getSteamSpecials() {
    	Intent myIntent = new Intent(this, Specials.class);
		startActivity(myIntent);
    }
    private void getSteamGroups() {
    	if(!checkID()){
    		showpopup("Steam ID is blank!", "Your Steam ID seems to be blank. Don't worry, you can set it in the Settings section :D");
    	} else {
    		Intent myIntent = new Intent(this, Groups.class);
    		myIntent.putExtra("steamid", steamid);
    		startActivity(myIntent);
    	}
    }
    
    private void getWishlist() {
    	if(!checkID()){
    		showpopup("Steam ID is blank!", "Your Steam ID seems to be blank. Don't worry, you can set it in the Settings section :D");
    	} else {
    		Intent myIntent = new Intent(this, Wishlist.class);
    		myIntent.putExtra("steamid", steamid);
    		startActivity(myIntent);
    	}
    }
    
    private void getTwitterStream() {
    	
    	Intent myIntent = new Intent(this, TwitterStream.class);
		startActivity(myIntent);
    }
    
    private void getSettings() {
    	Intent settingsIntent = new Intent(this, Settings.class);
		startActivity(settingsIntent);
    }
    private void showpopup(String title, String desc)
    {
    	AlertDialog a = new AlertDialog.Builder(this).create();
    	a.setTitle(title);
    	a.setMessage(desc);
    	a.setButton("Set Steam ID", new DialogInterface.OnClickListener() {

    	      public void onClick(DialogInterface dialog, int which) {
    	    	  dialog.dismiss();
    	    	  Intent settingsIntent = new Intent(Selector.this, Settings.class);
      			  startActivity(settingsIntent);
    	        return;

    	    } });
    	a.setButton2("Change Later", new DialogInterface.OnClickListener() {

  	      public void onClick(DialogInterface dialog, int which) {
  	    	  dialog.dismiss();
  	        return;

  	    } });
    	a.show();

    
     }
    
    private boolean checkID(){
    	DataHelper dh = new DataHelper(this);
	    List<String> names = dh.selectAll();
	    dh.close();
	    String steamDB = "";
	    for (String name : names) { Logger.log("Steam ID",name); steamDB = name; }
	    
    	SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String steamIDInfo = myPrefs.getString("steamID", "");
        if(steamIDInfo.equalsIgnoreCase("") || steamDB.equalsIgnoreCase("")){
            return false;
        } else {
        	//lets make sure we save it in here for the services. 
        	DataHelper di = new DataHelper(this);  
			di.insert(steamIDInfo);
			di.close();
			
        	return true;
        }

    }
    
}