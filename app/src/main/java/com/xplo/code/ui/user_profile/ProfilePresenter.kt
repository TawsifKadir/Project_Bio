package com.xplo.code.ui.user_profile

import android.util.Log
import com.xplo.code.base.BasePresenter
import com.xplo.code.data_module.model.user.ProfileInfo
import com.xplo.code.data_module.repo.UserRepo
import com.xplo.code.data_module.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class ProfilePresenter(var repo: UserRepo) : BasePresenter<ProfileContract.View>(),
    ProfileContract.Presenter {

    companion object {
        private const val TAG = "ProfilePresenter"
    }

    override fun getProfileData(userId: String?) {
        Log.d(TAG, "getProfileData() called with: userId = $userId")
//        repo.getProfileData(userId, object : ResponseCallback<ProfileInfo> {
//            override fun onSuccess(data: ProfileInfo?) {
//                Log.d(TAG, "getProfileData: onSuccess() called with: data = $data")
//                view?.onGetProfileData(data!!)
//            }
//
//            override fun onFailure(th: Throwable) {
//                Log.d(TAG, "onFailure() called with: th = $th")
//                view?.onError(th.message)
//            }
//
//        })
    }

    override fun updateProfile(body: ProfileInfo) {
        Log.d(TAG, "updateProfile() called with: body = $body")

//        repo.updateProfile(body, object : ResponseCallback<JsonObject> {
//            override fun onSuccess(data: JsonObject?) {
//                Log.d(TAG, "getProfileData: onSuccess() called with: data = $data")
//
//            }
//
//            override fun onFailure(th: Throwable) {
//                Log.d(TAG, "onFailure() called with: th = $th")
//                view?.onError(th.message)
//            }
//
//        })
    }

    override fun uploadImage(filePath: String, mediaType: MediaType) {
        Log.d(TAG, "uploadImage() called with: filePath = $filePath, mediaType = $mediaType")


        val file: File = FileUtils.reduceFileSize(File(filePath))
        val reqFile = RequestBody.create(mediaType, file)
        val body = MultipartBody.Part.createFormData("profile_pic", file.name, reqFile)

//        repo.uploadImage(body, object : ResponseCallback<ImageUploadRsp> {
//            override fun onSuccess(data: ImageUploadRsp?) {
//                Log.d(TAG, "getProfileData: onSuccess() called with: data = $data")
//
//            }
//
//            override fun onFailure(th: Throwable) {
//                Log.d(TAG, "onFailure() called with: th = $th")
//                view?.onError(th.message)
//            }
//
//        })
    }


}