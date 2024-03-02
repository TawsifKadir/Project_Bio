package com.xplo.code.data_module.core

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-10
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface NRCallback<T> {

    fun onSuccess(data: T?, callInfo: com.xplo.code.data_module.core.CallInfo?)

    fun onFailure(th: Throwable, callInfo: com.xplo.code.data_module.core.CallInfo?)

}