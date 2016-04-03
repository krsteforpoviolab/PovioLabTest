package com.poviolab.poviolabtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.poviolab.poviolabtest.R;
import com.poviolab.poviolabtest.activities.CityDetailsActivity;
import com.poviolab.poviolabtest.model.City;

public class CityHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private TextView mCityTextView;
    private TextView mTemperatureTextView;
    private Context mContext;
    private City mCity;

    public CityHolder(Context c, View itemView) {
        super(itemView);
        mContext = c;
        itemView.setOnClickListener(this);
        mCityTextView = (TextView) itemView.findViewById(R.id.city_name_text_view);
        mTemperatureTextView = (TextView) itemView.findViewById(R.id.temperature_city_text_view);
     }

    public void bindCity(City city) {
        mCity = city;
        mCityTextView.setText(mCity.getName());
        if (mCity.getLastKnownTemperature()<-100000){
            mTemperatureTextView.setText("n/a");//not available yet
        }else{
            mTemperatureTextView.setText(String.valueOf(mCity.getLastKnownTemperature()));
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = CityDetailsActivity.newIntent(mContext, mCity.get_id());
        mContext.startActivity(intent);
    }
}