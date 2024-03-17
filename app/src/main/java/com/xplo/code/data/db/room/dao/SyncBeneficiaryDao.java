package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xplo.code.data.db.room.model.SyncBeneficiary;

import java.util.List;

@Dao
public interface SyncBeneficiaryDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertSyncBeneficiary(SyncBeneficiary syncBeneficiary);

    @Update
    void updateSyncBeneficiary(SyncBeneficiary syncBeneficiary);

    @Delete
    void deleteSyncBeneficiary(SyncBeneficiary syncBeneficiary);

    @Query("SELECT * FROM sync_beneficiary WHERE id = :id")
    SyncBeneficiary getSyncBeneficiaryById(Long id);

    @Query("SELECT * FROM sync_beneficiary WHERE application_id = :appId")
    List<SyncBeneficiary> getSyncBeneficiaryByAppId(String appId);

    @Query("SELECT * FROM sync_beneficiary")
    List<SyncBeneficiary> getAllSyncBeneficiaries();

    @Query("DELETE FROM sync_beneficiary WHERE application_id = :appId")
    void deleteSyncBeneficiaryByAppId(String appId);
}
