package com.poviolab.poviolabtest.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
                Resources res = getResources();
                String deleteCity = String.format(res.getString(R.string.delete_city),
                        cityLab.getCity(viewHolder.getAdapterPosition()).getName());

                Snackbar.make(findViewById(R.id.parent_coordinator_layout), deleteCity, Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        switch (event) {
                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                break;
                            case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                            case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                            case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
                                cityLab.deleteCity(mCities.get(viewHolder.getAdapterPosition()).get_id());
                                mCities = cityLab.getCities();
                                mAdapter.setCrimes(mCities);
                                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                break;
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                }).setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(mCrimeRecyclerView);

        //add some dummy data
        CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
        for (int i=0; i<30;i++) {
            City c = new City();
            c.setName("London "+i);
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
