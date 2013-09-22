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
