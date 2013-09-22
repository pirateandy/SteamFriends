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

public class Stream {
	private String stream;
	private String user;
	private String date;
	private String link;
	
	public String getStream() {
        return stream;
    }
    public void setStream(String s) {
        this.stream = s;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String u) {
        this.user = u;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String d) {
        this.date = d;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String l) {
        this.link = l;
    }
}
