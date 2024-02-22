package com.xplo.code.data.pref

import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.xplo.code.core.Contextor.Companion.getInstance

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object PrefUtils {
    private const val TAG = "PrefUtils"

    private const val PREF_NAME = "shared_pref"
    private val sp: SharedPreferences

    init {
        val context = getInstance().context
        //sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // use this if you want to sync with settings page automatically
        sp = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun putInt(key: String, value: Int) {
        val ed = sp.edit()
        ed.putInt(key, value)
        ed.apply()
    }

    fun putLong(key: String, value: Long) {
        val ed = sp.edit()
        ed.putLong(key, value)
        ed.apply()
    }

    fun putFloat(key: String, value: Float) {
        val ed = sp.edit()
        ed.putFloat(key, value)
        ed.apply()
    }

    fun putString(key: String, value: String?) {
        val ed = sp.edit()
        ed.putString(key, value)
        ed.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val ed = sp.edit()
        ed.putBoolean(key, value)
        ed.apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }

    @JvmStatic
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }

    fun getString(key: String, defValue: String?): String? {
        return sp.getString(key, defValue)
    }

    fun remove(key: String) {
        val ed = sp.edit()
        try {
            ed.remove(key)
            ed.apply()
            Log.d(TAG, "remove: $key removed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "remove: " + e.message, e)
            e.printStackTrace()
        }
    }

    fun clear() {
        val ed = sp.edit()
        try {
            ed.clear()
            //ed.commit();
            ed.apply()
            Log.d(TAG, "clear: Preference cleared successfully")
        } catch (e: Exception) {
            Log.e(TAG, "clear: " + e.message, e)
            e.printStackTrace()
        }
    }


}