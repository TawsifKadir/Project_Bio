package com.xplo.code.data_module.repo

import com.xplo.code.data_module.core.Resource
import com.xplo.code.data_module.model.user.ImageUploadRsp
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.ProfileInfo
import com.xplo.code.data_module.model.user.ProfileUpdateRqb
import com.xplo.code.data_module.model.user.TokenRsp
import okhttp3.MediaType

interface UserRepo {

    suspend fun passwordLogin(body: LoginRqb): com.xplo.code.data_module.core.Resource<TokenRsp>

    suspend fun getProfileData(): com.xplo.code.data_module.core.Resource<ProfileInfo>

    suspend fun updateProfileInfo(body: ProfileUpdateRqb): com.xplo.code.data_module.core.Resource<ProfileInfo>

    suspend fun uploadProfileAvatar(filePath: String, type: MediaType?): com.xplo.code.data_module.core.Resource<ImageUploadRsp>

}