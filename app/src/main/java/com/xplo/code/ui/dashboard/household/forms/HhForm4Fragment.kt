package com.xplo.code.ui.dashboard.household.forms

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.toBool
import com.xplo.code.databinding.FragmentHhForm4CapPhotoBinding
import com.xplo.code.ui.components.XDialogSheet
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.photo.ImagePickerActivity
import com.xplo.code.ui.photo.ImageUtil
import com.xplo.data.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 4/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class HhForm4Fragment : BasicFormFragment(), HouseholdContract.Form4View {

    companion object {
        const val TAG = "HhForm4Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm4Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm4Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm4CapPhotoBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


    val REQUEST_IMAGE = 100
    private var fileName: String? = null
    var file: File? = null
    var newPhotoBase64 = ""
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
        binding = FragmentHhForm4CapPhotoBinding.inflate(inflater, container, false)
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
        ImagePickerActivity.clearCache(requireContext())
        binding.quickStartCroppedImage?.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Dexter.withActivity(requireActivity())
                    .withPermissions(
                        Manifest.permission.CAMERA
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                //showImagePickerOptions()
                                launchCameraIntent()
                            }
                            if (report.isAnyPermissionPermanentlyDenied) {
                                showSettingsDialog()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
            } else {
                Dexter.withActivity(requireActivity())
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                //showImagePickerOptions()
                                launchCameraIntent()
                            }
                            if (report.isAnyPermissionPermanentlyDenied) {
                                showSettingsDialog()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).check()
            }

        }
    }

    override fun initView() {

    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Capture Photo")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()
        onReinstateData(interactor?.getRootForm()?.form4)
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm4?) {
        Log.d(TAG, "onValidated() called with: form = $form")
    }

    override fun onReinstateData(form: HhForm4?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")

    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToForm5()
        askForConsent()
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

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            requireContext(),
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {} /* @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }*/
            })
    }

    private fun launchCameraIntent() {
        val intent = Intent(requireContext(), ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(
            intent, REQUEST_IMAGE
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                file = uri!!.path?.let { File(it) }
                fileName = uri.lastPathSegment
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    newPhotoBase64 = ImageUtil.convert(bitmap)
                    setToModel(uri.toString())
                    loadProfile(uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setToModel(newPhotoBase64: String?) {
        val form = HhForm4()
        form.img = newPhotoBase64
        val rootForm = interactor?.getRootForm()
        rootForm?.form4 = form
        interactor?.setRootForm(rootForm)
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder =
            AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(
            getString(R.string.go_to_settings)
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun loadProfile(url: String) {
        // filePath=url;
        Glide.with(this).load(url)
            .into(this.binding.img)
        binding.img.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
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
        if (interactor?.getRootForm()?.form1?.countryName.equals("JUBA")) {
            interactor?.navigateToPreview()
        } else {
            interactor?.navigateToForm6()
        }

    }

    fun isConsentGiven(): Boolean {
        return interactor?.getRootForm()?.consentStatus?.isConsentGivenNominee.toBool()
    }
}
