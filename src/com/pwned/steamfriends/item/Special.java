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
