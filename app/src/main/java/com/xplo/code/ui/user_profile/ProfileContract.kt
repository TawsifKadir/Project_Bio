package com.xplo.code.ui.user_profile

import com.xplo.code.base.BaseContract
import com.xplo.data.model.user.ProfileInfo
import okhttp3.MediaType

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface ProfileContract {

    interface View : BaseContract.View {
        fun onGetProfileData(profileInfo: ProfileInfo)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getProfileData(userId: String?)

        fun updateProfile(body: ProfileInfo)

        fun uploadImage(filePath: String, mediaType: MediaType)

    }
}