package com.xplo.code.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.Lottie
import com.xplo.code.BuildConfig
import com.xplo.code.data.db.DbController
import com.xplo.code.data.db.room.database.BeneficiaryDatabase.dbCloseFromDB
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


object PermissionHelper {

    private const val TAG = "PermissionHelper"

    fun checkAndCallPermissionForCapturePhoto(activity: Activity, context: Context) {

    }


    fun askForPermissionStorage(activity: Activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            activity.startActivity(
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            )
            return
        }

        // Permission is not granted, request it
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100
        )
    }

    fun askForPermissionCamera(activity: Activity) {

        // Permission is not granted, request it
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            100
        )
    }

    fun hasStoragePermission(context: Context): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCameraAndStoragePermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return hasCameraPermission(context)
        }else{
            return hasCameraPermission(context) && hasStoragePermission(context)
        }
    }
}