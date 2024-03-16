package com.xplo.code.ui.dashboard.alternate

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.xplo.code.base.BaseContract
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.room.model.Beneficiary
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.AlForm2
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.Finger

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

//        fun navigateToAlternateHome()
        fun navigateToForm1(
            id: String?,
            hhName: String?,
            addToBackStack: Boolean,
            clearBackStack: Boolean
        )

        fun navigateToForm2()
        fun navigateToForm3()
        fun navigateToPreview()
//        fun navigateToFormDetails(item: HouseholdItem?)


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

      //  fun getHouseholdItem(): HouseholdItem?
//        fun setHouseholdItem(item: HouseholdItem?)

        fun getRequestCode(): Int
        fun isCallForResult(): Boolean

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
        fun navigateToHouseholdDetails(item: Beneficiary)

        fun onGetHouseholdList(items: List<Beneficiary>?)
        fun onGetHouseholdListFailure(msg: String?)


    }

    interface Form1View : BaseContract.View, CommonView {
        fun onValidated(form: AlForm1?)
        fun onReinstateData(form: AlForm1?)

//        fun onGetHouseholdItem(item: HouseholdItem?)
//        fun onGetHouseholdItemFailure(msg: String?)
    }

    interface Form2View : BaseContract.View, CommonView {

        fun onValidated(form: AlForm2?)
        fun onReinstateData(form: AlForm2?)

        fun onClickCapturePhoto()


        fun onGetImageUri(uri: Uri?)

    }

    interface Form3View : BaseContract.View, CommonView {

        fun onValidated(form: AlForm3?)
        fun onReinstateData(form: AlForm3?)

        fun onStartFingerprintCapture()
        fun onGetFingerprintIntent(intent: Intent?)
        fun onGetFingerprintData(items: List<Finger>?, noFingerprintReason: String?)
        fun onGetFingerprintData(items: List<Finger>?, noFingerprintReason: String?, noFingerprintReasonText: String?)

        fun onRefreshFingerprints(items: List<Finger>?)
        fun onRefreshFingerDrawable(img: ImageView, finger: Finger?)

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
        fun onPublishResult()

        fun onUpdateSuccess(id: String?)
        fun onUpdateFailure(msg: String?)

    }


}