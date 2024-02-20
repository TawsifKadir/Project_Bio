package com.xplo.code.data.db.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xplo.data.model.content.HouseholdMember
import com.xplo.data.model.content.Nominee
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

    @TypeConverter
    fun toNomineeList(value: String?): ArrayList<Nominee>? {
        val listType = object : TypeToken<ArrayList<Nominee>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromNomineeList(items: ArrayList<Nominee>?): String? {
        return Gson().toJson(items)
    }


}