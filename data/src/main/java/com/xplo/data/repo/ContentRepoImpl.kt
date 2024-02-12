package com.xplo.data.repo

import com.xplo.data.core.CallInfo
import com.xplo.data.model.content.FormRqb
import com.xplo.data.model.content.FormRsp
import com.xplo.data.model.content.FormsRqb
import com.xplo.data.model.content.FormsRsp
import com.xplo.data.network.api.ContentApi
import com.xplo.data.core.Resource
import javax.inject.Inject

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/23/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class ContentRepoImpl @Inject constructor(
    private val api: ContentApi
) : ContentRepo {

    override suspend fun saveForm(body: FormRqb): Resource<FormRsp> {
        return try {
            val response = api.saveForm(body)
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

    override suspend fun saveForms(body: FormsRqb): Resource<FormsRsp> {
        return try {
            val response = api.saveForms(body)
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


}