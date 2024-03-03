package com.xplo.code.data.db.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xplo.code.data_module.model.content.Alternate
import com.xplo.code.data_module.model.content.Biometric
import com.xplo.code.data_module.model.content.HouseholdMember
import com.xplo.code.data_module.model.content.Nominee
import java.util.Date

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

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


    @TypeConverter
    fun toAlternateList(value: String?): ArrayList<Alternate>? {
        val listType = object : TypeToken<ArrayList<Alternate>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromAlternateList(items: ArrayList<Alternate>?): String? {
        return Gson().toJson(items)
    }


    @TypeConverter
    fun toBiometricList(value: String?): ArrayList<Biometric>? {
        val listType = object : TypeToken<ArrayList<Biometric>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromBiometricList(items: ArrayList<Biometric>?): String? {
        return Gson().toJson(items)
    }




}