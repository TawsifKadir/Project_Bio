package com.xplo.code.data.db.dao

import androidx.room.*
import com.xplo.code.data.db.models.BeneficiaryEntity

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@Dao
interface BeneficiaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // or .IGNORE .REPLACE
    fun insert(item: BeneficiaryEntity)

    @Query("select * from beneficiary_items where id =:id")
    fun read(id: String): BeneficiaryEntity

    @Query("select * from beneficiary_items where hid =:hid")
    fun readByHid(hid: String): BeneficiaryEntity


    @Query("select * from beneficiary_items")
    fun readAll(): List<BeneficiaryEntity>

    @Update
    fun update(user: BeneficiaryEntity)

    @Delete
    fun delete(user: BeneficiaryEntity)
}