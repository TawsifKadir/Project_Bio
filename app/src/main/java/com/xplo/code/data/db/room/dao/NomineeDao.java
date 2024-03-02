package com.xplo.code.data.db.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.xplo.code.data.db.room.model.Nominee;

import java.util.List;

@Dao
public interface NomineeDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertNominee(Nominee nominee);

    @Update
    void updateNominee(Nominee nominee);

    @Delete
    void deleteNominee(Nominee nominee);

    @Query("SELECT * FROM nominee WHERE id = :id")
    Nominee getNomineeById(Long id);

    @Query("SELECT * FROM nominee where application_id=:appId")
    List<Nominee> getNomineeList(String appId);

    @Query("SELECT * FROM nominee")
    List<Nominee> getAllNominees();
}
