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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.pwned.steamfriends.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
    private HashMap<String, SoftReference<BitmapDrawable>> imageCache;

    public AsyncImageLoader() {
    	//NO MEDIA FILE, WE DON'T NEED TO SEE THIS IN THE GALLERY
    	String noMediaDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			noMediaDir = Environment.getExternalStorageDirectory() + Constants.CACHEDIR;
		} else {
			noMediaDir = Environment.getDataDirectory() + Constants.INTERNAL_CACHEDIR;
		}
		File noMediaFile = new File(noMediaDir,".nomedia");
		FileWriter noMediaFileWriter;
		
		try {
			noMediaFileWriter = new FileWriter(noMediaFile);
			BufferedWriter out = new BufferedWriter(noMediaFileWriter);
		    out.write("");
		    out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
    	imageCache = new HashMap<String, SoftReference<BitmapDrawable>>();
    }

    public BitmapDrawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
    	if (imageCache.containsKey(imageUrl)) {
            SoftReference<BitmapDrawable> softReference = imageCache.get(imageUrl);
            BitmapDrawable drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
    	}
    	final Handler handler = new Handler() {
    		@Override
    		public void handleMessage(Message message) {
                imageCallback.imageLoaded((BitmapDrawable) message.obj, imageUrl);
    		}
    	};
    	new Thread() {
    		@Override
    		public void run() {
    			BitmapDrawable drawable = loadImageFromUrl(imageUrl);
                imageCache.put(imageUrl, new SoftReference<BitmapDrawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
    		}
    	}.start();
        return null;
    }

    public BitmapDrawable loadImageFromUrl(String url) {
    	String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			cachePath = Environment.getExternalStorageDirectory() + Constants.CACHEDIR;
		} else {
			cachePath = Environment.getDataDirectory() + Constants.INTERNAL_CACHEDIR;
		}	
    	
   	 InputStream inputStream = null;
   	 URL imgURL = null;
   	 String fileName = "";
   	 String filePath = "";
   	 String[] bits = null;
		try {
			imgURL = new URL(url);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	 filePath = imgURL.getFile();
   	 bits = filePath.split("/");
   	 fileName = bits[bits.length-1];
   	 
   	 //Logger.log("File Path",filePath);
   	 //Logger.log("File Name",fileName);
   	 //Logger.log("super file path",filePath.replaceFirst(fileName,""));
   	 File fileCheck = new File(Environment.getExternalStorageDirectory() + Constants.CACHEDIR + filePath );
   	 if(fileCheck.exists()){
   		 
   		 return (BitmapDrawable) Drawable.createFromPath(cachePath + filePath);
   		 
   	 } else {
   		 
   			 
   		try {
       	 
       	
            	inputStream = imgURL.openStream();
        	} catch (IOException e) {
       	 	try {
       		 	inputStream = new URL("https://steamcommunity.com/public/images/header/globalheader_logo.png").openStream();
       	 	} catch (MalformedURLException e1) {
       	 		// TODO Auto-generated catch block
       	 		//e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
        	}
        
        
        	OutputStream outStream = null;
        
        	File pathCreator = new File(cachePath + filePath.replaceFirst(fileName,"") );
			pathCreator.mkdirs(); 
		     
			File file = new File(cachePath + filePath.replaceFirst(fileName,""),fileName );
       	try {
       	 		Bitmap bm = BitmapFactory.decodeStream(inputStream);
         		outStream = new FileOutputStream(file);
         		try {
					bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
         		outStream.flush();
         		outStream.close();         
        
        	} catch (FileNotFoundException e) {
            	return (BitmapDrawable) Drawable.createFromStream(inputStream, "src");
        	} catch (IOException e) {
        		return (BitmapDrawable) Drawable.createFromStream(inputStream, "src");
        	}
        	//createFromStream(inputStream, "src");
        	//return (BitmapDrawable) Drawable.createFromStream(inputStream, "src");
        	return (BitmapDrawable) Drawable.createFromPath(cachePath + filePath);
   	 }
   }

    public interface ImageCallback {
        public void imageLoaded(BitmapDrawable imageDrawable, String imageUrl);
    }
    
}