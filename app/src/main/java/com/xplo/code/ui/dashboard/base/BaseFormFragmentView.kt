package com.xplo.code.ui.dashboard.base

import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner

interface BaseFormFragmentView {

    fun chkEditText(editText: EditText, error: String?): String?

    //    fun getOrAssertEditTextInt(view: EditText, error: String?): Int?
//    fun getOrAssertEditTextDouble(view: EditText, error: String?): Double?
    fun chkSpinner(spinner: Spinner, error: String?): String?
    fun chkRadioGroup(radioGroup: RadioGroup, error: String?): String?

    fun setSpinnerItem(spinner: Spinner, items: Array<String>, item: String?)

    fun isValidationEnabled(): Boolean
}