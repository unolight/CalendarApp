package com.mixerbox.mixerboxcalender;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

/**
 * Created by kwea on 2016/8/25.
 */
public class DB extends SQLiteOpenHelper {

    public static final String db_name = "mb.db";
    public static final String eventTable = "events";
    public static final String courseTable = "courses";
    public static final String mcTable = "mcs";
    private static final int db_version = 1;

    static SimpleDateFormat dateParser = new java.text.SimpleDateFormat("yyyy/MM/dd");
    static SimpleDateFormat dateDisplayer = new SimpleDateFormat("M/d");

    DB(Context context){
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + eventTable + " (" +
                "_id"+ " INTEGER PRIMARY KEY," +
                "startDate" + " DATE," +
                "endDate" + " DATE," +
                "alertTime" + " DATE," +
                "name" + " TEXT," +
                "type" + " TEXT," +
                "alert" + " BOOLEAN" +
                " )");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + mcTable + " (" +
                "_id"+ " INTEGER PRIMARY KEY," +
                "startDate" + " DATE," +
                "endDate" + " DATE," +
                "duration" + " INTEGER" +
                " )");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + courseTable + " (" +
                "_id"+ " INTEGER PRIMARY KEY," +
                "name" + " TEXT," +
                "day" + " INTEGER," + //1~5
                "startSlot" + " INTEGER," +
                "endSlot" + " INTEGER," +
                "prof" + " TEXT," +
                "location" + "TEXT" +
                " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
