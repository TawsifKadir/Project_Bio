package com.xplo.code.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import com.xplo.code.core.Contextor
import com.xplo.code.data.pref.PrefHelperImpl
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/20/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object Utils {

    private const val TAG = "Utils"

    fun isLanguageBangla(): Boolean {
        val locale = PrefHelperImpl().getLocale()
        if (locale.isNullOrEmpty()) return false
        return locale.equals("bn", true)
    }

    fun getDeviceDensityName(context: Context): String? {

        val density = context.resources.displayMetrics.density
        Log.d(TAG, "getDeviceDensityName: density: $density")

        val densityDpi = context.resources.displayMetrics.densityDpi
        Log.d(TAG, "getDeviceDensityName: densityDpi: $densityDpi")

        when (densityDpi) {
            DisplayMetrics.DENSITY_LOW -> return "ldpi"
            DisplayMetrics.DENSITY_MEDIUM -> return "mdpi"
            DisplayMetrics.DENSITY_TV, DisplayMetrics.DENSITY_HIGH -> return "hdpi"
            DisplayMetrics.DENSITY_260, DisplayMetrics.DENSITY_280, DisplayMetrics.DENSITY_300, DisplayMetrics.DENSITY_XHIGH -> return "xhdpi"
            DisplayMetrics.DENSITY_340, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_440, DisplayMetrics.DENSITY_XXHIGH -> return "xxhdpi"
            DisplayMetrics.DENSITY_560, DisplayMetrics.DENSITY_XXXHIGH -> return "xxxhdpi"
        }
        return null
    }

    fun getAccessCode(countryCode: String?): String {
        if (countryCode == null) return ""

        try {
            val base64AccessCode = Base64.encodeToString(
                countryCode.toByteArray(charset("UTF-8")),
                Base64.DEFAULT
            )
            return URLEncoder.encode(base64AccessCode.trim { it <= ' ' }, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return ""

    }


    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {

        var androidId = Settings.Secure.getString(
            Contextor.getInstance().context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        if (androidId.equals("unknown"))
            androidId = getRandomString()

        return androidId

    }

    fun getRandomString(): String {
        val sizeOfRandomString = 16
        val random = Random()
        val uuidString = UUID.randomUUID().toString().replace("-".toRegex(), "")
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString) sb.append(uuidString[random.nextInt(uuidString.length)])
        return sb.toString()
    }


}