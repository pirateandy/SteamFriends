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

package com.pwned.steamfriends.item;

public class Special {
	private String title;
	private String image;
	private String link;
	private String release;
	private String oldPrice;
	private String newPrice;
	private String meta;
	private String platforms;
	
	public void setTitle(String t){
		this.title = t;
	}
	public void setImage(String t){
		this.image = t;
	}
	public void setLink(String t){
		this.link = t;
	}
	public void setRelease(String t){
		this.release = t;
	}
	public void setOld(String t){
		this.oldPrice = t;
	}
	public void setNew(String t){
		this.newPrice = t;
	}
	public void setMeta(String t){
		this.meta = t;
	}
	public void setPlatforms(String t){
		this.platforms = t;
	}
	
	public String getTitle(){ return title; }
	public String getImage(){ return image; }
	public String getLink(){ return link; }
	public String getRelease(){ return release; }
	public String getOld(){ return oldPrice; }
	public String getNew(){ return newPrice; }
	public String getMeta(){ return meta; }
	public String getPlatforms(){ return platforms; }
}
