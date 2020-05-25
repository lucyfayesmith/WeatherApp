package com.example.weatherapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private static final String TAG = "MainActivity";
    private static final String SELECTED_UNIT = "SelectedUnit";

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
    LinearLayout mainLayout;


    private static final int REQUEST_LOCATION = 1;
    public static String CURRENT_WEATHER_DATA_JSON;
    public static String ONECALL_WEATHER_DATA_JSON;
    private int unitPreference;

    private WeatherAppRepository repository;
    private DrawerLayout drawerLayout;
    private int newLocationActivityRequestCode = 1;
    private LocationViewModel locationViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("my_shared_preferences", MODE_PRIVATE);


        unitPreference =pref.getInt(SELECTED_UNIT,0);


        Log.d(TAG, "onCreate: started.");
        repository = new WeatherAppRepository(getApplication());
        drawerLayout = findViewById(R.id.drawer);


        locationViewModel = new LocationViewModel(getApplication());
        initRecyclerView();

        NavigationView navigationView = findViewById(R.id.navigation_view);

        setupToolbar();
        createMenu(navigationView);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getTitle().equals("Add Location")) {
                Intent intent = new Intent(this, NewLocationActivity.class);
                startActivityForResult(intent, newLocationActivityRequestCode);
                return true;
            } else if (item.getTitle().equals("Current Location")) {
                currentLocationData();
            } else if (item.getGroupId() == R.id.unit) {
                if(item.getTitle().equals("Metric"))
                    unitPreference = 0;
                if(item.getTitle().equals("Imperial"))
                    unitPreference = 1;
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(SELECTED_UNIT, unitPreference);
                editor.apply();
                makeSearchQuery();
            } else {
                makeSearchQueryMenu(item.getTitle().toString());
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        LoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        location = (TextView) findViewById(R.id.location);
        wind_speed = (TextView) findViewById(R.id.wind_speed);
        humidity = (TextView) findViewById(R.id.rain_possibility);
        temperature = (TextView) findViewById(R.id.temperature);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);

        //ONCLICK METHOD THAT WILL DISPLAY EXTRA WEATHER DETAILS
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: mainLayout");
                launchActivity();
            }
        });

        currentLocationData();
    }

    private void currentLocationData() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            warnNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            makeSearchQuery();
        }
    }

    private void launchActivity() {
        Log.d(TAG, "launchActivity: activityDetails");
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private void createMenu(NavigationView navigationView) {

        LocationViewModel locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        locationViewModel.getAllLocations().observe(this, locations -> {
            Menu menu = navigationView.getMenu();
            menu.clear();
            navigationView.invalidate();

            MenuItem item = menu.add(R.id.locations, 0, 0, "Current Location");
            item.setIcon(R.mipmap.ic_launcher);
            item.setCheckable(true);

            for (int i = 1; i < locations.size(); i++) {
                item = menu.add(R.id.locations, i, 0, locations.get(i).getLocation());
                item.setIcon(R.mipmap.ic_launcher);
                item.setCheckable(true);
            }

            item = menu.add(R.id.add_location, 0, 1, "Add Location");
            item.setIcon(R.drawable.ic_add);
            item.setCheckable(true);

            item = menu.add(R.id.unit, 0, 1, "Metric");
            item.setIcon(R.mipmap.ic_launcher);
            item.setCheckable(true);

            item = menu.add(R.id.unit, 0, 2, "Imperial");
            item.setIcon(R.mipmap.ic_launcher);
            item.setCheckable(true);


        });
    }

    private void setupToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == newLocationActivityRequestCode && resultCode == RESULT_OK) {
            com.example.weatherapp.db.entity.Location location = new com.example.weatherapp.db.entity.Location(data.getStringExtra(NewLocationActivity.EXTRA_REPLY));
            new cityExistsTask().execute(location);


        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void makeSearchQueryMenu(String location) {
        new GeocodingTask(this).execute(location);
    }

    private void makeSearchQuery() {
        URL oneCallUrl = NetworkCalls.buildUrlOneCall(getLocation(), unitPreference);
        URL CurrentWeatherUrl = NetworkCalls.buildUrlCurrent(getLocation(), unitPreference);
        new currentWeatherQueryTask().execute(oneCallUrl, CurrentWeatherUrl);
    }

    public void updateMainScreen() throws JSONException {
        location.setText(repository.getLocationName(CURRENT_WEATHER_DATA_JSON));
        temperature.setText(repository.getTemperature(CURRENT_WEATHER_DATA_JSON));
        wind_speed.setText(repository.getWindSpeed(CURRENT_WEATHER_DATA_JSON));
        humidity.setText(repository.getHumidity(CURRENT_WEATHER_DATA_JSON));
        weather_icon.setImageResource(getImageFromDrawable(repository.getIcon(CURRENT_WEATHER_DATA_JSON)));
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
            } else if (location1 != null) {
                return location1;
            } else {
                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    public static int getImageFromDrawable(String code) {
        switch (code) {
            case "01d":
                return R.drawable.a01d;
            case "01n":
                return R.drawable.a01n;
            case "02d":
                return R.drawable.a02d;
            case "02n":
                return R.drawable.a02n;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                return R.drawable.a03d;
            case "09d":
            case "09n":
                return R.drawable.a09d;
            case "10d":
                return R.drawable.a10d;
            case "10n":
                return R.drawable.a10n;
            case "11d":
            case "11n":
                return R.drawable.a11d;
            case "13d":
            case "13n":
                return R.drawable.a13d;
            case "50d":
            case "50n":
                return R.drawable.a50d;
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

    private void getImages() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        String[] days = {"Mon", "Tue", "Wed", "Thurs", "Fri", "Sat", "Sun"};
        String[] dailyTemp = new String[7];
        String[] dailyIcon = new String[7];

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        try {
            dailyTemp = this.repository.getDailyTemperatures(ONECALL_WEATHER_DATA_JSON);
            dailyIcon = repository.getDailyIcons(ONECALL_WEATHER_DATA_JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i <= 6; i++) {
            mImageUrls.add(getImageFromDrawable(dailyIcon[i]));
            mDays.add(days[day % 7]);
            mTemperature.add(dailyTemp[i]);
            day++;
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        DailyRecyclerViewAdapter adapter = new DailyRecyclerViewAdapter(this, mDays, mImageUrls, mTemperature);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private class GeocodingTask extends AsyncTask<String, Void, Object> {
        Context mContext;

        public GeocodingTask(Context context) {
            super();
            mContext = context;
        }

        //Make the geocoder call in the background
        protected Object doInBackground(String... locations) {
            Geocoder geocoder = new Geocoder(mContext);

            List<Address> addresses = null;
            String location = locations[0];

            try {
                addresses = geocoder.getFromLocationName(location, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses.isEmpty())
                return new Object();

            return addresses.get(0);
        }

        protected void onPostExecute(Object addressObject) {

            if (addressObject.getClass() == Address.class) {
                Address address = (Address) addressObject;
                Location menuLocation = new Location("");
                Log.d("AddressLat", "" + address.getLatitude());
                Log.d("AddressLong", "" + address.getLongitude());

                menuLocation.setLatitude(address.getLatitude());
                menuLocation.setLongitude(address.getLongitude());

                URL oneCallUrl = NetworkCalls.buildUrlOneCall(menuLocation, unitPreference);
                URL CurrentWeatherUrl = NetworkCalls.buildUrlCurrent(menuLocation, unitPreference);
                new currentWeatherQueryTask().execute(oneCallUrl, CurrentWeatherUrl);
            }

        }

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
            URL currentWeatherUrl = urls[1];
            String oneCall = null;
            String currentWether = null;
            try {
                oneCall = NetworkCalls.getResponseFromHttpUrl(oneCallUrl);
                currentWether = NetworkCalls.getResponseFromHttpUrl(currentWeatherUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new String[]{oneCall, currentWether};
        }

        @Override
        protected void onPostExecute(String s[]) {
            LoadingIndicator.setVisibility(View.INVISIBLE);

            if (s != null && !s.equals("")) {
                Log.d("JsonData", s[1]);
                ONECALL_WEATHER_DATA_JSON = s[0];
                CURRENT_WEATHER_DATA_JSON = s[1];
                try {
                    updateMainScreen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "Error getting data. Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class cityExistsTask extends AsyncTask<com.example.weatherapp.db.entity.Location, Void, Object> {

        @Override
        protected Object doInBackground(com.example.weatherapp.db.entity.Location... locations) {
            URL checkIfCityExistsURL = NetworkCalls.checkIfCityExistsCall(locations[0].getLocation());
            String checkIfCityExists = null;

            try {
                checkIfCityExists = NetworkCalls.getResponseFromHttpUrl(checkIfCityExistsURL);
            } catch (IOException e) {
                return new Object();
            }
            return locations[0];
        }

        @Override
        protected void onPostExecute(Object o) {
            if (o.getClass() == com.example.weatherapp.db.entity.Location.class)
                locationViewModel.insert((com.example.weatherapp.db.entity.Location) o);
            else {
                Toast.makeText(
                        getApplicationContext(),
                        "Invalid City",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
