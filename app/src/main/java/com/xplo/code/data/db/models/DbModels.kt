package com.xplo.code.data.db.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,
    var title: String?,
    var body: String?,
    var imageUrl: String?
)