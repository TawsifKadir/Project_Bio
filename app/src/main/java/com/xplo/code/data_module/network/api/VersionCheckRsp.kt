package com.xplo.code.data_module.network.api


import com.google.gson.annotations.SerializedName

data class VersionCheckRsp(

    @SerializedName("apkUrl")
    var apkUrl: String? = null,
    @SerializedName("appVersion")
    var appVersion: String? = null,
    @SerializedName("forcedUpdate")
    var forcedUpdate: Boolean = false,
    @SerializedName("updateAvailable")
    var updateAvailable: Boolean = false,

    @SerializedName("errorCode")
    var errorCode: Int? = null,
    @SerializedName("errorMsg")
    var errorMsg: String? = null,
    @SerializedName("operationResult")
    var operationResult: Boolean? = null
)