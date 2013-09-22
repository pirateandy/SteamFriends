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

public class Game {
	   
    private String title;
    private String image;
    private String link;
    private String steamid;
    private String twoWeeks;
    private String totalHours;
    
    public String getImage() {
    	return image;
    }
    public void setImage(String im) {
    	this.image = im;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSteamID() {
        return steamid;
    }
    public void setSteamID(String id) {
        this.steamid = id;
    }
    public void setTwoWeeks(String txt){
    	this.twoWeeks = txt;
    }
    public String getTwoWeeks(){
    	return twoWeeks;
    }
    public void setTotalHours(String txt){
    	this.totalHours = txt;
    }
    public String getTotalHours(){
    	return totalHours;
    }
}