package com.xplo.code.utils

import android.content.Context
import com.kit.integrationmanager.model.ServerInfo
import com.kit.integrationmanager.service.IntegrationManager
import com.kit.integrationmanager.service.OnlineIntegrationManager
import com.xplo.code.data.Constants
import com.xplo.code.data_module.core.ApiType
import com.xplo.code.data_module.core.Config
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

//    private val token =
//        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaG92b24iLCJpYXQiOjE3MDg4ODY5OTYsImV4cCI6MTg2NjU2Njk5Nn0.L-75R-EYM1GbrAqj-KdRpWLjxfxCMdVsAboepITEnI2I6AtTUtRhTgQaevzb5GOLWPnGaAUzggcC6SsArnMj-g"

    fun getHeader(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer ${Config.ACCESS_TOKEN}"
        headers["DeviceId"] = Constants.TEST_DEVICE_ID
        return headers
    }

    fun getServerInfo(): ServerInfo {
        val serverInfo = ServerInfo()

        if (Config.API_TYPE == ApiType.LIVE) {
            serverInfo.port = 443
            serverInfo.protocol = "https"
            serverInfo.host_name = "snsopafis.southsudansafetynet.info"
        } else {
            serverInfo.port = 8090
            serverInfo.protocol = "http"
            serverInfo.host_name = "snsopafis.karoothitbd.com"
        }

        return serverInfo
    }

    fun getIntegrationManager(context: Context, observer: Observer): IntegrationManager {
        return OnlineIntegrationManager(context, observer, getServerInfo())
    }

}