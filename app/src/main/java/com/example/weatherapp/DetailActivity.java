package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    //vars
    private ArrayList<String> mHours = new ArrayList<>();
    private ArrayList<Integer> ImageUrls = new ArrayList<>();
    private ArrayList<String> mTemp = new ArrayList<>();
    TextView mSunriseTime, mSunsetTime, mMaxTemp, mMinTemp;
    private WeatherAppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: started.");

        mSunriseTime = findViewById(R.id.sunrise_time);
        mSunsetTime= findViewById(R.id.sunset_time);
        mMaxTemp = findViewById(R.id.max_temp);
        mMinTemp =  findViewById(R.id.min_temp);
        repository = new WeatherAppRepository(getApplication());

        try {
            updateMainScreen();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateMainScreen() throws JSONException {
        mSunriseTime.setText(repository.getSunrise(MainActivity.CURRENT_WEATHER_DATA_JSON));
        mSunsetTime.setText(repository.getSunset(MainActivity.CURRENT_WEATHER_DATA_JSON));
        mMaxTemp.setText(repository.getMaxTemperature(MainActivity.CURRENT_WEATHER_DATA_JSON));
        mMinTemp.setText(repository.getMinTemperature(MainActivity.CURRENT_WEATHER_DATA_JSON));
        getImages();
    }

    private void launch_Activity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: The data used in the getImages below has to be retrieved from weather database,
    // it should retrieve hourly forecast for current day and store it, decide how this should be display (eg, 24hr time, am/pm)
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        String[] hours = new String[25];
        String[] hourlyTemp = new String[25];
        String[] hourlyIcon = new String[25];

        try {
            hours = repository.getHourlyTime(MainActivity.ONECALL_WEATHER_DATA_JSON);
            hourlyTemp = repository.getHourlyTemperatures(MainActivity.ONECALL_WEATHER_DATA_JSON);
            hourlyIcon  = repository.getHourlyIcons(MainActivity.ONECALL_WEATHER_DATA_JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<=24; i++){
            mHours.add(hours[i]);
            ImageUrls.add(MainActivity.getImageFromDrawable(hourlyIcon[i]));
            mTemp.add(hourlyTemp[i]);
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview2.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(layoutManager);
        HourlyRecyclerViewAdapter adapter = new HourlyRecyclerViewAdapter(this, mHours, ImageUrls, mTemp);
        recyclerView.setAdapter(adapter);
    }

}