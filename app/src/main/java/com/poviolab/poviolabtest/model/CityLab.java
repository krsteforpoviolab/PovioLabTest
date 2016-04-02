package com.poviolab.poviolabtest.model;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CityLab {
    private static CityLab sCityLab;

    private Context mContext;
    private List<City> mCities;

    public static CityLab get(Context context) {
        if (sCityLab == null) {
            sCityLab = new CityLab(context);
        }
        return sCityLab;
    }

    private CityLab(Context context) {
        mContext = context.getApplicationContext();
        mCities = new ArrayList<>();
    }

    public void addCity(City c) {
        mCities.add(c);
    }


    public List<City> getCities() {
        return mCities;
    }

    public City getCity(int i) {
        return mCities.get(i);
    }
}
