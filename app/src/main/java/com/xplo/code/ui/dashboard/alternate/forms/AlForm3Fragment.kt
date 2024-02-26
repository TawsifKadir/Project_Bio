package com.xplo.code.ui.dashboard.alternate.forms

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.viewModels
import com.faisal.fingerprintcapture.FingerprintCaptureActivity
import com.faisal.fingerprintcapture.model.FingerprintID
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.data.mapper.BiometricHelper
import com.xplo.code.databinding.FragmentAlForm3FingerBinding
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.HhForm5FingerFragment
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.code.ui.dashboard.model.Finger
import com.xplo.code.ui.dashboard.model.isContainValidFingerprint
import com.xplo.code.ui.dashboard.model.isOk
import com.xplo.data.BuildConfig
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlForm3Fragment : BasicFormFragment(), AlternateContract.Form3View {

    companion object {
        const val TAG = "AlForm3Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): AlForm3Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlForm3Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlForm3FingerBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null

    private var fingerItemsStore: List<Finger>? = listOf<Finger>()
    private var noFingerprintReasonStore: String? = null



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
        binding = FragmentAlForm3FingerBinding.inflate(inflater, container, false)
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
        onReinstateData(interactor?.getRootForm()?.form3)
    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        binding.llCapture.setOnClickListener {
            onStartFingerprintCapture()
        }

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }

    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Alternate Fingerprints")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()
//        binding.viewButtonBackNext.btNext.text = "Submit"


    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


    override fun onValidated(form: AlForm3?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        if (form == null) return
        //showToast(form.toString())

        val rootForm = interactor?.getRootForm()
        rootForm?.form3 = form
        interactor?.setRootForm(rootForm)

        Log.d(TAG, "onValidated: $rootForm")

        interactor?.navigateToPreview()

    }

    override fun onReinstateData(form: AlForm3?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")
        if (form == null) return
        val fingers = form.fingers
        //if (fingers.isEmpty()) return

        this.noFingerprintReasonStore = form.noFingerprintReason
        //fingerItemsStore.clear()
        this.fingerItemsStore = fingers

        onRefreshFingerprints(fingers)

    }

    override fun onStartFingerprintCapture() {
        Log.d(TAG, "onStartFingerprintCapture() called")

        val intent = Intent(context, FingerprintCaptureActivity::class.java)
        getResult.launch(intent)
    }


    override fun onGetFingerprintIntent(intent: Intent?) {
        Log.d(TAG, "onGetFingerprintIntent() called with: intent = $intent")
        if (intent == null) return
        //val data = intent.data
        //if (data == null) return

        val reason = BiometricHelper.fingerPrintIntentToNoFingerprintReason(intent, "ALTERNATE")
        val fingers = BiometricHelper.fingerPrintIntentToFingerItems(intent, "ALTERNATE")


        onGetFingerprintData(fingers, reason)

    }

    override fun onGetFingerprintData(items: List<Finger>?, noFingerprintReason: String?) {
        Log.d(
            TAG,
            "onGetFingerprintData() called with: items = $items, noFingerprintReason = $noFingerprintReason"
        )
        //if (items.isNullOrEmpty()) return

        //fingerItemsStore.clear()
        this.fingerItemsStore = items
        this.noFingerprintReasonStore = noFingerprintReason

        onRefreshFingerprints(items)

    }

    override fun onRefreshFingerprints(items: List<Finger>?) {
        Log.d(TAG, "onRefreshFingerprints() called with: items = $items")
        if (items.isNullOrEmpty()) return

        for (item in items) {
            when (item.fingerId) {
                FingerprintID.RIGHT_THUMB.name -> onRefreshFingerDrawable(binding.imgRT, item)
                FingerprintID.RIGHT_INDEX.name -> onRefreshFingerDrawable(binding.imgRI, item)
                FingerprintID.RIGHT_MIDDLE.name -> onRefreshFingerDrawable(binding.imgRM, item)
                FingerprintID.RIGHT_RING.name -> onRefreshFingerDrawable(binding.imgRR, item)
                FingerprintID.RIGHT_SMALL.name -> onRefreshFingerDrawable(binding.imgRL, item)

                FingerprintID.LEFT_THUMB.name -> onRefreshFingerDrawable(binding.imgLT, item)
                FingerprintID.LEFT_INDEX.name -> onRefreshFingerDrawable(binding.imgLI, item)
                FingerprintID.LEFT_MIDDLE.name -> onRefreshFingerDrawable(binding.imgLM, item)
                FingerprintID.LEFT_RING.name -> onRefreshFingerDrawable(binding.imgLR, item)
                FingerprintID.LEFT_SMALL.name -> onRefreshFingerDrawable(binding.imgLL, item)
            }
        }
    }

    override fun onRefreshFingerDrawable(img: ImageView, finger: Finger?) {
        Log.d(TAG, "onRefreshFingerDrawable() called with: img = $img, finger = $finger")
        if (finger == null) return

        if (finger.isContainValidFingerprint()) {
            img.setImageResource(R.drawable.ic_finger_add)
            val color = ContextCompat.getColor(requireContext(), R.color.green) // Your color resource
            ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(color))
        } else {
            img.setImageResource(R.drawable.ic_finger_minus)
            val color = ContextCompat.getColor(requireContext(), R.color.li_waring) // Your color resource
            ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(color))
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

        val form = AlForm3()
        if (fingerItemsStore != null) {
            form.fingers = this.fingerItemsStore!!
        }
        form.noFingerprintReason = this.noFingerprintReasonStore

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

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                onGetFingerprintIntent(it.data)
            } else {
                Log.d(TAG, "Received Negative Result From Fingerprint Capture")
                showToast("Received Negative Result From Fingerprint Capture")
            }
        }


}
