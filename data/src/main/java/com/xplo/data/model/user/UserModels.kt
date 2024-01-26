package com.xplo.data.model.user

import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("id")
    val id: String? = null

)

data class TokenRsp(
    @SerializedName("token")
    var token: String? = null
)

data class LoginRqb(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("password")
    var password: String? = null,
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