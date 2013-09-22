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
