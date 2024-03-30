package com.xplo.code.base


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tapadoo.alerter.Alerter
import com.xplo.code.BuildConfig
import com.xplo.code.R
import com.xplo.code.core.utils.AttrUtils
import com.xplo.code.core.utils.NetUtils
import com.xplo.code.core.utils.ProgressDialog
import com.xplo.code.data.Constants
import com.xplo.code.data.pref.PrefHelper
import com.xplo.code.data.pref.PrefHelperImpl
import com.xplo.code.ui.components.SpItemAdapter
import com.xplo.code.ui.dashboard.alternate.AlternateActivity
import com.xplo.code.ui.dashboard.household.HouseholdActivity
import com.xplo.code.ui.dashboard.payment.PaymentActivity
import com.xplo.code.ui.dashboard.report.ReportActivity
import com.xplo.code.ui.login.LoginActivity
import com.xplo.code.ui.main_act.MainActivity
import com.xplo.code.ui.settings.SettingsActivity
import java.util.*
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.xplo.code.ui.dashboard.model.PayrollEntry
import com.xplo.code.ui.dashboard.payroll.PayrollActivity
import com.xplo.code.ui.dashboard.payroll.PayrollDetailsActivity


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
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val selectedTheme = getPrefHelper().getSelectedTheme()
        Log.d(TAG, "initTheme: selectedTheme: $selectedTheme")

//        //apply theme only dark selected
//        if (selectedTheme == Constants.THEME_DARK) {
//            setTheme(R.style.AppTheme_Dark)
//        }

        //apply theme only custom theme selected
        when (selectedTheme) {
            //Constants.THEME_LIGHT -> setTheme(R.style.AppTheme_Light)
            Constants.THEME_DARK -> setTheme(R.style.AppTheme_Dark)
            Constants.THEME_PINK -> setTheme(R.style.AppTheme_Pink)
            Constants.THEME_GREEN -> setTheme(R.style.AppTheme_Green)
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
        Log.d(TAG, "onOptionsItemSelected() called with: item = $item")

        when (item.itemId) {
            android.R.id.home -> {
                //finish()
                onBackPressed()
                // Create a callback and override onBackPressed
//                val callback = object : OnBackPressedCallback(true /* enabled by default */) {
//                    override fun handleOnBackPressed() {
//                        // Handle the back button press event here
//                        // For example, you can navigate back or perform other actions
//                        // Call isEnabled() to check if the callback is enabled
//                        // Call remove() to remove the callback if it's no longer needed
//                    }
//                }
//
//                // Add the callback to the back press dispatcher
//                onBackPressedDispatcher.addCallback(this, callback)
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
//        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//
//
//        window.decorView.systemUiVisibility = flags

        // Ensure that you are targeting at least Android 11 (API level 30)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.let {
                // Hide both the status bar and the navigation bar
                it.hide(WindowInsets.Type.systemBars())
                // Set the behavior to immersive with sticky bars
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For devices running older versions of Android
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

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

    override fun showAlerter(title: String?, msg: String?) {
        Alerter.create(this)
            .setTitle(title ?: "")
            .setText(msg ?: "")
            .enableSwipeToDismiss()
            //.setDuration(3000)      // 3s default
            .setBackgroundColorInt(
                AttrUtils.getAttrColor(
                    this,
                    R.attr.colorWarning
                )
            ) // or setBackgroundColorInt(Color.CYAN)
            .setTextAppearance(R.style.AlertTextAppr_Text)
            .setTitleAppearance(R.style.AlertTextAppr_Title)
            .show()
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

//    override fun onForceUpdate() {
//        Log.d(TAG, "onForceUpdate: ")
//    }

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

    override fun navigateToAlternate(id: String?, name: String?) {
        AlternateActivity.open(this, null, id, name)
    }

//    override fun navigateToAlternateNew(id: String?, hhName: String, type: String) {
//        AlternateActivity.openNew(this, null, id, hhName, type)
//    }

    override fun navigateToPayment() {
        PaymentActivity.open(this, null)
    }

    override fun navigateToReport() {
        ReportActivity.open(this, null)
    }
    override fun navigateToPayroll() {
        PayrollActivity.open(this, null)
    }
    override fun navigateToPayrollDetails(result: PayrollEntry.Result) {
        PayrollDetailsActivity.open(this, result)
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

    //begin keyboard hide
    @SuppressLint("ClickableViewAccessibility")
    fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        if (v != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE)
            && v is EditText
            && !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.getLeft() - scrcoords[0]
            val y = ev.rawY + v.getTop() - scrcoords[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()
            ) hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null
        ) {
            val imm = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                activity.window.decorView
                    .windowToken, 0
            )
        }
    }
    //end keyboard hide


}
