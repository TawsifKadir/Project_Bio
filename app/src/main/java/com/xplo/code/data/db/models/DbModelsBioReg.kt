package com.xplo.code.data.db.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.xplo.code.ui.dashboard.model.HouseholdForm
import java.util.UUID

@Entity(tableName = "household")
data class HouseholdItem(

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    //var id: String = UUID.randomUUID().toString(),
    var id: String,
    var data: String? = null,
    var isSynced: Boolean = false
)

fun HouseholdItem?.toHouseholdForm(): HouseholdForm? {
    return Gson().fromJson(this?.data, HouseholdForm::class.java)
}

