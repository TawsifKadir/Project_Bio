package com.xplo.code.ui.dashboard.household.forms.nominee

import androidx.fragment.app.Fragment
import com.xplo.code.ui.dashboard.model.Nominee

/**
 * Copyright 2024 (C) Bongo
 *
 * Created  : 2024/01/10
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
interface NomineeModalContract {

    interface View : InputListener, CommonView {

        fun showLoading()
        fun hideLoading()

        fun showBackButton()
        fun hideBackButton()
        fun showCrossButton()
        fun hideCrossButton()

        fun onBackButton()
        fun onCrossButton()
        fun onCloseDialog()

        fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            clearBackStack: Boolean
        )

        fun onPageAdd()

        fun navigateToFirstPage()
    }

    interface Presenter {

    }


    interface InputView : CommonView {

        fun onReinstateData(item: Nominee?)

        fun onGetNomineeSuccess(item: Nominee?)
        fun onGetNomineeFailure(msg: String?)

        fun onReadInput()
        fun onLongClickDataGeneration()
        fun onGenerateDummyInput()
        fun onPopulateView()


    }

    interface CommonView {

//        fun onShowError(msg: String?)
//        fun showMessage(msg: String?)
//        fun getPrefHelper(): PreferencesHelper
//        fun logPage(pageName: String, value: String?)

    }

    /**
     * Implement in Nominee input modal
     */
    interface InputListener {

        fun onCompleteModal(
            item: Nominee?
        )

    }

}