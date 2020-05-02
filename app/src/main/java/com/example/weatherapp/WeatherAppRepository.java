/*
Data repository of the app.
Either chooses to retrieve data from the database or network (Weather API).
 */
package com.example.weatherapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.db.WeatherAppRoomDatabase;
import com.example.weatherapp.db.dao.LocationDao;
import com.example.weatherapp.db.entity.Location;

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


}
