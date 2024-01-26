package com.xplo.code.base


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xplo.code.BuildConfig
import com.xplo.code.R
import com.xplo.code.core.utils.NetUtils
import com.xplo.code.data.Constants
import com.xplo.code.data.pref.PrefHelper
import com.xplo.code.data.pref.PrefHelperImpl
import com.xplo.code.ui.login.LoginActivity
import com.xplo.code.ui.main_act.MainActivity
import com.xplo.code.ui.settings.SettingsActivity
import com.xplo.code.core.utils.ProgressDialog
import com.xplo.code.ui.dashboard.alternate.AlternateActivity
import com.xplo.code.ui.components.SpItemAdapter
import com.xplo.code.ui.dashboard.household.HouseholdActivity
import com.xplo.code.ui.dashboard.payment.PaymentActivity
import com.xplo.code.ui.dashboard.report.ReportActivity
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
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), BaseContract.View {


    companion object {
        private const val TAG = "BaseActivity"
        //var shouldRestart = false
    }

    private var toolbar: Toolbar? = null
    private var progressDialog: Dialog? = null
    private var prefHelper: PrefHelperImpl? = null

    private lateinit var basePresenter: BaseContract.Presenter<BaseContract.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)
        initBasePresenter()
        setupLoading()
        initValue()
    }

    protected open fun initTheme() {
        val selectedTheme = getPrefHelper().getSelectedTheme()
        Log.d(TAG, "initTheme: selectedTheme: $selectedTheme")

        //apply theme only dark selected
        if (selectedTheme == Constants.THEME_DARK) {
            setTheme(R.style.AppTheme_Dark)
        }

    }

    abstract fun initInitial()

    abstract fun initView()

    abstract fun initObserver()

    private fun initBasePresenter() {
        basePresenter = BasePresenter()
        basePresenter.attach(this)
    }

    private fun initValue() {
        //prefHelper = PrefHelperImpl()
    }

//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "onResume: isLocaleChanged: " + RMemory.isLocaleChanged)
//        if (RMemory.isLocaleChanged) {
//            RMemory.isLocaleChanged = false
//            restartActivity()
//        }
//    }

    override fun attachBaseContext(baseContext: Context) {
        prefHelper = PrefHelperImpl()
        super.attachBaseContext(updateBaseContextLocale(baseContext))
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_common, menu)
//
//        //        MenuItem menuItem = menu.findItem(R.id.action_search);
//        //        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        //
//        //        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        //        SearchView searchView = (SearchView) menu.findItem(R.id.mSearch).getActionView();
//        //        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        //        searchView.setIconifiedByDefault(true);
//
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                //finish()
                onBackPressed()
            }

            R.id.mSettings -> {
                navigateToSettings()
            }

            R.id.mLogin -> {
                navigateToLogin()
            }

            R.id.mLogout -> {
                basePresenter.logout()
                navigateToLogin()
            }


//            R.id.mSearch -> {
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }


    }

    override fun getToolbar(): Toolbar? {
        return toolbar
    }

    override fun showToolbar() {
        supportActionBar?.show()
    }

    override fun hideToolbar() {
        supportActionBar?.hide()
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun showStatusBar() {

    }

    override fun hideStatusBar() {

        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gradEnd));
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)


        window.decorView.systemUiVisibility = flags
    }

    override fun showFullScreen() {
        hideStatusBar()
    }

    override fun isDarkTheme(): Boolean {
        return getPrefHelper().getSelectedTheme() == Constants.THEME_DARK
    }

    override fun setupLoading() {
        progressDialog = ProgressDialog.createProgressDialog(this, isCancelable = true)
        //progressDialog?.setMessage(getString(R.string.loading))
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun hideLoading() {
        progressDialog?.hide()
    }

    override fun bindSpinnerData(spinner: Spinner, items: Array<String>) {
        val adapter = SpItemAdapter(this, items.toList())
        spinner.adapter = adapter
    }

    override fun showMessage(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok), null)
            .create()
            .show()
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage(msg: String?) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show()
        }

    }

    override fun isNetworkConnected(): Boolean {
        return NetUtils.isNetworkConnected(this)
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        Log.d(TAG, "onNetworkStatusChanged() called with: isConnected = $isConnected")
        showToast("Network Status: isConnected: $isConnected")
    }

    override fun onNetworkError(errorCode: Int, msg: String) {
        Log.d(TAG, "onNetworkError() called with: errorCode = $errorCode, msg = $msg")
        showToast("onNetworkError: msg: $msg")
    }

    override fun onError(msg: String?) {
        showErrorMessage(msg)
    }

    override fun getPrefHelper(): PrefHelper {
        if (prefHelper == null) {
            prefHelper = PrefHelperImpl()
        }
        return prefHelper!!
    }

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun updateBaseContextLocale(context: Context): Context {
        val language = getPrefHelper().getLocale()
        //val language = "en"

        Log.d(TAG, "updateBaseContextLocale: language: $language")
        val locale = Locale(language)
        Locale.setDefault(locale)

        return updateLocale(context, locale)
    }

    override fun updateLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    override fun getFragmentManagerX(): FragmentManager? {
        return supportFragmentManager
    }

    override fun loadFragment(fragment: Fragment, bundle: Bundle?) {
        supportFragmentManager
            .beginTransaction()
//            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.frame, fragment)
            .commit()
    }

    override fun openActivity(mClass: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, mClass)
        if (bundle != null)
            intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onRestartActivity() {
        Log.d(TAG, "onRestartActivity() called")
        restartActivity()
    }

    override fun restartActivity() {
        Log.d(TAG, "restartActivity() called")
        this.recreate()
    }

    override fun onRestartApp() {
        Log.d(TAG, "onRestartApp() called")
        restartApp()
    }

    override fun restartApp() {
        Log.d(TAG, "restartApp() called")
        //to do
    }

    override fun isLoggedIn(): Boolean {
        return getPrefHelper().isLoggedIn()
    }

    override fun onLogout() {
        Log.d(TAG, "onLogout() called")
        getPrefHelper().logout()
        showToast("Logout successfully")
        //finish()

    }

    override fun onRefreshLoginLogoutView() {

    }

    override fun onForceUpdate() {
        Log.d(TAG, "onForceUpdate: ")
    }

    override fun navigateToHome() {
        //openActivity(MainActivity::class.java, null)
        MainActivity.open(this, null)
        //finish()
    }

    override fun navigateToSettings() {
        //openActivity(SettingsActivity::class.java, null)
        SettingsActivity.open(this, null)
        //finish()
    }

    override fun navigateToLogin() {
        //openActivity(LoginActivity::class.java, null)
        LoginActivity.open(this, null)
    }

    override fun navigateToMyAccount() {

    }

    override fun navigateToFavorite() {

    }

    override fun navigateToHistory() {

    }

    override fun navigateToHousehold() {
        HouseholdActivity.open(this, null)
    }

    override fun navigateToAlternate() {
        AlternateActivity.open(this, null)
    }

    override fun navigateToPayment() {
        PaymentActivity.open(this, null)
    }

    override fun navigateToReport() {
        ReportActivity.open(this, null)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d(TAG, "onConfigurationChanged: ")
        showToast("onConfigurationChanged: ")

        //restartActivity(this)

        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        basePresenter.detach()
    }


}
