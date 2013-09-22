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

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

public class BitmapDrawableResize {
	
	public static BitmapDrawable resize(float newWidth, float newHeight,BitmapDrawable bdImage) {
		
		Bitmap bitmapOrig = bdImage.getBitmap();
    	float scaleWidth = (newWidth) / bitmapOrig.getWidth();
    	float scaleHeight = (newHeight) / bitmapOrig.getHeight();
    	Matrix matrix = new Matrix();
    	matrix.postScale(scaleWidth, scaleHeight);
    	Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrig, 0, 0, bitmapOrig.getWidth(), bitmapOrig.getHeight(), matrix, true);
    	BitmapDrawable bitmapDrawableResized = new BitmapDrawable(resizedBitmap);
    	return bitmapDrawableResized; 
	}
}
