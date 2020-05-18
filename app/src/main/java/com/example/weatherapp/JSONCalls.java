package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//  Example for current weather API: https://api.openweathermap.org/data/2.5/weather?lat=37&lon=-122&appid=549fb53c7c192bf46ad12689b7eed108
//  Example for ONE CALL API : https://api.openweathermap.org/data/2.5/onecall?lat=37&lon=-122&appid=549fb53c7c192bf46ad12689b7eed108

public class JSONCalls {

    protected static String getLocationName(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        JSONObject sys = obj.getJSONObject("sys");

        String name = obj.getString("name");
        String country = sys.getString("country");

        return ( name + ", " + country );

    }

    protected static String getTemperature(String jsonString) throws  JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);
        JSONObject main = tempJSON.getJSONObject("main");

        Double tempDouble = main.getDouble("temp");
        int tempInt = (int) Math.round(tempDouble);
        tempInt-=273;

        return (tempInt+"\u00B0");
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

    protected static String getIcon(String jsonString) throws JSONException{
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray weather = tempJSON.getJSONArray("weather");

        return weather.getJSONObject(0).getString("icon");
    }

    protected static String[] getDailyTemperatures(String jsonString) throws  JSONException{
        String dailyTemp[]= new String[7];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray daily = tempJSON.getJSONArray("daily");

        for (int i=0; i<7; i++){
            JSONObject thisDay = daily.getJSONObject(i);
            JSONObject tempThisDay = thisDay.getJSONObject("temp");
            Double tempDouble= tempThisDay.getDouble("day");
            int tempInt = (int) Math.round(tempDouble);
            tempInt-=273;
            dailyTemp[i]=(tempInt + "\u00B0");
        }

        return dailyTemp;
    }

    protected static String[] getDailyIcons(String jsonString) throws  JSONException{
        String dailyIcon[]= new String[7];
        JSONObject tempJSON = new JSONObject(jsonString);

        JSONArray daily = tempJSON.getJSONArray("daily");

        for (int i=0; i<7; i++){
            JSONObject thisDay = daily.getJSONObject(i);
            JSONArray weather = thisDay.getJSONArray("weather");
            dailyIcon[i]=weather.getJSONObject(0).getString("icon");
        }

        return dailyIcon;
    }
}