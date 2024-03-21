package com.xplo.code.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kit.integrationmanager.model.Beneficiary
import com.xplo.code.data.db.room.database.BeneficiaryDatabase.dbCloseFromDB
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DbExporter {

    private const val DB_NAME = "benedb.db"
    private const val PERMISSION_REQUEST_CODE = 100
    private const val BENEFICIARY_CACHE_DIR = "bio_reg/beneficiary/"
    private const val DATABASE_EXPORT_DIR = "bio_reg/database/"

    private val TAG = DbExporter::class.java.simpleName

    fun exportWithPermission(context: Context, activity: Activity): Boolean {
        return if (!hasStoragePermission(context)) {
            askForStoragePermission(activity)
            false
        } else {
            closeAndExportDatabase(context)
            true
        }
    }

    private fun closeAndExportDatabase(context: Context) {
        val dbFile: File = context.getDatabasePath(DB_NAME)
        if (dbFile.exists()) {
            dbCloseFromDB()
            exportDatabase(context, dbFile)
        } else {
            showErrorMessage(context, "Database file does not exist.")
        }
    }

    private fun exportDatabase(context: Context, dbFile: File) {
        try {
            val exportDir = File(Environment.getExternalStorageDirectory(), DATABASE_EXPORT_DIR)
            exportDir.mkdirs()
            val exportFile = File(exportDir, "${getCurrentDateTimeInMillis()}_$DB_NAME")
            exportFile.createNewFile()

            FileOutputStream(exportFile).use { output ->
                FileInputStream(dbFile).use { input ->
                    input.copyTo(output)
                }
            }
            showSuccessMessage(context, "Database exported successfully.")
        } catch (e: Exception) {
            showErrorMessage(context, "Error exporting database: ${e.message}")
            Log.e(TAG, "Error exporting database")
        }
    }

    private fun askForStoragePermission(activity: Activity) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
        ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    private fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getCurrentDateTimeInMillis(): Long {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).toLong()
    }

    fun saveLoginInfoToCache(context: Context, beneficiary: Beneficiary?): Boolean {
        val mapper = ObjectMapper()
        val data = try {
            mapper.writeValueAsString(beneficiary)
        } catch (e: JsonProcessingException) {
            Log.e(TAG, "Error converting Beneficiary to JSON")
            return false
        }
        return writeToCache(context, data, beneficiary?.applicationId ?: "")
    }

    private fun writeToCache(context: Context, data: String, applicationId: String): Boolean {
        val exportDir = File(Environment.getExternalStorageDirectory(), BENEFICIARY_CACHE_DIR)
        val exportFile = File(exportDir, "$applicationId${"_" + getCurrentDateTimeInMillis()}.json")

        return try {
            exportDir.mkdirs()
            exportFile.createNewFile()
            exportFile.writeText(data)
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error writing to cache")
            false
        }
    }

    fun fileWriteWithPermission(context: Context, activity: Activity): Boolean {
        return if (!hasStoragePermission(context)) {
            askForStoragePermission(activity)
            false
        } else {
            true
        }
    }

    private fun showErrorMessage(context: Context, message: String) {
        DialogUtil.showLottieDialogFailMsg(context, message)
        Log.e(TAG, message)
    }

    private fun showSuccessMessage(context: Context, message: String) {
        DialogUtil.showLottieDialogSuccessMsg(context, "Success", message)
        Log.d(TAG, message)
    }
}
