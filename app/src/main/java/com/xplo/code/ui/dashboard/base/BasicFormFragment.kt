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
    override fun chkEditText3Char(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "" || txt.length < 3) {
                editText.error = error
                return null
            }
        }
        return txt
    }
    override fun chkEditTextMax3Digit(editText: EditText, error: String?): String? {
        val txt = editText.text.toString()
        if (isValidationEnabled()) {
            if (txt.isEmpty() || txt == "") {
                editText.error = error
                return null
            }else if(txt.toInt()>999){
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

    override fun checkRbPosNeg(
        radioGroup: RadioGroup,
        rbPos: RadioButton,
        rbNeg: RadioButton,
        item: String?
    ) {
        radioGroup.checkRbPosNeg(rbPos, rbNeg, item)
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