package com.xplo.code.data.db

import androidx.room.Room
import com.xplo.code.core.Contextor

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/1/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object DbController {

    const val dbName = "db.db"
    private var appDb: AppDb =
        Room.databaseBuilder(Contextor.getInstance().context, AppDb::class.java, dbName)
            .allowMainThreadQueries()
            .build()

    fun getAppDb(): AppDb {
        return appDb
    }

}