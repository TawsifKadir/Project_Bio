package com.xplo.code.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.xplo.code.R
import com.xplo.code.core.Contextor
import com.xplo.code.data.pref.PkSettings
import com.xplo.code.data.pref.PrefUtils

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object AppInfo {

    private const val TAG = "AppInfo"

    //private var resources: Resources = Contextor.getInstance().context.resources
    private val APP_PACKAGE = Contextor.getInstance().context.packageName

    val APP_NAME = getString(R.string.app_name)
    val APP_TITLE = getString(R.string.app_title)
    val APP_LINK = "https://play.google.com/store/apps/details?id=$APP_PACKAGE"
    val APP_LINK_PRIVACY_POLICY = getString(R.string.app_link_privacy_policy)
    val PROMO_TEXT = getString(R.string.app_promo_text)
    val CONTACT_US_TEXT = getString(R.string.app_contact_us_txt)


    //developer
    val DEVELOPER_CODE = getString(R.string.app_developer_code)
    val DEVELOPER_NAME = getString(R.string.app_developer_name)
    val DEVELOPER_EMAIL = getString(R.string.app_developer_email)


    val appVersionCode: Int
        get() {
            //val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            // val verCode = PackageInfoCompat.getLongVersionCode(packageInfo).toInt()
            return PackageInfoCompat.getLongVersionCode(packageInfo).toInt()
        }

    val appVersionName: String
        get() {
            return packageInfo.versionName
        }

    @Suppress("DEPRECATION")
    private val packageInfo: PackageInfo
        get() {
            val context = Contextor.getInstance().context
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
        }


    @JvmStatic
    val aboutInfo: String
        get() {

            var txt: String? = null
            var pro = ""

            if (isPro()) {
                pro = " (Pro)"
            }

            txt = (APP_TITLE + pro + "\nVersion : " + appVersionName
                    + "\nCopyright © 2024, " + DEVELOPER_CODE + "\nDeveloper : " + DEVELOPER_CODE
                    + "\nContact : " + DEVELOPER_EMAIL
                    + "")

            return txt
        }

    val textToShareApp: String
        get() = APP_TITLE + "\n" + PROMO_TEXT + "\n" + APP_LINK


    val developerUri: Uri
        get() = Uri.parse("market://search?q=pub:$DEVELOPER_CODE")

    val developerUriWeb: Uri
        get() = Uri.parse("https://play.google.com/store/apps/developer?id=$DEVELOPER_CODE")


    val appUri: Uri
        get() = Uri.parse("market://details?id=$APP_PACKAGE")

    val appUriWeb: Uri
        get() = Uri.parse("https://play.google.com/store/apps/details?id=$APP_PACKAGE")


    /**
     * Method to check is this version is pro
     *
     * @return true if pro else false
     */
    fun isPro(): Boolean {

        //if (BuildConfig.DEBUG) return true
        //from shared preference
        return PrefUtils.getBoolean(PkSettings.pfIsPro, false)


    }


    private fun getString(resId: Int): String {
        return Contextor.getInstance().context.getString(resId)
    }

}
