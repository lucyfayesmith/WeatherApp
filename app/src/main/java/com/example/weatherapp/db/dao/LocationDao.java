/*
This interface (can be an abstract class as well) is the Data Access Object for the locations table.
Responsible for SELECT, Insert and Delete functions.
 */
package com.example.weatherapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.weatherapp.db.entity.Location;

import java.util.List;

@Dao
public interface LocationDao {

    /**
     * Inserts a location the database.
     * If a location exists then we replace the existing, we want 1 entry of each location.
     * @param location The location to insert in the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(Location location);

    /**
     * Deletes a location in the database.
     * @param location The location to delete from the database.
     */
    @Delete
    void deleteLocation(Location location);

    /**
     * Returns a list with all the locations in the database.
     * @return locations
     */
    @Query("SELECT * FROM locations")
    LiveData<List<Location>> getLocations();
}
