package com.xplo.code.ui.login

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.xplo.code.core.ext.toBool
import com.xplo.code.data_module.model.user.TokenRsp
import com.xplo.code.ui.login.model.LoginCredentials
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object OfflineLoginUtils {
    private const val TAG = "OfflineLoginUtils"

    fun tryOfflineLogin(context: Context, credentials: LoginCredentials): TokenRsp? {
        val loginInfo = readLoginInfoFromCache(context)
        if (loginInfo == null) return null

        if (loginInfo.tokenRsp == null) return null
        if (loginInfo.tokenRsp.token.isNullOrEmpty()) return null
        if (loginInfo.tokenRsp.userName.isNullOrEmpty()) return null

        if (loginInfo.credentials == null) return null
        if (loginInfo.credentials.userId == null) return null
        if (loginInfo.credentials.password == null) return null


        if (credentials.userId?.equals(loginInfo.credentials.userId).toBool()
            && credentials.password.equals(loginInfo.credentials.password)
        ) {
            return loginInfo.tokenRsp
        }
        return null
    }

    fun readLoginInfoFromCache(context: Context): LoginInfo? {
        Log.d(TAG, "readLoginInfoFromCache() called with: context = $context")
        val json = readJsonFromCache(context)
        return Gson().fromJson(json, LoginInfo::class.java)
    }

    fun writeLoginInfoToCache(context: Context, credentials: LoginCredentials, rsp: TokenRsp) {
        Log.d(
            TAG,
            "writeLoginInfoToCache() called with: context = $context, credentials = $credentials, rsp = $rsp"
        )
        val loginInfo = LoginInfo(
            credentials = credentials,
            tokenRsp = rsp
        )
        val json = Gson().toJson(loginInfo)
        writeJsonToCache(context, json)
    }

    private fun readJsonFromCache(context: Context): String? {
        val file = File(context.filesDir, "login_cache.json")
        if (!file.exists()) return null
        try {
            FileInputStream(file).use { fis ->
                InputStreamReader(fis).use { isr ->
                    BufferedReader(isr).use { br ->
                        val sb = StringBuilder()
                        var line: String?
                        while (br.readLine().also { line = it } != null) {
                            sb.append(line)
                        }
                        return sb.toString()
                    }
                }
            }
        } catch (exc: Exception) {
            Log.e(TAG, "Error occurred while reading cache.")
            exc.printStackTrace()
        }
        return null
    }

    private fun writeJsonToCache(context: Context, json: String?) {

        val file = File(context.filesDir, "login_cache.json")

        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (exc: java.lang.Exception) {
                Log.e(TAG, "Error occurred while creating cache file.")
                exc.printStackTrace()
                return
            }
        }

        try {
            FileOutputStream(file).use { fos ->
                OutputStreamWriter(fos).use { osw ->
                    osw.write(json)
                    osw.flush()
                }
            }
        } catch (exc: java.lang.Exception) {
            Log.e(TAG, "Error occurred while writing to cache.")
            exc.printStackTrace()
            return
        }

    }
}


data class LoginInfo(
    val credentials: LoginCredentials?,
    val tokenRsp: TokenRsp?
)