package com.xplo.code.data.pref

import com.xplo.code.R
import com.xplo.code.core.Contextor

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-11-16
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

open class PkSettings {

    companion object {

        val resources = Contextor.getInstance().context.resources

        //settings
        val pfLanguage = resources.getString(R.string.pfLanguage)

        val pfTheme = resources.getString(R.string.pfTheme)
        val pfAbout = resources.getString(R.string.pfAbout)
        val pfMoreApps = resources.getString(R.string.pfMoreApps)
        val pfResetAll = resources.getString(R.string.pfResetAll)
        val pfDeveloper = resources.getString(R.string.pfDeveloper)
        val pfRate = resources.getString(R.string.pfRate)
        val pfShare = resources.getString(R.string.pfShare)
        val pfFeedback = resources.getString(R.string.pfFeedback)
        val pfDevOption = resources.getString(R.string.pfDevOption)
        val pfCatAdvanceSettings = resources.getString(R.string.pfCatAdvanceSettings)
        val pfExportDb = resources.getString(R.string.pfExportDb)


        //settings developer
        val pfIsPro = resources.getString(R.string.pfIsPro)


    }

}