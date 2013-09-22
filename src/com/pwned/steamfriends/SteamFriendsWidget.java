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

import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
 
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;
 
import android.content.Intent;
import android.widget.Toast;

public class SteamFriendsWidget extends AppWidgetProvider {
 
	private Date currentTime;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new AppTime(context, appWidgetManager), 1, 2000000);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	private class AppTime extends TimerTask {
		
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		java.text.DateFormat format = java.text.DateFormat.getTimeInstance(java.text.DateFormat.MEDIUM, Locale.getDefault());
		
		public AppTime(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
			thisWidget = new ComponentName(context, SteamFriendsWidget.class);
		}
		
		@Override
		public void run() {
			currentTime = new Date();
			
			remoteViews.setTextViewText(R.id.widget_textview, "Time = " + format.format(currentTime));
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			super.onReceive(context, intent);
		}
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Toast.makeText(context, "onDelete", Toast.LENGTH_SHORT).show();
		super.onDeleted(context, appWidgetIds);
	}
	
}
