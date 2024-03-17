package com.xplo.code.ui.login.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/23/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@Keep
data class LoginCredentials(
    @SerializedName("userId")
    var userId: String? = null,
    @SerializedName("password")
    var password: String? = null
)