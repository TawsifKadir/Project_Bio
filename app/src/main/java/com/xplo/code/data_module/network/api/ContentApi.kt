package com.xplo.code.data_module.network.api

import com.kit.integrationmanager.model.Beneficiary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ContentApi {

//    @POST("/afis/api/register")
//    suspend fun submitForm(@Body body: FormRqb?): Response<FormRsp>
//
//    @POST("/afis/api/beneficiary/register/batch")
//    suspend fun submitForms(@Body body: FormsRqb?): Response<FormsRsp>

    @POST("/afis/api/beneficiary/register")
    suspend fun sendBeneficiary(@Body body: Beneficiary?): Response<Unit>



}