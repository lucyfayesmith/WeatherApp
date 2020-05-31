/*
Data repository of the app.
Either chooses to retrieve data from the database or network (Weather API).
 */
package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.db.WeatherAppRoomDatabase;
import com.example.weatherapp.db.dao.LocationDao;
import com.example.weatherapp.db.entity.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAppRepository {

    private LocationDao mLocationDao;
    private LiveData<List<Location>> mAllLocations;

    WeatherAppRepository(Application application) {
        WeatherAppRoomDatabase db = WeatherAppRoomDatabase.getDatabase(application);
        mLocationDao = db.locationDao();
        mAllLocations = mLocationDao.getLocations();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Location>> getAllLocations() {
        return mAllLocations;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insertLocation(Location location) {
        WeatherAppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mLocationDao.insertLocation(location);
        });
    }

    void deleteLocation(Location location) {
        WeatherAppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mLocationDao.deleteLocation(location);
        });
    }

    String getLocationName(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONObject sys = obj.getJSONObject("sys");

        String name = obj.getString("name");
        String country = sys.getString("country");

        return ( name + ", " + country );

    }

    String getTemperature(String jsonString) throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double tempDouble = main.getDouble("temp");
        int tempInt = (int) Math.round(tempDouble);

        return (tempInt+"\u00B0");
    }

    String getWindSpeed(String jsonString,int unit) throws  JSONException{
        JSONObject obj = new JSONObject(jsonString);
        JSONObject wind = obj.getJSONObject("wind");
        String windUnit;

        if(unit==0)
            windUnit = "km/h";
        else
            windUnit = "mp/h";

        Double speed = wind.getDouble("speed");
        return speed + windUnit;
    }

    String getHumidity(String jsonString) throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double humidityDouble = main.getDouble("humidity");
        int humidity =(int) Math.round(humidityDouble);

        return (humidity +"%");
    }

    String getIcon(String jsonString) throws JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray weather = tempJSON.getJSONArray("weather");

        return weather.getJSONObject(0).getString("icon");
    }

    String[] getDailyTemperatures(String jsonString) throws  JSONException{
        String dailyTemp[]= new String[7];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray daily = tempJSON.getJSONArray("daily");

        for (int i=0; i<7; i++){
            JSONObject thisDay = daily.getJSONObject(i+1);
            JSONObject tempThisDay = thisDay.getJSONObject("temp");
            Double tempDouble= tempThisDay.getDouble("day");
            int tempInt = (int) Math.round(tempDouble);
            dailyTemp[i]=(tempInt + "\u00B0");
        }

        return dailyTemp;
    }

    String[] getHourlyTemperatures(String jsonString) throws  JSONException{
        String hourlyTemp[]= new String[25];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray hourly = tempJSON.getJSONArray("hourly");

        for (int i=0; i<=24; i++){
            JSONObject thisHour = hourly.getJSONObject(i);
            Double tempDouble= thisHour.getDouble("temp");
            int tempInt = (int) Math.round(tempDouble);
            hourlyTemp[i]=(tempInt + "\u00B0");
        }

        return hourlyTemp;
    }

    String[] getHourlyTime(String jsonString) throws  JSONException{
        String hourlyTemp[]= new String[25];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray hourly = tempJSON.getJSONArray("hourly");

        for (int i=0; i<=24; i++){
            JSONObject thisHour = hourly.getJSONObject(i);
            long hour = thisHour.getLong("dt");

            Date date = new java.util.Date(hour*1000L);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String formattedDate = sdf.format(date);
            hourlyTemp[i]=formattedDate;
        }

        return hourlyTemp;
    }

    String[] getDailyIcons(String jsonString) throws  JSONException{
        String dailyIcon[]= new String[7];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray daily = tempJSON.getJSONArray("daily");

        for (int i=0; i<7; i++){
            JSONObject thisDay = daily.getJSONObject(i+1);
            JSONArray weather = thisDay.getJSONArray("weather");
            dailyIcon[i]=weather.getJSONObject(0).getString("icon");
        }

        return dailyIcon;
    }

    String[] getHourlyIcons(String jsonString) throws  JSONException{
        String hourlyIcon[]= new String[25];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray hourly = tempJSON.getJSONArray("hourly");

        for (int i=0; i<25; i++){
            JSONObject thisHour = hourly.getJSONObject(i);
            JSONArray weather = thisHour.getJSONArray("weather");
            hourlyIcon[i]=weather.getJSONObject(0).getString("icon");
        }

        return hourlyIcon;
    }

    String getMaxTemperature(String jsonString) throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double tempDouble = main.getDouble("temp_max");
        int tempInt = (int) Math.round(tempDouble);

        return (tempInt+"\u00B0");
    }

    String getMinTemperature(String jsonString) throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double tempDouble = main.getDouble("temp_min");
        int tempInt = (int) Math.round(tempDouble);

        return (tempInt+"\u00B0");
    }

    String getSunrise(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONObject sys = obj.getJSONObject("sys");

        int timezone = obj.getInt("timezone");
        long sunrise = sys.getLong("sunrise");

        Date date = new java.util.Date(sunrise*1000L);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //sdf.setTimeZone(timezone);
        String formattedDate = sdf.format(date);
        return (formattedDate);

    }

    String getSunset(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONObject sys = obj.getJSONObject("sys");

        int timezone = obj.getInt("timezone");
        long sunset = sys.getLong("sunset");

        Date date = new java.util.Date(sunset*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //sdf.setTimeZone(timezone.getRawOffset());
        String formattedDate = sdf.format(date);
        return (formattedDate );
    }


}