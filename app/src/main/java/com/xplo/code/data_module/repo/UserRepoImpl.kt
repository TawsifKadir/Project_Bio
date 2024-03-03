package com.xplo.code.data_module.repo

import com.xplo.code.data_module.core.CallInfo
import com.xplo.code.data_module.core.Config
import com.xplo.code.data_module.core.Resource
import com.xplo.code.data_module.model.user.ImageUploadRsp
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.ProfileInfo
import com.xplo.code.data_module.model.user.ProfileUpdateRqb
import com.xplo.code.data_module.model.user.TokenRsp
import com.xplo.code.data_module.network.api.UserApi
import com.xplo.code.data_module.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val api: UserApi
) : UserRepo {

    private val TAG = "UserRepoImpl"

    override suspend fun passwordLogin(body: LoginRqb): com.xplo.code.data_module.core.Resource<TokenRsp> {
        //return Resource.Success(TokenRsp("test-token"), CallInfo(0, null))

        return try {
            val response = api.generateToken(body)
            val result = response.body()
            val callInfo =
                com.xplo.code.data_module.core.CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                com.xplo.code.data_module.core.Resource.Success(result, callInfo)
            } else {
                com.xplo.code.data_module.core.Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            com.xplo.code.data_module.core.Resource.Failure(
                com.xplo.code.data_module.core.CallInfo(
                    -1,
                    e.message
                )
            )
        }
    }

    override suspend fun getProfileData(): com.xplo.code.data_module.core.Resource<ProfileInfo> {
        return try {
            val response = api.getProfileData("bn")
            val result = response.body()
            val callInfo =
                com.xplo.code.data_module.core.CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                com.xplo.code.data_module.core.Resource.Success(result, callInfo)
            } else {
                com.xplo.code.data_module.core.Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            com.xplo.code.data_module.core.Resource.Failure(
                com.xplo.code.data_module.core.CallInfo(
                    -1,
                    e.message
                )
            )
        }
    }

    override suspend fun updateProfileInfo(body: ProfileUpdateRqb): com.xplo.code.data_module.core.Resource<ProfileInfo> {
        return try {
            val response = api.updateProfileInfo(body, getLocale())
            val result = response.body()
            val callInfo =
                com.xplo.code.data_module.core.CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                com.xplo.code.data_module.core.Resource.Success(result, callInfo)
            } else {
                com.xplo.code.data_module.core.Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            com.xplo.code.data_module.core.Resource.Failure(
                com.xplo.code.data_module.core.CallInfo(
                    -1,
                    e.message
                )
            )
        }
    }

    override suspend fun uploadProfileAvatar(
        filePath: String,
        type: MediaType?
    ): com.xplo.code.data_module.core.Resource<ImageUploadRsp> {

        val file = FileUtils.reduceFileSize(File(filePath))
        val reqFile: RequestBody = RequestBody.create(type, file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_pic", file.name, reqFile)


        return try {
            val response = api.uploadProfileAvatar(body)
            val result = response.body()
            val callInfo =
                com.xplo.code.data_module.core.CallInfo(response.code(), response.message())
            if (response.isSuccessful && result != null) {
                com.xplo.code.data_module.core.Resource.Success(result, callInfo)
            } else {
                com.xplo.code.data_module.core.Resource.Failure(callInfo)
            }
        } catch (e: Exception) {
            com.xplo.code.data_module.core.Resource.Failure(
                com.xplo.code.data_module.core.CallInfo(
                    -1,
                    e.message
                )
            )
        }
    }


    private fun getLocale(): String {
        return com.xplo.code.data_module.core.Config.LOCALE
    }
}