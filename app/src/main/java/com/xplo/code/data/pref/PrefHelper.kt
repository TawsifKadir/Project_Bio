package com.xplo.code.data.pref

/**
 * Copyright 2019 (C) xplo
 *
 * Created  : 2019-10-23
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface PrefHelper {

    fun resetAll()
    fun isPro(): Boolean

    fun isFirstOpen(): Boolean
    fun setFirstOpenStatus(isFirstOpen: Boolean)


    fun getAccessToken(): String?
    fun setAccessToken(token: String?)
    fun isAccessTokenExist(): Boolean

    fun isLoggedIn(): Boolean
    fun logout()
    fun getUserId(): String?
    fun setUserId(id: String?)


    fun getLaunchCounter(): Int
    fun incrementLaunchCounter()

    fun getLastNavOption(defaultOption: Int): Int
    fun setLastNavOption(lastOption: Int)

    fun isLearnedDrawer(): Boolean
    fun setLearnDrawerStatus(status: Boolean)

    fun getLocale(): String
    fun setLocale(locale: String)

    fun getSelectedTheme(): String
    fun setSelectedTheme(theme: String)

    fun isTooltipAlive(): Boolean
    fun offTooltip()

    fun isHouseholdConsentAccept(): Boolean
    fun setHouseholdConsentAcceptStatus(isAccept: Boolean)

    fun isNomineeConsentAccept(): Boolean
    fun setNomineeConsentAcceptStatus(isAccept: Boolean)


}