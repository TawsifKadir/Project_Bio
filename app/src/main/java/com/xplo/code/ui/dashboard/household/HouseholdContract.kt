package com.xplo.code.ui.dashboard.household

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.kit.integrationmanager.model.Beneficiary
import com.xplo.code.base.BaseContract
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.Finger
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.Nominee

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

        fun navigateToHouseholdHome()
        fun navigateToForm1()
        fun navigateToForm2()
        fun navigateToForm3()
        fun navigateToForm4()
        fun navigateToForm5()
        fun navigateToForm6()
        fun navigateToAlternateAddForm()
        fun navigateToPreview()
        fun navigateToFormDetails(item: HouseholdItem?)

        fun navigateToAnotherHousehold(hhForm1: HhForm1?)

        fun onBackButton()
        fun onNextButton()

        fun doFragmentTransaction(
            fragment: Fragment,
            tag: String,
            addToBackStack: Boolean,
            clearBackStack: Boolean
        )

        fun onPageAdd()

        fun onSaveBeneficiarySuccess(item: BeneficiaryEntity?)
        fun onSaveBeneficiaryFailure(msg: String?)

        fun getRootForm(): HouseholdForm?
        fun setRootForm(form: HouseholdForm?)
        fun resetRootForm()
        fun resetRootFormKeepSetup()

    }

    interface Presenter {

        fun saveFormPEntity(form: HouseholdForm?)

        fun saveHouseholdFormAsHouseholdItem(form: HouseholdForm?)
        fun getHouseholdItem(id: String?)
        fun getHouseholdItems()
        fun updateHouseholdItem(item: HouseholdItem?)
        fun deleteHouseholdItem(item: HouseholdItem?)
        fun sendHouseholdForm(form: HouseholdForm?, pos: Int)


        fun saveBeneficiaryEntity(item: BeneficiaryEntity?)
        fun getBeneficiaryEntity(id: String?)
        fun getBeneficiaryEntityItems()
        fun updateBeneficiaryEntity(item: BeneficiaryEntity?)
        fun deleteBeneficiaryEntity(item: BeneficiaryEntity?)
        fun sendBeneficiaryEntity(item: BeneficiaryEntity?, pos: Int)
        fun sendBeneficiary(item: Beneficiary?, pos: Int)




        fun getStateItems()
        fun getCountryItems(state: String?)
        fun getPayamItems(county: String?)
        fun getBomaItems(payam: String?)

        fun syncHouseholdForm(context: Context, form: HouseholdForm?, pos: Int)
        fun syncBeneficiaryEntity(context: Context, entity: BeneficiaryEntity?, pos: Int)
        fun syncBeneficiary(context: Context, beneficiary: Beneficiary?, pos: Int)
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

        fun onSubmitFormSuccess(id: String?, pos: Int)
        fun onSubmitFormFailure(msg: String?)

    }


    interface Form1View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm1?)
        fun onReinstateData(form: HhForm1?)

        fun onGetStateItems(items: List<OptionItem>?)
        fun onGetStateItemsFailure(msg: String?)
        fun onSelectStateItem(item: OptionItem?)

        fun onGetCountryItems(items: List<OptionItem>?)
        fun onGetCountryItemsFailure(msg: String?)
        fun onSelectCountryItem(item: OptionItem?)

        fun onGetPayamItems(items: List<OptionItem>?)
        fun onGetPayamItemsFailure(msg: String?)
        fun onSelectPayamItem(item: OptionItem?)

        fun onGetBomaItems(items: List<OptionItem>?)
        fun onGetBomaItemsFailure(msg: String?)
        fun onSelectBomaItem(item: OptionItem?)


    }

    interface Form2View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm2?)
        fun onReinstateData(form: HhForm2?)
    }

    interface Form3View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm3?)
        fun onReinstateData(form: HhForm3?)

    }

    interface Form4View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm4?)
        fun onReinstateData(form: HhForm4?)

        fun onGetImageUri(uri: Uri?)
    }

    interface Form5View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm5?)
        fun onReinstateData(form: HhForm5?)

        fun onStartFingerprintCapture()
        fun onGetFingerprintIntent(intent: Intent?)
        fun onGetFingerprintData(items: List<Finger>?)

        fun onRefreshFingerprints(items: List<Finger>?)
        fun onRefreshFingerDrawable(img: ImageView, finger: Finger?)


    }

    interface Form6View : BaseContract.View, CommonView {
        fun onValidated(form: HhForm6?)
        fun onReinstateData(form: HhForm6?)

        /**
         * nominee radio buttons
         */
        fun onChangeRGNomineeAdd(id: Int)
        fun onRGNomineeAddYes()
        fun onRGNomineeAddNo()
        fun onRGNomineeAddStatusClear()

        /**
         * Decision finalised, refresh view and start flow
         */
        fun onChooseNomineeAdd(gender: String?)
        fun onChooseNomineeNotAdd()

        fun onEnableDisableNominee(isNomineeAdd: Boolean)
        fun onClickAddNominee()
        fun onAddNominee(gender: String?)

        fun onGetANomineeFromPopup(nominee: Nominee?)
        fun onRefreshViewWhenListUpdated()
        fun onListHasData()
        fun onListEmpty()

        fun onSelectNoNomineeReason(item: String?)

    }

    interface Form62View : Form6View {

    }


    interface FormAlternateView : BaseContract.View, CommonView {
        fun onValidated(forms: ArrayList<AlternateForm>?)
        fun onReinstateData(forms: ArrayList<AlternateForm>?)

        fun onClickAddAlternate()
        fun onGetAnAlternate(form: AlternateForm?)

    }


    interface PreviewView : BaseContract.View, CommonView {
        fun onGetCompleteData(data: HouseholdForm?)
        fun onSaveSuccess(id: String?)
        fun onSaveFailure(msg: String?)
    }

    interface HouseholdListView : BaseContract.View {
        fun navigateToHouseholdDetails(item: HouseholdItem)

        fun onGetHouseholdList(items: List<HouseholdItem>?)
        fun onGetHouseholdListFailure(msg: String?)


    }

    interface FormDetailsView : BaseContract.View {
        fun onGetCompleteData(item: HouseholdItem?)
    }


}