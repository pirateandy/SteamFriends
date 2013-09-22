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
import com.pwned.steamfriends.item.Game;
import com.pwned.utils.AsyncImageLoader;
import com.pwned.utils.AsyncImageLoader.ImageCallback;

public class GamesAdapter extends ArrayAdapter<Game> {

    private ArrayList<Game> items;
    private AsyncImageLoader asyncImageLoader;
    private Context ctx;

    public GamesAdapter(Context context, int textViewResourceId, ArrayList<Game> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            asyncImageLoader = new AsyncImageLoader();
            ctx = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.game, null);
            } 
            final Game o = items.get(position);
            if (o != null) {
                    TextView tt = (TextView) v.findViewById(R.id.toptext);
                    TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                    TextView gt = (TextView) v.findViewById(R.id.gametext);
                    //ImageView iv = (ImageView) v.findViewById(R.id.icon);
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
                    	tt.setText("View Achievements");          
            		}
                    if(bt != null){
                          bt.setText(o.getTwoWeeks() + "hrs past 2 weeks \n"+o.getTotalHours() + "hrs on record");
                    }
                    if(gt != null){
                        gt.setText(o.getTitle());
                  }
                    
                    
            }
            return v;
    }        

}