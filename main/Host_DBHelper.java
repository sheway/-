package com.example.software_engineering;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Host_DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Host.db";
    public Host_DBHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB)
    {
        DB.execSQL("create Table Host(Info_id TEXT primary key, Name TEXT, Birthday TEXT, Cellphone TEXT, Personal_id TEXT, P_Key TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String[] get_personal_data(int id)
    {
        String[] personal_data = new String[6];
        SQLiteDatabase DB = this.getWritableDatabase();
        String str_id = String.valueOf(id);
        Cursor cursor = DB.rawQuery("Select * from Host where Info_id = ? ", new String[]{str_id});
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

        long result = DB.insert("Host", null, contentValues);
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

}
