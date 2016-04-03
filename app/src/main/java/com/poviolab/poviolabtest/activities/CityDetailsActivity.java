package com.poviolab.poviolabtest.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.poviolab.poviolabtest.R;
import com.poviolab.poviolabtest.model.City;
import com.poviolab.poviolabtest.model.CityLab;
import com.poviolab.poviolabtest.openweather.JSONHelper;
import com.poviolab.poviolabtest.openweather.OpenWeatherAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CityDetailsActivity extends AppCompatActivity {

    public static final String CITY_KEY = "city_key";

    private City mCity;

    public static Intent newIntent(Context c, long cityKey) {
        Intent i = new Intent(c, CityDetailsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(CITY_KEY, cityKey);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        if (getIntent().hasExtra(CITY_KEY)) {
            long key = getIntent().getLongExtra(CITY_KEY, -1);
            if (key > -1) {
                mCity = CityLab.get(CityDetailsActivity.this.getApplicationContext()).getCity(key);
                ((TextView) findViewById(R.id.city_name_text_view)).setText(mCity.getName());
                AppCompatActivity activity = (AppCompatActivity) CityDetailsActivity.this;
                activity.getSupportActionBar().setSubtitle(mCity.getName());
            } else {
                problemOccured();
            }
        } else {
            problemOccured();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTaskRunner().execute(mCity.getName());
    }

    public void problemOccured() {
        Toast.makeText(CityDetailsActivity.this, R.string.problem_fetching_msg, Toast.LENGTH_LONG).show();
        finish();
    }

    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private String resp;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CityDetailsActivity.this, "",
                    getResources().getString(R.string.please_wait), true);

            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String name = params[0];

                JSONHelper jsonHelper = new JSONHelper();
                String response = jsonHelper.getJSON(OpenWeatherAPI.addCityNameToUrl(OpenWeatherAPI.OPEN_WEATHER_URL, params[0]));
                if (response != null) {
                    return response;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                updateUI(result);
            } else {
                problemOccured();
            }
            progressDialog.dismiss();
        }
    }

    public void updateUI(String jsonString) {
        try {
            //ToDo add some checks for valid data
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray=jsonObject.getJSONArray("weather");
            JSONObject weatherJsonObject = jsonArray.getJSONObject(0);
            JSONObject mainJsonObject = jsonObject.getJSONObject("main");

            if (mCity.getName().equalsIgnoreCase(jsonObject.getString("name"))){
                ((TextView)findViewById(R.id.description_text_view)).setText(weatherJsonObject.getString("description"));
                ((TextView)findViewById(R.id.city_name_text_view)).setText(jsonObject.getString("name"));
                ((TextView)findViewById(R.id.temp_text_view)).setText("" + mainJsonObject.getDouble("temp"));
                ((TextView)findViewById(R.id.humidity_text_view)).setText("" + mainJsonObject.getDouble("humidity"));

                //update db with the last info for the temperature
                CityLab cityLab = CityLab.get(CityDetailsActivity.this.getApplicationContext());
                mCity.setLastKnownTemperature(mainJsonObject.getDouble("temp"));
                cityLab.updateCity(mCity);

            }else{
                Toast.makeText(CityDetailsActivity.this, R.string.invalid_city, Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}