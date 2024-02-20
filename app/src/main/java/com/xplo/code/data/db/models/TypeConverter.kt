package com.xplo.code.data.db.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.xplo.data.model.content.HouseholdMember
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun toHouseholdMember(value: String?): HouseholdMember? {
        return Gson().fromJson(value, HouseholdMember::class.java)
    }

    @TypeConverter
    fun fromHouseholdMember(item: HouseholdMember?): String? {
        return Gson().toJson(item)
    }


}