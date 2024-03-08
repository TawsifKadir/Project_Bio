package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xplo.code.data.db.room.model.Location;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertLocation(Location location);

    @Update
    void updateLocation(Location location);

    @Delete
    void deleteLocation(Location location);

    @Query("SELECT * FROM location WHERE id = :id")
    Location getLocationById(Long id);

    @Query("SELECT * FROM location WHERE application_id = :appId")
    Location getLocationByAppId(String appId);

    @Query("SELECT * FROM location")
    List<Location> getAllLocations();

    @Query("DELETE FROM location WHERE application_id = :appId")
    void deleteLocationByAppId(String appId);

}
