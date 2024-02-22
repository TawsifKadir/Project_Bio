package com.xplo.data.legacy_code.api

import com.xplo.data.model.user.LoginRqb
import com.xplo.data.model.user.TokenRsp
import retrofit2.Call
import retrofit2.http.*


/*
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-10
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface ApiEndpoint {

    @POST("/auth/api/login_check")
    fun generateToken(@Body body: LoginRqb): Call<TokenRsp>


}