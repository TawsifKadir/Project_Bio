package com.xplo.code.utils

import com.kit.integrationmanager.model.ServerInfo
import com.xplo.code.core.TestConfig
import com.xplo.code.ui.dashboard.model.HouseholdForm


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/20/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object FormAppUtils {

    private const val TAG = "FormAppUtils"

    fun canNomineeAdd(form: HouseholdForm?): Boolean {
        if (form == null) return false

        //if (BuildConfig.DEBUG) return true

        if (TestConfig.isNomineeAlwaysEnabled) return true

        if (form.form1?.county?.name.equals("JUBA", ignoreCase = true)) return true
        return false
    }


    fun getHeaderForIntegrationManager(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Authorization"] =
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwNzkyODUwOCwiZXhwIjoxODY1NjA4NTA4fQ.yRgZYaP2WlSoTtP8ZjhFLCTD3_Ov7SZtVLzrWG9BK7qDrXSCIlMwJM5kS0HDyrD1_qNbJFPm8Hz9KlkFGDfQ7Q"
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

}