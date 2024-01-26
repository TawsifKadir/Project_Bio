package com.xplo.data.legacy_code.call

import com.xplo.data.core.NRCallback
import com.xplo.data.model.user.LoginRqb
import com.xplo.data.model.user.TokenRsp

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

    fun generateToken(body: LoginRqb, callback: NRCallback<TokenRsp>?)

}
