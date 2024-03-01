package com.xplo.code.data_module.repo
import com.kit.integrationmanager.model.Beneficiary
import com.xplo.code.data_module.core.Resource

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/23/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface ContentRepo {

//    suspend fun submitForm(body: FormRqb?): Resource<FormRsp>
//    suspend fun submitForms(body: FormsRqb?): Resource<FormsRsp>
    suspend fun sendBeneficiary(body: Beneficiary?): com.xplo.code.data_module.core.Resource<Unit>


}