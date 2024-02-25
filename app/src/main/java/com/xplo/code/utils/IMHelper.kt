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
        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbndhcjMzNiIsImlhdCI6MTcwODY5MzY3MSwiZXhwIjoxODY2MzczNjcxfQ.NKYlfpD9fntB5T7jJ1agLVihZLVak7Z3qNYsA1YMIvdosIRwrEXPMJcIb2MZD4nUV0Sqa8fLzdEZMP9dD3eocg"

    fun getHeader(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = token
        headers["DeviceId"] = "1234"
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