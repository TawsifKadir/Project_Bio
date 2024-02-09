package com.xplo.code.utils

import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.data.BuildConfig


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/20/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object FormAppUtils {

    private const val TAG = "FormAppUtils"

    fun canNomineeAdd(form: HouseholdForm?): Boolean {
        if (form == null) return false

        if (BuildConfig.DEBUG) return true

        if (form.form1?.countryName.equals("JUBA", ignoreCase = true)) return true
        return false
    }

}