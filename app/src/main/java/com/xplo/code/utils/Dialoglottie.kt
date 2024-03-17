package com.xplo.code.utils

import android.content.Context
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog

object DialogUtil {
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

    fun showLottieDialogFailMsg(context: Context,  description: String) {
//        alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
//            .setTitle(title)
//            .setDescription(description)
//            .build().apply {
//                show()
//                setCancelable(true)
//            }
        val alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_ERROR)
            .setTitle("Attention!")
            .setDescription(description)
            .setPositiveText("Ok")
            .setPositiveListener(object : ClickListener {
                override fun onClick(dialog: LottieAlertDialog) {
                    dialog.dismiss()
                }
            })
            .build()
            .show()

    }


    fun dismissLottieDialog() {
        alertDialog?.dismiss()
    }
}
//just call
//DialogUtil.showLottieDialog(requireContext(), "Preparing Content", "Please wait")
//DialogUtil.dismissLottieDialog()