package com.xplo.code.base

import android.content.Context
import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceFragmentCompat
import com.xplo.code.core.utils.NetUtils
import com.xplo.code.BuildConfig
import com.xplo.code.data.pref.PrefHelper
import com.xplo.code.data.pref.PrefHelperImpl
import java.util.*

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
open class BaseSettingsFragment : PreferenceFragmentCompat(), BaseContract.View {


    override fun setupToolbar() {
        getBaseActivity()?.setupToolbar()
    }

    override fun getToolbar(): Toolbar? {
        return getBaseActivity()?.getToolbar()
    }

    override fun showToolbar() {
        getBaseActivity()?.showToolbar()
    }

    override fun hideToolbar() {
        getBaseActivity()?.hideToolbar()
    }

    override fun setToolbarTitle(title: String) {
        getBaseActivity()?.setToolbarTitle(title)
    }

    override fun showStatusBar() {
        getBaseActivity()?.showStatusBar()
    }

    override fun hideStatusBar() {
        getBaseActivity()?.hideStatusBar()
    }

    override fun showFullScreen() {
        getBaseActivity()?.showFullScreen()
    }

    override fun isDarkTheme(): Boolean {
        if (getBaseActivity() != null) {
            return getBaseActivity()!!.isDarkTheme()
        }
        return false
    }

    override fun setupLoading() {
        getBaseActivity()?.setupLoading()
    }

    override fun showLoading() {
        getBaseActivity()?.showLoading()
    }

    override fun hideLoading() {
        getBaseActivity()?.hideLoading()
    }

    override fun bindSpinnerData(spinner: Spinner, items: Array<String>) {

    }

    override fun showMessage(msg: String) {
        getBaseActivity()?.showMessage(msg)
    }

    override fun showToast(msg: String) {
        getBaseActivity()?.showToast(msg)
    }

    override fun showErrorMessage(msg: String?) {
        getBaseActivity()?.showErrorMessage(msg)
    }

    override fun showAlerter(title: String?, msg: String?) {
        getBaseActivity()?.showAlerter(title, msg)
    }

    override fun isNetworkConnected(): Boolean {
        if (getBaseActivity() != null) {
            return getBaseActivity()!!.isNetworkConnected()
        }
        return NetUtils.isNetworkConnected(requireContext())
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        getBaseActivity()?.onNetworkStatusChanged(isConnected)
    }

    override fun onNetworkError(errorCode: Int, msg: String) {
        getBaseActivity()?.onNetworkError(errorCode, msg)
    }

    override fun onError(msg: String?) {
        getBaseActivity()?.onError(msg)
    }

    override fun getPrefHelper(): PrefHelper {
        if (getBaseActivity() != null) {
            return getBaseActivity()!!.getPrefHelper()
        }
        return PrefHelperImpl()

    }

    override fun isDebugBuild(): Boolean {
        if (getBaseActivity() != null) {
            return getBaseActivity()!!.isDebugBuild()
        }
        return BuildConfig.DEBUG

    }

    override fun updateBaseContextLocale(context: Context): Context {
        return getBaseActivity()?.updateBaseContextLocale(context)!!
    }

    override fun updateLocale(context: Context, locale: Locale): Context {
        return getBaseActivity()?.updateLocale(context, locale)!!
    }

    override fun getFragmentManagerX(): FragmentManager? {
        return getBaseActivity()?.getFragmentManagerX()
    }

    override fun loadFragment(fragment: Fragment, bundle: Bundle?) {
        getBaseActivity()?.loadFragment(fragment, bundle)
    }

    override fun openActivity(mClass: Class<*>, bundle: Bundle?) {
        getBaseActivity()?.openActivity(mClass, bundle)
    }

    override fun onRestartActivity() {
        getBaseActivity()?.onRestartActivity()
    }

    override fun restartActivity() {
        getBaseActivity()?.restartActivity()
    }

    override fun onRestartApp() {
        getBaseActivity()?.onRestartApp()
    }

    override fun restartApp() {
        getBaseActivity()?.restartApp()
    }

    override fun isLoggedIn(): Boolean {
        return getBaseActivity()?.isLoggedIn()!!
    }

    override fun onLogout() {
        getBaseActivity()?.onLogout()
    }

    override fun onRefreshLoginLogoutView() {
        getBaseActivity()?.onRefreshLoginLogoutView()
    }

    override fun onForceUpdate() {
        getBaseActivity()?.onForceUpdate()
    }

    override fun navigateToHome() {
        getBaseActivity()?.navigateToHome()
    }

    override fun navigateToSettings() {
        getBaseActivity()?.navigateToSettings()
    }

    override fun navigateToLogin() {
        getBaseActivity()?.navigateToLogin()
    }

    override fun navigateToMyAccount() {
        getBaseActivity()?.navigateToMyAccount()
    }

    override fun navigateToFavorite() {
        getBaseActivity()?.navigateToFavorite()
    }

    override fun navigateToHistory() {
        getBaseActivity()?.navigateToHistory()
    }

    override fun navigateToHousehold() {
        getBaseActivity()?.navigateToHousehold()
    }

    override fun navigateToAlternate(id: String?) {
        getBaseActivity()?.navigateToAlternate(id)
    }

    override fun navigateToAlternateNew(id: String?, hhName: String, type: String) {
        getBaseActivity()?.navigateToAlternateNew(id, hhName, type)
    }

    override fun navigateToPayment() {
        getBaseActivity()?.navigateToPayment()
    }

    override fun navigateToReport() {
        getBaseActivity()?.navigateToReport()
    }

    private fun getBaseActivity(): BaseActivity? {

        if (activity != null) {
            return activity as BaseActivity
        }
        return null

    }

    /**
     * Called during [.onCreate] to supply the preferences for this fragment.
     * Subclasses are expected to call [.setPreferenceScreen] either
     * directly or via helper methods such as [.addPreferencesFromResource].
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     * this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     * [PreferenceScreen] with this key.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    }

}
