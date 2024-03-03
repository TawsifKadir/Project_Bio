package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.databind.ObjectMapper
import com.kit.integrationmanager.model.AlternatePayee
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.model.BiometricType
import com.kit.integrationmanager.model.BiometricUserType
import com.kit.integrationmanager.model.HouseholdMember
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.kit.integrationmanager.model.SelectionReasonEnum
import com.xplo.code.BuildConfig
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.room.dao.BeneficiaryTransactionDao
import com.xplo.code.data.db.room.database.BeneficiaryDatabase
import com.xplo.code.data.db.room.database.DatabaseExecutors
import com.xplo.code.data.db.room.model.Address
import com.xplo.code.data.db.room.model.Alternate
import com.xplo.code.data.db.room.model.Biometric
import com.xplo.code.data.db.room.model.HouseholdInfo
import com.xplo.code.data.db.room.model.Location
import com.xplo.code.data.db.room.model.Nominee
import com.xplo.code.data.db.room.model.SelectionReason
import com.xplo.code.data.mapper.EntityMapper
import com.xplo.code.data.mapper.FakeMapperValue
import com.xplo.code.data_module.core.Resource
import com.xplo.code.databinding.FragmentHhPreviewBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
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
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.utils.DialogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.UUID

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class HhPreviewFragment : BaseFragment(), HouseholdContract.PreviewView {

    companion object {
        const val TAG = "HhPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhPreviewFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhPreviewFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhPreviewBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null

    var uuid: String = ""
    var mDatabase: BeneficiaryDatabase? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is HouseholdContract.View) {
            interactor = activity as HouseholdContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHhPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = RegistrationPresenter(DataRepoImpl())
        //presenter.attach(this)
    }

    override fun initView() {
        val rootForm = interactor?.getRootForm()
        Log.d(TAG, "initView: $rootForm")
        //binding.tvDetails.text = rootForm.toJson()

        generateReport(rootForm)

    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

//                    is HouseholdViewModel.Event.SaveHouseholdFormSuccess -> {
//                        hideLoading()
//                        onSaveSuccess(event.id)
//                        viewModel.clearEvent()
//                    }
//
//                    is HouseholdViewModel.Event.SaveHouseholdFormFailure -> {
//                        hideLoading()
//                        onSaveFailure(event.msg)
//                        viewModel.clearEvent()
//                    }

                    is HouseholdViewModel.Event.SaveFormPEntitySuccess -> {
                        hideLoading()
                        onSaveSuccess(event.id)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.SaveFormPEntityFailure -> {
                        hideLoading()
                        onSaveFailure(event.msg)
                        viewModel.clearEvent()
                    }


                    else -> Unit
                }
            }
        }


        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Preview")

        binding.viewButtonBackNext.btBack.visible()
        binding.viewButtonBackNext.btNext.visible()
        binding.viewButtonBackNext.btNext.text = "Save"
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onGetCompleteData(data: HouseholdForm?) {
        Log.d(TAG, "onGetCompleteData() called with: data = $data")
    }

    override fun onSaveSuccess(id: String?) {
        Log.d(TAG, "onSaveSuccess() called with: id = $id")

        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle("Data Saved")
            .setMessage("Household successfully saved. Do you want register another household?")
            .setPosButtonText("yes")
            .setNegButtonText(getString(R.string.no))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToAnotherHousehold(interactor?.getRootForm()?.form1)
                }

                override fun onClickNegativeButton() {
                    requireActivity().finish()

                }

                override fun onClickNeutralButton() {

                }
            })
            .build()
            .show()

//        XDialog.Builder(requireActivity().supportFragmentManager)
//            .setLayoutId(R.layout.custom_dialog_pnn)
//            .setTitle(getString(R.string.review_complete_reg))
//            .setMessage(getString(R.string.review_complete_reg_msg))
//            .setPosButtonText("Alternate Registration")
//            .setNegButtonText(getString(R.string.home))
//            .setNeuButtonText("Household Registration")
//            .setThumbId(R.drawable.ic_logo_photo)
//            .setCancelable(false)
//            .setListener(object : XDialog.DialogListener {
//                override fun onClickPositiveButton() {
//                    requireActivity().finish()
//                    interactor?.navigateToAlternate(id)
//                }
//
//                override fun onClickNegativeButton() {
//                    requireActivity().finish()
//
//                }
//
//                override fun onClickNeutralButton() {
//                    //interactor?.navigateToHousehold()
//                    interactor?.navigateToAnotherHousehold(interactor?.getRootForm()?.form1)
//                }
//            })
//            .build()
//            .show()
    }

    override fun onSaveFailure(msg: String?) {
        Log.d(TAG, "onSaveFailure() called with: msg = $msg")
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()


        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.review_complete_reg))
            .setMessage(getString(R.string.review_complete_reg_msg))
            .setPosButtonText("Save")
            .setNegButtonText(getString(R.string.cancel))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(true)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    val rootForm = interactor?.getRootForm()
                    //viewModel.saveHouseholdFormAsHouseholdItem(rootForm)
                    //viewModel.saveFormPEntity(rootForm)
                    mDatabase = BeneficiaryDatabase.getInstance(requireContext())
                    val entity = EntityMapper.toBeneficiaryModelEntity(rootForm)
                    if (entity != null) {
                        insertBeneficiary(entity)
                    }

                }

                override fun onClickNegativeButton() {

                }

                override fun onClickNeutralButton() {

                }
            })
            .build()
            .show()


    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")
    }

    override fun onLongClickDataGeneration() {
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isLongClickDGEnabled) return

        binding.viewButtonBackNext.btNext.setOnLongClickListener {
            onGenerateDummyInput()
            return@setOnLongClickListener true
        }
    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")
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
//        binding.viewPreview.llAlternate.visibility = View.GONE
//        if((form.form6?.nominees?.size ?: 0) != 0){
//            addReportForm6(form.form6)
//        }else{
//            binding.viewPreview.llNominee.visibility = View.GONE
//        }
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
        if (form == null) return
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


    private fun getRowView(item: ReportRow?): View {
        Log.d(TAG, "getRowView() called with: item = $item")
        return ReportViewUtils.getRowView(requireContext(), layoutInflater, item)
    }

    private fun getAltRowView(item: AlternateForm?): View {
        Log.d(FormDetailsFragment.TAG, "getAltRowView() called with: item = $item")
        return ReportViewUtils.getAltFormView(requireContext(), layoutInflater, item)
    }

    fun insertBeneficiary(beneficiaryBO: Beneficiary) {
        Log.d(TAG, "beneficiaryBO applicationId: = ${beneficiaryBO.applicationId}")
        Log.d(TAG, "beneficiaryBO applicationId: = ${beneficiaryBO}")
        try {
            DatabaseExecutors.getInstance().diskIO().execute {
                // uuid = UUID.randomUUID()
                uuid = beneficiaryBO.applicationId
                val beneficiaryEO: com.xplo.code.data.db.room.model.Beneficiary =
                    prepareBeneficiaryEntity(uuid.toString(), beneficiaryBO)
                val addressEO: Address =
                    prepareAddressEntity(uuid.toString(), beneficiaryBO.address)
                val locationEO: Location =
                    prepareLocationEntity(uuid.toString(), beneficiaryBO.location)
                val empList = FakeMapperValue.selectionReasons
//                val empList = listOf(
//                    beneficiaryBO.selectionReason
//                )
//                val selectionReasonList: List<SelectionReason> =
//                    prepareSelectionReasonEntity(uuid.toString(), empList)

                val selectionReasonList: List<SelectionReason> =
                    prepareSelectionReasonEntity(uuid.toString(), empList)

                val alternateList: MutableList<Alternate> =
                    ArrayList<Alternate>()
                if(beneficiaryBO.alternatePayee1 != null){
                    val firstAlternateEO: Alternate =
                        prepareAlternateEntity(uuid.toString(), beneficiaryBO.alternatePayee1)
                    alternateList.add(firstAlternateEO)
                }
                if(beneficiaryBO.alternatePayee2 != null){
                    val secondAlternateEO: Alternate =
                        prepareAlternateEntity(uuid.toString(), beneficiaryBO.alternatePayee2)
                    alternateList.add(secondAlternateEO)
                }

                var nomineeList: List<Nominee> = ArrayList<Nominee>()
                if(beneficiaryBO.nominees != null){
                    nomineeList = prepareNomineeEntity(uuid.toString(), beneficiaryBO.nominees)
                }

                val householdInfoList: MutableList<HouseholdInfo> =
                    ArrayList<HouseholdInfo>()
                val householdInfo2EO: HouseholdInfo =
                    prepareHouseholdInfoEntity(uuid.toString(), beneficiaryBO.householdMember2)
                householdInfoList.add(householdInfo2EO)
                val householdInfo5EO: HouseholdInfo =
                    prepareHouseholdInfoEntity(uuid.toString(), beneficiaryBO.householdMember5)
                householdInfoList.add(householdInfo5EO)
                val householdInfo17EO: HouseholdInfo =
                    prepareHouseholdInfoEntity(uuid.toString(), beneficiaryBO.householdMember17)
                householdInfoList.add(householdInfo17EO)
                val householdInfo35EO: HouseholdInfo =
                    prepareHouseholdInfoEntity(uuid.toString(), beneficiaryBO.householdMember35)
                householdInfoList.add(householdInfo35EO)
                val householdInfo64EO: HouseholdInfo =
                    prepareHouseholdInfoEntity(uuid.toString(), beneficiaryBO.householdMember64)
                householdInfoList.add(householdInfo64EO)
                val householdInfo65EO: HouseholdInfo =
                    prepareHouseholdInfoEntity(uuid.toString(), beneficiaryBO.householdMember65)
                householdInfoList.add(householdInfo65EO)

                val biometricList: MutableList<Biometric> =
                    ArrayList<Biometric>()
                if (beneficiaryBO.biometrics != null) {
                    val beneficiaryBiometric: Biometric =
                        prepareBiometricEntity(uuid.toString(), beneficiaryBO.biometrics)
                    biometricList.add(beneficiaryBiometric)
                }
                if (beneficiaryBO.alternatePayee1 != null && beneficiaryBO.alternatePayee1
                        .biometrics != null
                ) {
                    val alternate1Biometric: Biometric = prepareBiometricEntity(
                        uuid.toString(),
                        beneficiaryBO.alternatePayee1.biometrics
                    )
                    biometricList.add(alternate1Biometric)
                }
                if (beneficiaryBO.alternatePayee2 != null && beneficiaryBO.alternatePayee2
                        .biometrics != null
                ) {
                    val alternate2Biometric: Biometric = prepareBiometricEntity(
                        uuid.toString(),
                        beneficiaryBO.alternatePayee2.biometrics
                    )
                    biometricList.add(alternate2Biometric)
                }
                val beneficiaryTransactionDao: BeneficiaryTransactionDao =
                    mDatabase!!.beneficiaryTransactionDao()
                beneficiaryTransactionDao.insertBeneficiaryRecord(
                    beneficiaryEO, addressEO, locationEO,
                    biometricList,
                    householdInfoList, alternateList, nomineeList, selectionReasonList
                )
                Log.d(TAG, "Inserted the beneficiary data")
                onSaveSuccess(null)
                //DialogUtil.showLottieDialogSuccessMsg(requireContext(), "Success", "Inserted the beneficiary data")
            }
        } catch (ex: Exception) {
            DialogUtil.showLottieDialogSuccessMsg(requireContext(), "Failed", "Error while sending data")
            Log.e(TAG, "Error while sending data : " + ex.message)
            ex.printStackTrace()
        }
        }

    fun prepareBiometricEntity(
        appId: String?,
        biometricList: List<com.kit.integrationmanager.model.Biometric?>?
    ): Biometric {
        val nowBiometricEO = Biometric()
        if (biometricList != null) {
            for (nowBiometric in biometricList) {
                if (nowBiometric != null) {
                    nowBiometricEO.applicationId = appId
                    nowBiometricEO.biometricUserType = nowBiometric.biometricUserType.id.toLong()
                    if(nowBiometric.noFingerprintReasonText != null){
                        nowBiometricEO.noFingerprintReasonText = nowBiometric.noFingerprintReasonText
                    }
                    if(nowBiometric.noFingerPrint != null){
                        nowBiometricEO.noFingerPrint = nowBiometric.noFingerPrint
                    }
                    if(nowBiometric.noFingerprintReason != null){
                        nowBiometricEO.noFingerprintReason = nowBiometric.noFingerprintReason.id.toLong()
                    }
                   if(nowBiometric.biometricType == BiometricType.PHOTO && nowBiometric.biometricData != null){
                        nowBiometricEO.photo = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.LT && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqLt = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.LI && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqLi = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.LM && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqLm = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.LR && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqLr = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.LL && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqLs = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.RT && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqRt = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.RI && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqRi = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.RM && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqRm = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.RR && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqRr = nowBiometric.biometricData
                    }else if (nowBiometric.biometricType == BiometricType.RL && nowBiometric.biometricData != null){
                        nowBiometricEO.wsqRs = nowBiometric.biometricData
                    }
                }
            }
        }
        return nowBiometricEO
    }

    fun prepareAddressEntity(
        appId: String?,
        addressBO: com.kit.integrationmanager.model.Address?
    ): Address {
        val addressEO = Address()
        if (addressBO != null) {
            addressEO.applicationId = appId
            //addressEO.setAddressLine(addressBO.getAddressLine());
            addressEO.stateId = addressBO.stateId
            addressEO.countyId = addressBO.countyId
            addressEO.payam = addressBO.payam
            addressEO.boma = addressBO.boma
        }
        return addressEO
    }

    fun prepareLocationEntity(
        appId: String?,
        locationBO: com.kit.integrationmanager.model.Location?
    ): Location {
        val locationEO = Location()
        if (locationBO != null) {
            locationEO.lat = locationBO.lat
            locationEO.lon = locationBO.lon
            locationEO.applicationId = appId
        }
        return locationEO
    }

    fun prepareAlternateEntity(appId: String?, alternateBO: AlternatePayee): Alternate {
        val alternateEO = Alternate()
        alternateEO.applicationId = appId
        alternateEO.payeeFirstName = alternateBO.payeeFirstName
        alternateEO.payeeMiddleName = alternateBO.payeeMiddleName
        alternateEO.payeeLastName = alternateBO.payeeLastName
        alternateEO.payeeNickName = alternateBO.payeeNickName
        alternateEO.payeeGender =
            if (alternateBO.payeeGender != null) alternateBO.payeeGender.ordinal.toLong() else null
        alternateEO.payeeAge = alternateBO.payeeAge
        alternateEO.documentType =
            if (alternateBO.documentType != null) alternateBO.documentType.ordinal.toLong() else null
        alternateEO.documentTypeOther = alternateBO.documentTypeOther
        alternateEO.nationalId = alternateBO.nationalId
        alternateEO.payeePhoneNo = alternateBO.payeePhoneNo
        return alternateEO
    }

    fun prepareNomineeEntity(
        appId: String?,
        nomineeList: List<com.kit.integrationmanager.model.Nominee>?
    ): List<Nominee> {
        val nominees: MutableList<Nominee> = java.util.ArrayList()
        if (nomineeList != null) {
            for (nominee in nomineeList) {
                val nomineeEO = Nominee()
                nomineeEO.applicationId = appId
                nomineeEO.nomineeFirstName = nominee.nomineeFirstName
                nomineeEO.nomineeMiddleName = nominee.nomineeMiddleName
                nomineeEO.nomineeLastName = nominee.nomineeLastName
                nomineeEO.nomineeNickName = nominee.nomineeNickName
                nomineeEO.relationshipWithHouseholdHead =
                    if (nominee.relationshipWithHouseholdHead != null) nominee.relationshipWithHouseholdHead.ordinal.toLong() else null
                nomineeEO.nomineeAge = nominee.nomineeAge
                nomineeEO.nomineeGender =
                    if (nominee.nomineeGender != null) nominee.nomineeGender.ordinal.toLong() else null
                nomineeEO.isReadWrite = nominee.isReadWrite
                nomineeEO.nomineeOccupation =
                    if (nominee.nomineeOccupation != null) nominee.nomineeOccupation.ordinal.toLong() else null
                nomineeEO.otherOccupation = nominee.otherOccupation
                nominees.add(nomineeEO)
            }
        }
        return nominees
    }

    fun prepareHouseholdInfoEntity(appId: String?, member: HouseholdMember?): HouseholdInfo {
        val householdInfoEO = HouseholdInfo()
        if (member != null) {
            householdInfoEO.applicationId = appId
            householdInfoEO.maleTotal = member.totalMale
            householdInfoEO.femaleTotal = member.totalFemale
            householdInfoEO.maleDisable = member.maleDisable
            householdInfoEO.femaleDisable = member.femaleDisable
            householdInfoEO.maleChronicalIll = member.maleChronicalIll
            householdInfoEO.femaleChronicalIll = member.femaleChronicalIll
            householdInfoEO.femaleNormal = member.femaleNormal
        }
        return householdInfoEO
    }

    fun prepareSelectionReasonEntity(
        appId: String,
        reasons:List<SelectionReasonEnum>
    ): List<SelectionReason> {
        //  Log.d(TAG, "Reason List: " + appId + reasons!![0].value)
        val selectionReasons: MutableList<SelectionReason> = java.util.ArrayList()
        if (reasons != null) {
            for (nowReason in reasons) {
                val nowSelectionReason = SelectionReason()
                nowSelectionReason.applicationId = appId
                nowSelectionReason.selectionReasonName = nowReason.value
                selectionReasons.add(nowSelectionReason)
            }
//            if (selectionReasons.size <= 0) {
//                val nowSelectionReason = SelectionReason()
//                nowSelectionReason.applicationId = appId
//                nowSelectionReason.selectionReasonName = SelectionReasonEnum.LIPW_REASON_4.value
//                selectionReasons.add(nowSelectionReason)
//            }
        }
        return selectionReasons
    }

    fun prepareBeneficiaryEntity(
        appId: String?,
        beneficiaryBO: Beneficiary
    ): com.xplo.code.data.db.room.model.Beneficiary {
        val beneficiaryEO = com.xplo.code.data.db.room.model.Beneficiary()
        if (beneficiaryBO != null) {
            beneficiaryEO.applicationId = appId
            beneficiaryEO.respondentFirstName = beneficiaryBO.respondentFirstName
            beneficiaryEO.respondentMiddleName = beneficiaryBO.respondentMiddleName
            beneficiaryEO.respondentLastName = beneficiaryBO.respondentLastName
            beneficiaryEO.respondentNickName = beneficiaryBO.respondentNickName
            beneficiaryEO.spouseFirstName = beneficiaryBO.spouseFirstName
            beneficiaryEO.spouseMiddleName = beneficiaryBO.spouseMiddleName
            beneficiaryEO.spouseLastName = beneficiaryBO.spouseLastName
            beneficiaryEO.spouseNickName = beneficiaryBO.spouseNickName
            beneficiaryEO.relationshipWithHouseholdHead =
                if (beneficiaryBO.relationshipWithHouseholdHead != null) beneficiaryBO.relationshipWithHouseholdHead.ordinal.toLong() else null
            beneficiaryEO.respondentAge = beneficiaryBO.respondentAge
            beneficiaryEO.respondentGender =
                if (beneficiaryBO.respondentGender != null) beneficiaryBO.respondentGender.ordinal.toLong() else null
            beneficiaryEO.respondentMaritalStatus =
                if (beneficiaryBO.respondentMaritalStatus != null) beneficiaryBO.respondentMaritalStatus.ordinal.toLong() else null
            beneficiaryEO.respondentLegalStatus =
                if (beneficiaryBO.respondentLegalStatus != null) beneficiaryBO.respondentLegalStatus.ordinal.toLong() else null
            beneficiaryEO.documentType =
                if (beneficiaryBO.documentType != null) beneficiaryBO.documentType.ordinal.toLong() else null
            beneficiaryEO.documentTypeOther = beneficiaryBO.documentTypeOther
            beneficiaryEO.respondentId = beneficiaryBO.respondentId
            beneficiaryEO.respondentPhoneNo = beneficiaryBO.respondentPhoneNo
            beneficiaryEO.householdIncomeSource =
                if (beneficiaryBO.householdIncomeSource != null) beneficiaryBO.householdIncomeSource.ordinal.toLong() else null
            beneficiaryEO.householdMonthlyAvgIncome = beneficiaryBO.householdMonthlyAvgIncome
            beneficiaryEO.householdSize = beneficiaryBO.householdSize
            beneficiaryEO.isOtherMemberPerticipating = beneficiaryBO.isOtherMemberPerticipating
            beneficiaryEO.isReadWrite = beneficiaryBO.isReadWrite
            beneficiaryEO.memberReadWrite = beneficiaryBO.memberReadWrite
            beneficiaryEO.notPerticipationReason =
                if (beneficiaryBO.notPerticipationReason != null) beneficiaryBO.notPerticipationReason.ordinal.toLong() else null
            beneficiaryEO.notPerticipationOtherReason = beneficiaryBO.notPerticipationOtherReason
            beneficiaryEO.createdBy = beneficiaryBO.createdBy
            beneficiaryEO.selectionCriteria =
                if (beneficiaryBO.selectionCriteria != null) beneficiaryBO.selectionCriteria.ordinal.toLong() else null
            beneficiaryEO.currency =
                if (beneficiaryBO.currency != null) beneficiaryBO.currency.ordinal.toLong() else null
        }
        return beneficiaryEO
    }


}
