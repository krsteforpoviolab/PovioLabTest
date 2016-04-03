package com.poviolab.poviolabtest.model.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.poviolab.poviolabtest.model.City;

public class CityCursorWrapper extends CursorWrapper {
        public CityCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public City getCity() {
            long id = getLong(getColumnIndex(CityWeatherDbSchema.CityTable.Cols.ID));
            String name = getString(getColumnIndex(CityWeatherDbSchema.CityTable.Cols.NAME));
            double lastKnownTemperature = getDouble(getColumnIndex(CityWeatherDbSchema.CityTable.Cols.LAST_KNOWN_TEMPERATURE));

            City city = new City();
            city.setName(name);
            city.set_id(id);
            city.setLastKnownTemperature(lastKnownTemperature);

            return city;
        }
    }

