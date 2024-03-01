package com.xplo.code.ui.dashboard.alternate.forms


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
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

import com.kit.integrationmanager.model.BiometricUserType
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.databinding.FragmentAlForm2CapPhotoBinding
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm2
import com.xplo.code.ui.dashboard.model.PhotoData

import com.xplo.code.ui.dashboard.model.isOk
import com.xplo.code.ui.photo.ImagePickerActivity
import com.xplo.code.ui.photo.ImageUtil
import com.xplo.data.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/14/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class AlForm2Fragment : BasicFormFragment(), AlternateContract.Form2View {

    companion object {
        const val TAG = "AlForm2Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): AlForm2Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlForm2Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlForm2CapPhotoBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null
    val REQUEST_IMAGE = 100
    private var fileName: String? = null
    var file: File? = null
    var newPhotoBase64 = ""

    var form = AlForm2()
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
        binding = FragmentAlForm2CapPhotoBinding.inflate(inflater, container, false)
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

        onReinstateData(interactor?.getRootForm()?.form2)

//        bindSpinnerData(binding.spCountryName, UiData.countryNameOptions)


    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }

    }

    override fun onPause() {
        super.onPause()
        //EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        //EventBus.getDefault().register(this)
        setToolbarTitle("Alternate Photo")

//        binding.viewButtonBackNext.btBack.gone()
//        binding.viewButtonBackNext.btNext.visible()


    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: AlForm2?) {
        Log.d(TAG, "onValidated() called with: form = $form")

    }

    override fun onReinstateData(form: AlForm2?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")

        if (form != null) {
            form.photoData?.imgPath?.let { loadProfile(it) }
            this.form = form
        }
    }
    override fun onGetImageUri(uri: Uri?) {
        Log.d(TAG, "onGetImageUri() called with: uri = $uri")
        file = uri!!.path?.let { File(it) }
        fileName = uri.lastPathSegment
        try {
            val bitmap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            val byteArray = convertBitmapToByteArray(requireContext(), bitmap)
            newPhotoBase64 = ImageUtil.convert(bitmap)
            setToModel(uri.toString(),byteArray)
            loadProfile(uri.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun convertBitmapToByteArray(context: Context, bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT
                // Additional checks for Android Studio Emulator
                || Build.FINGERPRINT.contains("sdk_gphone_x86")
                || Build.FINGERPRINT.contains("sdk_google_phone_x86")
                || Build.MANUFACTURER.contains("Google") // Emulator manufacturer is often Google
                || Build.BRAND.contains("google") // Brand for emulators can also be google
                || Build.DEVICE.contains("generic_x86") // Device for x86 architecture
                || Build.PRODUCT.contains("sdk_google") // Product name for Google's emulator images
                || Build.HARDWARE.contains("goldfish") // Hardware name for older emulators
                || Build.HARDWARE.contains("ranchu") // Hardware name for newer emulators
                || Build.HARDWARE.contains("goldfish_x86") // Specific hardware name for x86 emulators
                || Build.HARDWARE.contains("ranchu_x86")) // Specific hardware name for x86 emulators
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEventButtonAction(event: EventButtonAction?) {
//        Log.d(TAG, "onEventContentClick() called with: event = $event")
//        if (event == null) return
//
//        when (event.buttonAction) {
//            ButtonAction.BACK -> onBackButton()
//            ButtonAction.NEXT -> onNextButton()
//            ButtonAction.SUBMIT -> onSubmitButton()
//            else -> {}
//        }
//
//    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        onReadInput()
//        XDialog.Builder(requireActivity().supportFragmentManager)
//            .setLayoutId(R.layout.custom_dialog_pnn)
//            .setTitle(getString(R.string.alternate_reg))
//            .setMessage(getString(R.string.alternate_details))
//            .setPosButtonText(getString(R.string.ok))
//            .setNegButtonText(getString(R.string.cancel))
//            .setThumbId(R.drawable.ic_logo_photo)
//            .setCancelable(false)
//            .setListener(object : XDialog.DialogListener {
//                override fun onClickPositiveButton() {
//
//                }
//
//                override fun onClickNegativeButton() {
//
//                }
//
//                override fun onClickNeutralButton() {
//                }
//            })
//            .build()
//            .show()

    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")
        if(!isEmulator()){
            if (!form.isOk()) {
                showAlerter("Warning", "Please Add Photo")
                return
            }
        }
        interactor?.navigateToForm3()
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
        TODO("Not yet implemented")
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
                onGetImageUri(uri)
            }
        }
    }

    private fun setToModel(path: String?, bytearray: ByteArray) {
        var data = PhotoData()
        data.imgPath = path
        data.userType = BiometricUserType.BENEFICIARY.name
        data.img = bytearray
        form.photoData = data
        val rootForm = interactor?.getRootForm()
        rootForm?.form2 = form
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
            .into(this!!.binding.img!!)
        binding.img!!.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
    }

}
