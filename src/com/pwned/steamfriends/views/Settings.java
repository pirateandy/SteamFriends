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
import java.io.File;
 
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.pwned.steamfriends.Constants;
import com.pwned.steamfriends.DataHelper;
import com.pwned.steamfriends.R;
import com.pwned.steamfriends.StartPage;
import com.pwned.steamfriends.service.SteamService;
import com.pwned.steamfriends.service.WishlistService;
import com.pwned.utils.BufferedURL;
import com.pwned.utils.Logger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.widget.Toast;
 
public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
		GoogleAnalyticsTracker tracker;
		private ProgressDialog m_ProgressDialog = null;
		private String pwnedResponse = "";
		private Boolean pwnedEmailBool = false;
        
		@Override
        protected void onCreate(Bundle savedInstanceState) {
        	setMyTheme(); 
        	super.onCreate(savedInstanceState);
        	tracker = GoogleAnalyticsTracker.getInstance();
        	tracker.start(Constants.UACODE,20, this);
        	tracker.trackPageView("/Settings");
                addPreferencesFromResource(R.layout.settings);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).registerOnSharedPreferenceChangeListener((OnSharedPreferenceChangeListener) this);
                // Get the custom preference
                CheckBoxPreference lockedPrefs = (CheckBoxPreference) findPreference("locked");
                lockedPrefs.setChecked(prefs.getBoolean("locked", true));
                /*
                final CheckBoxPreference wishPrefs = (CheckBoxPreference) findPreference("wishserv");
                wishPrefs.setChecked(prefs.getBoolean("wishserv", true));
                wishPrefs.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                	
                	public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                        boolean condition = Boolean.parseBoolean(newValue.toString());
                        if(condition){
                        	Intent serviceIntent = new Intent();
                    		serviceIntent.setAction("com.pwned.steamfriends.service.WishlistService");
                    		startService(serviceIntent);
                        } else {
                        	Intent serviceIntent = new Intent();
                    		serviceIntent.setAction("com.pwned.steamfriends.service.WishlistService");
                    		stopService(serviceIntent);
                        }
                        wishPrefs.setChecked(condition);
                        Editor edit = preference.getEditor();
                        edit.putBoolean("wishserv", condition);
                        edit.commit();
                        return false;
                    }
				});
                 */    
                getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
                
                Preference findSteamID = findPreference("findSteamID");
                findSteamID.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                		public boolean onPreferenceClick(Preference preference) {
                        	
                			AlertDialog a = new AlertDialog.Builder(Settings.this).create();
                            a.setTitle("Where to find your Steam Profile ID");
                            a.setMessage("This is NOT the Steam ID you use to login. This is the ID / Number for your profile on http://SteamCommunity.com. To see your friends online, first you must make your profile PUBLIC and then you must either enter your Steam Community Profile ID \n\nhttp://steamcommunity.com/id/[STEAMID] \n\nor your profile number \n\nhttp://steamcommunity.com/profiles/[PROFILENUMBER]\n\n into the text field.\n\n");
                            a.setButton("OK, Got It!", new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	dialog.dismiss();
                                    return;

                            	}
                            });
                            
                            a.setButton2("Show Me",new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		  
                                    SharedPreferences.Editor editor = myPrefs.edit();
                              		editor.putString("steamID", "");
                              		editor.commit();
                              		killServices();
                                    Intent myIntent = new Intent(Settings.this, StartPage.class);
                                    startActivity(myIntent);
                                    return;
                            	}
                            });
                            
                            a.setButton3("Err, Still Doesn't Help", new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	dialog.dismiss();
                                    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    sendEmail("Steam Friends - Can't Find My Steam Profile ID / Profile Number", "Steam ID/Profile that's set: " + myPrefs.getString("steamID", ""));
                                    return;

                            	}
                            });
                            a.show();
                            return true;
                		}
                });
                
                Preference blankError = findPreference("blankError");
                blankError.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                		public boolean onPreferenceClick(Preference preference) {
                        	AlertDialog a = new AlertDialog.Builder(Settings.this).create();
                            a.setTitle("Setting your profile to see your stuff and friends");
                            a.setMessage("First, try clearing the cache of. If that still doesn't work, to see your friends online and your stuff, first you must make your profile PUBLIC. Log into http://steamcommunity.com -> edit profile -> settings -> set profile status to 'public'");
                            a.setButton("OK, Got It!", new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	dialog.dismiss();
                                    return;
                            	}
                            });
                            
                            a.setButton2("Show Me",new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		  
                                    SharedPreferences.Editor editor = myPrefs.edit();
                                	editor.putString("steamID", "");
                                	editor.commit();
                                	killServices();		  
                                    Intent myIntent = new Intent(Settings.this, StartPage.class);
                                    startActivity(myIntent);
                                    return;

                            	}
                            });
                            
                            a.setButton3("Err, Still Doesn't Help", new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	dialog.dismiss();
                                    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    sendEmail("Steam Friends - Can't See My Stuff","Steam ID/Profile: " + myPrefs.getString("steamID", ""));
                                    return;
                            		}
                            	});
                                a.show();
                                return true;
                			}
 
                });
                
                Preference supportEmail = findPreference("supportEmail");
                supportEmail.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                		public boolean onPreferenceClick(Preference preference) {
                        	sendEmail("Steam Friends Support / Suggestions","");
                            return true;
                		}
 
                });
                
                Preference clearCache = findPreference("clearCache");
                clearCache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                		public boolean onPreferenceClick(Preference preference) {
                        	AlertDialog a = new AlertDialog.Builder(Settings.this).create();
                            a.setTitle("Clear the cache for Steam Friends");
                            a.setMessage("This will clear all of your cache, including images and other stored data");
                            a.setButton("Clear It!", new DialogInterface.OnClickListener() {

                            	public void onClick(DialogInterface dialog, int which) {
                                	deleteDirectory(new File(Environment.getExternalStorageDirectory() + Constants.CACHEDIR));
                                    deleteDirectory(new File(Environment.getDataDirectory() + Constants.INTERNAL_CACHEDIR));
                                    return;
                            	} 
                            });
                            a.setButton2("Nope", new DialogInterface.OnClickListener() {
                            	public void onClick(DialogInterface dialog, int which) {
                                	dialog.dismiss();
                                    SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    sendEmail("Steam Friends - Can't See My Stuff","Steam ID/Profile: " + myPrefs.getString("steamID", ""));
                                    return;

                            	}
                            });
                            a.show();
                            return true;
                		}
 
                });
                
                
                Preference pwnedEmail = findPreference("pwnedEmail");
                pwnedEmail.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                	public boolean onPreferenceChange(Preference preference, Object newValue) {
                		final String emailToCheck = newValue.toString();
                	    Runnable checkEmail = new Thread(){
                            @Override
                            public void run() {
                                try {
									checkPwnedEmail(emailToCheck);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                            }
                        }; 
                        
                        Thread thread =  new Thread(null, checkEmail, "EmailBackground");
                        thread.start();
                        m_ProgressDialog = ProgressDialog.show(Settings.this,"","Checking your Pwned Account ...", true);
                        
                	    return true;
                	}
                });
                
                Preference steamIDPref = findPreference("steamID");
                steamIDPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                	public boolean onPreferenceChange(Preference preference, Object newValue) {
                		DataHelper dh = new DataHelper(Settings.this);  
    					dh.insert(newValue.toString());
    					dh.close();
                        
                	    return true;
                	}
                });
                 
                 
        }
		
		@Override
		  protected void onDestroy() {
		    super.onDestroy();
		    // Stop the tracker when it is no longer needed.
		    tracker.stop();
		  }
		
		@Override
		protected void onResume() {
		    super.onResume();
		    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		protected void onPause() {
		    super.onPause();
		    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}
		
		private Thread returnRes = new Thread() {

	        @Override
	        public void run() {
	            
	            m_ProgressDialog.dismiss();	    
	            SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	        	SharedPreferences.Editor editor = myPrefs.edit();
	        	editor.putBoolean("pwnedAccount", pwnedEmailBool);
	        	editor.commit();
	            Toast.makeText(Settings.this, pwnedResponse ,Toast.LENGTH_LONG).show();

	        }
	    }; 
	    
	    private void checkPwnedEmail(String email) throws Exception{
	    	
	    	String url = Constants.BANNERCHECK_URL + "?email="+email;
	     	BufferedInputStream response = BufferedURL.getDataFromURL(url,0);

	     	if (response != null) {
	     		 byte[] contents = new byte[1024];

	             int bytesRead=0;
	             String resCheck = null; 
	             while( (bytesRead = response.read(contents)) != -1){ 
	                 resCheck = new String(contents, 0, bytesRead);               
	             }
	            Logger.log("res",resCheck);
	            
	            if(resCheck.equalsIgnoreCase("good")){	            	
	            	pwnedResponse = "Thanks for being a member on Pwned and keeping Steam Friends alive!";
	            	pwnedEmailBool = true;
	            } else {
	            	pwnedResponse = "The Email address you provided isn't registered with Pwned. Please register or try again.";
	            }
	            
	            runOnUiThread(returnRes);
	            
	     	}
	    	
	    } 
	    
	    static public boolean deleteDirectory(File path) {
	        if( path.exists() ) {
	            File[] files = path.listFiles();
	            Logger.log("path","exists");
	            if (files == null) {
	            	return true;
	            }
	            for(int i=0; i<files.length; i++) {
	            	if(files[i].isDirectory()) {
	            	   deleteDirectory(files[i]);
	               } else {
	            	   files[i].delete();
	               }
	            }
	        } else {
	        	Logger.log("path","doesnt exists");
	        	
	        }
	        return( path.delete() );
	    }
		
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	        // Let's do something a preference value changes
	        if(key.equals("refreshInterval")){	
	        	SteamService.setMainActivity(this);
                Intent svc = new Intent(this, SteamService.class);
                stopService(svc);
                
                SteamService.setMainActivity(this);
          	  	Intent svcs = new Intent(this, SteamService.class);
          	  	startService(svcs);
	        }
	    }
        
        private void sendEmail(String title, String message){
        	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            String[] recipients = new String[]{"contact@pwned.com"};
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            emailIntent.setType("text/plain");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
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
        
        private void killServices() {
        	SteamService.setMainActivity(this);
            Intent svc = new Intent(this, SteamService.class);
            stopService(svc);
            
            WishlistService.setMainActivity(this);
            Intent wsvc = new Intent(this, WishlistService.class);
            stopService(wsvc);
        }
}
