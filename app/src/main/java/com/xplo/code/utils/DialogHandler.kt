package com.xplo.code.utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.xplo.code.R
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.ui.dashboard.household.forms.HhFormAlternateFragment
import com.xplo.code.ui.dashboard.household.forms.HouseholdHomeFragment
import com.xplo.code.ui.dashboard.model.AlternateForm

object DialogHandler {

    private var alertDialog: LottieAlertDialog? = null
    fun showNomineeConfirmfationDialog(
        context: Context,
    ) {
        //val dialog = Dialog(requireContext())
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_reasource)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        /* val window = dialog.window
         val width = resources.getDimensionPixelSize(R.dimen.dialog_min_width)
         window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
 */
        val btnOk : Button = dialog.findViewById<Button>(R.id.okButton)
//        val btnCancel : Button = dialog.findViewById<Button>(R.id.cancelButton)

        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showLottieDialog(context: Context, title: String, description: String) {
        DialogUtil.alertDialog = LottieAlertDialog.Builder(context, DialogTypes.TYPE_LOADING)
            .setTitle(title)
            .setDescription(description)
            .build().apply {
                show()
                setCancelable(false)
            }
    }

}
