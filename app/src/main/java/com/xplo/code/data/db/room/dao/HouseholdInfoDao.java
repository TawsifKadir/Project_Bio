package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.xplo.code.data.db.room.model.HouseholdInfo;

import java.util.List;

@Dao
public interface HouseholdInfoDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertHouseholdInfo(HouseholdInfo householdInfo);

    @Update
    void updateHouseholdInfo(HouseholdInfo householdInfo);

    @Delete
    void deleteHouseholdInfo(HouseholdInfo householdInfo);

    @Query("SELECT * FROM household_info WHERE id = :id")
    HouseholdInfo getHouseholdInfoById(Long id);

    @Query("SELECT * FROM household_info")
    List<HouseholdInfo> getAllHouseholdInfo();

}
