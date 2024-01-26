package com.xplo.data.repo

import com.xplo.data.model.user.ImageUploadRsp
import com.xplo.data.model.user.LoginRqb
import com.xplo.data.model.user.ProfileInfo
import com.xplo.data.model.user.ProfileUpdateRqb
import com.xplo.data.model.user.TokenRsp
import com.xplo.data.utils.Resource
import okhttp3.MediaType

interface UserRepo {

    suspend fun passwordLogin(credentials: LoginRqb): Resource<TokenRsp>

    suspend fun getProfileData(): Resource<ProfileInfo>

    suspend fun updateProfileInfo(body: ProfileUpdateRqb): Resource<ProfileInfo>

    suspend fun uploadProfileAvatar(filePath: String, type: MediaType?): Resource<ImageUploadRsp>

}