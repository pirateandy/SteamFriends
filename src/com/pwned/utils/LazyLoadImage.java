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

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;

public class LazyLoadImage {
	
	public static Drawable loadImageFromUrl(String url)
    {
 		try
 		{
 			InputStream is = (InputStream) new URL(url).getContent();
 			Drawable d = Drawable.createFromStream(is, "src name");
 			return d;
 		}catch (Exception e) {
 			System.out.println("Exc="+e);
 			return null;
 		}
 	}

}
