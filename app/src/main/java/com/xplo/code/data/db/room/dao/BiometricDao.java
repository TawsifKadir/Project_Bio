package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.xplo.code.data.db.room.model.Biometric;

import java.util.List;

@Dao
public interface BiometricDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertBiometric(Biometric biometric);

    @Update
    void updateBiometric(Biometric biometric);

    @Delete
    void deleteBiometric(Biometric biometric);

    @Query("SELECT * FROM biometric WHERE id = :id")
    Biometric getBiometricById(Long id);

    @Query("SELECT * FROM biometric WHERE application_id = :appId and biometric_user_type = 1")
    Biometric getBiometricsListByAppIdForBenaficiary(String appId);

    @Query("SELECT * FROM biometric WHERE application_id = :appId and type = 'ALT1'")
    Biometric getBiometricsListByAppIdForAlternate1(String appId);
    @Query("SELECT * FROM biometric WHERE application_id = :appId and type = 'ALT2'")
    Biometric getBiometricsListByAppIdForAlternate2(String appId);

    @Query("SELECT * FROM biometric")
    List<Biometric> getAllBiometrics();

    @Query("DELETE FROM biometric WHERE application_id = :appId")
    void deleteBiomatricByAppId(String appId);
}
