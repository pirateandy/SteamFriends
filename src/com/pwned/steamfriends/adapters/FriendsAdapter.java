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

package com.pwned.steamfriends.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwned.steamfriends.R;
import com.pwned.steamfriends.item.Friend;
import com.pwned.utils.AsyncImageLoader;
import com.pwned.utils.AsyncImageLoader.ImageCallback;

public class FriendsAdapter extends ArrayAdapter<Friend> {
 
    private ArrayList<Friend> items;
    private AsyncImageLoader asyncImageLoader;
    private Context ctx;
    

    public FriendsAdapter(Context context, int textViewResourceId, ArrayList<Friend> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            asyncImageLoader = new AsyncImageLoader();
            ctx = context;
    }
    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {    
    		View v = convertView;
            Friend o = items.get(position);
                LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(o.getOrderStatus().trim().equalsIgnoreCase("Online")) {
                	v = vi.inflate(R.layout.rowonline, null);
                } else if(o.getOrderStatus().trim().equalsIgnoreCase("Offline")) {
                	v = vi.inflate(R.layout.rowoffline, null);
                } else if(o.getInGame()) {
                	v = vi.inflate(R.layout.row, null);
                	
                }
            
            if (o != null) {
            		//Bitmap cachedImage = null;
                    TextView tt = (TextView) v.findViewById(R.id.toptext);
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    //downloadFile(o.getImage(),iv);
                    
                    final ImageView imageView = (ImageView) v.findViewById(R.id.icon);
                    BitmapDrawable cachedImage = asyncImageLoader.loadDrawable(o.getImage(), new ImageCallback() {
                        public void imageLoaded(BitmapDrawable imageDrawable, String imageUrl) {
                        	try {
								imageView.setImageDrawable(imageDrawable);
							} catch (Exception e) {
								imageView.setImageResource(R.drawable.uiholder);
							}
                        }
                    });
                    try {
						imageView.setImageDrawable(cachedImage);
					} catch (Exception e) {
						imageView.setImageResource(R.drawable.uiholder);
					}

					
                    
                    if (tt != null) {
                          tt.setText(o.getOrderName());                            
                    }
                    if(bt != null){
                    	if(o.getLastOnline() != null) {
                    		bt.setText(o.getLastOnline());
                    	} else {
                    		bt.setText(o.getOrderStatus());
                    	}
                          
                    }
            }
            return v;
    }
}
