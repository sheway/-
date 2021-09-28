package com.example.software_engineering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Attended_Event_DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Attended_Event.db";

    public Attended_Event_DBHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Attended_Event(Number TEXT primary key, P_Key TEXT, Event_key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public int get_number()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Attended_Event",null);
        if (cursor.getCount() == 0)
        {
            return 1;
        }
        else
        {
            cursor.moveToLast();
            return cursor.getShort(0) + 1;
        }
    }

    public Boolean insertData(int num, String p_key, String event_key)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Number", num);
        contentValues.put("P_Key", p_key);
        contentValues.put("Event_key", event_key);

        long result = DB.insert("Attended_Event", null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String[] attended_event_list(String p_key)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Attended_Event where P_Key = ? ", new String[]{p_key});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String[] list = new String[cursor.getCount()];
            for(int i = 0 ; i < cursor.getCount() ; i++)
            {
                list[i] = cursor.getString(2);
                cursor.moveToNext();
            }
            return list;
        }
        else
        {
            String[] list = new String[1];
            list[0] = "Haven't attended any event or error";
            return list;
        }
    }

    public String is_attended(int num, String p_key, String event_key)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Attended_Event where P_Key = ? and Event_key = ?", new String[]{p_key, event_key});
        if(cursor.getCount() > 0)
        {
            return "該活動已在列表中";
        }
        else
        {
            boolean a = insertData(num, p_key, event_key);
            if(a == true)
            {
                return "活動新增成功";
            }
            else
            {
                return "活動新增失敗";
            }
        }
    }

    public int attended_people(String event_key)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Attended_Event where Event_key = ?", new String[]{event_key});
        return cursor.getCount();
    }
}
