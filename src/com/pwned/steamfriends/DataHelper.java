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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
  
import java.util.ArrayList;
import java.util.List;
  
public class DataHelper {
  
      private static final String DATABASE_NAME = "steamfriends.db";
      private static final int DATABASE_VERSION = 1;
      private static final String TABLE_NAME = "steamid";
  
      private Context context;
      private OpenHelper openHelper;
      private SQLiteDatabase db;
  
      private SQLiteStatement insertStmt;
      private static final String INSERT = "insert into " + TABLE_NAME + "(steamid) values (?)";
  
      public DataHelper(Context ctx) {
            context = ctx;
            openHelper = new OpenHelper(context);
            insertStmt = openHelper.getWritableDatabase().compileStatement(INSERT);
      }
  
      public long insert(String name) {
    	  	deleteAll();
            insertStmt.bindString(1, name);
            return insertStmt.executeInsert();
      }
  
      public void deleteAll() {
    	  openHelper.getWritableDatabase().delete(TABLE_NAME, null, null);
      }
  
      public List<String> selectAll() {
            List<String> list = new ArrayList<String>();
            Cursor cursor = openHelper.getReadableDatabase().query(TABLE_NAME, new String[] { "steamid" }, null, null, null, null, "steamid desc");
            if (cursor.moveToFirst()) {
                  do {
                        list.add(cursor.getString(0)); 
                  } while (cursor.moveToNext());
            }
            if (cursor != null && !cursor.isClosed()) {
                  cursor.close();
            }
            return list;
      }
      public void close(){
    	  openHelper.getReadableDatabase().close();
    	  openHelper.getWritableDatabase().close();
    	  
      }
  
      private static class OpenHelper extends SQLiteOpenHelper {
  
            OpenHelper(Context context) {
                  super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }
  
            @Override
            public void onCreate(SQLiteDatabase db) {
                  db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, steamid TEXT)");
            }
  
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                  Log.w("Steam Friends", "Upgrading database, this will drop tables and recreate.");
                  db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                  onCreate(db);
            }
      }
}
