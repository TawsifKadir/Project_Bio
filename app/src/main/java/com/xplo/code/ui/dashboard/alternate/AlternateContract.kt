package com.xplo.code.ui.dashboard.alternate

import androidx.fragment.app.Fragment
import com.xplo.code.base.BaseContract
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.code.ui.dashboard.model.AlternateForm

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

        fun navigateToAlternateHome()
        fun navigateToForm1(id: String?)
        fun navigateToForm2()
        fun navigateToForm3()
        fun navigateToPreview()
        fun navigateToFormDetails(item: HouseholdItem?)


        fun onBackButton()
        fun onNextButton()

        fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            clearBackStack: Boolean
        )

        fun onPageAdd()

        fun getRootForm(): AlternateForm?
        fun setRootForm(form: AlternateForm?)

    }

    interface Presenter : BaseContract.Presenter<View> {

        fun saveData(data: String?)
    }

    interface CommonView {

        fun onClickBackButton()
        fun onClickNextButton()
        fun onReadInput()
        fun onLongClickDataGeneration()
        fun onGenerateDummyInput()
        fun onPopulateView()

    }

    interface HomeView : BaseContract.View {
        fun navigateToHouseholdDetails(item: HouseholdItem)

        fun onGetHouseholdList(items: List<HouseholdItem>?)
        fun onGetHouseholdListFailure(msg: String?)


    }

    interface Form1View : BaseContract.View, CommonView {
        fun onValidated(form: AlForm1?)

        fun onGetHouseholdItem(item: HouseholdItem?)
        fun onGetHouseholdItemFailure(msg: String?)
    }

    interface Form2View : BaseContract.View, CommonView {

        fun onValidated(form: AlForm1?)

    }

    interface Form3View : BaseContract.View, CommonView {

        fun onValidated(form: AlForm3?)

    }

//    interface Form4View : BaseContract.View, CommonView {
//
//
//    }
//
//    interface Form5View : BaseContract.View, CommonView {
//
//
//    }

    interface PreviewView : BaseContract.View, CommonView {

        fun onValidated(form: AlternateForm)

    }


}