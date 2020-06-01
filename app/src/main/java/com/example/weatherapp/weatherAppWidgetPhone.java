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

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget_phone);


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

        doLoadingProvider(views,context);

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



        public static void doLoadingProvider(RemoteViews views, Context context){
            Cursor cr = context.getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, "_id");
            StringBuilder stringBuilder = new StringBuilder();

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

