package com.xplo.code.ui.dashboard.base

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner

interface BasicFormView {

    fun chkEditText(editText: EditText, error: String?): String?
    fun chkEditText3Char(editText: EditText, error: String?): String?
    fun chkEditTextMax3Digit(editText: EditText, error: String?): String?

    //    fun getOrAssertEditTextInt(view: EditText, error: String?): Int?
//    fun getOrAssertEditTextDouble(view: EditText, error: String?): Double?
    fun chkSpinner(spinner: Spinner, error: String?): String?
    fun chkRadioGroup(radioGroup: RadioGroup, error: String?): String?

    fun setSpinnerItem(spinner: Spinner, items: Array<String>, item: String?)

    fun onSelectSpinnerItem(parent: AdapterView<*>?, view: View?, position: Int)

    fun isValidationEnabled(): Boolean
}