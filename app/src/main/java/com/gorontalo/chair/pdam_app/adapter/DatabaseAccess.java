package com.gorontalo.chair.pdam_app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private String TB_NOTIF = "tb_notifikasi";

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getNotifikasi(){
        List<String> listKabupaten = new ArrayList<String>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TB_NOTIF, null);
        if(cursor.moveToFirst()){
            do {
                listKabupaten.add(cursor.getString(0)+"/"+cursor.getString(1));
            }while (cursor.moveToNext());
        }
        cursor.close();

        return listKabupaten;
    }
}
