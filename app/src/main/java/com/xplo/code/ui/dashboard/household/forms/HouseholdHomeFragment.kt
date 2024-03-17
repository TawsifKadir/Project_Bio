package com.xplo.code.ui.dashboard.household.forms

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.toBool
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.room.model.Beneficiary
import com.xplo.code.databinding.FragmentHouseholdHomeBinding
import com.xplo.code.ui.components.XDialogSheet
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.list.HouseholdListAdapterNew
import com.xplo.code.utils.DialogUtil
import com.xplo.code.utils.DialogUtil.showLottieDialogFailMsg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class HouseholdHomeFragment : BaseFragment(), HouseholdContract.HomeView,
    HouseholdListAdapterNew.OnItemClickListener {

    companion object {
        const val TAG = "HouseholdHomeFragment"

        @JvmStatic
        fun newInstance(parent: String?): HouseholdHomeFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HouseholdHomeFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHouseholdHomeBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: HomeContract.Presenter
    private var interactor: HouseholdContract.View? = null

    // private var adapter: HouseholdListAdapter? = null
    private var adapterNew: HouseholdListAdapterNew? = null

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is HouseholdContract.View) {
            interactor = activity as HouseholdContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHouseholdHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }


    override fun initInitial() {

    }

    override fun initView() {

        //binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

//        adapter = HouseholdListAdapter()
//        adapter?.setOnItemClickListener(this)
//        binding.recyclerView.adapter = adapter

        adapterNew = HouseholdListAdapterNew()
        adapterNew?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapterNew

        //  showBeneficiary()

        //viewModel.getHouseholdItems()

        viewModel.showBeneficiary(requireContext())

        DialogUtil.showLottieDialog(requireContext(), "Preparing Content", "Please wait")

        binding.fab.setOnClickListener {
            viewModel.bulkBeneficiaryList(requireContext())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    override fun initObserver() {


        lifecycleScope.launch {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsSuccess -> {
                        Log.d(TAG, "GetHouseHoldList Called")
                        hideLoading()
                        onGetHouseholdList(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsFailure -> {
                        hideLoading()
                        onGetHouseholdListFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsSuccessMsg -> {
                        Log.d(TAG, "GetHouseHoldListSuccess Called")
                        hideLoading()
                        onGetHouseholdListSuccess(event.msg, event.appId)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetDataLocalDb -> {
                        hideLoading()
                        DialogUtil.dismissLottieDialog()
                        // onGetHouseholdListSuccess(event.msg)

                        if (event.beneficiary.isEmpty()) {
                            binding.llNoContentText.visibility = View.VISIBLE
                            binding.llBody.visibility = View.GONE
                        } else {
                            binding.llNoContentText.visibility = View.GONE
                            binding.llBody.visibility = View.VISIBLE
                        }

                        adapterNew?.addAll(event.beneficiary)
                        adapterNew?.notifyDataSetChanged()
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetDataLocalDbByAppId -> {

                        requireActivity().runOnUiThread {
                            DialogUtil.showLottieDialog(
                                requireContext(),
                                "Data will sync to server",
                                "Please wait"
                            )
                        }
                        //Log.d(TAG, "onClickHouseholdItemSend() called with: item = $item, pos = $pos")
                        //showToast("Feature not implemented yet")

                        //viewModel.sendHouseholdItem(item, pos)
                        GlobalScope.launch(Dispatchers.IO) {

                            viewModel.callRegisterApi(requireContext(), event.beneficiary)
                        }
                        //hideLoading()
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetDataLocalDbForBulk -> {

                        requireActivity().runOnUiThread {
                            DialogUtil.showLottieDialog(
                                requireContext(),
                                "Data will sync to server",
                                "Please wait"
                            )
                        }

                        GlobalScope.launch(Dispatchers.IO) {

                            viewModel.callRegisterApiBulk(requireContext(), event.beneficiaryList)
                        }
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.DeleteDataLocalDbByAppId -> {
                        hideLoading()
                        if (event.beneficiary) {
                            DialogUtil.showLottieDialog(
                                requireContext(),
                                "Preparing Content",
                                "Please wait"
                            )
                            //requireActivity().finish()
                            viewModel.showBeneficiary(requireContext())
                        }
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetDataLocalDbByAppIdForView -> {
                        hideLoading()
                        navigateToHouseholdDetailsFromBeneficiary(event.beneficiaryViewDetails)

                        viewModel.clearEvent()
                    }


//                    is HouseholdViewModel.Event.SendHouseholdFormSuccess -> {
//                        hideLoading()
//                        onSubmitFormSuccess(event.id, event.pos)
//                        viewModel.clearEvent()
//                    }
//
//                    is HouseholdViewModel.Event.SendHouseholdFormFailure -> {
//                        hideLoading()
//                        onSubmitFormFailure(event.msg)
//                        viewModel.clearEvent()
//                    }

                    is HouseholdViewModel.Event.SendHouseholdItemSuccess -> {
                        hideLoading()
                        onSubmitHouseholdItemSuccess(event.item, event.pos)
                    }

                    is HouseholdViewModel.Event.SendHouseholdItemFailure -> {
                        hideLoading()
                        onSubmitHouseholdItemFailure(event.msg)
                    }

                    is HouseholdViewModel.Event.UpdateHouseholdItemSuccess -> {
                        hideLoading()
                        onUpdateHouseholdItemSuccess(event.id)
                    }

                    is HouseholdViewModel.Event.UpdateHouseholdItemFailure -> {
                        hideLoading()
                        onUpdateHouseholdItemFailure(event.msg)
                    }

                    is HouseholdViewModel.Event.SaveBeneficiaryEntitySuccess -> {
                        hideLoading()
                        interactor?.onSaveBeneficiarySuccess(null)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.SaveBeneficiaryEntityFailure -> {
                        hideLoading()
                        interactor?.onSaveBeneficiaryFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.SendBeneficiaryEntitySuccess -> {
                        hideLoading()
                        onSubmitFormSuccess(event.id, event.pos)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.SendBeneficiaryEntityFailure -> {
                        hideLoading()
                        onSubmitFormFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    else -> Unit
                }
            }
        }

        binding.btRegistration.setOnClickListener {
            askForConsent()
        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Household")

        interactor?.resetRootForm()
        viewModel.showBeneficiary(requireContext())
    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun navigateToHouseholdDetails(content: HouseholdItem) {
        Log.d(TAG, "navigateToHouseholdDetails() called with: content = ${content.hid}")
        interactor?.navigateToFormDetails(content)
    }

    override fun navigateToHouseholdDetailsFromBeneficiary(item: com.kit.integrationmanager.model.Beneficiary?) {
        interactor?.navigateToFormDetailsBeneficiary(item)
    }

    override fun onGetHouseholdList(items: List<HouseholdItem>?) {
        Log.d(TAG, "onGetHouseholdList() called with: items = ${items?.size}")
        if (items == null) {
            binding.llNoContentText.visibility = View.VISIBLE
            binding.llBody.visibility = View.GONE
            return
        } else if (items.isEmpty()) {
            binding.llNoContentText.visibility = View.VISIBLE
            binding.llBody.visibility = View.GONE
            return
        } else {
            binding.llNoContentText.visibility = View.GONE
            binding.llBody.visibility = View.VISIBLE
        }

        //  adapter?.addAll(items)
    }

    override fun onGetHouseholdListFailure(msg: String?) {
        Log.d(TAG, "onGetHouseholdListFailure() called with: msg = $msg")
        //binding.llNoContentText.visibility = View.VISIBLE
        //binding.llBody.visibility = View.GONE

        DialogUtil.dismissLottieDialog()
        if (msg != null) {
            showLottieDialogFailMsg(requireContext(), msg)
        }
        Log.d(TAG, "onGetHouseholdListFailure() called with: msg = $msg")
        //showMessage(msg)
    }

    override fun onGetHouseholdListSuccess(msg: String?, appIdList: MutableList<String>?) {
        DialogUtil.dismissLottieDialog()
        if (msg != null) {
            if (appIdList != null) {
                for (appId in appIdList) {
                    //  viewModel.updateBeneficiary(requireContext(), appId)
                    viewModel.deleteAndInsertBeneficiary(requireContext(), appId)
                }
            }
            LottieAlertDialog.Builder(context, DialogTypes.TYPE_SUCCESS)
                .setTitle("Success")
                .setDescription(msg)
                .setPositiveText("Ok")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        viewModel.showBeneficiary(requireContext())
                        dialog.dismiss()
                    }
                })
                .build().apply {
                    show()
                    setCancelable(true)
                }
        }

        Log.d(TAG, "onGetHouseholdListSuccess() called with: msg = $msg")
        //showMessage(msg)
    }

    override fun onSubmitFormSuccess(id: String?, pos: Int) {
        Log.d(TAG, "onSubmitFormSuccess() called with: item = $id, pos = $pos")

    }

    override fun onSubmitFormFailure(msg: String?) {
        Log.d(TAG, "onSubmitFormFailure() called with: msg = $msg")
        showToast(msg ?: "")
    }

    override fun onSubmitHouseholdItemSuccess(item: HouseholdItem?, pos: Int) {
        Log.d(TAG, "onSubmitHouseholdItemSuccess() called with: item = $item, pos = $pos")
        item?.isSynced = true
        viewModel.updateHouseholdItem(item)
    }

    override fun onSubmitHouseholdItemFailure(msg: String?) {
        Log.d(TAG, "onSubmitHouseholdItemFailure() called with: msg = $msg")
        showToast(msg ?: "")
    }

    override fun onUpdateHouseholdItemSuccess(id: String?) {
        Log.d(TAG, "onUpdateHouseholdItemSuccess() called with: id = $id")
        viewModel.getHouseholdItems()
    }

    override fun onUpdateHouseholdItemFailure(msg: String?) {
        Log.d(TAG, "onUpdateHouseholdItemFailure() called with: msg = $msg")

    }


//    override fun onClickHouseholdItem(item: HouseholdItem, pos: Int) {
//        Log.d(TAG, "onClickHouseholdItem() called with: item = ${item.hid}, pos = $pos")
//        //dToast(item.title)
//        navigateToHouseholdDetails(item)
//    }
//
//    override fun onClickHouseholdItemDelete(item: HouseholdItem, pos: Int) {
//        Log.d(TAG, "onClickHouseholdItemDelete() called with: item = $item, pos = $pos")
//        //Create Dialog Here
//        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.delete_dialog_resource)
//        val window = dialog.window
//        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//
//        val btnOk: Button = dialog.findViewById<Button>(R.id.okButton)
//        val btnCancel: Button = dialog.findViewById<Button>(R.id.cancelButton)
//
//        btnOk.setOnClickListener {
//
//            viewModel.deleteHouseholdItem(item)
//            //  adapter?.remove(pos)
//            dialog.dismiss()
//        }
//
//        btnCancel.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
//
//    }

//    override fun onClickHouseholdItemSend(item: HouseholdItem, pos: Int) {
//        requireActivity().runOnUiThread {
//            DialogUtil.showLottieDialog(requireContext(), "Data will sync to server", "Please wait")
//        }
//        Log.d(TAG, "onClickHouseholdItemSend() called with: item = $item, pos = $pos")
//        //showToast("Feature not implemented yet")
//
//        //viewModel.sendHouseholdItem(item, pos)
//        GlobalScope.launch(Dispatchers.IO) {
//            viewModel.syncHouseholdForm(requireContext(), item.toHouseholdForm(), pos)
//        }
//        //viewModel.syncHouseholdForm(requireContext(), item.toHouseholdForm(), pos)
//
////        val beneficiary = Fake.getABenificiary()
////        viewModel.sendBeneficiary(beneficiary,0)
////        viewModel.syncBeneficiary(requireContext(), beneficiary, 0 )
//    }

//    override fun onClickHouseholdItemAddAlternate(item: HouseholdItem, pos: Int) {
//        Log.d(TAG, "onClickHouseholdItemAddAlternate() called with: item = $item, pos = $pos")
//        //navigateToAlternate(item.id)
//    }
//
//    override fun onClickHouseholdItemSave(item: HouseholdItem, pos: Int) {
//        Log.d(TAG, "onClickHouseholdItemSave() called with: item = $item, pos = $pos")
//        val entity = EntityMapper.toBeneficiaryEntity(item.toHouseholdForm())
//        viewModel.saveBeneficiaryEntity(entity)
//
//    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLocation() {

        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        interactor?.navigateToForm1()
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

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, _: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _: Int ->
            dialog.cancel()
        }
        builder.show()
    }

//    private fun openSettings() {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri = Uri.fromParts("package", requireContext().packageName, null)
//        intent.data = uri
//        startActivityForResult(intent, 101)
//    }

    private val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle the result if needed
            }
        }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        settingsLauncher.launch(intent)
    }


    private fun askForConsent() {

        if (isConsentGiven()) {
            onGetConsent()
            return
        }

        XDialogSheet.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.bsd_consent_sheet)
            .setTitle("Consent")
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

    fun isConsentGiven(): Boolean {
        if (!TestConfig.isConsentEnabled) return true
        return interactor?.getRootForm()?.consentStatus?.isConsentGivenHhHome.toBool()
    }

    fun isGpsAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
    }

    private fun onGetConsent() {
        getPrefHelper().setHouseholdConsentAcceptStatus(true)
        interactor?.getRootForm()?.consentStatus?.isConsentGivenHhHome = true
        val gpsAvailable = isGpsAvailable(requireContext())
        if (gpsAvailable) {
            getLocation()
        } else {
            interactor?.navigateToForm1()
        }

    }


    override fun onClickHouseholdItem(item: Beneficiary, pos: Int) {
        viewModel.showBeneficiaryByAppIdForViewDetails(requireContext(), item.applicationId)
        //navigateToHouseholdDetails(item)
    }

    override fun onClickHouseholdItemDelete(item: Beneficiary, pos: Int) {
        //Create Dialog Here
        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_dialog_resource)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnOk: Button = dialog.findViewById<Button>(R.id.okButton)
        val btnCancel: Button = dialog.findViewById<Button>(R.id.cancelButton)

        btnOk.setOnClickListener {
            viewModel.deleteBeneficiary(requireContext(), item.applicationId)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onClickHouseholdItemSend(item: Beneficiary, pos: Int) {
        viewModel.showBeneficiaryByAppId(requireContext(), item.applicationId)
    }

    override fun onClickHouseholdItemAddAlternate(item: Beneficiary, pos: Int) {
        if (item.alternateSize >= 2) {
//            Toast.makeText(requireContext(), "Maximum 2 Alternate Add Options.", Toast.LENGTH_SHORT)
//                .show()
            showLottieDialogFailMsg(requireContext(), "Maximum 2 Alternate Add Options.")
        } else if (item.applicationStatus == 1) {

            showLottieDialogFailMsg(requireContext(), "Completed Data Not Edit Permission")

        } else {
            // navigateToAlternate(item.applicationId)
//            navigateToAlternateNew(
//                item.applicationId,
//                item.respondentFirstName + " " + item.respondentMiddleName + " " + item.respondentLastName,
//                "V"
//            )

            navigateToAlternate(
                item.applicationId,
                item.respondentFirstName + " " + item.respondentMiddleName + " " + item.respondentLastName
            )

        }
    }

    override fun onClickHouseholdItemSave(item: Beneficiary, pos: Int) {
        // Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
        //   viewModel.updateBeneficiary(requireContext(), item.applicationId)
        Log.d(TAG, "onClickHouseholdItemSave: ${item.isSynced}")

        if (item.isSynced) {
            Toast.makeText(
                requireContext(),
                "Data is synced to remote database",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Click Data is synced to remote database",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}