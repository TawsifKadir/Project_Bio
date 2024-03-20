package com.xplo.code.ui.dashboard.household.forms


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kit.integrationmanager.model.BiometricUserType
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.IncomeSourceEnum
import com.kit.integrationmanager.model.Nominee
import com.kit.integrationmanager.model.OccupationEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.core.ext.loadImage
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.data.db.offline.DbCall
import com.xplo.code.data.db.offline.DbCallImpl
import com.xplo.code.databinding.FragmentFormDetailsBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.AlForm2
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.CheckboxItem
import com.xplo.code.ui.dashboard.model.Finger
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.HhMember
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.PhotoData
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
        fun newInstance(
            parent: String?,
            item: com.kit.integrationmanager.model.Beneficiary?
        ): FormDetailsFragment {
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

    private var householdItem: HouseholdItem? = null
    private var item: String? = null
    private var beneficiary: com.kit.integrationmanager.model.Beneficiary? = null


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
        addReportForm4(form.form4, "")
        addReportForm5(form.form5)
        addReportForm6(form.form6)
        addReportAlternate(form)
    }

    private fun generateReportFromBeneficiary(beneficiary: com.kit.integrationmanager.model.Beneficiary?) {
        Log.d(
            TAG,
            "generateReportFromBeneficiary() called with: beneficiary = ${beneficiary?.alternatePayee1?.payeeAge}"
        )
        Log.d(
            TAG,
            "generateReportFromBeneficiary() called with: beneficiary = ${beneficiary?.alternatePayee2?.payeeAge}"
        )
        if (beneficiary == null) return
        val dbcall = DbCallImpl()

        // state,county,payam,boma

        // s_code,c_code,p_code,b_code

        val form1 = HhForm1()
        form1.lon = beneficiary.location.lon
        form1.lat = beneficiary.location.lat
        //  form1.state?.name = stateName
        form1.state?.name = dbcall.getItemName("state", "s_code", beneficiary.address.stateId, 0)
        form1.payam?.name = dbcall.getItemName("payam", "p_code", beneficiary.address.payam, 0)
        form1.boma?.name = dbcall.getItemName("boma", "b_code", beneficiary.address.boma, 0)
        form1.county?.name = dbcall.getItemName("county", "c_code", beneficiary.address.countyId, 0)
        addReportForm1(form1)

        val form2 = HhForm2()
        form2.firstName = beneficiary.respondentFirstName
        form2.middleName = beneficiary.respondentMiddleName
        form2.lastName = beneficiary.respondentLastName
        form2.nickName = beneficiary.respondentNickName
        form2.spouseFirstName = beneficiary.spouseFirstName
        form2.spouseMiddleName = beneficiary.spouseMiddleName
        form2.spouseLastName = beneficiary.spouseLastName
        form2.spouseNickName = beneficiary.respondentNickName
        form2.age = beneficiary.respondentAge
        form2.idNumber = beneficiary.respondentId

        if (beneficiary.documentType != null) {
            form2.idNumberType = beneficiary.documentType.value
            if (form2.idNumberType.equals(DocumentTypeEnum.OTHER.value, ignoreCase = true)) {
                form2.idNumberOthersvalue = beneficiary.documentTypeOther
            }
        }
        form2.phoneNumber = beneficiary.respondentPhoneNo

        if (beneficiary.householdIncomeSource != null) {
            form2.mainSourceOfIncome = beneficiary.householdIncomeSource.value
            if (form2.mainSourceOfIncome.equals(IncomeSourceEnum.OTHER.value, ignoreCase = true)) {
                form2.mainSourceOfIncomeOthers = beneficiary.incomeSourceOther
            }
        }
        form2.currency = CurrencyEnum.SUDANESE_POUND.value
//        if (beneficiary.householdIncomeSource != null) {
//            form2.currency = beneficiary.householdIncomeSource.value
//        }
        form2.monthlyAverageIncome = beneficiary.householdMonthlyAvgIncome

        if (beneficiary.respondentGender != null) {
            form2.gender = beneficiary.respondentGender.value
        }

        if (beneficiary.respondentMaritalStatus != null) {
            form2.respondentRlt = beneficiary.respondentMaritalStatus.value
            if (form2.respondentRlt.equals(RelationshipEnum.OTHER.value, ignoreCase = true)) {
                form2.respondentRltOthersValue = beneficiary.relationshipOther
            }
        }
        if (beneficiary.respondentMaritalStatus != null) {
            form2.maritalStatus = beneficiary.respondentMaritalStatus.value
        }
        if (beneficiary.respondentLegalStatus != null) {
            form2.legalStatus = beneficiary.respondentLegalStatus.value
        }
        if (beneficiary.selectionReason != null) {
            // Convert the string to a list of CheckboxItem objects
            val checkboxItemList = ArrayList<CheckboxItem>()

            for (reason in beneficiary.selectionReason) {
                val item = CheckboxItem()
                item.id = reason.id
                item.title = reason.value
                item.isChecked = !reason.value.isNullOrEmpty()
                checkboxItemList.add(item)
            }
            form2.itemsSupportType = checkboxItemList

        }

        if (beneficiary.selectionCriteria != null) {
            form2.selectionCriteria = beneficiary.selectionCriteria.value
        }
        addReportForm2(form2)


        val form3 = HhForm3()

        val hhMemberf = HhMember()
        hhMemberf.total = beneficiary.householdMember2.femaleTotal
        hhMemberf.disable = beneficiary.householdMember2.femaleDisable
        hhMemberf.ill = beneficiary.householdMember2.femaleChronicalIll
        hhMemberf.normal = beneficiary.householdMember2.femaleBoth
        form3.female0_2 = hhMemberf

        val hhMemberm = HhMember()
        hhMemberm.total = beneficiary.householdMember2.maleTotal
        hhMemberm.disable = beneficiary.householdMember2.maleDisable
        hhMemberm.ill = beneficiary.householdMember2.maleChronicalIll
        hhMemberm.normal = beneficiary.householdMember2.maleBoth
        form3.male0_2 = hhMemberm


        val hhMember5f = HhMember()
        hhMember5f.total = beneficiary.householdMember5.femaleTotal
        hhMember5f.disable = beneficiary.householdMember5.femaleDisable
        hhMember5f.ill = beneficiary.householdMember5.femaleChronicalIll
        hhMember5f.normal = beneficiary.householdMember5.femaleBoth
        form3.female3_5 = hhMember5f

        val hhMember5m = HhMember()
        hhMember5m.total = beneficiary.householdMember5.maleTotal
        hhMember5m.disable = beneficiary.householdMember5.maleDisable
        hhMember5m.ill = beneficiary.householdMember5.maleChronicalIll
        hhMember5m.normal = beneficiary.householdMember5.maleBoth
        form3.male3_5 = hhMember5m


        val hhMember64f = HhMember()
        hhMember64f.total = beneficiary.householdMember64.femaleTotal
        hhMember64f.disable = beneficiary.householdMember64.femaleDisable
        hhMember64f.ill = beneficiary.householdMember64.femaleChronicalIll
        hhMember64f.normal = beneficiary.householdMember64.femaleBoth
        form3.female36_64 = hhMember64f

        val hhMember64m = HhMember()
        hhMember64m.total = beneficiary.householdMember64.maleTotal
        hhMember64m.disable = beneficiary.householdMember64.maleDisable
        hhMember64m.ill = beneficiary.householdMember64.maleChronicalIll
        hhMember64m.normal = beneficiary.householdMember64.maleBoth
        form3.male36_64 = hhMember64m


        val hhMember65f = HhMember()
        hhMember65f.total = beneficiary.householdMember65.femaleTotal
        hhMember65f.disable = beneficiary.householdMember65.femaleDisable
        hhMember65f.ill = beneficiary.householdMember65.femaleChronicalIll
        hhMember65f.normal = beneficiary.householdMember65.femaleBoth
        form3.female65p = hhMember65f

        val hhMember65m = HhMember()
        hhMember65m.total = beneficiary.householdMember65.maleTotal
        hhMember65m.disable = beneficiary.householdMember65.maleDisable
        hhMember65m.ill = beneficiary.householdMember65.maleChronicalIll
        hhMember65m.normal = beneficiary.householdMember65.maleBoth
        form3.male65p = hhMember65m


        val hhMember35f = HhMember()
        hhMember35f.total = beneficiary.householdMember35.femaleTotal
        hhMember35f.disable = beneficiary.householdMember35.femaleDisable
        hhMember35f.ill = beneficiary.householdMember35.femaleChronicalIll
        hhMember35f.normal = beneficiary.householdMember35.femaleBoth
        form3.female18_35 = hhMember35f

        val hhMember35m = HhMember()
        hhMember35m.total = beneficiary.householdMember35.maleTotal
        hhMember35m.disable = beneficiary.householdMember35.maleDisable
        hhMember35m.ill = beneficiary.householdMember35.maleChronicalIll
        hhMember35m.normal = beneficiary.householdMember35.maleBoth
        form3.male18_35 = hhMember35m


        val hhMember17f = HhMember()
        hhMember17f.total = beneficiary.householdMember17.femaleTotal
        hhMember17f.disable = beneficiary.householdMember17.femaleDisable
        hhMember17f.ill = beneficiary.householdMember17.femaleChronicalIll
        hhMember17f.normal = beneficiary.householdMember17.femaleBoth
        form3.female6_17 = hhMember17f

        val hhMember17m = HhMember()
        hhMember17m.total = beneficiary.householdMember17.maleTotal
        hhMember17m.disable = beneficiary.householdMember17.maleDisable
        hhMember17m.ill = beneficiary.householdMember17.maleChronicalIll
        hhMember17m.normal = beneficiary.householdMember17.maleBoth
        form3.male6_17 = hhMember17m

        form3.isReadWrite = beneficiary.isReadWrite.toString()

        form3.readWriteNumber = beneficiary.memberReadWrite

        val totalList = listOf<Int>(
            hhMemberm.total, hhMemberf.total,
            hhMember5m.total, hhMember5f.total,
            hhMember17m.total, hhMember17f.total,
            hhMember35m.total, hhMember35f.total,
            hhMember64m.total, hhMember64f.total,
            hhMember65m.total, hhMember65f.total
        )
        form3.householdSize = totalList.sum()
        addReportForm3(form3)

        val form4 = HhForm4()
        val photoData = PhotoData()
        photoData.imgPath = beneficiary.biometrics[0].biometricUrl
        photoData.img = beneficiary.biometrics[0].biometricData
        Log.d(TAG, "generateReportFromBeneficiary() called with: beneficiary = $beneficiary")
        photoData.userType = beneficiary.biometrics[0].biometricUserType.name
        form4.photoData = photoData

        addReportForm4(form4, "V")

        val form5 = HhForm5()
        val fingers: MutableList<Finger> =
            mutableListOf() // Use MutableList for easier modification
        for (item in beneficiary.biometrics) {
            if (item.biometricData != null) {
                val finger = Finger().apply {
                    fingerId = item.applicationId
                    fingerType = item.biometricType.name
                    fingerPrint = item.biometricData
                    userType = item.biometricUserType?.value
                    noFingerprint = item.noFingerPrint
                    noFingerprintReason = item.noFingerprintReason?.value
                }
                fingers.add(finger)
            }
        }
        form5.fingers = fingers
        addReportForm5(form5)

        // Assuming you have a MutableList<com.kit.integrationmanager.model.Nominee?>!
        if (beneficiary.nominees.isNullOrEmpty()) {

        } else {
            val form6 = HhForm6()
            val mutableList: MutableList<Nominee?> = beneficiary.nominees
            // Convert MutableList to ArrayList
            val arrayList: ArrayList<Nominee> = ArrayList(mutableList.filterNotNull())
            val newlist = ArrayList<com.xplo.code.ui.dashboard.model.Nominee>()
            for (item in arrayList) {
                val nominee = com.xplo.code.ui.dashboard.model.Nominee()
                nominee.firstName = item.nomineeFirstName
                nominee.middleName = item.nomineeMiddleName
                nominee.lastName = item.nomineeLastName
                nominee.nickName = item.nomineeNickName
                nominee.relation = item.relationshipWithHouseholdHead?.value
                if (nominee.relation.equals(RelationshipEnum.OTHER.value)) {
                    nominee.relationOthers = item.relationshipOther
                }
                nominee.age = item.nomineeAge
                nominee.gender = item.nomineeGender?.value
                nominee.occupation = item.nomineeOccupation?.value
                if (nominee.occupation.equals(OccupationEnum.OTHER.value, ignoreCase = true)) {
                    nominee.occupationOthers = item.otherOccupation
                }
                nominee.isReadWrite = item.isReadWrite.toString()
                newlist.add(nominee)
            }
            form6.nominees = newlist
            addReportForm6(form6)
        }
        val arrayListValue: ArrayList<AlternateForm> = ArrayList<AlternateForm>()

        try {
            val alternateForm = AlternateForm()

            if (beneficiary.alternatePayee1.payeeFirstName.isNotEmpty()) {
                val a1Form = AlForm1()
                val a2Form = AlForm2()
                a1Form.alternateFirstName =
                    beneficiary.alternatePayee1?.payeeFirstName
                a1Form.alternateMiddleName =
                    beneficiary.alternatePayee1?.payeeMiddleName
                a1Form.alternateLastName = beneficiary.alternatePayee1?.payeeLastName
                a1Form.alternateNickName = beneficiary.alternatePayee1?.payeeNickName
                a1Form.age = beneficiary.alternatePayee1?.payeeAge
                a1Form.idNumber = beneficiary.alternatePayee1?.nationalId
                a1Form.idNumberType = beneficiary.alternatePayee1?.documentType?.name
                if (a1Form.idNumberType.equals(DocumentTypeEnum.OTHER.value, ignoreCase = true)) {
                    a1Form.documentTypeOther =
                        beneficiary.alternatePayee1?.documentTypeOther
                }
                //  alternateForm.form1?. idIsOrNot=beneficiary.alternatePayee1.
                alternateForm.form1?.phoneNumber = beneficiary.alternatePayee1?.payeePhoneNo
                //    alternateForm.form1?. selectAlternateRlt=beneficiary.alternatePayee1.
                a1Form.gender = beneficiary.alternatePayee1?.payeeGender?.value
                a1Form.selectAlternateRlt =
                    beneficiary.alternatePayee1?.relationshipWithHouseholdHead?.value
                if (a1Form.selectAlternateRlt.equals(
                        RelationshipEnum.OTHER.value,
                        ignoreCase = true
                    )
                ) {
                    a1Form.relationOther = beneficiary.alternatePayee1?.relationshipOther
                }
                // alternateForm.form2?.photoData=beneficiary.alternatePayee1.biometrics[0].biometricData
                val phdata = PhotoData()
                phdata.imgPath = beneficiary.alternatePayee1.biometrics[0].biometricUrl
                phdata.img = beneficiary.alternatePayee1.biometrics[0].biometricData
                phdata.userType = beneficiary.alternatePayee1.biometrics[0].biometricUserType.name
                a2Form.photoData = phdata
                alternateForm.form1 = a1Form
                alternateForm.form2 = a2Form
                alternateForm.appId = beneficiary.applicationId
//                alternateForm.hhType = "V"
                arrayListValue.add(alternateForm)
            }
        } catch (ex: Exception) {

        }


        try {
            if (beneficiary.alternatePayee2.payeeFirstName.isNotEmpty()) {
                val alternateForm2 = AlternateForm()
                val a1Form = AlForm1()
                val a2Form = AlForm2()
                a1Form.alternateFirstName =
                    beneficiary.alternatePayee2.payeeFirstName
                a1Form.alternateMiddleName =
                    beneficiary.alternatePayee2.payeeMiddleName
                a1Form.alternateLastName = beneficiary.alternatePayee2.payeeLastName
                a1Form.alternateNickName = beneficiary.alternatePayee2.payeeNickName
                a1Form.age = beneficiary.alternatePayee2.payeeAge
                a1Form.idNumber = beneficiary.alternatePayee2.nationalId
                a1Form.idNumberType = beneficiary.alternatePayee1?.documentType?.name
                if (a1Form.idNumberType.equals(DocumentTypeEnum.OTHER.value, ignoreCase = true)) {
                    a1Form.documentTypeOther =
                        beneficiary.alternatePayee1?.documentTypeOther
                }
                a1Form.selectAlternateRlt =
                    beneficiary.alternatePayee1?.relationshipWithHouseholdHead?.value
                if (a1Form.selectAlternateRlt.equals(
                        RelationshipEnum.OTHER.value,
                        ignoreCase = true
                    )
                ) {
                    a1Form.relationOther = beneficiary.alternatePayee1?.relationshipOther
                }
                a1Form.phoneNumber = beneficiary.alternatePayee2.payeePhoneNo
                //  alternateForm2.form1?. selectAlternateRlt=beneficiary.alternatePayee1.
                a1Form.gender = beneficiary.alternatePayee2.payeeGender.name
                //alternateForm.form2?.photoData=beneficiary.alternatePayee1.biometrics[0].biometricData
                val phdata = PhotoData()
                phdata.imgPath = beneficiary.alternatePayee2.biometrics[0].biometricUrl
                phdata.img = beneficiary.alternatePayee2.biometrics[0].biometricData
                phdata.userType = beneficiary.alternatePayee2.biometrics[0].biometricUserType.name
                a2Form.photoData = phdata
                alternateForm2.form1 = a1Form
                alternateForm2.form2 = a2Form
                alternateForm2.appId = beneficiary.applicationId
//                alternateForm2.hhType = "V"
                arrayListValue.add(alternateForm2)
            }

        } catch (ex: Exception) {

        }

        addReportAlternateForView(arrayListValue)


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

    private fun addReportForm4(form: HhForm4?, type: String?) {
        if (form == null) return
        if (form.photoData?.img?.isEmpty() == true) return
        if (type == "V") {
            binding.viewPreview.ivAvatar.loadImage(form.photoData?.img)
        } else {
            binding.viewPreview.ivAvatar.loadAvatar(form.photoData?.imgPath)
        }
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

        for (item in form.alternates) {
            val view = getAltRowView(item)
            binding.viewPreview.blockAlternate.addView(view)
        }

    }

    private fun addReportAlternateForView(form: ArrayList<AlternateForm>?) {
//        Log.d(
//            TAG,
//            "addReportAlternateForView() called with: form = ${form?.get(0)?.form1?.alternateLastName}"
//        )
        if (form == null) return

        for (item in form) {
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