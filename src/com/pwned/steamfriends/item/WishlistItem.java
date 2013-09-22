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

public class WishlistItem {
	private String title;
	private String image;
	private String link;
	private String added;
	private String oldPrice;
	private String newPrice;
	private String discount;
	
	
	public void setTitle(String title){
		this.title = title;
	}
	public void setImage(String image){
		this.image = image;
	}
	public void setLink(String link){
		this.link = link;
	}
	public void setAdded(String added){
		this.added = added;
	}
	public void setOldPrice(String oldPrice){
		this.oldPrice = oldPrice;
	}
	public void setNewPrice(String newPrice){
		this.newPrice = newPrice;
	}
	public void setDiscount(String discount){
		this.discount = discount;
	}
	
	public String getTitle(){ return title; }
	public String getImage(){ return image; }
	public String getLink(){ return link; }
	public String getAdded(){ return added; }
	public String getOldPrice(){ return oldPrice; }
	public String getNewPrice(){ return newPrice; }
	public String getDiscount(){ return discount; }

}
