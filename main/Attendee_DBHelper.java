package com.example.software_engineering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Attendee_DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Attendee.db";

    public Attendee_DBHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("create Table Attendee(Info_id TEXT primary key, Name TEXT, Birthday TEXT, Cellphone TEXT, Personal_id TEXT, P_Key TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String[] get_personal_data(int id)
    {
        String[] personal_data = new String[6];
        SQLiteDatabase DB = this.getWritableDatabase();
        String str_id = String.valueOf(id);
        Cursor cursor = DB.rawQuery("Select * from Attendee where Info_id = ? ", new String[]{str_id});
        cursor.moveToFirst();
        for(int i = 0 ; i < 6 ; i++)
        {
            if(cursor.getCount() > 0)
            {
                personal_data[i] = cursor.getString(i);
            }
            else
            {
                personal_data[i] = "error";
            }
        }
        return personal_data;
    }

    public Boolean insertData(String Info_id, String Name, String Birthday, String Cellphone, String Personal_id)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Info_id", Info_id);
        contentValues.put("Name", Name);
        contentValues.put("Birthday", Birthday);
        contentValues.put("Cellphone", Cellphone);
        contentValues.put("Personal_id", Personal_id);
        String a = Info_id + Name + Birthday + Cellphone + Personal_id;
        contentValues.put("P_Key", shaEncrypt(a));

        long result = DB.insert("Attendee", null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
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
    public Boolean checkinfo(String birth, String phonenum, String identitynum)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Attendee where Birthday = ? ", new String[]{birth});
        Cursor cursor2 = DB.rawQuery("Select * from Attendee where Cellphone = ? ", new String[]{phonenum});
        Cursor cursor3 = DB.rawQuery("Select * from Attendee where Personal_id = ? ", new String[]{identitynum});

        if(cursor.getCount() > 0 || cursor2.getCount() > 0 || cursor3.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public String get_key(String id)
    {
        Log.d("id", id);
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Attendee where Personal_id = ? ", new String[]{id});
        cursor.moveToFirst();
        Log.d("id2", cursor.getString(5));
        return cursor.getString(5);
    }

}
