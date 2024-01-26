package com.xplo.data.network.api

import com.xplo.data.model.user.ImageUploadRsp
import com.xplo.data.model.user.LoginRqb
import com.xplo.data.model.user.ProfileInfo
import com.xplo.data.model.user.ProfileUpdateRqb
import com.xplo.data.model.user.TokenRsp
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("/auth/api/login_check")
    suspend fun generateToken(@Body body: LoginRqb): Response<TokenRsp>

    @GET("/auth/api/{language}/profile/me")
    suspend fun getProfileData(@Path("language") language: String): Response<ProfileInfo>

    @POST("/auth/api/{language}/profile/me/update")
    suspend fun updateProfileInfo(
        @Body body: ProfileUpdateRqb,
        @Path("language") language: String
    ): Response<ProfileInfo>

    @Multipart
    @POST("/auth/api/en/profile/me/update-profile-pic")
    suspend fun uploadProfileAvatar(@Part image: MultipartBody.Part): Response<ImageUploadRsp>

}