//package com.example.weatherapp;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class DetailActivity {
//    private static final String TAG = "DetailActivity";
//
//    //vars
//    private ArrayList<String> mHours = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_details);
//        Log.d(TAG, "onCreate: started.");
//
//        getImages();
//    }
//
//    //TODO: The data used in the getImages below has to be retrieved from weather database,
//    // it should retrieve hourly forecast for current day and store it, decide how this should be display (eg, 24hr time, am/pm)
//    private void getImages(){
//        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
//
//        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_1-512.png");
//        mHours.add("8:00");
//
//        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_30-512.png");
//        mHours.add("9:00");
//
//        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_2-512.png");
//        mHours.add("10:00");
//
//        mImageUrls.add("https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_35-512.png");
//        mHours.add("11:00");
//
//
//        initRecyclerView();
//    }
//
//    private void initRecyclerView(){
//        Log.d(TAG, "initRecyclerView: init recyclerview.");
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(layoutManager);
//        HourlyRecyclerViewAdapter adapter = new HourlyRecyclerViewAdapter(this, mHours, mImageUrls);
//        recyclerView.setAdapter(adapter);
//    }
//}
