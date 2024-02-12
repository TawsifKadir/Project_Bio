package com.xplo.data.repo

import com.xplo.data.core.CallInfo
import com.xplo.data.core.Config
import com.xplo.data.model.user.ImageUploadRsp
import com.xplo.data.model.user.LoginRqb
import com.xplo.data.model.user.ProfileInfo
import com.xplo.data.model.user.ProfileUpdateRqb
import com.xplo.data.model.user.TokenRsp
import com.xplo.data.network.api.UserApi
import com.xplo.data.utils.FileUtils
import com.xplo.data.core.Resource
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val api: UserApi
) : UserRepo {

    private val TAG = "UserRepoImpl"

    override suspend fun passwordLogin(body: LoginRqb): Resource<TokenRsp> {
        //return Resource.Success(TokenRsp("test-token"), CallInfo(0, null))

        return try {
            val response = api.generateToken(body)
            val result = response.body()
            val callInfo = CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                Resource.Success(result, callInfo)
            } else {
                Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun getProfileData(): Resource<ProfileInfo> {
        return try {
            val response = api.getProfileData("bn")
            val result = response.body()
            val callInfo = CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                Resource.Success(result, callInfo)
            } else {
                Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun updateProfileInfo(body: ProfileUpdateRqb): Resource<ProfileInfo> {
        return try {
            val response = api.updateProfileInfo(body, getLocale())
            val result = response.body()
            val callInfo = CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                Resource.Success(result, callInfo)
            } else {
                Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun uploadProfileAvatar(
        filePath: String,
        type: MediaType?
    ): Resource<ImageUploadRsp> {

        val file = FileUtils.reduceFileSize(File(filePath))
        val reqFile: RequestBody = RequestBody.create(type, file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_pic", file.name, reqFile)


        return try {
            val response = api.uploadProfileAvatar(body)
            val result = response.body()
            val callInfo = CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                Resource.Success(result, callInfo)
            } else {
                Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }


    private fun getLocale(): String {
        return Config.LOCALE
    }
}