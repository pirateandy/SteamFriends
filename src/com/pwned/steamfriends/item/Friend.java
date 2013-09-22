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