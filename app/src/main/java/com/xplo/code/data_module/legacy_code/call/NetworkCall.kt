package com.xplo.code.data_module.legacy_code.call

import com.xplo.code.data_module.core.NRCallback
import com.xplo.code.data_module.model.user.LoginRqb
import com.xplo.code.data_module.model.user.TokenRsp

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-10
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface NetworkCall {

    fun generateToken(body: LoginRqb, callback: com.xplo.code.data_module.core.NRCallback<TokenRsp>?)

}
