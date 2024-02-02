package com.xplo.code.data.db.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.toJson
import com.xplo.code.ui.dashboard.model.toSummary
import java.io.Serializable

@Entity(tableName = "household")
data class HouseholdItem(

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "uuid")
    //var id: String = UUID.randomUUID().toString(),
    var uuid: String? = null,
    var data: String? = null,
    var isSynced: Boolean = false
) : Serializable

fun HouseholdItem?.toJson(): String? {
    return GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this)
}

fun HouseholdItem?.toHouseholdForm(): HouseholdForm? {
    return Gson().fromJson(this?.data, HouseholdForm::class.java)
}

fun HouseholdItem?.toSummary(): String? {
    if (this == null) return null
    val form = this.toHouseholdForm()
    var txt = "" +
//            "id: " + this.id.toString() +
            "" + form.toSummary()+
            "\n" + "Not Synced"
    return txt.trim()
}

