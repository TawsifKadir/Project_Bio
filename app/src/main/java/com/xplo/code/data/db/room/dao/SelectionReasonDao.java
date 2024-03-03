package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.xplo.code.data.db.room.model.SelectionReason;

import java.util.List;

@Dao
public interface SelectionReasonDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertSelectionReason(SelectionReason selectionReason);

    @Update
    void updateSelectionReason(SelectionReason selectionReason);

    @Delete
    void deleteSelectionReason(SelectionReason selectionReason);

    @Query("SELECT * FROM selection_reason WHERE id = :id")
    SelectionReason getSelectionReasonById(Long id);

    @Query("SELECT * FROM selection_reason WHERE application_id = :appId")
    SelectionReason getSelectionReasonByAppId(String appId);

    @Query("SELECT * FROM selection_reason")
    List<SelectionReason> getAllSelectionReasons();

    @Query("DELETE FROM selection_reason WHERE application_id = :appId")
    void deleteReasonByAppId(String appId);

}

