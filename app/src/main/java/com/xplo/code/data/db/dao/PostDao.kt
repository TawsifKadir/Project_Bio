package com.xplo.code.data.db.dao

import androidx.room.*
import com.xplo.code.data.db.models.Post

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // or .IGNORE .REPLACE
    fun insert(item: Post)

    @Query("select * from post where id =:id")
    fun read(id: Int): Post

//    @Query("select * from post where postid =:id")
//    fun readByUserId(id: String): Post

    @Query("select * from post")
    fun readAll(): List<Post>

    @Update
    fun update(user: Post)

    @Delete
    fun delete(user: Post)
}