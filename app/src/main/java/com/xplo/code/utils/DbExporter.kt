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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xplo.code.BuildConfig
import com.xplo.code.data.db.DbController
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


object DbExporter {

    private const val DB_NAME = "db.db"

    fun exportWithPermission(context: Context, activity: Activity) {
        if (!DbExporter.hasStoragePermission(context)) {
            // ask permission
            DbExporter.askForPermission(activity)
            return
        }
        DbController.close()
        DbExporter.exportToSQLite(context)
    }

    fun exportToSQLite(context: Context) {
        try {
            val dbFile: File = context.getDatabasePath(DB_NAME)
            if (dbFile.exists()) {
                val exportDir = File(Environment.getExternalStorageDirectory(), "bio_reg/database")
                if (!exportDir.exists()) exportDir.mkdirs()
                val exportFile = File(exportDir, DB_NAME)
                exportFile.createNewFile()
                val src = FileInputStream(dbFile).channel
                val dst = FileOutputStream(exportFile).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun askForPermission(activity: Activity) {

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

    fun hasStoragePermission(context: Context): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    }
}