package com.xplo.code.data_module.model.user

import com.google.gson.annotations.SerializedName
import com.kit.integrationmanager.model.Address
import com.kit.integrationmanager.model.Location


data class User(

    @SerializedName("id")
    val id: String? = null

)

data class TokenRsp(
    @SerializedName("token")
    var token: String? = null,
//    @SerializedName("id")
//    var id: String? = null,
    @SerializedName("userName")
    var userName: String? = null,
    @SerializedName("status")
    var status: String? = null,

)

fun TokenRsp?.isPending(): Boolean {
    if (this == null) return false
    if (this.status.equals("PENDING", true)) return true
    return false
}

fun TokenRsp?.isAuthenticated(): Boolean {
    if (this == null) return false
    if (this.token.isNullOrEmpty()) return false
    if (this.status.equals("ACTIVE", true)) return true
    return false
}

data class LoginRqb(
    @SerializedName("userName")
    var id: String? = null,
    @SerializedName("password")
    var password: String? = null,
)

data class ResetPassRqb(
    @SerializedName("newPassword")
    var newPassword: String? = null
)

data class ResetPassRsp(

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("errorCode")
    var errorCode: Int? = null,
    @SerializedName("errorMsg")
    var errorMsg: String? = null,
    @SerializedName("operationResult")
    var operationResult: Boolean? = null

)


data class RegisterDeviceRqb(
    @SerializedName("address")
    var address: Address? = null,
    @SerializedName("location")
    val location: Location? = null,
    @SerializedName("deviceId")
    val deviceId: String? = null,
    @SerializedName("imei")
    val imei: String? = null,
)

data class RegisterDeviceRsp(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("deviceId")
    var deviceId: String? = null,

    @SerializedName("errorCode")
    var errorCode: Int? = null,
    @SerializedName("errorMsg")
    var errorMsg: String? = null,
    @SerializedName("operationResult")
    var operationResult: Boolean? = null

)

data class LogoutRsp(
    @SerializedName("errorCode")
    var errorCode: Int? = null,
    @SerializedName("errorMsg")
    var errorMsg: String? = null,
    @SerializedName("operationResult")
    var operationResult: Boolean? = null
)

data class ProfileInfo(
    @SerializedName("status")
    var status: String? = null
)

data class ProfileUpdateRqb(
    @SerializedName("status")
    var status: String? = null
)

data class ImageUploadRsp(
    @SerializedName("status")
    var status: String? = null
)