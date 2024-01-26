package com.xplo.code.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object EasyMenu {

    @JvmStatic
    fun showAboutText(context: Context) {
        val body = getAboutBody()
        AlertDialog.Builder(context)
            .setTitle("About")
            .setMessage(body)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .show()
    }

    @JvmStatic
    fun showAboutPopupWithMore(context: Context) {
        val body = getAboutBody()
        AlertDialog.Builder(context)
            .setTitle("About")
            .setMessage(body)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .setNeutralButton("More Info") { _: DialogInterface?, _: Int ->
                openPrivacyPolicy(context)
            }
            .show()
    }

    @JvmStatic
    fun showContactUsPopup(context: Context) {
        val body = getContactUsBody()
        AlertDialog.Builder(context)
            .setTitle("Contact Us")
            .setMessage(body)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .show()
    }

    @JvmStatic
    fun shareApp(context: Context) {

        val appTitle = getAppTitle()
        val textToShareAppPromote = getTextToShareApp()
        val clipData = ClipData.newPlainText("text", textToShareAppPromote)
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(clipData)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, textToShareAppPromote)
        context.startActivity(
            Intent.createChooser(intent, "Share $appTitle")
        )
    }

    @JvmStatic
    fun shareText(context: Context, text: String?) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(Intent.createChooser(intent, "Share By"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun addToClip(context: Context, text: String?) {
        val clipData = ClipData.newPlainText("text", text)
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(clipData)
    }

    @JvmStatic
    fun shareTextWithClip(context: Context, text: String?) {
        addToClip(context, text)
        shareText(context, text)
    }

    @JvmStatic
    fun moreApps(context: Context) {
        try {
            val moreApps = Intent(Intent.ACTION_VIEW, getDeveloperUri())
            // intent.setAction(Intent.ACTION_VIEW);
            context.startActivity(moreApps)
        } catch (e: ActivityNotFoundException) {
            val moreApps = Intent(Intent.ACTION_VIEW, getDeveloperUriWeb())
            // intent.setAction(Intent.ACTION_VIEW);
            context.startActivity(moreApps)
        }
    }

    @JvmStatic
    fun openPrivacyPolicy(context: Context) {
        openAWebsite(context, getPrivacyPolicyUrl())
    }

    @JvmStatic
    fun openAWebsite(context: Context, url: String?) {
        try {
            val uri = Uri.parse(url)
            val moreApps = Intent(Intent.ACTION_VIEW, uri)
            // intent.setAction(Intent.ACTION_VIEW);
            context.startActivity(moreApps)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun feedback(context: Context) {
        val appTitle = getAppTitle()
        val devEmail = getDeveloperEmail()
        try {
            val email = Intent(Intent.ACTION_SEND)
            // Email.setType("text/email");
            email.type = "plain/text"
            // Email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(devEmail))
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback $appTitle")
            email.putExtra(Intent.EXTRA_TEXT, "Dear Developer")
            context.startActivity(Intent.createChooser(email, "Send Feedback"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun rate(context: Context) {
        try {
            AppRater.Builder()
                .build()
                .showRateDialog(context)
            return
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun goToPlayStore(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, getAppUri())
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, getAppUriWeb())
            context.startActivity(intent)
        }
    }

    private fun getAppTitle(): String {
        return AppInfo.APP_TITLE
    }

    private fun getPrivacyPolicyUrl(): String {
        return AppInfo.APP_LINK_PRIVACY_POLICY
    }

    private fun getAppUri(): Uri {
        return AppInfo.appUri
    }

    private fun getAppUriWeb(): Uri {
        return AppInfo.appUriWeb
    }

    private fun getDeveloperEmail(): String {
        return AppInfo.DEVELOPER_EMAIL
    }

    private fun getDeveloperUri(): Uri {
        return AppInfo.developerUri
    }

    private fun getDeveloperUriWeb(): Uri {
        return AppInfo.developerUriWeb
    }

    private fun getAboutBody(): String {
        return AppInfo.aboutInfo
    }

    private fun getContactUsBody(): String {
        return AppInfo.aboutInfo
    }

    private fun getTextToShareApp(): String {
        return AppInfo.textToShareApp
    }


}