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
