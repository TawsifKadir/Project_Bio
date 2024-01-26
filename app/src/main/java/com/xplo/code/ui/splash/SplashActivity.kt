package com.xplo.code.ui.splash

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.data.db.DbController
import com.xplo.code.core.utils.DbHelper
import com.xplo.code.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SplashActivity : BaseActivity(), SplashContract.View, CoroutineScope {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var presenter: SplashPresenter

    private lateinit var job: Job
    private var mediaPlayer: MediaPlayer? = null


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private val splashTimeout: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initInitial()
        initView()
        initObserver()

        job = Job()

        GlobalScope.launch(Dispatchers.Main) {
            copyDb()
        }

    }

    override fun initTheme() {
        Log.d(TAG, "initTheme: ")

        if (isDarkTheme()) {
            setTheme(R.style.SplashActivityTheme_Dark)
        }
    }

    override fun initInitial() {
        presenter = SplashPresenter()
        presenter.attach(this)
    }

    override fun initView() {

        if (getPrefHelper().isFirstOpen()) {
            getPrefHelper().setLocale("en")
            getPrefHelper().setFirstOpenStatus(false)
        }

        if (isDebugBuild()) {
            playSound()
            Log.d(TAG, "initView: getDeviceDensityName: " + Utils.getDeviceDensityName(this))
        }

    }

    override fun initObserver() {

    }

    suspend fun copyDb() {
        val dbHelper = DbHelper(this, DbController.dbName)
        withContext(Dispatchers.IO) {

            if (!dbHelper.isDbExist) {
                dbHelper.createDataBase()
            }

            dbHelper.close()
            presenter.splashDelay(splashTimeout)
        }
    }


    override fun playSound() {
        try {
            mediaPlayer = MediaPlayer.create(baseContext, R.raw.startup) // must be mp3
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "playStartupSound: ${e.message}", e)
        }

    }

    override fun onAuthCompleted(token: String) {
        Log.d(TAG, "onAuthCompleted() called with: token = $token")
    }

    override fun navigateToNext() {
        Log.d(TAG, "navigateToNext() called")

//        navigateToHome()
//        finish()

        if (getPrefHelper().isLoggedIn()) {
            navigateToHome()
            //navigateToLogin()
            finish()
        } else {
            //navigateToHome()
            navigateToLogin()
            finish()
        }

    }

    override fun onPause() {
        super.onPause()

        mediaPlayer?.release()

    }

    override fun onDestroy() {
        job.cancel()
        presenter.detach()
        super.onDestroy()
    }

}
