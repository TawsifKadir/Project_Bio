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
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kit.integrationmanager.model.Beneficiary
import com.xplo.code.BuildConfig
import com.xplo.code.data.db.room.database.BeneficiaryDatabase.dbCloseFromDB
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object DbExporter {

    private const val DB_NAME = "benedb.db"

    fun exportWithPermission(context: Context, activity: Activity): Boolean {
        return if (!hasStoragePermission(context)) {
            Log.d("TAG", "exportWithPermission: ")
            askForStoragePermission(activity)
            false
        } else {
            Log.d("TAG", "exportWithPermission:2 ")
            closeAndExportDatabase(context)
            true
        }
    }

    private fun closeAndExportDatabase(context: Context) {
        try {
            val dbFile: File = context.getDatabasePath(DB_NAME)

            if (dbFile.exists()) {
                dbCloseFromDB()
                exportDatabase(context, dbFile)
            } else {
                DialogUtil.showLottieDialogFailMsg(context, "Database file does not exist.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun exportDatabase(context: Context, dbFile: File) {
        try {
            if (dbFile.exists()) {
                val exportDir = File(Environment.getExternalStorageDirectory(), "bio_reg/database")
                if (!exportDir.exists()) exportDir.mkdirs()

                val exportFile =
                    File(exportDir, getCurrentDateTimeInMillis().toString() + "_" + DB_NAME)
                exportFile.createNewFile()

                val src = FileInputStream(dbFile).channel
                val dst = FileOutputStream(exportFile).channel
                dst.transferFrom(src, 0, src.size())

                src.close()
                dst.close()

                DialogUtil.showLottieDialogSuccessMsg(
                    context,
                    "Success",
                    "Database exported successfully. Please check in your folder (bio_reg/database)"
                )
                Log.d("DatabaseExport", "Database exported successfully.")
            } else {
                DialogUtil.showLottieDialogFailMsg(context, "Source database file does not exist.")
                Log.e("DatabaseExport", "Source database file does not exist.")
            }
        } catch (e: Exception) {
            DialogUtil.showLottieDialogFailMsg(context, "Error exporting database: ${e.message}")
            Log.e("DatabaseExport", "Error exporting database: ${e.message}", e)
        }
    }


    private fun askForStoragePermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            activity.startActivity(
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
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

    fun getCurrentDateTimeInMillis(): Long {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        val formattedDateTime = currentDateTime.format(formatter)
        return formattedDateTime.toLong()
    }

    fun saveLoginInfoToCache(
        context: Context,
        beneficiary: Beneficiary?
    ): Boolean {
        val mapper = ObjectMapper()
        if (context == null) return false
        var data: String? = null
        data = try {
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(beneficiary)
        } catch (jsonException: JsonProcessingException) {
            Log.e("TAG", jsonException.message!!)
            jsonException.printStackTrace()
            return false
        }
        if (data == null) {
            Log.e("TAG", "Null data received after JSON parsing")
            return false
        }
        if (beneficiary != null) {
            return writeToCache(context, data, beneficiary.applicationId)
        } else {
            return false
        }
    }

    private fun writeToCache(context: Context, data: String, applicationId: String): Boolean {
        val exportDir = File(Environment.getExternalStorageDirectory(), "bio_reg/beneficiary/")
        val exportFile =
            File(exportDir, applicationId + "_" + getCurrentDateTimeInMillis() + ".json")
        if (!exportFile.exists()) {
            exportDir.mkdirs()
            try {
                exportFile.createNewFile()
            } catch (exc: java.lang.Exception) {
                Log.e("TAG", "Error occured while creating cahce file.")
                exc.printStackTrace()
                return false
            }
        }
        try {
            FileOutputStream(exportFile).use { fos ->
                OutputStreamWriter(fos).use { osw ->
                    osw.write(data)
                    osw.flush()
                }
            }
        } catch (exc: Exception) {
            Log.e("TAG", "Error occured while writing to cahce.")
            exc.printStackTrace()
            return false
        }
        return true
    }

    fun fileWriteWithPermission(
        context: Context, activity: Activity
    ): Boolean {
        return if (!hasStoragePermission(context)) {
            Log.d("TAG", "exportWithPermission: ")
            askForStoragePermission(activity)
            false
        } else {
            true
        }
    }
}
