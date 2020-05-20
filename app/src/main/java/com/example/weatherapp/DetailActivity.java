package com.example.weatherapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    //vars
    private ArrayList<String> mHours = new ArrayList<>();
    private ArrayList<String> ImageUrls = new ArrayList<>();
    private ArrayList<String> mTemp = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: started.");

        getImages(); //collecting hourly weather data with relevant images

        TextView textView1 = (TextView) findViewById(R.id.sunrise_time);
        textView1.setTextSize(30);
        textView1.setText("6:30am");

        TextView textView2 = (TextView) findViewById(R.id.sunset_time);
        textView2.setTextSize(30);
        textView2.setText("7:00pm");

        TextView textView3 = (TextView) findViewById(R.id.max_temp);
        textView3.setTextSize(30);
        textView3.setText("25C");

        TextView textView4 = (TextView) findViewById(R.id.min_temp);
        textView4.setTextSize(30);
        textView4.setText("10C");



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

        ImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_1-512.png");
        mHours.add("8:00");
        mTemp.add("25C");

        ImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_30-512.png");
        mHours.add("9:00");
        mTemp.add("22C");

        ImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_2-512.png");
        mHours.add("10:00");
        mTemp.add("23C");

        ImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_35-512.png");
        mHours.add("11:00");
        mTemp.add("24C");


        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HourlyRecyclerViewAdapter adapter = new HourlyRecyclerViewAdapter(this, mHours, ImageUrls, mTemp);
        recyclerView.setAdapter(adapter);
    }
}
