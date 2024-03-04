package com.xplo.code.ui.dashboard.household.forms


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentFormDetailsBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.ReportRow
import com.xplo.code.ui.dashboard.model.getReportRows
import com.xplo.code.ui.dashboard.model.getReportRowsAltSummary
import com.xplo.code.ui.testing_lab.FormPGActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class FormDetailsFragment : BaseFragment(), HouseholdContract.FormDetailsView {

    companion object {
        const val TAG = "FormDetailsFragment"

        @JvmStatic
        fun newInstance(parent: String?, item: HouseholdItem?): FormDetailsFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent, item = ${item?.hid}")
            val fragment = FormDetailsFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putSerializable(Bk.KEY_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
        @JvmStatic
        fun newInstance(parent: String?, item: com.kit.integrationmanager.model.Beneficiary?): FormDetailsFragment {
           // Log.d(TAG, "newInstance() called with: parent = $parent, item = ${item?.hid}")
            val fragment = FormDetailsFragment()
            fragment.item = item?.applicationId
            fragment.beneficiary = item
            //val bundle = Bundle()
            //bundle.putString(Bk.KEY_PARENT, parent)
            //bundle.putSerializable(Bk.KEY_ITEM, item)
            //fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentFormDetailsBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: HomeContract.Presenter
    //private var interactor: HouseholdContract.View? = null

    private var householdItem : HouseholdItem? = null
    private var item : String? = null
    private var beneficiary : com.kit.integrationmanager.model.Beneficiary? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }


    override fun initInitial() {
        //binding.tvDetails.movementMethod = ScrollingMovementMethod()

    }

    override fun initView() {
        generateReportFromBeneficiary(beneficiary)
//        if (arguments != null) {
//            householdItem = arguments?.getSerializable(Bk.KEY_ITEM) as HouseholdItem
//            onGetCompleteData(householdItem)
//        }
    }

    override fun initObserver() {

        binding.tvPage.setOnLongClickListener {
            //JvActivity.open(requireContext(), null, householdItem?.data)
            FormPGActivity.open(requireContext(), null, householdItem?.id)
            return@setOnLongClickListener true
        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Form Details")

    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onGetCompleteData(item: HouseholdItem?) {
        Log.d(TAG, "onGetCompleteData() called with: item = $item")

        //binding.tvDetails.text = item.toString()

        val form = item?.toHouseholdForm()

        generateReport(form)

    }

    private fun generateReport(form: HouseholdForm?) {
        Log.d(TAG, "generateReport() called with: form = $form")
        if (form == null) return
        addReportForm1(form.form1)
        addReportForm2(form.form2)
        addReportForm3(form.form3)
        addReportForm4(form.form4)
        addReportForm5(form.form5)
        addReportForm6(form.form6)
        addReportAlternate(form)
    }
    private fun generateReportFromBeneficiary(beneficiary: com.kit.integrationmanager.model.Beneficiary?) {
        //Log.d(TAG, "generateReport() called with: form = $form")
        if (beneficiary == null) return

        val form1 = HhForm1()
        form1.lon = beneficiary.location.lon
        form1.lat = beneficiary.location.lat
        form1.state?.name = beneficiary.address.stateId.toString()
        form1.payam?.name =  beneficiary.address.payam.toString()
        form1.boma?.name =  beneficiary.address.boma.toString()
        form1.county?.name =  beneficiary.address.countyId.toString()
        addReportForm1(form1)

        val form2 = HhForm2()
        form2.firstName = beneficiary.respondentFirstName
        form2.middleName = beneficiary.respondentMiddleName
        form2.lastName =  beneficiary.respondentLastName
        form2.nickName = beneficiary.respondentNickName
        form2.spouseFirstName = beneficiary.spouseFirstName
        form2.spouseMiddleName = beneficiary.spouseMiddleName
        form2.spouseLastName = beneficiary.spouseLastName
        form2.spouseNickName = beneficiary.respondentNickName
        form2.age = beneficiary.respondentAge
        form2.idNumber = beneficiary.respondentId

        if(beneficiary.documentType != null){
            form2.idNumberType = beneficiary.documentType.value
        }
        form2.phoneNumber = beneficiary.respondentPhoneNo

        if (beneficiary.householdIncomeSource != null){
            form2.mainSourceOfIncome = beneficiary.householdIncomeSource.value
        }
        if(beneficiary.householdIncomeSource != null){
            form2.currency = beneficiary.householdIncomeSource.value
        }
        form2.monthlyAverageIncome = beneficiary.householdMonthlyAvgIncome

        if(beneficiary.respondentGender != null){
            form2.gender = beneficiary.respondentGender.value
        }

        if(beneficiary.respondentMaritalStatus != null){
            form2.respondentRlt = beneficiary.respondentMaritalStatus.value
        }
        if(beneficiary.respondentMaritalStatus != null){
            form2.maritalStatus = beneficiary.respondentMaritalStatus.value
        }
        if(beneficiary.respondentLegalStatus != null){
            form2.legalStatus = beneficiary.respondentLegalStatus.value
        }
        if(beneficiary.selectionReason[0] != null){
            form2.selectionReason = beneficiary.selectionReason[0].value
        }
        if(beneficiary.selectionCriteria != null){
            form2.selectionCriteria = beneficiary.selectionCriteria.value
        }
        addReportForm2(form2)

        /*
        val form3 = HhForm3()
        form3.female0_2 = beneficiary.householdMember2.totalFemale
        addReportForm3(form.form3)

        val form4 = HhForm4()
        form4.photoData = beneficiary.biometrics[0].biometricData[0].pho

        val form5 = HhForm5()
        form5.fingers = beneficiary.biometrics
        addReportForm5(form5)

        val form6 = HhForm6()
        form6.nominees = beneficiary.nominees
        addReportForm6(form6)

        addReportAlternate(form)
         */
    }

    private fun addReportForm1(form: HhForm1?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockLocation.addView(view)
        }
    }

    private fun addReportForm2(form: HhForm2?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockPerInfo.addView(view)
        }
    }

    private fun addReportForm3(form: HhForm3?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockHouseholdBreakdown.addView(view)
        }
    }

    private fun addReportForm4(form: HhForm4?) {
        if (form == null) return
        binding.viewPreview.ivAvatar.loadAvatar(form.photoData?.imgPath)
    }

    private fun addReportForm5(form: HhForm5?) {
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockFinger.addView(view)
        }
    }

    private fun addReportForm6(form: HhForm6?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockNominee.addView(view)
        }
    }

    private fun addReportAlternate(form: HouseholdForm?) {
        if (form == null) return
        val rows = form.getReportRowsAltSummary()

        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockAlternate.addView(view)
        }

        for (item in form.alternates){
            val view = getAltRowView(item)
            binding.viewPreview.blockAlternate.addView(view)
        }

    }


    private fun getRowView(item: ReportRow?): View {
        Log.d(TAG, "getRowView() called with: item = $item")
        return ReportViewUtils.getRowView(requireContext(), layoutInflater, item)
    }

    private fun getAltRowView(item: AlternateForm?): View {
        Log.d(TAG, "getAltRowView() called with: item = $item")
        return ReportViewUtils.getAltFormView(requireContext(), layoutInflater, item)
    }


}