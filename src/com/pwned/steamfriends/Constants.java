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

package com.pwned.steamfriends;

public class Constants {
	
	/* SETTINGS */
	public static final Boolean DEVELOPMENT = false;
	
	/* CACHE DIR */
	public static final String CACHEDIR = "/Android/data/com.pwned.steamfriends/files";
	public static final String XML_CACHEDIR = "/Android/data/com.pwned.steamfriends/files/xml";
	public static final String INTERNAL_CACHEDIR = "/data/com.pwned.steamfriends/files";
	public static final String INTERNAL_XML_CACHEDIR = "/data/com.pwned.steamfriends/files/xml";
	
	/* CACHE XML FILE TIMEOUT */
	public static final int XML_FRIENDS_TIMEOUT = 600000; 
	public static final int XML_GAMES_TIMEOUT = 600000; 
	public static final int XML_ACHIEVEMENTS_TIMEOUT = 600000; 
	public static final int XML_GROUPS_TIMEOUT = 600000;
	public static final int XML_WISHLIST_TIMEOUT = 600000;
	
	/*GOOGLE ANALYTICS UA CODE */
	public static final String UACODE = "UA-XXXXXX-X";
	 
	/*HEADER CTA URL */
	public static final String HEADER_URL = "http://example.com/";
	public static final String SUPPORT_REGISTER_URL = "http://example.com/register";
	
	/* MARKET URL */
	public static final String MARKET_URL = "https://market.android.com/details?id=com.pwned.steamfriends";
	 
	/* API URLS */
	public static final String BASE_URL = "http://example.com/api/steam/";
	public static final String SERVICE_URL = BASE_URL + "friends";
	public static final String ACHIEVEMENTS_URL = BASE_URL + "gameachievementslocked";
	public static final String FRIENDS_URL = BASE_URL + "friends";
	public static final String GAMES_URL = BASE_URL + "games";
	public static final String GROUP_ACTIVITY_URL = BASE_URL + "groupactivity";
	public static final String GROUPS_URL = BASE_URL + "groups";
	public static final String SPECIALS_URL = BASE_URL + "specials";
	public static final String WISHLIST_URL = BASE_URL + "wishlist";
	public static final String WISHLIST_SERVICE_URL = BASE_URL + "wishlistservice";
	public static final String BANNERCHECK_URL = BASE_URL + "check";
	public static final String VERSIONCHECK_URL = BASE_URL + "version";
	public static final String TWITTER_URL = "http://twitter.com/statuses/user_timeline/36803580.json";
  
	
}
