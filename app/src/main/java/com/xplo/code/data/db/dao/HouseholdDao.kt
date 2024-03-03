package com.xplo.code.data.db.dao

import androidx.room.*
import com.xplo.code.data.db.models.HouseholdItem

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@Dao
interface HouseholdDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // or .IGNORE .REPLACE
    fun insert(item: HouseholdItem)

    @Query("select * from household where id =:id")     // uuid
    fun read(id: String): HouseholdItem
    @Query("select * from household where hid =:hid")
    fun readByHid(hid: String): HouseholdItem


//    @Query("select * from household where householdid =:id")
//    fun readByUserId(id: String): HhFormDbItem

    @Query("select * from household")
    fun readAll(): List<HouseholdItem>

    @Update
    fun update(user: HouseholdItem)

    @Delete
    fun delete(user: HouseholdItem)
}