package com.xplo.code.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object DeviceUtils {
    private const val TAG = "DeviceUtils"

    fun getDeviceIdForAdv(context: Context): String {
        return getDeviceId(context)
    }

    fun getDeviceId(context: Context): String {
        @SuppressLint("HardwareIds")
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val deviceId = md5(androidId).uppercase(Locale.getDefault())
        Log.d(TAG, "getDeviceId: $deviceId")
        return deviceId
    }

    fun getDeviceName(): String {
        return Build.MANUFACTURER + " " + Build.MODEL
    }

    private fun md5(s: String): String {

        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuffer()
            for (i in messageDigest.indices) {
                var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }


}