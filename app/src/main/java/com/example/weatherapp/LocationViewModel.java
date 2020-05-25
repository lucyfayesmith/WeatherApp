package com.example.weatherapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weatherapp.db.entity.Location;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private WeatherAppRepository mRepository;

    private LiveData<List<Location>> mAllLocations;

    public LocationViewModel (Application application) {
        super(application);
        mRepository = new WeatherAppRepository(application);
        mAllLocations = mRepository.getAllLocations();
    }

    LiveData<List<Location>> getAllLocations() { return mAllLocations; }

    public void insert(Location location) { mRepository.insertLocation(location); }


}
