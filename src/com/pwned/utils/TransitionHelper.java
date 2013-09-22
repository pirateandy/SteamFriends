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

import java.lang.reflect.Method;

import android.app.Activity;

public class TransitionHelper {
	private static Method overridePendingTransition;
	static {
		try {
			overridePendingTransition = Activity.class.getMethod("overridePendingTransition", new Class[] {Integer.TYPE, Integer.TYPE}); //$NON-NLS-1$
		} catch (NoSuchMethodException e) {
			overridePendingTransition = null;
		}
	}

	/**
	* Calls Activity.overridePendingTransition if the method is available (>=Android 2.0)
	* @param activity the activity that launches another activity
	* @param animEnter the entering animation
	* @param animExit the exiting animation
	*/
	public static void overridePendingTransition(Activity activity, int animEnter, int animExit) throws NoSuchMethodException {
		if (overridePendingTransition!=null) {
			try {
				overridePendingTransition.invoke(activity, animEnter, animExit);
			} catch (Exception e) {
				// do nothing
			}
		}
	}
}
