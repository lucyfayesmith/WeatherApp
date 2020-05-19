package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private ArrayList<String> mTemperature = new ArrayList<>();

    private TextView location;
    private TextView wind_speed;
    private TextView humidity;
    private TextView temperature;
    private ProgressBar LoadingIndicator;
    private ImageView weather_icon;

    LocationManager locationManager;

    private static final int REQUEST_LOCATION = 1;
    private static String CURRENT_WEATHER_DATA_JSON;
    private static String ONECALL_WEATHER_DATA_JSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        LoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        location = (TextView) findViewById(R.id.location);
        wind_speed = (TextView) findViewById(R.id.wind_speed);
        humidity = (TextView) findViewById(R.id.rain_possibility);
        temperature = (TextView) findViewById(R.id.temperature);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            warnNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            makeSearchQuery();
        }
    }

    private void makeSearchQuery( ) {
        URL oneCallUrl = NetworkCalls.buildUrlOneCall(getLocation());
        URL CurrentWeatherUrl = NetworkCalls.buildUrlCurrent(getLocation());
        new currentWeatherQueryTask().execute(oneCallUrl,CurrentWeatherUrl);
    }

    public class currentWeatherQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL oneCallUrl = urls[0];
            URL currentWeatherUrl= urls[1];
            String oneCall = null;
            String currentWether = null;
            try{
                oneCall = NetworkCalls.getResponseFromHttpUrl(oneCallUrl);
                currentWether = NetworkCalls.getResponseFromHttpUrl(currentWeatherUrl);
            }catch (IOException e){
                e.printStackTrace();
            }

            return new String[] {oneCall, currentWether};
        }

        @Override
        protected void onPostExecute(String s[]) {
            LoadingIndicator.setVisibility(View.INVISIBLE);

            if (s!=null  && !s.equals("")){
                ONECALL_WEATHER_DATA_JSON = s[0];
                CURRENT_WEATHER_DATA_JSON = s[1];
                try {
                    updateMainScreen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this,"Error getting data. Please try again",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateMainScreen() throws JSONException {
        location.setText(JSONCalls.getLocationName(CURRENT_WEATHER_DATA_JSON));
        temperature.setText(JSONCalls.getTemperature(CURRENT_WEATHER_DATA_JSON));
        wind_speed.setText(JSONCalls.getWindSpeed(CURRENT_WEATHER_DATA_JSON));
        humidity.setText(JSONCalls.getHumidity(CURRENT_WEATHER_DATA_JSON));
        weather_icon.setImageResource(getImageFromDrawable(JSONCalls.getIcon(CURRENT_WEATHER_DATA_JSON)));
        getImages();
    }

    private Location getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                return location;
            } else  if (location1 != null) {
                return location1;
            }else{
                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    public int getImageFromDrawable(String code){
        switch (code){
            case "01d": return R.drawable.a01d;
            case "01n": return R.drawable.a01n;
            case "02d": return R.drawable.a02d;
            case "02n": return R.drawable.a02n;
            case "03d":
            case "03n":
            case "04d":
            case "04n": return R.drawable.a03d;
            case "09d":
            case "09n": return R.drawable.a09d;
            case "10d": return R.drawable.a10d;
            case "10n": return R.drawable.a10n;
            case "11d":
            case "11n": return R.drawable.a11d;
            case "13d":
            case "13n": return R.drawable.a13d;
            case "50d":
            case "50n": return R.drawable.a50d;
        }
        return R.drawable.a01d;
    }

    protected void warnNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat"};
        String[] dailyTemp = new String[7];
        String[] dailyIcon = new String[7];

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;

        try {
            dailyTemp= JSONCalls.getDailyTemperatures(ONECALL_WEATHER_DATA_JSON);
            dailyIcon = JSONCalls.getDailyIcons(ONECALL_WEATHER_DATA_JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i<=6; i++){
            mImageUrls.add(getImageFromDrawable(dailyIcon[i]));
            mDays.add(days[day % 7]);
            mTemperature.add(dailyTemp[i]);
            day++;
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        DailyRecyclerViewAdapter adapter = new DailyRecyclerViewAdapter(this, mDays, mImageUrls, mTemperature);
        recyclerView.setAdapter(adapter);
    }
}
