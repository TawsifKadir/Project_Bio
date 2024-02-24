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
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbndhcjMzNiIsImlhdCI6MTcwODY5MzY3MSwiZXhwIjoxODY2MzczNjcxfQ.NKYlfpD9fntB5T7jJ1agLVihZLVak7Z3qNYsA1YMIvdosIRwrEXPMJcIb2MZD4nUV0Sqa8fLzdEZMP9dD3eocg"
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