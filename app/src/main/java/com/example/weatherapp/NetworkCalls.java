package com.example.weatherapp;

import android.location.Location;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkCalls {

    // Links to the free APIs and API KEYS.
    final static String ONE_CALL_API = "https://api.openweathermap.org/data/2.5/onecall?";

    final static String CURRENT_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?";

    final static String FIVE_DAY_3H_API = "pro.openweathermap.org/data/2.5/forecast/hourly?";

    final static String API_KEY = "549fb53c7c192bf46ad12689b7eed108";

    final static String SECOND_API_KEY = "bbacb51aa2d6511cd7eab0abdfec1048";


    public static URL checkIfCityExistsCall(String city){
        Uri builtUri = Uri.parse(CURRENT_WEATHER_API).buildUpon()
                .appendQueryParameter("q",city)
                .appendQueryParameter("appid",API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildUrlOneCall(Location location) {
        String lon =  location.getLongitude() + "";
        String lat =  location.getLatitude() + "";

        Uri builtUri = Uri.parse(ONE_CALL_API).buildUpon()
                .appendQueryParameter("lat",lat)
                .appendQueryParameter("lon",lon)
                .appendQueryParameter("appid",API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlCurrent(Location location) {
        String lon =  location.getLongitude() + "";
        String lat =  location.getLatitude() + "";

        Uri builtUri = Uri.parse(CURRENT_WEATHER_API).buildUpon()
                .appendQueryParameter("lat",lat)
                .appendQueryParameter("lon",lon)
                .appendQueryParameter("appid",API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForHourly(Location location){
        String lon = (int) location.getLongitude() + "";
        String lat = (int) location.getLatitude() + "";

        Uri builtUri = Uri.parse(FIVE_DAY_3H_API).buildUpon()
                .appendQueryParameter("lat",lat)
                .appendQueryParameter("lon",lon)
                .appendQueryParameter("appid",API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}