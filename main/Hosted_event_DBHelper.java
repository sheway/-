package com.example.software_engineering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hosted_event_DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Hosted_event.db";
    public Hosted_event_DBHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("create Table Event(Info_id TEXT, Date TEXT, Place TEXT, Event_name TEXT, Host_name TEXT, Max_ppl TEXT, P_Key TEXT primary key, is_infected TEXT)");
        //String event_data[] = {Info_id, 日期，地點, 活動名稱, 主辦單位, max ppl, key, is_infected}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean insertData(String Info_id, String Date, String Place, String Event_name, String Host_name, String Max_ppl, String is_infected)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Info_id", Info_id);
        contentValues.put("Date", Date);
        contentValues.put("Place", Place);
        contentValues.put("Event_name", Event_name);
        contentValues.put("Host_name", Host_name);
        contentValues.put("Max_ppl", Max_ppl);
        String a = Info_id + Date + Place + Event_name + Host_name + Max_ppl;
        String b = shaEncrypt(a);
        Log.d("Event_key", "Event:" + Event_name + " Key:" + b);
        contentValues.put("P_Key", shaEncrypt(a));
        contentValues.put("is_infected", is_infected);

        long result = DB.insert("Event", null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String[] hosted_event(int info_id)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Event where Info_id = ? ", new String[]{String.valueOf(info_id)});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String[] list = new String[cursor.getCount()];
            for(int i = 0 ; i < cursor.getCount() ; i++)
            {
                list[i] = cursor.getString(6);
                cursor.moveToNext();
            }
            return list;
        }
        else
        {
            String[] list = new String[1];
            list[0] = "Haven't host any event or error";
            return list;
        }
    }

    public String[] find_event_data(String key)
    {
        String[] event_data = new String[8];
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Event where P_Key = ? ", new String[]{String.valueOf(key)});
        cursor.moveToFirst();
        for(int i = 0 ; i < 8 ; i++)
        {
            if(cursor.getCount() > 0)
            {
                event_data[i] = cursor.getString(i);
            }
            else
            {
                event_data[i] = "error";
            }
        }
        return event_data;
    }
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 將此換成SHA-1、SHA-512、SHA-384等引數
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    public Boolean checkinfo(String event_name,String Host_name,String max_ppl)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Event where Event_name = ? and Host_name = ? and Max_ppl = ? ", new String[]{event_name, Host_name, max_ppl});

        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean labelinfected(String event_key)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Event where P_Key = ? ", new String[]{event_key});
        cursor.moveToFirst();
        if(cursor.getCount() > 0)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Info_id", cursor.getString(0));
            contentValues.put("Date", cursor.getString(1));
            contentValues.put("Place", cursor.getString(2));
            contentValues.put("Event_name", cursor.getString(3));
            contentValues.put("Host_name", cursor.getString(4));
            contentValues.put("Max_ppl", cursor.getString(5));
            contentValues.put("P_Key", cursor.getString(6));
            contentValues.put("is_infected", "True");
            long result = DB.update("Event", contentValues, "P_Key=?", new String[]{String.valueOf(event_key)});
            if(result == -1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        return false;
    }
}
