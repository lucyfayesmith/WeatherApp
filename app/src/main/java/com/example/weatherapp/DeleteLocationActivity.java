package com.example.weatherapp;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.db.entity.Location;

import java.util.List;

public class DeleteLocationActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.weatherapp.REPLY";

    private LocationViewModel mLocationViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_location);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final LocationListAdapter adapter = new LocationListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mLocationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mLocationViewModel.getAllLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(@Nullable final List<Location> locations) {
                // Update the cached copy of the locations in the adapter.
                adapter.setLocations(locations);
            }
        });
    }

}