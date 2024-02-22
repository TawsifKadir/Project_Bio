package com.xplo.code.core

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-10
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface RspCallback<T> {

    fun onSuccess(data: T?)

    fun onFailure(th: Throwable)

}