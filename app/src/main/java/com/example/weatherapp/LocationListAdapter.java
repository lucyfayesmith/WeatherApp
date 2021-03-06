package com.example.weatherapp;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.db.entity.Location;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder> {

    private final LayoutInflater mInflater;
    private List<Location> mLocations;

    LocationListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView locationItemView;
        final LocationListAdapter mAdapter;

        LocationViewHolder(View itemView, LocationListAdapter adapter) {
            super(itemView);
            locationItemView = itemView.findViewById(R.id.locationTextView);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            Toast.makeText(v.getContext(), "Location successfully deleted!", Toast.LENGTH_SHORT).show();
            MainActivity.getRepository().deleteLocation(mLocations.get(mPosition));
            mAdapter.notifyDataSetChanged();
        }
    }

    @NotNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new LocationViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NotNull LocationViewHolder holder, int position) {
        if (mLocations != null) {
            Location current = mLocations.get(position);
            holder.locationItemView.setText(current.getLocation());
        } else {
            // Covers the case of data not being ready yet.
            holder.locationItemView.setText("No Location");
        }
    }

    void setLocations(List<Location> locations) {
        mLocations = locations;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (mLocations != null)
            return mLocations.size();
        else return 0;
    }

}