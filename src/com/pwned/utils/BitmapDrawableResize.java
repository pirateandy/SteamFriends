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
