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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootComplete extends BroadcastReceiver{
	@Override 
	public void onReceive(Context context, Intent intent) {
		/* 
		try{
			Intent serviceIntent = new Intent();
			serviceIntent.setAction("com.pwned.steamfriends.service.WishlistService");
			serviceIntent.putExtra("fromBoot", true);
			context.startService(serviceIntent);
			Logger.log("received boot","WE FOUND IT");
		} catch(Exception e){
			//Logger.log("Service Error","Cannot Start Steam Friends Wishlist Serivce");
    	}
    	*/ 
	}
}