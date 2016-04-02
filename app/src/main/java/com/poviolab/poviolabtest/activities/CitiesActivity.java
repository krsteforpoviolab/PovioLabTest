package com.poviolab.poviolabtest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.poviolab.poviolabtest.R;
import com.poviolab.poviolabtest.adapter.CityAdapter;
import com.poviolab.poviolabtest.model.City;
import com.poviolab.poviolabtest.model.CityLab;

import java.util.List;

public class CitiesActivity extends AppCompatActivity {

    private RecyclerView mCrimeRecyclerView;
    private CityAdapter mAdapter;
    private List<City> mCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        mCrimeRecyclerView = (RecyclerView)
                findViewById(R.id.cities_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(CitiesActivity.this));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = AddCityActivity.newIntent(CitiesActivity.this);
                startActivity(i);
            }
        });

        //add some dummy data
        CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
        for (int i = 0; i < 100; i++) {
            City c = new City();
            c.setName("name " + i);
            cityLab.addCity(c);
        }

        updateUI();
    }

    private void updateUI() {
        CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
        mCities = cityLab.getCities();

        if (mAdapter == null) {
            mAdapter = new CityAdapter(CitiesActivity.this.getApplicationContext(), mCities);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(mCities);
            mAdapter.notifyDataSetChanged();
        }

        if (mCities.isEmpty()) {
            ((TextView) findViewById(R.id.no_items_text_view)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.no_items_text_view)).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
