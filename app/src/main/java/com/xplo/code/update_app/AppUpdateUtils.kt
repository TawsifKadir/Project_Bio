package com.xplo.code.update_app

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.pouyaheydari.appupdater.AppUpdaterDialog
import com.pouyaheydari.appupdater.core.pojo.Store
import com.pouyaheydari.appupdater.core.pojo.StoreListItem
import com.pouyaheydari.appupdater.pojo.UpdaterDialogData

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object AppUpdateUtils {

    private const val TAG = "AppUpdateUtils"

    //https://cafebazaar.ir/download/bazaar.apk
    //https://snsopapk.southsudansafetynet.info/download/test_nasif/test50.apk
    private const val APK_URL_BASE = "https://snsopapk.southsudansafetynet.info/download/test_nasif/"
    private const val APK_NAME_LATEST = "test50.apk"

    const val APK_PACKAGE_NAME = "com.tencent.mm"

    const val UPDATE_TITLE = "Update available"
    const val UPDATE_MSG_NORMAL = "There is a update available. Please update."
    const val UPDATE_MSG_FORCE =
        "There is a force update available. You have to update to continue. Please update."

    fun getApkUrl(): String {
        return APK_URL_BASE + APK_NAME_LATEST
    }


    val storeItem = StoreListItem(
        store = Store.DIRECT_URL,
        title = "Download",
        url = getApkUrl(),
        packageName = APK_PACKAGE_NAME,
    )

    fun showNormalDialog(context: Context, fm: FragmentManager) {
        AppUpdaterDialog.getInstance(
            UpdaterDialogData(
                title = UPDATE_TITLE,
                description = UPDATE_MSG_NORMAL,
                storeList = listOf(storeItem),
                isForceUpdate = false
            ),
        ).show(fm, TAG)
    }

    fun showForceDialog(context: Context, fm: FragmentManager) {
        AppUpdaterDialog.getInstance(
            UpdaterDialogData(
                title = UPDATE_TITLE,
                description = UPDATE_MSG_FORCE,
                storeList = listOf(storeItem),
                isForceUpdate = true
            ),
        ).show(fm, TAG)
    }


}