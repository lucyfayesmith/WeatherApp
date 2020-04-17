package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        getImages();
    }



    //TODO: The data used in the getImages below has to be retrieved from weather database,
    // it should retrieve forecast for next 7 days and store it, thus should be 7 days stored with respective image displaying forecast
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_1-512.png");
        mDays.add("Thurs");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_30-512.png");
        mDays.add("Fri");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_2-512.png");
        mDays.add("Sat");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_35-512.png");
        mDays.add("Sun");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_35-512.png");
        mDays.add("Mon");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_1-512.png");
        mDays.add("Tues");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_30-512.png");
        mDays.add("Wed");

        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_2-512.png");
        mDays.add("Thurs");





        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        DailyRecyclerViewAdapter adapter = new DailyRecyclerViewAdapter(this, mDays, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

//
//    //TODO: connect to weather database, i believe this is done through this WeatherContract? See comment below
//    //TODO: get this onClick to work so when the daily forecast is clicked it will display "activity_details"
//    @Override
//    public void onClick(long date) {
//        Intent weatherDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
//        Uri uriForDateClicked = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
//
//        //Is this weather contract for connecting to the DB? I got this function from the Udacity sunshine program
//        //this weatherContract in the udacity app correlates to a class found in:
//        //      S12.04-Solution-ResourceQualifiers/app/src/main/java/com/example/android/sunshine/data/WeatherContract.java
//
//        weatherDetailIntent.setData(uriForDateClicked);
//        startActivity(weatherDetailIntent);
//    }
}
