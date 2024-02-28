package com.xplo.code.ui.dashboard.base

import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.checkRbPosNeg
import com.xplo.code.core.ext.setItem


abstract class BasicFormFragment : BaseFragment(), BasicFormView {

    private val TAG = "BasicFormFragment"

    override fun getEditTextInt(editText: EditText): Int {
        val txt = editText.text.toString()
        if (txt.isEmpty()) return 0
        return txt.toInt()
    }

    override fun getEditText(editText: EditText): String? {
        val txt = editText.text.toString()
        if (txt.isEmpty()) return null
        return txt
    }

    override fun chkEditText(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "") {
                editText.error = error
                return null
            }
        }
        return txt
    }


    override fun chkAge(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if(txt.isEmpty()){
                editText.error = "Please enter an age"
                return null
            }
            val age = txt.toInt()
            if (age < 18 || age > 35) {
                editText.error = "Age must be between 18 and 35"
                return null
            }
        }
        return txt
    }
    override fun chkEditTextOnlyNumberAndChar(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "" || txt.length < 10) {
                editText.error = "Minimum 10 character"
                return null
            } else if (!isOnlyNumberAndChar(txt)) {
                editText.error = "No Special Character Allow"
                return null
            }
        }
        return txt
    }

    override fun chkPhoneNumber(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if(txt.isNotEmpty()){
                if(txt.length != 10){
                    editText.error = "Invalid Phone Number"
                    return null
                }
            }else if(txt.isEmpty()){
                return ""
            }
        }
        return txt
    }

    override fun chkEditTextOnlyNumber(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "" || txt.length < 10) {
                editText.error = "Minimum 10 character"
                return null
            } else if (!isOnlyNumber(txt)) {
                editText.error = "No Character Allow"
                return null
            }
        }
        return txt
    }

    override fun chkEditTextMonthlyAvgIncome(editText: EditText, error: String?): Int? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "") {
                editText.error = error
                return null
            }
//            else if (txt.toInt() > 10000) {
//                editText.error = "Income must be less than 10000"
//                return null
//            }
        }
        return txt.toInt()
    }

    override fun chkEditText3Char(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "" || txt.length < 3) {
                editText.error = "Minimum 3 character"
                return null
            } else if (!isOnlyLetters(txt)) {
                editText.error = "Only Character Allow"
                return null
            }
        }
        return txt
    }
    override fun chkEditTextNickName3Char(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if ( txt.length in 1..2) {
                editText.error = "Minimum 3 character"
                return null
            } else if (txt.isEmpty() || txt == ""){
                return txt
            }else if (!isOnlyLetters(txt)) {
                editText.error = "Only Character Allow"
                return null
            }
        }
        return txt
    }


    private fun isOnlyLetters(input: String): Boolean {
        val pattern = "^[a-zA-Z]+$".toRegex()
        return input.matches(pattern)
    }

    private fun isOnlyNumberAndChar(input: String): Boolean {
        val pattern = "^[a-zA-Z0-9]+$".toRegex()
        return input.matches(pattern)
    }

    private fun isOnlyNumber(input: String): Boolean {
        val pattern = "^[0-9]+$".toRegex()
        return input.matches(pattern)
    }

    override fun chkEditTextMax3Digit(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "") {
                editText.error = error
                return null
            } else if (txt.toInt() > 999) {
                editText.error = error
            }
        }
        return txt
    }


    override fun chkSpinner(spinner: Spinner, error: String?): String? {

        val pos = spinner.selectedItemPosition
        if (pos < 0) return null
        val txt = spinner.selectedItem.toString()

        if (!isValidationEnabled()) return txt

        if (pos == 0) {

            val errorView = spinner.selectedView as TextView
            errorView.error = ""
            errorView.setTextColor(Color.RED)
            errorView.text = error

            return null
        }

        return txt
    }

    override fun chkRadioGroup(radioGroup: RadioGroup, error: String?): String? {
        val id = radioGroup.checkedRadioButtonId

        if (id != -1) {
            val rb = radioGroup.findViewById(id) as RadioButton
            return rb.text.toString()
        }

        if (isValidationEnabled()) {
            val lastChildPos: Int = radioGroup.childCount - 1
            (radioGroup.getChildAt(lastChildPos) as RadioButton).error = error
        }

        return null
    }

    override fun getRadioGroup(radioGroup: RadioGroup): String? {

        val id = radioGroup.checkedRadioButtonId

        if (id != -1) {
            val rb = radioGroup.findViewById(id) as RadioButton
            return rb.text.toString()
        }

        return null

    }

    override fun checkRbPosNeg(
        radioGroup: RadioGroup,
        rbPos: RadioButton,
        rbNeg: RadioButton,
        item: String?
    ) {
        radioGroup.checkRbPosNeg(rbPos, rbNeg, item)
    }

    override fun checkRbYes(radioGroup: RadioGroup, rbPos: RadioButton, rbNeg: RadioButton) {
        radioGroup.checkRbPosNeg(rbPos, rbNeg, "yes")
    }

    override fun checkRbNo(radioGroup: RadioGroup, rbPos: RadioButton, rbNeg: RadioButton) {
        radioGroup.checkRbPosNeg(rbPos, rbNeg, "no")
    }

    override fun setSpinnerItem(spinner: Spinner, items: Array<String>, item: String?) {
        //spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(item));
        spinner.setItem(items, item)
    }

    override fun onSelectSpinnerItem(parent: AdapterView<*>?, view: View?, position: Int) {
    }

    override fun isValidationEnabled(): Boolean {
        return TestConfig.isValidationEnabled
    }

}