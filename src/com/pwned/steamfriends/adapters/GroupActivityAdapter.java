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

import com.pwned.steamfriends.R;
import com.pwned.steamfriends.item.GroupDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupActivityAdapter extends ArrayAdapter<GroupDetail> {

    private ArrayList<GroupDetail> items;
    private Context ctx;

    public GroupActivityAdapter(Context context, int textViewResourceId, ArrayList<GroupDetail> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            ctx = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.groupactivity, null);
            }
            GroupDetail o = items.get(position);
            if (o != null) {
                    TextView tt = (TextView) v.findViewById(R.id.activitytitle);
                    TextView bt = (TextView) v.findViewById(R.id.activitydescription);
                    TextView gt = (TextView) v.findViewById(R.id.activitydate);
                    
                    
                    if (tt != null) {
                    	tt.setText(o.getTitle());          
            		}
                    if(bt != null){
                        bt.setText(o.getDescription());
                    }
                    if(gt != null){
                        gt.setText(o.getDate());
                  }
                    
                    
            }
            return v;
    }        

}