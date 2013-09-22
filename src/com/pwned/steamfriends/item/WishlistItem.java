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
