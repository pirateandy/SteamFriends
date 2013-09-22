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