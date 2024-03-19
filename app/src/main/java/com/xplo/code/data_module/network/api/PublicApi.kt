package com.xplo.code.data_module.network.api

import retrofit2.Response
import retrofit2.http.GET

interface PublicApi {

    @GET("/afis/api/common/apk-update")
    suspend fun versionCheck(): Response<VersionCheckRsp>

}