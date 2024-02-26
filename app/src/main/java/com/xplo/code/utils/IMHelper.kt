package com.xplo.code.utils

import android.content.Context
import com.kit.integrationmanager.model.ServerInfo
import com.kit.integrationmanager.service.IntegrationManager
import com.kit.integrationmanager.service.OnlineIntegrationManager
import java.util.Observer


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/20/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object IMHelper {

    private const val TAG = "IMHelper"

    private val token =
        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYWthZGlyXzIzIiwiaWF0IjoxNzA4ODg1NDcyLCJleHAiOjE4NjY1NjU0NzJ9.Fjf-FbaLDdIHuUsGuyOgiKlzbW2V9Bg2lTxxf7cAq4XGLYCaYsIEw4LX_sOp-WtWp7n-8pP95EkTSuvY31eMSg"

    fun getHeader(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = token
        headers["DeviceId"] = "47951385-a13f-409a-9a79-c4aaef0e3f9b"
        return headers
    }

    fun getServerInfo(): ServerInfo {
        val serverInfo = ServerInfo()
        serverInfo.port = 8090
        serverInfo.protocol = "http"
        serverInfo.host_name = "snsopafis.karoothitbd.com"
        return serverInfo
    }

    fun getIntegrationManager(context: Context, observer: Observer): IntegrationManager {
        return OnlineIntegrationManager(context, observer, getServerInfo())
    }

}