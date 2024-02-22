package com.xplo.code.utils

import android.content.Context
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog

object DialogUtil {
    private var alertDialog: LottieAlertDialog? = null

    fun showLottieDialog(context: Context, title: String, description: String) {
        alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
            .setTitle(title)
            .setDescription(description)
            .build().apply {
                show()
                setCancelable(false)
            }
    }
    fun dismissLottieDialog() {
        alertDialog?.dismiss()
    }
}
//just call
//DialogUtil.showLottieDialog(requireContext(), "Preparing Content", "Please wait")
//DialogUtil.dismissLottieDialog()