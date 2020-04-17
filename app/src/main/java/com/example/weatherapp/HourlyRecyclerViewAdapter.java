//package com.example.weatherapp;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import java.util.ArrayList;
//import de.hdodenhof.circleimageview.CircleImageView;
//
////TODO: This is exactly the same code as the DailyRecyclerView, however it has to be implemented to retrieve
//// hourly weather for one day
//
//public class HourlyRecyclerViewAdapter extends RecyclerView.Adapter<HourlyRecyclerViewAdapter.ViewHolder>{
//
//    private static final String TAG = "HourlyRecyclerViewAdapter";
//
//    //vars
//    private ArrayList<String> mHours = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();
//    private Context mContext;
//
//    public HourlyRecyclerViewAdapter(Context context, ArrayList<String> hours, ArrayList<String> imageUrls) {
//        mHours = hours;
//        mImageUrls = imageUrls;
//        mContext = context;
//    }
//
//    @Override
//    //inflates each individual layout
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG,"onCreateViewHolder: called.");
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_layout_list_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    //bind the data to each individual list items
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: called.");
//
//        Glide.with(mContext)
//                .asBitmap() //will load whatever image is at that list
//                .load(mImageUrls.get(position))
//                .into(holder.weather); //referencing viewHolder because the widgets are saved in memory in viewHolder class
//
//        holder.hour.setText(mHours.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mHours.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        CircleImageView weather;
//        TextView hour;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            weather = itemView.findViewById(R.id.image);
//            hour = itemView.findViewById(R.id.hour);
//
//        }
//    }
//}
