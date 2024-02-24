package com.xplo.code.utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.xplo.code.R
import com.xplo.code.ui.dashboard.household.forms.HhFormAlternateFragment
import com.xplo.code.ui.dashboard.model.AlternateForm

object DialogHandler {
    fun showNomineeCOnfirmfationDialog(
        context: Context,
        nowObj: HhFormAlternateFragment,
        nowList: ArrayList<AlternateForm>
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
        val btnCancel : Button = dialog.findViewById<Button>(R.id.cancelButton)

        btnOk.setOnClickListener {

            nowObj.onValidated(nowList)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
