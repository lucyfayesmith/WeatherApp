package com.example.weatherapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class weatherAppWidgetPhone extends AppWidgetProvider {
    public static String widgetTemper;
    public static String widgetIcon;
    public static String widgetCity;

    private ImageView weather_icon;

    private static final String mSharedPrefFile =
            "com.example.android.appwidgetsample";
    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
//<code for debuging (last update and widget ID) (a few of this code will be commented at the end)
//        shared instance
        SharedPreferences prefs = context.getSharedPreferences(
                mSharedPrefFile, 0);
//        count update
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;

//          get date for logic of last update
        String dateString =
                DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());


//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget_phone);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//        views.setTextViewText(R.id.appwidget_id, String.valueOf(appWidgetId));
//        set date to last update (view text)
//        views.setTextViewText(R.id.appwidget_update,
//                context.getResources().getString(
//                        R.string.date_count_format, count, dateString));

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();

        Intent intentUpdate = new Intent(context, weatherAppWidgetPhone.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);

//        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);


//</ code for debuging

//        //        set data to layout from json which is called in MainActivity
//        views.setTextViewText(R.id.AppWidgetTemper,
//                String.valueOf(widgetTemper));
//        views.setTextViewText(R.id.AppWidgetCity,
//                String.valueOf(widgetCity));
////        set data to layout from json which is called in MainActivity (drawable)
//        Resources resources = context.getResources();
//        final int resourceId = resources.getIdentifier("a"+"02d", "drawable",
//                context.getPackageName());
//        Drawable omg = resources.getDrawable(resourceId);
//        views.setImageViewResource(R.id.weather_icon, resourceId);

        doLoadingProvider(views,context);

//        //set data


 //        Application app = (Application) context.getApplicationContext();

//        Application application = MainActivity.getApplicationFromMainActivity();
//        int ahoj;
//        ((Activity) mContext).getApplication
//        LocationManager locationManager = (LocationManager) Context.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//    public void doLoading(View view, Context context){
//        Cursor cr = context.getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, "_id");
//        StringBuilder stringBuilder = new StringBuilder();
//
//        while (cr.moveToNext()){
//            int id = cr.getInt(0);
//            String s1 = cr.getString(1);
//            String s2 = cr.getString(2);
//            stringBuilder.append(id + "    " +s1+"     "+s2+"\n");
//        }
////        Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
//
//    }

        public static void doLoadingProvider(RemoteViews views, Context context){
            Cursor cr = context.getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, "_id");
            StringBuilder stringBuilder = new StringBuilder();

//        while (cr.moveToNext()){
//            int id = cr.getInt(0);
//            String s1 = cr.getString(1);
//            String s2 = cr.getString(2);
//            stringBuilder.append(id + "    " +s1+"     "+s2+"\n");
//        }

            cr.moveToLast();
            String location = cr.getString(1);
            String temperature = cr.getString(2);
            String icon = cr.getString(5);


            //        set data to layout from json which is called in MainActivity
            views.setTextViewText(R.id.AppWidgetTemper,
                    String.valueOf(temperature));
            views.setTextViewText(R.id.AppWidgetCity,
                    String.valueOf(location));
//        set data to layout from json which is called in MainActivity (drawable)
            Resources resources = context.getResources();
//        final int resourceId = resources.getIdentifier("a"+icon, "drawable",
//                context.getPackageName());
            int  resourceId = MainActivity.getImageFromDrawable(icon);
//        Drawable omg = resources.getDrawable(resourceId);
            views.setImageViewResource(R.id.weather_icon, resourceId);
//        //set data
//        Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
    }

    public static Drawable GetImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }


}

