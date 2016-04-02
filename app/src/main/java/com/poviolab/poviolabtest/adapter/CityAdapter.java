package com.poviolab.poviolabtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poviolab.poviolabtest.R;
import com.poviolab.poviolabtest.model.City;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityHolder>  {

    private List<City> mCities;
    private Context mContext;

    public CityAdapter(Context c, List<City> cities) {
        mContext = c;
        mCities = cities;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_city_temperature, parent, false);
        return new CityHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, int position) {
        City city = mCities.get(position);
        holder.bindCity(city);
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public void setCrimes(List<City> cities) {
        mCities = cities;
    }

}