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

public class GroupDetail {
	private String title;
	private String description;
	private String date;
	
	public void setDate(String d){
		this.date = d;
	}
	public void setDescription(String d){
		this.description = d;
	}
	public void setTitle(String t){
		this.title = t;
	}
	
	public String getDate() {return date;}
	public String getDescription() {return description;}
	public String getTitle() {return title;}
}
