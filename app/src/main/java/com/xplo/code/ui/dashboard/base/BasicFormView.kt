package com.xplo.code.ui.dashboard.base

import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner

interface BasicFormView {

    fun getEditTextInt(editText: EditText): Int
    fun getEditText(editText: EditText): String?
    fun chkEditText(editText: EditText, error: String?): String?
    fun chkEditTextOnlyNumber(editText: EditText, error: String?): String?
    fun chkEditTextOnlyNumberAndChar(editText: EditText, error: String?): String?
    fun checkIDNumber(editText: EditText, error: String?, idType : String?): String?
    fun chkEditTextMonthlyAvgIncome(editText: EditText, error: String?): Int?
    fun chkEditText3Char(editText: EditText, error: String?): String?
    fun chkEditText3CharAllowSpace(editText: EditText, error: String?): String?
    fun chkEditTextNickName3Char(editText: EditText, error: String?): String?
    fun chkEditTextMax3Digit(editText: EditText, error: String?): String?
    fun chkPhoneNumber(editText: EditText, error: String?): String?
    fun chkAge(editText: EditText, error: String?): String?
    fun chkOtherText(editText: EditText,error: String?): String?

    //    fun getOrAssertEditTextInt(view: EditText, error: String?): Int?
//    fun getOrAssertEditTextDouble(view: EditText, error: String?): Double?
    fun chkSpinner(spinner: Spinner, error: String?): String?
    fun chkNomineeOccupationSpinner(spinner: Spinner, error: String?): String?
    fun chkRadioGroup(radioGroup: RadioGroup, error: String?): String?
    fun getRadioGroup(radioGroup: RadioGroup): String?

    fun checkRbPosNeg(radioGroup: RadioGroup, rbPos: RadioButton, rbNeg: RadioButton, item: String?)
    fun checkRbYes(radioGroup: RadioGroup, rbPos: RadioButton, rbNeg: RadioButton)
    fun checkRbNo(radioGroup: RadioGroup, rbPos: RadioButton, rbNeg: RadioButton)

    fun setSpinnerItem(spinner: Spinner, items: Array<String>, item: String?)

    fun onSelectSpinnerItem(parent: AdapterView<*>?, view: View?, position: Int)

    fun isValidationEnabled(): Boolean
}