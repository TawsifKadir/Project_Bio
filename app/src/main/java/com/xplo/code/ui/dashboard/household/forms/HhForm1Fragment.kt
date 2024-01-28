package com.xplo.code.ui.dashboard.household.forms


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.viewModels
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.visible
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
class HhForm1Fragment : BasicFormFragment(), HouseholdContract.Form1View {

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


    private lateinit var spCountryName: Spinner
    private lateinit var spStateName: Spinner
    private lateinit var etPayamName: EditText
    private lateinit var etBomaName: EditText


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

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        //presenter = RegistrationPresenter(DataRepoImpl())
        //presenter.attach(this)

        spCountryName = binding.spCountryName
        spStateName = binding.spStateName
        etPayamName = binding.etPayamName
        etBomaName = binding.etBomaName
    }

    override fun initView() {


        bindSpinnerData(binding.spCountryName, UiData.countryNameOptions)
        bindSpinnerData(binding.spStateName, UiData.stateNameOptions)

//        bindSpinnerAdapter(binding.viewGroupS1.spRelationship, UiData.relationshipOptions)
//        bindSpinnerAdapter(binding.viewGroupS1.spGender, UiData.genderOptions)
//        bindSpinnerAdapter(binding.viewGroupS1.spMaritalStatus, UiData.maritalStatusOptions)
//        bindSpinnerAdapter(binding.viewGroupS1.spLegalStatus, UiData.legalStatusOptions)
//        bindSpinnerAdapter(binding.viewGroupS2.spMainSourceOfIncome, UiData.mainIncomeOptions)
//        bindSpinnerAdapter(binding.viewGroupS2.spCountryName, UiData.countryNameOptions)
//        bindSpinnerAdapter(binding.viewGroupS2.spStateName, UiData.stateNameOptions)

        onPopulateView()
    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

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

        onReinstateData(interactor?.getRootForm()?.form1)


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

//        spCountryName.setSelection(1)
//        spStateName.setSelection(1)
        etPayamName.setText(form.payamName)
        etBomaName.setText(form.bomaName)

        setSpinnerItem(spCountryName, UiData.countryNameOptions, form.countryName)
        setSpinnerItem(spStateName, UiData.stateNameOptions, form.stateName)
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

        form.countryName = chkSpinner(spCountryName, UiData.ER_SP_DF)
        form.stateName = chkSpinner(spStateName, UiData.ER_SP_DF)

        form.payamName = chkEditText(etPayamName, UiData.ER_ET_DF)
        form.bomaName = chkEditText(etBomaName, UiData.ER_ET_DF)


        if (!form.isOk()) {
            return
        }

        onValidated(form)
    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

        spCountryName.setSelection(1)
        spStateName.setSelection(1)
        etPayamName.setText("Payma")
        etBomaName.setText("Boma")


    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

        val form = interactor?.getRootForm()?.form1
        if (form == null) return

        setSpinnerItem(spCountryName, UiData.countryNameOptions, form.countryName)
        setSpinnerItem(spStateName, UiData.stateNameOptions, form.stateName)

    }


}
