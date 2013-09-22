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
