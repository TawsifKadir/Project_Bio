package com.xplo.code.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object JsonUtils {

    fun toJsonPretty(o: Any?): String {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
        return gson.toJson(o)
    }

    fun toJsonPrettyLowerCaseUnderscore(o: Any?): String {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create()
        return gson.toJson(o)
    }

    fun toJson(o: Any?): String {
        val gson = Gson()
        return gson.toJson(o)
    }

    fun fromJson(jsonTxt: String?, ourClass: Class<*>?): Any {
        val gson = Gson()
        return gson.fromJson<Any>(jsonTxt, ourClass)
    }
}
