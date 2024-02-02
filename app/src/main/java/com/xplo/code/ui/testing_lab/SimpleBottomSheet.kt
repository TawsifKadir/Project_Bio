package com.xplo.code.ui.testing_lab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xplo.code.R

class SimpleBottomSheet(private val callback: (String?) -> Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bsd_input_a_text, container, false)

        val etName = view.findViewById<EditText>(R.id.etName)
        val btOk = view.findViewById<Button>(R.id.btOk)

        btOk.setOnClickListener {
            callback.invoke(etName.text.toString())
            dismiss()
        }

        return view
    }

}
