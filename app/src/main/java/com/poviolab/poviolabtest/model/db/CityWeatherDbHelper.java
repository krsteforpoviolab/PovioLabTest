package com.poviolab.poviolabtest.model.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CityWeatherDbHelper extends SQLiteOpenHelper {
    //private static final String TAG = "CityWeatherDbHelper";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "city_weather.db";

    public CityWeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + CityWeatherDbSchema.CityTable.NAME + "(" +
                        CityWeatherDbSchema.CityTable.Cols.ID +" integer primary key autoincrement, " +
                        CityWeatherDbSchema.CityTable.Cols.NAME +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CityWeatherDbSchema.CityTable.NAME);
        onCreate(db);
    }
}
