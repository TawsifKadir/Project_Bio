package com.xplo.code.ui.dashboard.household.forms

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.viewModels
import com.faisal.fingerprintcapture.FingerprintCaptureActivity
import com.faisal.fingerprintcapture.model.FingerprintData
import com.faisal.fingerprintcapture.model.FingerprintID
import com.faisal.fingerprintcapture.utils.ImageProc
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.toBool
import com.xplo.code.data.mapper.BiometricHelper
import com.xplo.code.databinding.FragmentHhForm5FingerBinding
import com.xplo.code.ui.components.XDialogSheet
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.Finger
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.isOk
import com.xplo.code.utils.FormAppUtils
import com.xplo.data.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/15/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class HhForm5FingerFragment : BasicFormFragment(), HouseholdContract.Form5View {

    companion object {
        const val TAG = "HhForm5FingerFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm5FingerFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm5FingerFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm5FingerBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null

    private var fingerItemsStore = arrayListOf<Finger>()

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
        binding = FragmentHhForm5FingerBinding.inflate(inflater, container, false)
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
        onReinstateData(interactor?.getRootForm()?.form5)
    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        onLongClickDataGeneration()
        onGenerateDummyInput()
        binding.llCapture.setOnClickListener {
            onStartFingerprintCapture()
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Enroll Fingerprints")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm5?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        if (form == null) return
        //showToast(form.toString())

        val rootForm = interactor?.getRootForm()
        rootForm?.form5 = form
        interactor?.setRootForm(rootForm)

        Log.d(TAG, "onValidated: $rootForm")


        if (TestConfig.isAlternateAddInHouseholdFlow) {
            interactor?.navigateToAlternateAddForm()
            return
        }

        if (FormAppUtils.canNomineeAdd(interactor?.getRootForm())) {
            askForConsent()
        } else {
            interactor?.navigateToPreview()
        }

    }

    override fun onReinstateData(form: HhForm5?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")

//        if (form != null) {
//            if (form.fingerData?.fingerRT != null || form.fingerData?.fingerRT == ""){
//                addFingerDrawable(binding.imgRT)
//            }
//            if(form.fingerData?.fingerRI != null || form.fingerData?.fingerRI == "") {
//                addFingerDrawable(binding.imgRI)
//            }
//            if(form.fingerData?.fingerRM != null || form.fingerData?.fingerRM == "") {
//                addFingerDrawable(binding.imgRM)
//            }
//            if(form.fingerData?.fingerRR != null || form.fingerData?.fingerRR == "") {
//                addFingerDrawable(binding.imgRR)
//            }
//            if(form.fingerData?.fingerRL != null || form.fingerData?.fingerRL == "") {
//                addFingerDrawable(binding.imgRL)
//            }
//            if (form.fingerData?.fingerLT != null || form.fingerData?.fingerLT == ""){
//                addFingerDrawable(binding.imgLT)
//            }
//            if(form.fingerData?.fingerLI != null || form.fingerData?.fingerLI == "") {
//                addFingerDrawable(binding.imgLI)
//            }
//            if(form.fingerData?.fingerLM != null || form.fingerData?.fingerLM == "") {
//                addFingerDrawable(binding.imgLM)
//            }
//            if(form.fingerData?.fingerLR != null || form.fingerData?.fingerLR == "") {
//                addFingerDrawable(binding.imgLR)
//            }
//            if(form.fingerData?.fingerLL != null || form.fingerData?.fingerLL == "") {
//                addFingerDrawable(binding.imgLL)
//            }
//        }
        //Toast.makeText(activity, "Received Positive Result From Fingerprint Capture", Toast.LENGTH_LONG).show()
    }

    override fun onGetFingerprintIntent(intent: Intent?) {
        Log.d(TAG, "onGetFingerprintIntent() called with: intent = $intent")
        if (intent == null) return
        //val data = intent.data
        //if (data == null) return

        val fingers = BiometricHelper.fingerPrintIntentToFingerItems(intent, "BENEFICIARY")

        onGetFingerprintData(fingers)

    }

    override fun onGetFingerprintData(items: List<Finger>?) {
        Log.d(TAG, "onGetFingerprintData() called with: items = $items")
        if (items.isNullOrEmpty()) return

        fingerItemsStore.clear()
        fingerItemsStore.addAll(items)

        for (item in items) {
            when (item.fingerId) {
                FingerprintID.RIGHT_THUMB.name -> addFingerDrawable(binding.imgRT)
                FingerprintID.RIGHT_INDEX.name -> addFingerDrawable(binding.imgRI)
                FingerprintID.RIGHT_MIDDLE.name -> addFingerDrawable(binding.imgRM)
                FingerprintID.RIGHT_RING.name -> addFingerDrawable(binding.imgRR)
                FingerprintID.RIGHT_SMALL.name -> addFingerDrawable(binding.imgRL)

                FingerprintID.LEFT_THUMB.name -> addFingerDrawable(binding.imgLT)
                FingerprintID.LEFT_INDEX.name -> addFingerDrawable(binding.imgLI)
                FingerprintID.LEFT_MIDDLE.name -> addFingerDrawable(binding.imgLM)
                FingerprintID.LEFT_RING.name -> addFingerDrawable(binding.imgLR)
                FingerprintID.LEFT_SMALL.name -> addFingerDrawable(binding.imgLL)
            }
        }

    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()

        onReadInput()

    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

//        if (TestConfig.isFingerPrintRequired) {
//            if (fingerItemsStore.isEmpty()) {
//                showAlerter("Warning", "Please Add Fingerprint")
//                return
//            }
//        }

        val form = HhForm5()
        form.fingers = fingerItemsStore

        if (!form.isOk()) {
            showAlerter("Warning", "Please Add Fingerprint")
            return
        }

        onValidated(form)
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

    private fun askForConsent() {

        if (isConsentGiven()) {
            onGetConsent()
            return
        }

        XDialogSheet.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.bsd_consent_sheet)
            .setTitle("Consent Nominee")
            .setMessage(getString(R.string.agreement))
            .setPosButtonText("Yes")
            .setNegButtonText("No")
            .setCancelable(true)
            .setListener(object : XDialogSheet.DialogListener {
                override fun onClickPositiveButton() {
                    onGetConsent()
                }

                override fun onClickNegativeButton() {

                }

                override fun onClickNeutralButton() {

                }
            })
            .build()
            .show()
    }

    private fun onGetConsent() {
        //getPrefHelper().setNomineeConsentAcceptStatus(true)
        interactor?.getRootForm()?.consentStatus?.isConsentGivenNominee = true
        if (FormAppUtils.canNomineeAdd(interactor?.getRootForm())) {
            interactor?.navigateToForm6()
        } else {
            interactor?.navigateToPreview()
        }

    }

    fun isConsentGiven(): Boolean {
        if (!TestConfig.isConsentEnabled) return true
        return interactor?.getRootForm()?.consentStatus?.isConsentGivenNominee.toBool()
    }


    fun onStartFingerprintCapture() {
        Log.d(TAG, "onStartFingerprintCapture() called")

        val intent = Intent(context, FingerprintCaptureActivity::class.java)
        getResult.launch(intent)

    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                onGetFingerprintIntent(it.data)
            } else {
                Toast.makeText(
                    activity,
                    "Received Negative Result From Fingerprint Capture",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun addFingerDrawable(img: ImageView) {
        img.setImageResource(R.drawable.ic_finger_add)
        val color = ContextCompat.getColor(requireContext(), R.color.green) // Your color resource
        ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(color))
    }


}
