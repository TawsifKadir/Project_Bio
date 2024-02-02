package com.xplo.code.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xplo.code.R

class ConsentSheet(private val callback: (Boolean) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bsd_consent_sheet, container, false)

        val btDialogPositiveButton = view.findViewById<Button>(R.id.btDialogPositiveButton)
        val btDialogNegativeButton = view.findViewById<Button>(R.id.btDialogNegativeButton)

        btDialogPositiveButton.setOnClickListener {
            callback.invoke(true)
            dismiss()
        }

        btDialogPositiveButton.setOnClickListener {
            callback.invoke(false)
            dismiss()
        }

        return view
    }

}
