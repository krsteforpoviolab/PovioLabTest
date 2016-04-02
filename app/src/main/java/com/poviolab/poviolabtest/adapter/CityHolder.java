package com.poviolab.poviolabtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.poviolab.poviolabtest.R;
import com.poviolab.poviolabtest.model.City;

public class CityHolder extends RecyclerView.ViewHolder {

    private TextView mCityTextView;
    private Context mContext;
    private City mCity;

    public CityHolder(Context c, View itemView) {
        super(itemView);
        mContext = c;

        mCityTextView = (TextView) itemView.findViewById(R.id.temperature_city_text_view);
     }

    public void bindCity(City city) {
        mCity = city;
        mCityTextView.setText(mCity.getName());
    }

}