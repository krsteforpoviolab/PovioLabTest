package com.poviolab.poviolabtest.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poviolab.poviolabtest.model.db.CityCursorWrapper;
import com.poviolab.poviolabtest.model.db.CityWeatherDbHelper;
import com.poviolab.poviolabtest.model.db.CityWeatherDbSchema;

import java.util.ArrayList;
import java.util.List;

public class CityLab {
    private static CityLab sCityLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CityLab get(Context context) {
        if (sCityLab == null) {
            sCityLab = new CityLab(context);
        }
        return sCityLab;
    }

    private CityLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CityWeatherDbHelper(mContext)
                .getWritableDatabase();
    }


    public void addCity(City c) {
        ContentValues values = getContentValues(c);
        long id = mDatabase.insert(CityWeatherDbSchema.CityTable.NAME, null, values);

    }

    public City getCity(long id) {
        CityCursorWrapper cursor = queryCities(
                CityWeatherDbSchema.CityTable.Cols.ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCity();
        } finally {
            cursor.close();
        }
    }

    public void deleteCity(long id) {
        mDatabase.delete(CityWeatherDbSchema.CityTable.NAME, CityWeatherDbSchema.CityTable.Cols.ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //
    public List<City> getCities() {
        List<City> crimes = new ArrayList<>();

        CityCursorWrapper cursor = queryCities(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            crimes.add(cursor.getCity());
            cursor.moveToNext();
        }
        cursor.close();

        return crimes;
    }

    private CityCursorWrapper queryCities(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CityWeatherDbSchema.CityTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new CityCursorWrapper(cursor);
    }

//    public void updateCity(City city) {
//        String name = city.getName().toString();
//        ContentValues values = getContentValues(city);
//
//        mDatabase.update(CityWeatherDbSchema.CityTable.NAME, values,
//                CityTemperatureDbSchema.CityTable.Cols.NAME + " = ?",
//                new String[]{name});
//    }

    private static ContentValues getContentValues(City city) {
        ContentValues values = new ContentValues();
        //values.put(CityTemperatureDbSchema.CityTable.Cols.ID, city.get_id());
        values.put(CityWeatherDbSchema.CityTable.Cols.NAME, city.getName());
        return values;
    }
}
