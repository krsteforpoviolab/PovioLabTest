package com.poviolab.poviolabtest.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.poviolab.poviolabtest.R;
import com.poviolab.poviolabtest.model.City;
import com.poviolab.poviolabtest.model.CityLab;

public class AddCityActivity extends AppCompatActivity {


    private Button mAddCityButton;

    public static Intent newIntent(Context c) {
        Intent i = new Intent(c, AddCityActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_city);

        mAddCityButton = (Button) findViewById(R.id.add_city_button);
        mAddCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                City city = new City();
                city.setName(((EditText) findViewById(R.id.input_city_edit_text)).getText().toString());

                CityLab cityLab = CityLab.get(AddCityActivity.this.getApplicationContext());
                cityLab.addCity(city);

                finish();
            }
        });

    }

}
