package com.xplo.code.data_module.repo
import com.kit.integrationmanager.model.Beneficiary
import com.xplo.code.data_module.core.CallInfo
import com.xplo.code.data_module.core.Resource
import com.xplo.code.data_module.network.api.ContentApi
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

//    override suspend fun submitForm(body: FormRqb?): Resource<FormRsp> {
//        return try {
//            val response = api.submitForm(body)
//            val result = response.body()
//            val callInfo = CallInfo(response.code(), response.message())
//            if (response.isSuccessful && result != null) {
//                Resource.Success(result, callInfo)
//            } else {
//                Resource.Failure(callInfo)
//            }
//        } catch (e: Exception) {
//            Resource.Failure(CallInfo(-1, e.message))
//        }
//    }
//
//    override suspend fun submitForms(body: FormsRqb?): Resource<FormsRsp> {
//        return try {
//            val response = api.submitForms(body)
//            val result = response.body()
//            val callInfo = CallInfo(response.code(), response.message())
//            if (response.isSuccessful && result != null) {
//                Resource.Success(result, callInfo)
//            } else {
//                Resource.Failure(callInfo)
//            }
//        } catch (e: Exception) {
//            Resource.Failure(CallInfo(-1, e.message))
//        }
//    }

    override suspend fun sendBeneficiary(body: Beneficiary?): com.xplo.code.data_module.core.Resource<Unit> {
        return try {
            val response = api.sendBeneficiary(body)
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

}