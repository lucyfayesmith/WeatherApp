/*
This is the locations table in the database represented as a ROOM entity.
 */

package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class Location {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "location")
    private String mLocation;

    public Location(@NonNull String location) {
        this.mLocation = location;
    }

    public String getLocation() {
        return this.mLocation;
    }


}
