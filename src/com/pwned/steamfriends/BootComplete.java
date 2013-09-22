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