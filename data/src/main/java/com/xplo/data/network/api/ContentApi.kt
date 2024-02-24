package com.xplo.data.network.api

import com.kit.integrationmanager.model.Beneficiary
import com.xplo.data.model.content.FormRqb
import com.xplo.data.model.content.FormRsp
import com.xplo.data.model.content.FormsRqb
import com.xplo.data.model.content.FormsRsp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ContentApi {

    @POST("/afis/api/register")
    suspend fun submitForm(@Body body: FormRqb?): Response<FormRsp>

    @POST("/afis/api/beneficiary/register/batch")
    suspend fun submitForms(@Body body: FormsRqb?): Response<FormsRsp>


    //http://snsopafis.karoothitbd.com:8090/afis/api/beneficiary/register
    @POST("/afis/api/beneficiary/register")
    suspend fun submitBeneficiary(@Body body: Beneficiary?): Response<Unit>


}