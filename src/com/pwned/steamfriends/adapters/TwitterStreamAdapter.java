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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pwned.steamfriends.R;
import com.pwned.steamfriends.item.Stream;


public class TwitterStreamAdapter extends ArrayAdapter<Stream> {

    private ArrayList<Stream> items;
    private Context ctx;
    
    public TwitterStreamAdapter(Context context, int textViewResourceId, ArrayList<Stream> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            ctx = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.stream, null);
            }
            Stream o = items.get(position);
            if (o != null) {
                    TextView tt = (TextView) v.findViewById(R.id.datetext);
                    TextView bt = (TextView) v.findViewById(R.id.streamtext);
                    
                    DateFormat formatter ; 
                    Date date = new Date(o.getDate());
                    String d = null;
                    formatter = new SimpleDateFormat("dd/MMM/yyyy H:m a");
                    d = new StringBuilder(formatter.format(date)).toString();    
                   
                    if (tt != null) { tt.setText(Html.fromHtml(d)); }
                    if(bt != null){ bt.setText(Html.fromHtml(o.getStream())); }
                    
                    
            }
            return v;
    }      

}
