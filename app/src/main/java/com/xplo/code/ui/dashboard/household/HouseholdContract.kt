package com.xplo.code.ui.dashboard.household

import androidx.fragment.app.Fragment
import com.xplo.code.base.BaseContract
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.HouseholdForm

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
interface HouseholdContract {


    interface View : BaseContract.View {

        fun navigateToForm1()
        fun navigateToForm2()
        fun navigateToForm3()
        fun navigateToForm4()
        fun navigateToForm5()
        fun navigateToForm6()
        fun navigateToPreview()

        fun onBackButton()
        fun onNextButton()

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
        fun onPopulateView()

    }

    interface Form1View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm1?)
    }

    interface Form2View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm2?)
    }

    interface Form3View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm3?)

    }

    interface Form4View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm4?)

    }

    interface Form5View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm5?)

    }
    interface Form6View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm6?)

    }

    interface PreviewView : BaseContract.View, CommonView {
        fun onGetCompleteData(data: HouseholdForm?)

    }

    interface HouseholdListView : BaseContract.View {
        fun navigateToHouseholdDetails(item: HouseholdItem)

        fun onGetHouseholdList(items: List<HouseholdItem>?)
        fun onGetHouseholdListFailure(msg: String?)


    }


}