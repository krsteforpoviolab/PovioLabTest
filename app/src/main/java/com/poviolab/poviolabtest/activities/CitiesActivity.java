package com.poviolab.poviolabtest.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.poviolab.poviolabtest.openweather.JSONHelper;
import com.poviolab.poviolabtest.openweather.OpenWeatherAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CitiesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mCitiesRecyclerView;
    private CityAdapter mAdapter;
    private List<City> mCities;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        mCitiesRecyclerView = (RecyclerView)
                findViewById(R.id.cities_recycler_view);
        mCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(CitiesActivity.this));


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
                        cityLab.getCity(mCities.get(viewHolder.getAdapterPosition()).get_id()).getName());

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
                                mAdapter.setCities(mCities);
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

        itemTouchHelper.attachToRecyclerView(mCitiesRecyclerView);

        //add some dummy data
//        CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
//        for (int i=0; i<10;i++) {
//            City c = new City();
//            c.setName("London "+i);
//            cityLab.addCity(c);
//        }


        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);

        updateUI();
        new AsyncTaskRunner().execute();
    }

    private void updateUI() {
        CityLab cityLab = CityLab.get(CitiesActivity.this.getApplicationContext());
        mCities = cityLab.getCities();

        if (mAdapter == null) {
            mAdapter = new CityAdapter(CitiesActivity.this.getApplicationContext(), mCities);
            mCitiesRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.setCities(mCities);
        mAdapter.notifyDataSetChanged();

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

    @Override
    public void onRefresh() {
        new AsyncTaskRunner().execute();
    }


    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<City> cities = CityLab.get(CitiesActivity.this.getApplicationContext()).getCities();
                JSONHelper jsonHelper = new JSONHelper();

                for (int i = 0; i < cities.size(); i++) {
                    String response = jsonHelper.getJSON(OpenWeatherAPI.addCityNameToUrl(OpenWeatherAPI.OPEN_WEATHER_URL, cities.get(i).getName()));
                    if (response != null) {
                        //parse temperature
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray=jsonObject.getJSONArray("weather");
                        JSONObject weatherJsonObject = jsonArray.getJSONObject(0);
                        JSONObject mainJsonObject = jsonObject.getJSONObject("main");
                        //update city temperature
                        cities.get(i).setLastKnownTemperature(mainJsonObject.getDouble("temp"));
                        CityLab.get(CitiesActivity.this.getApplicationContext()).updateCity(cities.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSwipeLayout.setRefreshing(false);
            updateUI();
        }
    }
}
