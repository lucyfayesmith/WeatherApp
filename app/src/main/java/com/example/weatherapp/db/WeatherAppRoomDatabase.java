/*
This class represents the database of the Weather App.
Current version = 1 (whenever the schema changes, version should be updated)
 */
package com.example.weatherapp.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.weatherapp.db.dao.LocationDao;
import com.example.weatherapp.db.entity.Location;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
public abstract class WeatherAppRoomDatabase extends RoomDatabase {

    public abstract LocationDao locationDao();

    private static volatile WeatherAppRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newWorkStealingPool();

    /**
     * Get a database instance.
     * This is a singleton as to not create multiple instances of database at the same time.
     *
     * @return Instance of database
     */
    public static WeatherAppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherAppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherAppRoomDatabase.class, "weather_app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
