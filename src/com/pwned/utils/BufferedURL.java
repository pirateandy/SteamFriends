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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pwned.steamfriends.Constants;

import android.os.Environment;
import com.pwned.utils.Base64;
import android.util.Log;

public class BufferedURL {
	
	public static BufferedInputStream getDataFromURL(String url, Integer timeout) throws Exception{
		Boolean getNewFile = false;
		String xmlFilePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			xmlFilePath = Environment.getExternalStorageDirectory() + Constants.XML_CACHEDIR;
		} else {
			xmlFilePath = Environment.getDataDirectory() + Constants.INTERNAL_XML_CACHEDIR;
		}
		Logger.log("file path",xmlFilePath);
		File outputFilePaths = new File(xmlFilePath);
		outputFilePaths.mkdirs();
		
		/* CONNECT TO THE URL */
		URL aURL = new URL(url);
		
		
		
		String xmlFileName = getFileNameFromURL(aURL);
		File fileCheck = new File(xmlFilePath + "/" + xmlFileName );
		
		if(timeout != null){
			Long currentTimestamp = new Date().getTime();
			Long lastModified = fileCheck.lastModified();
			Long subtract = (currentTimestamp-lastModified);
			Logger.log("subtract",Long.toString(subtract));
			Logger.log("currentTimestamp",Long.toString(currentTimestamp));
			Logger.log("timeout",Long.toString(timeout));
			if(subtract >= timeout){
				getNewFile = true;
			}
			
		}
		
		
	   	if(!fileCheck.exists() || getNewFile){
	   		 Logger.log("get new file", "true!");
	   		 /* OPEN THE CONNECTION AND CONNECT */
		   	URLConnection conn = aURL.openConnection();
			conn.connect();
			
			/* GET THE INPUT STREAM */
			InputStream is = conn.getInputStream();
			
			File outputFile = new File(xmlFilePath,xmlFileName);
			try {
				xmlFileName = writeCacheFile(is,outputFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	   		 
	   	} else {
	   	 Logger.log("get new file", "false!");
	   	}
		
		
	  //Logger.log("file path",Constants.XML_CACHEDIR + "/" + xmlFileName);
		/* GET THE STREAM, BUFFER IT */
		BufferedInputStream response = readCacheFile(xmlFilePath + "/" + xmlFileName);
		
		/* RETURN THE BUFFERED STREAM RESPONSE */
		return response;
	}
	
	public static BufferedInputStream getDataFromURLDate(String url, Long timeout) throws Exception{
		Boolean getNewFile = false;
		String xmlFilePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			xmlFilePath = Environment.getExternalStorageDirectory() + Constants.XML_CACHEDIR;
		} else {
			xmlFilePath = Environment.getDataDirectory() + Constants.INTERNAL_XML_CACHEDIR;
		}
		Logger.log("file path",xmlFilePath);
		File outputFilePaths = new File(xmlFilePath);
		outputFilePaths.mkdirs();
		
		/* CONNECT TO THE URL */
		URL aURL = new URL(url);
		
		
		
		String xmlFileName = getFileNameFromURL(aURL);
		File fileCheck = new File(xmlFilePath + "/" + xmlFileName );
		
		if(!isOnline()){
			timeout = null;
			getNewFile = false;
		}
		
		
		if(timeout != null){
			Long currentTimestamp = new Date().getTime();
			if(currentTimestamp >= timeout){
				getNewFile = true;
				try {
					fileCheck.delete();
				} catch (Exception e) {
					//no file to delete
				}
			}
			
		}
		 
		
	   	if(!fileCheck.exists() || getNewFile){
	   		 Logger.log("get new DATE file", "true!");
	   		 /* OPEN THE CONNECTION AND CONNECT */
		   	URLConnection conn = aURL.openConnection();
			conn.connect();
			
			/* GET THE INPUT STREAM */
			InputStream is = conn.getInputStream();
			
			File outputFile = new File(xmlFilePath,xmlFileName);
			xmlFileName = writeCacheFile(is,outputFile);
	   		 
	   	}
		
		
		//Logger.log("file path",Constants.XML_CACHEDIR + "/" + xmlFileName);
		/* GET THE STREAM, BUFFER IT */
		BufferedInputStream response = readCacheFile(xmlFilePath + "/" + xmlFileName);
		
		/* RETURN THE BUFFERED STREAM RESPONSE */
		return response;
	}
	
	public static BufferedReader getDataFromURLBufferedReader(String url) throws IOException{
		
		/* CONNECT TO THE URL */
		URL aURL = new URL(url);
		
		/* OPEN THE CONNECTION AND CONNECT */
		URLConnection conn = aURL.openConnection();
		conn.connect();
		
		/* GET THE INPUT STREAM */
		InputStream is = conn.getInputStream();
		
		/* GET THE STREAM, BUFFER IT */
		BufferedReader response = new BufferedReader(new InputStreamReader(is));
		
		/* RETURN THE BUFFERED STREAM RESPONSE */
		return response;
	}
	
	public static BufferedInputStream getDataFromURLBufferedInputStream(String url) throws IOException{
		
		/* CONNECT TO THE URL */
		URL aURL = new URL(url);
		
		/* OPEN THE CONNECTION AND CONNECT */
		URLConnection conn = aURL.openConnection();
		conn.connect();
		
		/* GET THE INPUT STREAM */
		InputStream is = conn.getInputStream();
		
		/* GET THE STREAM, BUFFER IT */
		//BufferedInputStream response = new BufferedReader(new InputStreamReader(is));
		BufferedInputStream response = new BufferedInputStream(is);
		
		
		/* RETURN THE BUFFERED STREAM RESPONSE */
		return response;
	}
	
	public static Map<Integer, String> getQueryMap(String query) {

	    String[] params = query.split("&");
	    Map<Integer, String> map = new HashMap<Integer, String>();
	    int c = 0;
	    for (String param : params)
	    {
	    	Integer name = c;
	        String value = param.split("=")[1];
	        map.put(name, value);
	        c++;
	    }
	    return map;
	}
	
	private static String getFileNameFromURL(URL url) {
		String filePath = url.getFile();
		Map qMap = getQueryMap(url.getQuery());
	   	String[] bits = filePath.split("/");
	   	String fileName = bits[bits.length-1];
	   	
	   	String[] bitsFile = fileName.split("\\.");
	   	String pageType = bitsFile[0];
	   	////Logger.log("filename",pageType);
	   	String xmlFileName = pageType;
	   	
	   	for(int i = 0;i<=(qMap.size()-1);i++){
	   		//////Logger.log("PARAM ",(String) qMap.get(i));
	   		xmlFileName += (String) qMap.get(i);
	   	}
	   	//Logger.log("Filename",xmlFileName);
		return Base64.encodeToString(xmlFileName.getBytes(), Base64.DEFAULT).replace("=", "").trim().toUpperCase();
	}
	
	private static BufferedInputStream readCacheFile(String file) throws Exception{
		String strFileContents = "";
		//BufferedInputStream fileReadIn = new BufferedInputStream(new FileInputStream(file).get);
		BufferedInputStream fileReadIn = new BufferedInputStream(new FileInputStream(file));
		byte[] contents = new byte[1024];

        int bytesRead=0;
        while( (bytesRead = fileReadIn.read(contents)) != -1){ 
            strFileContents += new String(contents, 0, bytesRead);   
        }
		//BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(Base64.decode(fileContent, Base64.DEFAULT))));
		////Logger.log("decrypted string",Crypto.decrypt("Kana&7a!!.!", strFileContents));
		return new BufferedInputStream(new ByteArrayInputStream(Crypto.decrypt("Kana&7a!!.!", strFileContents).getBytes()));
	}
	
	private static String writeCacheFile(InputStream is, File file) throws Exception{
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = null;
		osw = new OutputStreamWriter(fos);
		String strFileContents = "";
		BufferedInputStream fileReadIn = new BufferedInputStream(is);
		byte[] contents = new byte[1024];

        int bytesRead=0;
        while( (bytesRead = fileReadIn.read(contents)) != -1){ 
            strFileContents += new String(contents, 0, bytesRead);   
        }
        ////Logger.log("encoded string",strFileContents);

        osw.write(Crypto.encrypt("Kana&7a!!.!",strFileContents));
        
        osw.flush();
        osw.close();
		/*
        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len1);
            ////Logger.log("buffer len",new String(buffer));
        }
        */
		
        fos.close();
        is.close();
        return file.getName();
	}
	
	private static boolean isOnline() {
		try {

            URL url = new URL("http://google.com");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
            	return new Boolean(true);
            }
		} catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
			return false;
		} catch (IOException e) {
            // TODO Auto-generated catch block
           return false;
		}
		return false;
	}

}
