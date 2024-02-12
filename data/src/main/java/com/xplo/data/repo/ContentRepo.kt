package com.xplo.data.repo

import com.xplo.data.model.content.FormRqb
import com.xplo.data.model.content.FormRsp
import com.xplo.data.model.content.FormsRqb
import com.xplo.data.model.content.FormsRsp
import com.xplo.data.core.Resource

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

    suspend fun saveForm(body: FormRqb): Resource<FormRsp>
    suspend fun saveForms(body: FormsRqb): Resource<FormsRsp>


}