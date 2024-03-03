package com.xplo.code.core

import android.annotation.SuppressLint
import android.content.Context

/**
 * Copyright 2018 (C) Xplo
 *
 *
 * Created  : 10/23/2018
 * Author   : xplo
 * Version  : 1.0
 * Desc     :
 * Comment  : Contextor.getInstance().init(appContext);
 */
class Contextor {

    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var sInstance: Contextor? = null

        @JvmStatic
        fun getInstance(): Contextor {
            if (sInstance == null) {
                sInstance = Contextor()
            }
            return sInstance as Contextor
        }

    }
}