package com.xplo.code.data.pref

import com.xplo.code.data.Constants

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-10-23
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class PrefHelperImpl : PrefHelper {


    //private var prefUtils: PrefUtils = PrefUtils.getInstance()

    private val userSessionKeys = arrayOf(
        Pk.USER_ID,
        Pk.USER_ACCESS_TOKEN
    )

    override fun resetAll() {
        PrefUtils.clear()
    }

    override fun isPro(): Boolean {
        return PrefUtils.getBoolean(PkSettings.pfIsPro, false)
    }

    override fun isFirstOpen(): Boolean {
        return PrefUtils.getBoolean(Pk.IS_FIRST_OPEN, true)
    }

    override fun setFirstOpenStatus(isFirstOpen: Boolean) {
        PrefUtils.putBoolean(Pk.IS_FIRST_OPEN, isFirstOpen)
    }

    override fun getAccessToken(): String? {
        return PrefUtils.getString(Pk.USER_ACCESS_TOKEN, null)
    }

    override fun setAccessToken(token: String?) {
        PrefUtils.putString(Pk.USER_ACCESS_TOKEN, token)
    }

    override fun isAccessTokenExist(): Boolean {
        val token = getAccessToken()
        if (token.isNullOrEmpty()) return false

        return true
    }

    override fun getUserId(): String? {
        return PrefUtils.getString(Pk.USER_ID, null)
    }

    override fun setUserId(id: String?) {
        PrefUtils.putString(Pk.USER_ID, id)
    }

    override fun isLoggedIn(): Boolean {
        return isAccessTokenExist()
    }

    override fun logout() {
        for (item in userSessionKeys) {
            PrefUtils.remove(item)
        }
    }

    override fun getLocale(): String {
        return PrefUtils.getString(Pk.LOCALE, "en")!!
    }

    override fun setLocale(locale: String) {
        PrefUtils.putString(Pk.LOCALE, locale)
    }

    override fun getSelectedTheme(): String {
        return PrefUtils.getString(PkSettings.pfTheme, Constants.THEME_LIGHT)!!
    }

    override fun setSelectedTheme(theme: String) {
        PrefUtils.putString(PkSettings.pfTheme, theme)
    }

    override fun isTooltipAlive(): Boolean {
        return PrefUtils.getBoolean(Pk.IS_TOOLTIP_ACTIVE, true)
    }

    override fun offTooltip() {
        PrefUtils.putBoolean(Pk.IS_TOOLTIP_ACTIVE, false)
    }

    override fun getLaunchCounter(): Int {
        return PrefUtils.getInt(Pk.LAUNCH_COUNTER, 0)
    }

    override fun incrementLaunchCounter() {
        PrefUtils.putInt(Pk.LAUNCH_COUNTER, getLaunchCounter() + 1)
    }

    override fun getLastNavOption(defaultOption: Int): Int {
        return PrefUtils.getInt(Pk.NAV_OPTION_LAST, defaultOption)
    }

    override fun setLastNavOption(lastOption: Int) {
        PrefUtils.putInt(Pk.NAV_OPTION_LAST, lastOption)
    }

    override fun isLearnedDrawer(): Boolean {
        return PrefUtils.getBoolean(Pk.IS_LEARNED_DRAWER, false)
    }

    override fun setLearnDrawerStatus(status: Boolean) {
        PrefUtils.putBoolean(Pk.IS_LEARNED_DRAWER, status)
    }

}