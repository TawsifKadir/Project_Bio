package com.xplo.code.ui.dashboard.payment

import androidx.fragment.app.Fragment
import com.xplo.code.base.BaseContract

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface PaymentContract {


    interface View : BaseContract.View {

        fun navigateToForm1()
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

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun saveData(data: String?)
    }

    interface CommonView {

        fun onClickBackButton()
        fun onClickNextButton()

    }

    interface Form1View : BaseContract.View, CommonView {


    }

    interface Form2View : BaseContract.View, CommonView {


    }

    interface Form3View : BaseContract.View, CommonView {


    }

    interface Form4View : BaseContract.View, CommonView {


    }

    interface Form5View : BaseContract.View, CommonView {


    }

    interface PreviewView : BaseContract.View, CommonView {


    }


}