package com.xplo.code.ui.dashboard.alternate.forms

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.databinding.FragmentAlForm3FingerBinding
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.data.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

import com.faisal.fingerprintcapture.FingerprintCaptureActivity
import com.faisal.fingerprintcapture.model.FingerprintData
import com.faisal.fingerprintcapture.model.FingerprintID
import com.faisal.fingerprintcapture.utils.ImageProc
import com.xplo.code.ui.dashboard.model.FingerData


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
    private var fingerprintTotalEnroll = 0

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

        onReinstateData(interactor?.getRootForm()?.form3)
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: AlForm3?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        val rootForm = interactor?.getRootForm()
        rootForm?.form3 = form
        interactor?.setRootForm(rootForm)
    }

    override fun onReinstateData(form: AlForm3?) {
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

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        if(TestConfig.isFingerPrintRequired){
            if(fingerprintTotalEnroll == 0){
                showAlerter("Warning", "Please Add Fingerprint")
                return
            }
        }
        interactor?.navigateToPreview()
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
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

    }

    override fun onPopulateView() {

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
                val fpList = ArrayList<FingerprintData>()
                val names =  arrayOf("right_thumb", "right_index","right_middle", "right_ring", "right_small", "left_thumb", "left_index","left_middle", "left_ring", "left_small")
                val ids = arrayOf(com.xplo.code.R.id.right_thumb, com.xplo.code.R.id.right_index,com.xplo.code.R.id.right_middle, com.xplo.code.R.id.right_ring,
                    com.xplo.code.R.id.right_small, com.xplo.code.R.id.left_thumb, com.xplo.code.R.id.left_index,
                    com.xplo.code.R.id.left_middle, com.xplo.code.R.id.left_ring, com.xplo.code.R.id.left_small)

                val data: Intent? = it.data

                for (i in 0 until names.size){
                    val nowName = names[i]
                    val nowID = ids[i]

                    val nowFPData = data?.getParcelableExtra(nowName) as FingerprintData?
                    if (nowFPData != null && nowFPData.fingerprintData != null) {
                        drawWSQ(nowID,nowFPData)
                        Log.d("HouseHold Fingerprint", ">>>>>> $nowName is not null >>>>>>")
                    }
                    if (nowFPData != null) {
                        fpList.add(nowFPData)
                    }
                }
                //binding.llDataShow.visibility = View.VISIBLE

                fingerprintTotalEnroll = fpList.size
                val form = AlForm3()
                form.fingerData = FingerData()
//                for (item in fpList){
//                    if (item.fingerprintId.name == FingerprintID.RIGHT_THUMB.name){
//                        form.fingerData?.fingerRT = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgRT)
//                    }else if (item.fingerprintId.name == FingerprintID.RIGHT_INDEX.name){
//                        form.fingerData?.fingerRI = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgRI)
//                    }else if (item.fingerprintId.name == FingerprintID.RIGHT_MIDDLE.name){
//                        form.fingerData?.fingerRM = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgRM)
//                    }else if (item.fingerprintId.name == FingerprintID.RIGHT_RING.name){
//                        form.fingerData?.fingerRR = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgRR)
//                    }else if (item.fingerprintId.name == FingerprintID.RIGHT_SMALL.name){
//                        form.fingerData?.fingerRL = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgRL)
//                    }else if (item.fingerprintId.name == FingerprintID.LEFT_THUMB.name){
//                        form.fingerData?.fingerLT = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgLT)
//                    }else if (item.fingerprintId.name == FingerprintID.LEFT_INDEX.name){
//                        form.fingerData?.fingerLI = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgLI)
//                    }else if (item.fingerprintId.name == FingerprintID.LEFT_MIDDLE.name){
//                        form.fingerData?.fingerLM = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgLM)
//                    }else if (item.fingerprintId.name == FingerprintID.LEFT_RING.name){
//                        form.fingerData?.fingerLR = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgLR)
//                    }else if (item.fingerprintId.name == FingerprintID.LEFT_SMALL.name){
//                        form.fingerData?.fingerLL = item.fingerprintData.toString()
//                        addFingerDrawable(binding.imgLL)
//                    }
//                    onValidated(form)
//                    //Toast.makeText(activity, "Received Positive Result From Fingerprint Capture", Toast.LENGTH_LONG).show()
//                }

            }else{
                //Toast.makeText(activity, "Received Negative Result From Fingerprint Capture", Toast.LENGTH_LONG).show()
            }
        }

    private fun addFingerDrawable(img: ImageView) {
        fingerprintTotalEnroll += 1
        img.setImageResource(R.drawable.ic_finger_add)
        val color = ContextCompat.getColor(requireContext(), R.color.green) // Your color resource
        ImageViewCompat.setImageTintList(img, ColorStateList.valueOf(color))
    }


    fun drawWSQ(id: Int, fpData: FingerprintData?) {
        Log.d("IDEMIADeviceIntegration", ">>>> Entering drawWSQ >>>>")
        try {
            if (fpData == null) {
                Log.e("IDEMIADeviceIntegration", ">>>> fpData is null >>>>")
                return
            }
            val fingerprintData = fpData.getFingerprintData()
            if (fingerprintData == null) {
                Log.e("IDEMIADeviceIntegration", ">>>> fpData.getFingerprintData() is null >>>>")
                return
            }
            val imView = activity?.findViewById<ImageView>(id)
            if (imView == null) {
                Log.e("IDEMIADeviceIntegration", ">>>> Image View is null >>>>")
                return
            }
            val width = 248
            val height = 448
            val data = ImageProc.fromWSQ(fingerprintData, width, height)
            if (data == null) {
                Log.e("IDEMIADeviceIntegration", ">>>> Could not decode WSQ >>>>")
                return
            }
            val bitmap = Bitmap.createBitmap(ImageProc.toGrayscale(data, width, height))
            imView.setImageBitmap(bitmap)
        } catch (t: Throwable) {
            Log.e("IDEMIADeviceIntegration", t.localizedMessage ?: "Unknown error")
            t.printStackTrace()
        }
        Log.d("IDEMIADeviceIntegration", ">>>> Leaving drawWSQ >>>>")
    }


}
