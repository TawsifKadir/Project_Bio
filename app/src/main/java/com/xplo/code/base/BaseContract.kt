package com.xplo.code.base

import android.content.Context
import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xplo.code.data.pref.PrefHelper
import com.xplo.code.ui.dashboard.model.PayrollEntry
import java.util.Locale

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface BaseContract {

    interface View {

        /**
         * toolbar
         */
        fun setupToolbar()
        fun getToolbar(): Toolbar?
        fun showToolbar()
        fun hideToolbar()
        fun setToolbarTitle(title: String)

        /**
         * status bar
         */
        fun showStatusBar()
        fun hideStatusBar()
        fun showFullScreen()

        /**
         * theme
         */
        fun isDarkTheme(): Boolean

        /**
         * progress bar
         */
        fun setupLoading()
        fun showLoading()
        fun hideLoading()

        fun bindSpinnerData(spinner: Spinner, items: Array<String>)

        /**
         * Error, Msg, Network
         */
        fun showMessage(msg: String)
        fun showToast(msg: String)
        fun showErrorMessage(msg: String?)
        fun showAlerter(title: String?, msg: String?)
        fun isNetworkConnected(): Boolean
        fun onNetworkStatusChanged(isConnected: Boolean)
        fun onNetworkError(errorCode: Int, msg: String)
        fun onError(msg: String?)

        /**
         * Preference
         */
        fun getPrefHelper(): PrefHelper
        fun isDebugBuild(): Boolean

        /**
         * locale
         */
        fun updateBaseContextLocale(context: Context): Context
        fun updateLocale(context: Context, locale: Locale): Context

        /**
         * fragment
         */
        fun getFragmentManagerX(): FragmentManager?
        fun loadFragment(fragment: Fragment, bundle: Bundle?)

        /**
         * navigation
         */
        fun openActivity(mClass: Class<*>, bundle: Bundle?)
        fun onRestartActivity()
        fun restartActivity()
        fun onRestartApp()
        fun restartApp()

        fun navigateToHome()
        fun navigateToSettings()
        fun navigateToLogin()
        fun navigateToMyAccount()
        fun navigateToFavorite()
        fun navigateToHistory()

        fun navigateToHousehold()
        fun navigateToAlternate(id: String?, name: String?)
//        fun navigateToAlternateNew(id: String?, hhName: String, type: String)
        fun navigateToPayment()
        fun navigateToReport()
        fun navigateToPayroll()
        fun navigateToPayrollDetails(result: PayrollEntry.Result)

        /**
         * Extra method
         */
        fun isLoggedIn(): Boolean
        fun onLogout()
        fun onRefreshLoginLogoutView()
        //fun onForceUpdate()


    }

    interface Presenter<V : View> {

        /**
         * Check view Attached or not
         */
        val isAttached: Boolean

        /**
         * Attach view
         */
        fun attach(view: V)

        /**
         * Detach view
         */
        fun detach()

        fun logout()

    }


}