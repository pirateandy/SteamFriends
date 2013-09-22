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

package com.pwned.steamfriends;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.service.WishlistService;
import com.pwned.steamfriends.views.Selector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SteamFriends extends Activity {
	
	private EditText steamid;
	private Button getSteamFriends;
	private TextView helperText;
	private ImageView steamHeader;
	private Boolean changeID = false;;
	private NotificationManager mManager;
    private static int APP_ID = R.layout.main;
    private String steamIDInfo;
    GoogleAnalyticsTracker tracker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = GoogleAnalyticsTracker.getInstance();
    	tracker.start(Constants.UACODE,20, this);
    	tracker.trackPageView("/SteamFriends");
        
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(APP_ID);
      
        
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
			changeID = true;
		}
         
        setContentView(R.layout.steamfriends);
        steamid=(EditText)findViewById(R.id.steamid);
        steamHeader=(ImageView)findViewById(R.id.headerimage);
        getSteamFriends=(Button)findViewById(R.id.btnreset);
        helperText=(TextView)findViewById(R.id.disclaimer);
        helperText.setFocusable(true);
        
        checkSteamLoggedIn();
         
        /*
        FileInputStream fis = null;
        try {
            fis = openFileInput("SteamID.txt");
            byte[] reader = new byte[fis.available()];
            while (fis.read(reader) != -1) {}
            steamid.setText(new String(reader));
            if(!changeID) { 
            	Intent myIntentSet = new Intent(this, Selector.class);
				startActivity(myIntentSet);
            }
        } catch (IOException e) {
           // Logger.log(ReadFile.LOGTAG, e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // swallow
                }
            }
        }
        */
        
        steamHeader.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				  Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HEADER_URL)); 
				  startActivity(myIntent);
			  }
			});
        
        helperText.setOnClickListener(new View.OnClickListener() {
			  public void onClick(View v) {
				showpopup();
				  
			  }
			});
        
        getSteamFriends.setOnClickListener(new Button.OnClickListener() {
			  public void onClick(View v) {
				  
				  SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		  
				  SharedPreferences.Editor editor = myPrefs.edit();
				  editor.putString("steamID", steamid.getText().toString());
				  editor.commit();
				if(!(steamid.getText().toString().equals(""))) { 
					getFriends();
				}
				  
			  }
			});
        /*
         
         */
        
    }
    
    @Override
	 protected void onResume() 
	 {
		 super.onResume();
		 mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	     mManager.cancel(APP_ID);
	     checkSteamLoggedIn();
	     
	 }
    
    @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	  }
    
    private void getFriends() {
    	Intent wishsvc = new Intent(this, WishlistService.class);
        startService(wishsvc);
        
    	Intent myIntent = new Intent(this, Selector.class);
    	myIntent.putExtra("steamid",steamid.getText().toString());
    	startActivity(myIntent);
    }
    
    private void showpopup()
    {
    	AlertDialog a = new AlertDialog.Builder(this).create();
    	a.setTitle("Directions of Use");
    	a.setMessage("To see your friends online, first you must make your profile PUBLIC and then you must either enter your Steam Community Profile ID \n\nhttp://steamcommunity.com/id/[STEAMID] \n\nor your profile number \n\nhttp://steamcommunity.com/profiles/[PROFILENUMBER]\n\n into the text field.\n\n");
    	a.setButton("OK", new DialogInterface.OnClickListener() {

    	      public void onClick(DialogInterface dialog, int which) {
    	    	  dialog.dismiss();
    	        return;

    	    } });
    	a.show();

    
     }
    
    private void checkSteamLoggedIn() {
    	SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	     steamIDInfo = myPrefs.getString("steamID", "");
	     steamid.setText(steamIDInfo);
	      if(!steamIDInfo.equalsIgnoreCase("")){
	    	  Intent wishsvc = new Intent(this, WishlistService.class);
	          startService(wishsvc);
	            Intent myIntentSet = new Intent(this, Selector.class);
				startActivity(myIntentSet);
				finish();
	        }
    	
    }
    
}