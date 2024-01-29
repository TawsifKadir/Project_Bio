package com.xplo.code.ui.dashboard.household.forms


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
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
class HhForm1Fragment : BasicFormFragment(), HouseholdContract.Form1View,
    AdapterView.OnItemSelectedListener {

    companion object {
        const val TAG = "HhForm1Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm1Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm1Fragment()
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
    }

    override fun initView() {

        viewModel.getStateItems()
    }

    override fun initObserver() {


        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
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

//        spStateName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                Log.d(
//                    TAG,
//                    "onItemSelected() called with: parent = $parent, view = $view, position = $position, id = $id"
//                )
//
//                onSelectSpinnerItem(view, position)
//
//
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//        }

        spStateName.onItemSelectedListener = this
        spCountryName.onItemSelectedListener = this
        spPayamName.onItemSelectedListener = this
        spBomaName.onItemSelectedListener = this



        if (BuildConfig.DEBUG) {
            binding.viewButtonBackNext.btNext.setOnLongClickListener {
                onGenerateDummyInput()
                return@setOnLongClickListener true
            }
        }

        onGenerateDummyInput()

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


//        setSpinnerItem(spStateName, UiData.stateNameOptions, form.stateName)
//        setSpinnerItem(spCountryName, UiData.countryNameOptions, form.countryName)


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
        Log.d(TAG, "onValidation() called")

        val form = HhForm1()


        form.stateName = chkSpinner(spStateName, UiData.ER_SP_DF)
        form.countryName = chkSpinner(spCountryName, UiData.ER_SP_DF)
        form.payamName = chkSpinner(spPayamName, UiData.ER_SP_DF)
        form.bomaName = chkSpinner(spBomaName, UiData.ER_SP_DF)


        if (!form.isOk()) {
            return
        }

        onValidated(form)
    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

//        spStateName.setSelection(1)
//        spCountryName.setSelection(1)
//        spPayamName.setSelection(1)
//        spBomaName.setSelection(1)


    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

        val form = interactor?.getRootForm()?.form1
        if (form == null) return

//        setSpinnerItem(spCountryName, UiData.countryNameOptions, form.countryName)
//        setSpinnerItem(spStateName, UiData.stateNameOptions, form.stateName)

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


}
