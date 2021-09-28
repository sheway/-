package com.example.software_engineering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Login_DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";

    public Login_DBHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("create Table users(info_id TEXT primary key, mailaddr TEXT, password TEXT, account_type TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1)
    {
        DB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String info_id, String mailaddr, String password, String acc_type)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("info_id", info_id);
        contentValues.put("mailaddr", mailaddr);
        contentValues.put("password", password);
        contentValues.put("account_type", acc_type);

        long result = DB.insert("users", null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public int getInfo_id()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from users",null);
        cursor.moveToLast();
        return cursor.getShort(0);
    }

    public Boolean checkusername(String mailaddr)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from users where mailaddr = ? ", new String[]{mailaddr});
        if(cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String[] checkusernamepassword(String mailaddr, String password)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from users where mailaddr = ? and password = ? ", new String[]{mailaddr, password});
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            String[] list = new String[2];
            list[0] = cursor.getString(3);
            list[1] = cursor.getString(0);
            return list;
        }
        else
        {
            String[] list = new String[1];
            list[0] = "Error";
            return list;
        }
    }
}
