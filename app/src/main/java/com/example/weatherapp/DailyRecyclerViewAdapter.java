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
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

//TODO: This is exactly the same code as the HourlyRecyclerView, however it has to be implemented to retrieve
// daily weather for one week

public class DailyRecyclerViewAdapter extends RecyclerView.Adapter<DailyRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "DailyRecyclerViewAdapter";

    //vars
    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mTemperature = new ArrayList<>();
    private Context mContext;

    public DailyRecyclerViewAdapter(Context context, ArrayList<String> days, ArrayList<String> imageUrls, ArrayList<String> temp) {
        mDays = days;
        mImageUrls = imageUrls;
        mTemperature = temp;
        mContext = context;
    }

    @Override
    //inflates each individual layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    //bind the data to each individual list items
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap() //will load whatever image is at that list
                .load(mImageUrls.get(position))
                .into(holder.weather); //referencing viewHolder because the widgets are saved in memory in viewHolder class

        holder.day.setText(mDays.get(position));

        holder.temperature.setText(mTemperature.get(position));
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView weather;
        TextView day;
        TextView temperature;

        public ViewHolder(View itemView) {
            super(itemView);
            weather = itemView.findViewById(R.id.image);
            day = itemView.findViewById(R.id.day);
            temperature = itemView.findViewById(R.id.temperature);

        }
    }
}
