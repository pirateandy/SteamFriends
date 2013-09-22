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
