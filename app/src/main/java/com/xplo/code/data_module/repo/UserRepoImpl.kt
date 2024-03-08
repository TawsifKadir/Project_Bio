package com.xplo.code.data_module.repo


import com.xplo.code.data_module.core.CallInfo
import com.xplo.code.data_module.core.Config
import com.xplo.code.data_module.core.Resource
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

    override suspend fun resetPassword(body: ResetPassRqb): Resource<ResetPassRsp> {
        return try {
            val response = api.resetPassword(body)
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

    override suspend fun registerDevice(body: RegisterDeviceRqb): Resource<RegisterDeviceRsp> {
        return try {
            val response = api.registerDevice(body)
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

    override suspend fun logout(token: String?): Resource<LogoutRsp> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return try {
            val response = api.logout(headers)
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