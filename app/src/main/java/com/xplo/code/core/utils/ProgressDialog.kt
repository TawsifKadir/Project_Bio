package com.xplo.code.core.utils

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.xplo.code.R


class ProgressDialog {

    companion object {

        fun createProgressDialog(context: Context, isCancelable: Boolean = false): Dialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(isCancelable)
            builder.setView(R.layout.progress_dialog)
            return builder.create()
        }


    }
}