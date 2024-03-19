package com.xplo.code.ui.dashboard.alternate.forms

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kit.integrationmanager.model.AlternatePayee
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.model.BiometricType
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.core.ext.toBool
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.data.db.room.dao.AlternateDao
import com.xplo.code.data.db.room.dao.BeneficiaryDao
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
import com.xplo.code.databinding.FragmentAlPreviewBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.HhPreviewFragment
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.AlForm2
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.ReportRow
import com.xplo.code.ui.dashboard.model.getReportRows
import com.xplo.code.ui.dashboard.model.toJson
import com.xplo.code.utils.DialogUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class AlPreviewFragment : BasicFormFragment(), AlternateContract.PreviewView {

    companion object {
        const val TAG = "AlPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): AlPreviewFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlPreviewFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlPreviewBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AlternateContract.View) {
            interactor = activity as AlternateContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlPreviewBinding.inflate(inflater, container, false)
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

        generateReport(rootForm)
    }


    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.UpdateHouseholdItemSuccess -> {
                        hideLoading()
                        onUpdateSuccess(event.id)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.UpdateHouseholdItemFailure -> {
                        hideLoading()
                        onUpdateFailure(event.msg)
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

        setToolbarTitle("Alternate Fingerprints")

        binding.viewButtonBackNext.btBack.visible()
        binding.viewButtonBackNext.btNext.visible()
        binding.viewButtonBackNext.btNext.text = "Save"

        if (interactor?.isCallForResult().toBool()) {
            binding.viewButtonBackNext.btNext.text = "Add this"
        }
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


    override fun onValidated(form: AlternateForm) {
        Log.d(TAG, "onValidated() called with: form = $form")

    }

    override fun onPublishResult() {
        Log.d(TAG, "onPublishResult() called")

        val rootForm = interactor?.getRootForm()

        val resultIntent = Intent()
        //resultIntent.putExtra("result", "result")
        val bundle = Bundle()
        bundle.putSerializable(Bk.KEY_ITEM, rootForm)
        resultIntent.putExtras(bundle)
        requireActivity().setResult(RESULT_OK, resultIntent)
        requireActivity().finish()

    }

    override fun onUpdateSuccess(id: String?) {
        Log.d(TAG, "onUpdateSuccess() called with: id = $id")
        showToast("update success")
        navigateToHome()

//        XDialog.Builder(requireActivity().supportFragmentManager)
//            .setLayoutId(R.layout.custom_dialog_pnn)
//            .setTitle("Alternate")
//            .setMessage("Do you want to register another alternate?")
//            .setPosButtonText("Another Alternate")
//            .setNegButtonText(getString(R.string.cancel))
//            //.setNeuButtonText(getString(R.string.alternate_reg_title))
//            .setThumbId(R.drawable.logo_splash)
//            .setCancelable(false)
//            .setListener(object : XDialog.DialogListener {
//                override fun onClickPositiveButton() {
//                    interactor?.navigateToAlternateHome()
//                }
//
//                override fun onClickNegativeButton() {
//                    requireActivity().finish()
//                    //interactor?.navigateToHome()
//                }
//
//                override fun onClickNeutralButton() {
//                    //interactor?.navigateToAlternate(id)
//                }
//            })
//            .build()
//            .show()
    }

    override fun onUpdateFailure(msg: String?) {
        Log.d(TAG, "onUpdateFailure() called with: msg = $msg")

    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")

        if (interactor?.isCallForResult().toBool()) {
            onPublishResult()
            return
        }

        val rootForm = interactor?.getRootForm()
        if (rootForm == null) {
            return
        }

        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.alternate_reg))
            .setMessage(getString(R.string.review_complete_reg_msg))
            .setPosButtonText("Complete Registration")
            .setNeuButtonText("Add Another")
            .setNegButtonText(getString(R.string.cancel))
            .setThumbId(R.drawable.logo_splash)
            .setCancelable(true)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    val entity = EntityMapper.toAlternateModelEntity(rootForm)
                    if (entity != null) {
                        insertAlternate(entity, entity.applicationId, 1)
                    }

                    navigateToHome()
                }

                override fun onClickNegativeButton() {
                    navigateToHome()
                }

                override fun onClickNeutralButton() {
                    val entity = EntityMapper.toAlternateModelEntity(rootForm)
                    if (entity != null) {
                        insertAlternate(entity, entity.applicationId, 0)
                    }
                    navigateToHome()
                }
            })
            .build()
            .show()


        // var hhItem = interactor?.getHouseholdItem()
        //   if (hhItem == null) return

        //   val hhForm = hhItem.toHouseholdForm()
        //    hhForm?.alternates?.add(rootForm)
        //   hhItem.data = hhForm.toJson()


//        if (rootForm.hhType == "V") {
//
//
//
////            val entity = EntityMapper.toAlternateModelEntity(rootForm)
////            if (entity != null) {
////                insertAlternate(entity, entity.applicationId)
////            }
//
//        }

        //   viewModel.updateHouseholdItem(hhItem)


    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

    }

    override fun onLongClickDataGeneration() {

    }

    override fun onGenerateDummyInput() {

    }

    override fun onPopulateView() {

    }


    private fun generateReport(form: AlternateForm?) {
        Log.d(TAG, "generateReport() called with: form = $form")
        if (form == null) return
        addReportForm1(form.form1)
        addReportForm2(form.form2)
        addReportForm3(form.form3)
    }

    private fun addReportForm1(form: AlForm1?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockPerInfo.addView(view)
        }
    }

    private fun addReportForm2(form: AlForm2?) {
        if (form == null) return
        binding.viewPreview.ivAvatar.loadAvatar(form.photoData?.imgPath)
    }

    private fun addReportForm3(form: AlForm3?) {
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockFinger.addView(view)
        }
    }

    private fun getRowView(item: ReportRow?): View {
        //Log.d(TAG, "getRowView() called with: item = $item")
        return ReportViewUtils.getRowView(requireContext(), layoutInflater, item)
    }

    fun insertAlternate(beneficiaryBO: Beneficiary, appId: String, applicationStatus: Int) {
        Log.d(TAG, "insertAlternate() called with: beneficiaryBO = $beneficiaryBO, appId = $appId")

        try {
            DatabaseExecutors.getInstance().diskIO().execute {
                val mDatabase = BeneficiaryDatabase.getInstance(requireContext())
                val alternateDao: AlternateDao = mDatabase.alternateDao()
                val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()

                val alternate = alternateDao.getAlternateList(appId)
                var type = ""
                if (alternate.size == 0) {
                    type = "ALT1"
                    val bene =
                        beneficiaryDao.updateBeneficiaryByAppIdAndAppStatus(
                            appId,
                            applicationStatus
                        )
                } else if (alternate.size == 1) {
                    type = "ALT2"
                    val bene =
                        beneficiaryDao.updateBeneficiaryByAppIdAndAppStatus(appId, 1)
                }

                val alternateList: MutableList<Alternate> =
                    ArrayList<Alternate>()

                //  if (alternate.size == 0) {
                if (beneficiaryBO.alternatePayee1 != null) {
                    val firstAlternateEO: Alternate =
                        prepareAlternateEntity(
                            appId,
                            beneficiaryBO.alternatePayee1,
                            type
                        )
                    alternateList.add(firstAlternateEO)
                }
                //   } else if (alternate.size == 1) {
                if (beneficiaryBO.alternatePayee2 != null) {
                    val secondAlternateEO: Alternate =
                        prepareAlternateEntity(
                            appId,
                            beneficiaryBO.alternatePayee2,
                            type
                        )
                    alternateList.add(secondAlternateEO)
                    //     }
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        "Maximum 2 Alternate Add Permission.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }


                val biometricList: MutableList<Biometric> =
                    ArrayList<Biometric>()
                //   if (alternate.size == 0) {
                if (beneficiaryBO.alternatePayee1 != null && beneficiaryBO.alternatePayee1
                        .biometrics != null
                ) {
                    val alternate1Biometric: Biometric = prepareBiometricEntity(
                        appId,
                        beneficiaryBO.alternatePayee1.biometrics, type
                    )
                    biometricList.add(alternate1Biometric)
                }
                //   } else if (alternate.size == 1) {
                if (beneficiaryBO.alternatePayee2 != null && beneficiaryBO.alternatePayee2
                        .biometrics != null
                ) {
                    val alternate2Biometric: Biometric = prepareBiometricEntity(
                        appId,
                        beneficiaryBO.alternatePayee2.biometrics, type
                    )
                    biometricList.add(alternate2Biometric)
                }
                // }


                val beneficiaryTransactionDao: BeneficiaryTransactionDao =
                    mDatabase!!.beneficiaryTransactionDao()
                beneficiaryTransactionDao.insertAlternateRecord(
                    biometricList, alternateList
                )
                Log.d(TAG, "Inserted the Alternate data")
                // onSaveSuccess(null)
                //DialogUtil.showLottieDialogSuccessMsg(requireContext(), "Success", "Inserted the beneficiary data")
                //   mDatabase.close()
            }
        } catch (ex: Exception) {
            DialogUtil.showLottieDialogSuccessMsg(
                requireContext(),
                "Failed",
                "Error while sending data"
            )
            Log.e(TAG, "Error while sending data : " + ex.message)
            ex.printStackTrace()
        }
    }

    fun prepareAlternateEntity(
        appId: String?,
        alternateBO: AlternatePayee,
        type: String
    ): Alternate {
        Log.d(
            TAG,
            "prepareAlternateEntity() called with: appId = $appId, alternateBO = $alternateBO, type = $type"
        )
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
            if (alternateBO.documentType != null) alternateBO.documentType.ordinal.toLong() else 3
        alternateEO.documentTypeOther = alternateBO.documentTypeOther
        alternateEO.nationalId = alternateBO.nationalId
        alternateEO.payeePhoneNo = alternateBO.payeePhoneNo
        alternateEO.type = type
        alternateEO.relationshipWithHousehold = alternateBO.relationshipWithHouseholdHead.name
        alternateEO.relationshipOther = alternateBO.relationshipOther
        return alternateEO
    }

    fun prepareBiometricEntity(
        appId: String?,
        biometricList: List<com.kit.integrationmanager.model.Biometric?>?,
        type: String
    ): Biometric {
        Log.d(
            TAG,
            "prepareBiometricEntity() called with: appId = $appId, biometricList = $biometricList, type = $type"
        )
        val nowBiometricEO = Biometric()
        if (biometricList != null) {
            for (nowBiometric in biometricList) {
                if (nowBiometric != null) {
                    nowBiometricEO.applicationId = appId
                    nowBiometricEO.type = type
                    //  nowBiometricEO.biometricUserType = nowBiometric.biometricUserType.id.toLong()
                    nowBiometricEO.biometricUserType = nowBiometric.biometricUserType?.id?.toLong()
                    if (nowBiometric.noFingerprintReasonText != null) {
                        nowBiometricEO.noFingerprintReasonText =
                            nowBiometric.noFingerprintReasonText
                    }
                    if (nowBiometric.noFingerPrint != null) {
                        nowBiometricEO.noFingerPrint = nowBiometric.noFingerPrint
                    }
                    if (nowBiometric.noFingerprintReason != null) {
                        nowBiometricEO.noFingerprintReason =
                            nowBiometric.noFingerprintReason.id.toLong()
                    }
                    if (nowBiometric.biometricType == BiometricType.PHOTO && nowBiometric.biometricData != null) {
                        nowBiometricEO.photo = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LT && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLt = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LI && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLi = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LM && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLm = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LR && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLr = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LL && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLs = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RT && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRt = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RI && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRi = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RM && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRm = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RR && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRr = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RL && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRs = nowBiometric.biometricData
                    }
                }
            }
        }
        return nowBiometricEO
    }


}
