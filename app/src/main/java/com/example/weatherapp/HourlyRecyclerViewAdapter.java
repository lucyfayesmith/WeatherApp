package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;


public class HourlyRecyclerViewAdapter extends RecyclerView.Adapter<HourlyRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "HourlyRecyclerViewAdapter";

    //vars
    private ArrayList<String> mHours;
    private ArrayList<Integer> ImageUrls;
    private ArrayList<String> mTemp;
    private Context Context;

    HourlyRecyclerViewAdapter(Context context, ArrayList<String> hours, ArrayList<Integer> imageUrls, ArrayList<String> temp) {
        mHours = hours;
        ImageUrls = imageUrls;
        mTemp = temp;
        Context = context;
    }

    @NotNull
    @Override
    //inflates each individual layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    //bind the data to each individual list items
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(Context)
                .asBitmap() //will load whatever image is at that list
                .load(ImageUrls.get(position))
                .into(holder.weather); //referencing viewHolder because the widgets are saved in memory in viewHolder class

        holder.hour.setText(mHours.get(position));
        holder.temp.setText(mTemp.get(position));

    }

    @Override
    public int getItemCount() {
        return mHours.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView weather;
        TextView hour;
        TextView temp;

        ViewHolder(View itemView) {
            super(itemView);
            weather = itemView.findViewById(R.id.image);
            hour = itemView.findViewById(R.id.time_period);
            temp = itemView.findViewById(R.id.temperature);

        }
    }
}