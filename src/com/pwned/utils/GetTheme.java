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


package com.pwned.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GetTheme {
	public static void getTheme(Context ctx, Context appCtx){
		SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String themeInfo = myPrefs.getString("displayTheme", "dark");
        if(themeInfo.equalsIgnoreCase("dark")){
        	appCtx.setTheme(android.R.style.Theme_Black_NoTitleBar); 
        } else {
        	appCtx.setTheme(android.R.style.Theme_Light_NoTitleBar); 
        }
	}
}
