package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONCalls {
    protected static String getLocationName(String jsonString)  throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONObject sys = obj.getJSONObject("sys");

        String name = obj.getString("name");
        String country = sys.getString("country");

        return ( name + ", " + country );

    }

    protected static String getTemperature(String jsonString)throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double tempDouble = main.getDouble("temp");
        int temp = (int) Math.round(tempDouble);
        temp-=273;

        return (temp+"\u00B0");
    }

    protected static String getWindSpeed(String jsonString) throws  JSONException{
        JSONObject obj = new JSONObject(jsonString);
        JSONObject wind = obj.getJSONObject("wind");

        Double speed = wind.getDouble("speed");
        return (speed +"km/h");
    }

    protected static String getHumidity(String jsonString) throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double humidityDouble = main.getDouble("humidity");
        int humidity =(int) Math.round(humidityDouble);

        return (humidity +"%");
    }

    //     https://api.openweathermap.org/data/2.5/onecall?lat=122&lon=33&appid=549fb53c7c192bf46ad12689b7eed108

    protected static String getIcon(String jsonString) throws JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray weather = tempJSON.getJSONArray("weather");

        return weather.getJSONObject(0).getString("icon");
    }

}