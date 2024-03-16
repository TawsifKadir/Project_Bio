package com.xplo.code.ui.settings

import com.xplo.code.base.BaseContract

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/17/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface SettingsContract {

    interface View : BaseContract.View, AppUpdate {

    }

    interface Presenter : BaseContract.Presenter<View> {

    }

    interface AppUpdate {
        fun onCheckAppUpdate()
        fun onNormalUpdate()
        fun onForceUpdate()
    }
}