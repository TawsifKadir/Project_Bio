package com.xplo.code.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog

object DialogUtil {
    @SuppressLint("StaticFieldLeak")
    var alertDialog: LottieAlertDialog? = null

    fun showLottieDialog(context: Context, title: String, description: String) {
        alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
            .setTitle(title)
            .setDescription(description)
            .build().apply {
                show()
                setCancelable(false)
            }
    }

    fun showLottieDialogSuccessMsg(context: Context, title: String, description: String) {
        alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_SUCCESS)
            .setTitle(title)
            .setDescription(description)
            .build().apply {
                show()
                setCancelable(true)
            }
    }

    fun showLottieDialogFailMsg(context: Context, description: String) {
        LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
            .setTitle("Attention!")
            .setDescription(description)
            .setPositiveText("Ok")
            .setPositiveListener(object : ClickListener {
                override fun onClick(dialog: LottieAlertDialog) {
                    dialog.dismiss()
                }
            })
            .setPositiveButtonColor(Color.RED)
            .setPositiveTextColor(Color.WHITE)
            .build().apply {
                show()
                setCancelable(true)
            }

    }


    fun dismissLottieDialog() {
        alertDialog?.dismiss()
    }
}
