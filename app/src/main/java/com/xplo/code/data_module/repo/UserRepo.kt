package com.xplo.code.data_module.repo

import com.xplo.code.data_module.core.Resource
import com.xplo.code.data_module.model.user.ImageUploadRsp
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.ProfileInfo
import com.xplo.code.data_module.model.user.ProfileUpdateRqb
import com.xplo.code.data_module.model.user.RegisterDeviceRqb
import com.xplo.code.data_module.model.user.RegisterDeviceRsp
import com.xplo.code.data_module.model.user.ResetPassRqb
import com.xplo.code.data_module.model.user.ResetPassRsp
import com.xplo.code.data_module.model.user.TokenRsp
import okhttp3.MediaType

interface UserRepo {

    suspend fun passwordLogin(body: LoginRqb): Resource<TokenRsp>

    suspend fun resetPassword(body: ResetPassRqb): Resource<ResetPassRsp>

    suspend fun registerDevice(body: RegisterDeviceRqb): Resource<RegisterDeviceRsp>

    suspend fun getProfileData(): Resource<ProfileInfo>

    suspend fun updateProfileInfo(body: ProfileUpdateRqb): Resource<ProfileInfo>

    suspend fun uploadProfileAvatar(filePath: String, type: MediaType?): Resource<ImageUploadRsp>

}