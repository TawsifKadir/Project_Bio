package com.xplo.code.ui.dashboard.household.forms


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.code.data.db.offline.addSpinnerHeader
import com.xplo.code.data.db.offline.toSpinnerOptions
import com.xplo.code.databinding.FragmentHhForm1RegSetupBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.isOk
import com.xplo.data.BuildConfig
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
class HhForm1RegSetupFragment : BasicFormFragment(), HouseholdContract.Form1View,
    AdapterView.OnItemSelectedListener,
    LocationListener {

    companion object {
        const val TAG = "HhForm1RegSetupFragment"
        private var isUseOldView = false

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm1RegSetupFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            isUseOldView = false
            val fragment = HhForm1RegSetupFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm1RegSetupBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


    private lateinit var spStateName: Spinner
    private lateinit var spCountryName: Spinner
    private lateinit var spPayamName: Spinner
    private lateinit var spBomaName: Spinner

    private lateinit var etLat: EditText
    private lateinit var etLon: EditText

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2


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
        Log.d(
            TAG,
            "onCreateView() called with: inflater = $inflater, container = $container, savedInstanceState = $savedInstanceState"
        )

        if (this::binding.isInitialized) {
            isUseOldView = true
            return binding.root
        }

        binding = FragmentHhForm1RegSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            TAG,
            "onViewCreated() called with: view = $view, savedInstanceState = $savedInstanceState"
        )

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = RegistrationPresenter(DataRepoImpl())
        //presenter.attach(this)


        spStateName = binding.spStateName
        spCountryName = binding.spCountryName
        spPayamName = binding.spPayamName
        spBomaName = binding.spBomaName
        etLat = binding.etLat
        etLon = binding.etLon

    }

    fun isGpsAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
    }

    override fun initView() {

        if (!isUseOldView) {

            if (isGpsAvailable(requireContext())) {
                getLocation()
            }
            viewModel.getStateItems()
        }


    }

    override fun initObserver() {


        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.GetStateItemsSuccess -> {
                        hideLoading()
                        onGetStateItems(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetStateItemsFailure -> {
                        hideLoading()
                        onGetStateItemsFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetCountryItemsSuccess -> {
                        hideLoading()
                        onGetCountryItems(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetCountryItemsFailure -> {
                        hideLoading()
                        onGetCountryItemsFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetPayamItemsSuccess -> {
                        hideLoading()
                        onGetPayamItems(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetPayamItemsFailure -> {
                        hideLoading()
                        onGetPayamItemsFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetBomaItemsSuccess -> {
                        hideLoading()
                        onGetBomaItems(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetBomaItemsFailure -> {
                        hideLoading()
                        onGetBomaItemsFailure(event.msg)
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

        spStateName.onItemSelectedListener = this
        spCountryName.onItemSelectedListener = this
        spPayamName.onItemSelectedListener = this
        spBomaName.onItemSelectedListener = this

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            //onGenerateDummyInput()
        }

    }

    override fun onPause() {
        super.onPause()
        //EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        //EventBus.getDefault().register(this)
        setToolbarTitle("Registration Setup")

        binding.viewButtonBackNext.btBack.gone()
        binding.viewButtonBackNext.btNext.visible()

        //onReinstateData(interactor?.getRootForm()?.form1)


    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm1?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        //showToast(form.toString())

        val rootForm = interactor?.getRootForm()
        rootForm?.form1 = form
        interactor?.setRootForm(rootForm)

        interactor?.navigateToForm2()
    }

    override fun onReinstateData(form: HhForm1?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")
        if (form == null) return


    }

    override fun onGetStateItems(items: List<OptionItem>?) {
        Log.d(TAG, "onGetStateItems() called with: items = $items")
        if (items.isNullOrEmpty()) return

        val items1 = items.addSpinnerHeader().toSpinnerOptions()
        Log.d(TAG, "onGetStateItems: $items1")
        bindSpinnerData(spStateName, items1)

    }

    override fun onGetStateItemsFailure(msg: String?) {
        Log.d(TAG, "onGetStateItemsFailure() called with: msg = $msg")
    }

    override fun onSelectStateItem(item: OptionItem?) {
        Log.d(TAG, "onSelectStateItem() called with: item = $item")
        viewModel.getCountryItems(item?.name)
    }

    override fun onGetCountryItems(items: List<OptionItem>?) {
        Log.d(TAG, "onGetCountryItems() called with: items = $items")
        if (items.isNullOrEmpty()) return

        val items1 = items.addSpinnerHeader().toSpinnerOptions()
        Log.d(TAG, "onGetCountryItems: $items1")
        bindSpinnerData(spCountryName, items1)
        bindSpinnerData(spPayamName, arrayOf(""))
        bindSpinnerData(spBomaName, arrayOf(""))
    }

    override fun onGetCountryItemsFailure(msg: String?) {
        Log.d(TAG, "onGetCountryItemsFailure() called with: msg = $msg")
    }

    override fun onSelectCountryItem(item: OptionItem?) {
        Log.d(TAG, "onSelectCountryItem() called with: item = $item")
        viewModel.getPayamItems(item?.name)
    }


    override fun onGetPayamItems(items: List<OptionItem>?) {
        Log.d(TAG, "onGetPayamItems() called with: items = $items")
        if (items.isNullOrEmpty()) return

        val items1 = items.addSpinnerHeader().toSpinnerOptions()
        Log.d(TAG, "onGetPayamItems: $items1")
        bindSpinnerData(spPayamName, items1)
        bindSpinnerData(spBomaName, arrayOf(""))

    }

    override fun onGetPayamItemsFailure(msg: String?) {
        Log.d(TAG, "onGetPayamItemsFailure() called with: msg = $msg")
    }

    override fun onSelectPayamItem(item: OptionItem?) {
        Log.d(TAG, "onSelectPayamItem() called with: item = $item")
        viewModel.getBomaItems(item?.name)
    }

    override fun onGetBomaItems(items: List<OptionItem>?) {
        Log.d(TAG, "onGetBomaItems() called with: items = $items")
        if (items.isNullOrEmpty()) return

        val items1 = items.addSpinnerHeader().toSpinnerOptions()
        Log.d(TAG, "onGetBomaItems: $items1")
        bindSpinnerData(spBomaName, items1)

    }

    override fun onGetBomaItemsFailure(msg: String?) {
        Log.d(TAG, "onGetBomaItemsFailure() called with: msg = $msg")
    }

    override fun onSelectBomaItem(item: OptionItem?) {
        Log.d(TAG, "onSelectBomaItem() called with: item = $item")
        //viewModel.getCountryItems()
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
        //interactor?.navigateToForm2()

//        if (!TestConfig.isValidationEnabled) {
//            interactor?.navigateToForm2()
//            return
//        }

        onReadInput()
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

        val form = HhForm1()


        form.stateName = chkSpinner(spStateName, UiData.ER_SP_DF)
        form.countryName = chkSpinner(spCountryName, UiData.ER_SP_DF)
        form.payamName = chkSpinner(spPayamName, UiData.ER_SP_DF)
        form.bomaName = chkSpinner(spBomaName, UiData.ER_SP_DF)

        form.lat = getEditText(etLat)?.toDouble()
        form.lon = getEditText(etLon)?.toDouble()

//        if (chkEditText(etLat, UiData.ER_ET_DF) == null) {
//            form.lat = null
//        } else {
//            form.lat = chkEditText(etLat, UiData.ER_ET_DF)?.toDouble()
//        }
//
//        if (chkEditText(etLon, UiData.ER_ET_DF) == null) {
//            form.lon = null
//        } else {
//            form.lon = chkEditText(etLon, UiData.ER_ET_DF)?.toDouble()
//        }


        if (!form.isOk()) {
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

        bindSpinnerData(spStateName, UiData.stateNameOptions)
        bindSpinnerData(spCountryName, UiData.countryNameOptions)
        bindSpinnerData(spPayamName, UiData.payaamNameOptions)
        bindSpinnerData(spBomaName, UiData.bomaNameOptions)

        spStateName.setSelection(1)
        spCountryName.setSelection(1)
        spPayamName.setSelection(1)
        spBomaName.setSelection(1)

        //etLat.setText("99.99")
        //etLon.setText("99.99")


    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

        val form = interactor?.getRootForm()?.form1
        if (form == null) return

        setSpinnerItem(spStateName, UiData.stateNameOptions, form.stateName)
        setSpinnerItem(spCountryName, UiData.countryNameOptions, form.countryName)
        setSpinnerItem(spPayamName, UiData.stateNameOptions, form.stateName)
        setSpinnerItem(spBomaName, UiData.stateNameOptions, form.stateName)

    }

    override fun onSelectSpinnerItem(parent: AdapterView<*>?, view: View?, position: Int) {
        super.onSelectSpinnerItem(parent, view, position)
        Log.d(TAG, "onSelectSpinnerItem() called with: view = , position = $position")
        if (position == 0) return

        when (parent?.id) {
            R.id.spStateName -> {
                val txt = spStateName.selectedItem.toString()
                onSelectStateItem(OptionItem(0, txt))
            }

            R.id.spCountryName -> {
                val txt = spCountryName.selectedItem.toString()
                onSelectCountryItem(OptionItem(0, txt))
            }

            R.id.spPayamName -> {
                val txt = spPayamName.selectedItem.toString()
                onSelectPayamItem(OptionItem(0, txt))
            }

            R.id.spBomaName -> {
                val txt = spBomaName.selectedItem.toString()
                onSelectBomaItem(OptionItem(0, txt))
            }


        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d(TAG, "onItemSelected() called with: p0 = $p0, p1 = $p1, p2 = $p2, p3 = $p3")
        onSelectSpinnerItem(p0, p1, p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected() called with: p0 = $p0")

    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged() called with: location = $location")

        etLat.setText(location.latitude.toString())
        etLon.setText(location.latitude.toString())

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
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, get the last known location
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                // Use the last known location
                updateUIWithLocation(lastKnownLocation)
            } else {
                // If no last known location, request updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 5f, this)
            }
        } else {
            // Permission is not granted, request for permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    private fun updateUIWithLocation(location: Location) {
        // Update your UI here with the location data
        etLat.setText(location.latitude.toString())
        etLon.setText(location.longitude.toString())
    }


}
