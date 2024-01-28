package com.xplo.code.ui.dashboard.alternate

import androidx.fragment.app.Fragment
import com.xplo.code.base.BaseContract
import com.xplo.code.ui.dashboard.model.ALTForm1
import com.xplo.code.ui.dashboard.model.ALTForm3
import com.xplo.code.ui.dashboard.model.HouseholdForm

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface AlternateContract {


    interface View : BaseContract.View {

        fun navigateToForm1(id: String?)
        fun navigateToForm2()
        fun navigateToForm3()
        fun navigateToForm4()
        fun navigateToForm5()
        fun navigateToPreview()

//        fun showBackButton()
//        fun hideBackButton()
//        fun showNextButton()
//        fun hideNextButton()
//        fun showSubmitButton()
//        fun hideSubmitButton()

        fun onBackButton()
        fun onNextButton()
        //fun onSubmitButton()

        fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            clearBackStack: Boolean
        )

        fun onPageAdd()

        fun getRootForm(): HouseholdForm?
        fun setRootForm(form: HouseholdForm?)
    }

    interface Presenter : BaseContract.Presenter<View> {

        fun saveData(data: String?)
    }

    interface CommonView {

        fun onClickBackButton()
        fun onClickNextButton()
        fun onReadInput()
        fun onGenerateDummyInput()

//        fun onClickSubmitButton()

//        fun bindSpinnerData(spinner: Spinner, items: Array<String>)

    }

    interface Form1View : BaseContract.View, CommonView {


        fun onValidated(form: ALTForm1?)
    }

    interface Form2View : BaseContract.View, CommonView {

        fun onValidated(form: ALTForm1?)

    }

    interface Form3View : BaseContract.View, CommonView {

        fun onValidated(form: ALTForm3?)

    }

    interface Form4View : BaseContract.View, CommonView {


    }

    interface Form5View : BaseContract.View, CommonView {


    }

    interface PreviewView : BaseContract.View, CommonView {

        fun onValidated(form: ALTForm1?)

    }


}