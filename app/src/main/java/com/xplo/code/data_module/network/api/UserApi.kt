package com.xplo.code.data_module.network.api

import com.xplo.code.data_module.model.user.ImageUploadRsp
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.LogoutRsp
import com.xplo.code.data_module.model.user.ProfileInfo
import com.xplo.code.data_module.model.user.ProfileUpdateRqb
import com.xplo.code.data_module.model.user.RegisterDeviceRqb
import com.xplo.code.data_module.model.user.RegisterDeviceRsp
import com.xplo.code.data_module.model.user.ResetPassRqb
import com.xplo.code.data_module.model.user.ResetPassRsp
import com.xplo.code.data_module.model.user.TokenRsp
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    //http://5.189.145.248:8090/afis/api/register

    @POST("/afis/api/auth/login")
    suspend fun generateToken(@Body body: LoginRqb): Response<TokenRsp>

    @POST("/afis/api/user/resetPassword")
    suspend fun resetPassword(@Body body: ResetPassRqb): Response<ResetPassRsp>

    @POST("/afis/api/device/register")
    suspend fun registerDevice(@Body body: RegisterDeviceRqb): Response<RegisterDeviceRsp>

    @GET("/afis/api/user/logout")
    suspend fun logout(@HeaderMap headers: Map<String, String>): Response<LogoutRsp>

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