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

public class Friend {
	   
    private String orderName;
    private String orderStatus;
    private String image;
    private String steamid;
    private Boolean ingame;
    private String lastOnline;
   
    public String getOrderName() {
        return orderName;
    }
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getImage() {
    	return image;
    }
    public void setImage(String im) {
    	this.image = im;
    }
    public String getSteamID() {
        return steamid;
    }
    public void setSteamID(String id) {
        this.steamid = id;
    }
    public Boolean getInGame() {
        return ingame;
    }
    public void setInGame(Boolean id) {
        this.ingame = id;
    }
    public void setLastOnline(String online){
    	this.lastOnline = online;
    }
    public String getLastOnline(){
    	return lastOnline;
    }
}