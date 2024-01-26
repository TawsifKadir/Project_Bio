package com.xplo.code.base

import android.util.Log

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
open class BasePresenter<V : BaseContract.View> : BaseContract.Presenter<V> {

    companion object {
        private const val TAG = "BasePresenter"
    }

    /**
     * Attached view
     */
    var view: V? = null

    override val isAttached = (view != null)

    override fun attach(view: V) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }

    override fun logout() {
        Log.d(TAG, "logout: ")
        view?.onLogout()
    }


}