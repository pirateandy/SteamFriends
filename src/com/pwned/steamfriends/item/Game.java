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