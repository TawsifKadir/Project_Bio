package com.xplo.code.ui.dashboard.household.forms

import android.Manifest
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
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.toBool
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.data.mapper.EntityMapper
import com.xplo.code.databinding.FragmentHouseholdHomeBinding
import com.xplo.code.network.fake.Fake
import com.xplo.code.ui.components.XDialogSheet
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.list.HouseholdListAdapter
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
class HouseholdHomeFragment : BaseFragment(), HouseholdContract.HomeView,
    HouseholdListAdapter.OnItemClickListener {

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

    private var adapter: HouseholdListAdapter? = null

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

        adapter = HouseholdListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

        //viewModel.getHouseholdItems()


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsSuccess -> {
                        hideLoading()
                        onGetHouseholdList(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsFailure -> {
                        hideLoading()
                        onGetHouseholdListFailure(event.msg)
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

        viewModel.getHouseholdItems()

    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun navigateToHouseholdDetails(content: HouseholdItem) {
        Log.d(TAG, "navigateToHouseholdDetails() called with: content = ${content.hid}")
        interactor?.navigateToFormDetails(content)
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

        adapter?.addAll(items)
    }

    override fun onGetHouseholdListFailure(msg: String?) {
        binding.llNoContentText.visibility = View.VISIBLE
        binding.llBody.visibility = View.GONE
        Log.d(TAG, "onGetHouseholdListFailure() called with: msg = $msg")
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

    override fun onClickHouseholdItem(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItem() called with: item = ${item.hid}, pos = $pos")
        //dToast(item.title)
        navigateToHouseholdDetails(item)
    }

    override fun onClickHouseholdItemDelete(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItemDelete() called with: item = $item, pos = $pos")
        //Create Dialog Here
        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_dialog_resource)
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnOk : Button = dialog.findViewById<Button>(R.id.okButton)
        val btnCancel : Button = dialog.findViewById<Button>(R.id.cancelButton)

        btnOk.setOnClickListener {

            viewModel.deleteHouseholdItem(item)
            adapter?.remove(pos)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun onClickHouseholdItemSend(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItemSend() called with: item = $item, pos = $pos")
        //showToast("Feature not implemented yet")

        viewModel.sendHouseholdItem(item, pos)
        //viewModel.syncHouseholdForm(requireContext(), item.toHouseholdForm(), pos)

        //val beneficiary = Fake.getABenificiary()
        //viewModel.syncBeneficiary(requireContext(), beneficiary, 0 )
    }

    override fun onClickHouseholdItemAddAlternate(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItemAddAlternate() called with: item = $item, pos = $pos")
        navigateToAlternate(item.id)
    }

    override fun onClickHouseholdItemSave(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItemSave() called with: item = $item, pos = $pos")
        val entity = EntityMapper.toBeneficiaryEntity(item.toHouseholdForm())
        viewModel.saveBeneficiaryEntity(entity)

    }


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

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
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
}