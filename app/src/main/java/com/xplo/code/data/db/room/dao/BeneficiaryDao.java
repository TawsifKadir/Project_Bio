package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xplo.code.data.db.room.model.Address;
import com.xplo.code.data.db.room.model.Alternate;
import com.xplo.code.data.db.room.model.Beneficiary;
import com.xplo.code.data.db.room.model.Biometric;
import com.xplo.code.data.db.room.model.HouseholdInfo;
import com.xplo.code.data.db.room.model.Location;
import com.xplo.code.data.db.room.model.Nominee;
import com.xplo.code.data.db.room.model.SelectionReason;

import java.util.List;

@Dao
public interface BeneficiaryDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertBeneficiary(Beneficiary beneficiary);

    @Update
    void updateBeneficiary(Beneficiary beneficiary);

    @Delete
    void deleteBeneficiary(Beneficiary beneficiary);

    @Query("SELECT * FROM beneficiary WHERE id = :id")
    Beneficiary getBeneficiaryById(Long id);

    @Query("SELECT * FROM beneficiary WHERE application_id = :appId")
    Beneficiary getBeneficiaryByAppId(String appId);

    @Query("SELECT * FROM biometric WHERE application_id = :appId")
    Biometric getBiometricByAppId(String appId);

    @Query("SELECT * FROM location WHERE application_id = :appId")
    Location getLocationByAppId(String appId);


    @Query("SELECT * FROM address WHERE application_id = :appId")
    Address getAddressByAppId(String appId);

    @Query("SELECT * FROM selection_reason WHERE application_id = :appId")
    List<SelectionReason> getSelectionReasonByAppId(String appId);

    @Query("SELECT * FROM alternate WHERE application_id = :appId")
    List<Alternate> getAlternateByAppId(String appId);

    @Query("SELECT * FROM household_info WHERE application_id = :appId")
    List<HouseholdInfo> getHouseholdInfoByAppId(String appId);

    @Query("SELECT * FROM nominee WHERE application_id = :appId")
    List<Nominee> getNomineeByAppId(String appId);

    @Query("SELECT * FROM beneficiary")
    List<Beneficiary> getAllBeneficiaries();


}
