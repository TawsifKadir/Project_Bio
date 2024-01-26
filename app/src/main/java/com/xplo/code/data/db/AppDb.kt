package com.xplo.code.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xplo.code.data.db.dao.HouseholdDao
import com.xplo.code.data.db.dao.PostDao
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.Post

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/1/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@Database(entities = [Post::class, HouseholdItem::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun householdDao(): HouseholdDao
}