package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.checkRbOpAB
import com.xplo.code.databinding.FragmentHhForm2PerInfoBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm2
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
class HhForm2Fragment : BasicFormFragment(), HouseholdContract.Form2View {

    companion object {
        const val TAG = "HhForm2Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm2Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm2Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm2PerInfoBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null

    private lateinit var spMainSourceOfIncome: Spinner
    private lateinit var spCurrency: Spinner
    private lateinit var spGender: Spinner
    private lateinit var spRespondentRlt: Spinner
    private lateinit var spMaritalStatus: Spinner
    private lateinit var spLegalStatus: Spinner
    private lateinit var spSelectionReason: Spinner
    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etAge: EditText
    private lateinit var etIdNumber: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etMonthlyAverageIncome: EditText
    private lateinit var etSpouseName: EditText
    private lateinit var rgSelectionCriteria: RadioGroup


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
        binding = FragmentHhForm2PerInfoBinding.inflate(inflater, container, false)
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


        spMainSourceOfIncome = binding.spMainSourceOfIncome
        spCurrency = binding.spCurrency
        spGender = binding.spGender
        spRespondentRlt = binding.spRespondentRlt
        spMaritalStatus = binding.spMaritalStatus
        spLegalStatus = binding.spLegalStatus
        spSelectionReason = binding.spSelectionReason
        etFirstName = binding.etFirstName
        etMiddleName = binding.etMiddleName
        etLastName = binding.etLastName
        etAge = binding.etAge
        etIdNumber = binding.etIdNumber
        etPhoneNumber = binding.etPhoneNumber
        etMonthlyAverageIncome = binding.etMonthlyAverageIncome
        //etSpouseName = binding.etSpouseName
        rgSelectionCriteria = binding.rgSelectionCriteria
    }

    override fun initView() {

        bindSpinnerData(spMainSourceOfIncome, UiData.mainIncomeOptions)
        bindSpinnerData(spCurrency, UiData.currency)
        bindSpinnerData(spGender, UiData.genderOptions)
        bindSpinnerData(spRespondentRlt, UiData.relationshipOptions)
        bindSpinnerData(spMaritalStatus, UiData.maritalStatusOptions)
        bindSpinnerData(spLegalStatus, UiData.legalStatusOptions)
        bindSpinnerData(spSelectionReason, UiData.selectionReason)

    }

    override fun initObserver() {
        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }


//        if (BuildConfig.DEBUG) {
//            binding.viewButtonBackNext.btNext.setOnLongClickListener {
//                onGenerateDummyInput()
//                return@setOnLongClickListener true
//            }
//        }

        onLongClickDataGeneration()
        onGenerateDummyInput()

        spMaritalStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem.equals("Married", ignoreCase = true)) {
                    binding.llspouse1.visibility = View.VISIBLE
                    binding.llspouse2.visibility = View.VISIBLE
                } else {
                    binding.llspouse1.visibility = View.GONE
                    binding.llspouse2.visibility = View.GONE
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Personal Information Registration")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()

        onReinstateData(interactor?.getRootForm()?.form2)

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm2?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        //showToast(form.toString())

        val rootForm = interactor?.getRootForm()
        rootForm?.form2 = form
        interactor?.setRootForm(rootForm)

        Log.d(TAG, "onValidated: $rootForm")

        interactor?.navigateToForm3()
    }

    override fun onReinstateData(form: HhForm2?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")
        if (form == null) return

        setSpinnerItem(spMainSourceOfIncome, UiData.mainIncomeOptions, form.mainSourceOfIncome)
        setSpinnerItem(spCurrency, UiData.countryNameOptions, form.currency)
        setSpinnerItem(spGender, UiData.genderOptions, form.gender)
        setSpinnerItem(spRespondentRlt, UiData.relationshipOptions, form.respondentRlt)
        setSpinnerItem(spMaritalStatus, UiData.maritalStatusOptions, form.maritalStatus)
        setSpinnerItem(spLegalStatus, UiData.legalStatusOptions, form.legalStatus)
        setSpinnerItem(spSelectionReason, UiData.selectionReason, form.selectionReason)

        rgSelectionCriteria.checkRbOpAB(binding.rbA, binding.rbB, form.selectionCriteria)

        etFirstName.setText(form.firstName)
        etMiddleName.setText(form.middleName)
        etLastName.setText(form.lastName)
        etAge.setText(form.age)
        etIdNumber.setText(form.idNumber)
        etPhoneNumber.setText(form.phoneNumber)
        etMonthlyAverageIncome.setText(form.monthlyAverageIncome)
        //etSpouseName.setText(form.spouseName)

    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToForm3()

//        if (!TestConfig.isValidationEnabled) {
//            interactor?.navigateToForm3()
//            return
//        }

        onReadInput()
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

        val form = HhForm2()

        form.mainSourceOfIncome = chkSpinner(spMainSourceOfIncome, UiData.ER_SP_DF)
        form.currency = chkSpinner(spCurrency, UiData.ER_SP_DF)
        form.gender = chkSpinner(spGender, UiData.ER_SP_DF)
        form.respondentRlt = chkSpinner(spRespondentRlt, UiData.ER_SP_DF)
        form.maritalStatus = chkSpinner(spMaritalStatus, UiData.ER_SP_DF)
        form.legalStatus = chkSpinner(spLegalStatus, UiData.ER_SP_DF)
        form.selectionReason = chkSpinner(spSelectionReason, UiData.ER_SP_DF)

//        form.firstName = chkEditText(etFirstName, UiData.ER_SP_DF)
//        form.middleName = chkEditText(etMiddleName, UiData.ER_SP_DF)
//        form.lastName = chkEditText(etLastName, UiData.ER_SP_DF)

        form.firstName = chkEditText3Char(etFirstName, UiData.ER_ET_DF)
        //form.middleName = chkEditText(etMiddleName, UiData.ER_ET_DF)
        form.lastName = chkEditText3Char(etLastName, UiData.ER_ET_DF)

        form.age = chkEditTextMax3Digit(etAge, UiData.ER_ET_DF)
        //form.idNumber = chkEditText(etIdNumber, UiData.ER_ET_DF)
        form.phoneNumber = chkEditText(etPhoneNumber, UiData.ER_ET_DF)
        form.monthlyAverageIncome = chkEditText(etMonthlyAverageIncome, UiData.ER_ET_DF)
        //form.spouseName = chkEditText(etSpouseName, UiData.ER_ET_DF)
        form.selectionCriteria = chkRadioGroup(rgSelectionCriteria, UiData.ER_RB_DF)


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

        spMainSourceOfIncome.setSelection(1)
        spGender.setSelection(1)
        spRespondentRlt.setSelection(1)
        spMaritalStatus.setSelection(1)
        spLegalStatus.setSelection(1)
        spSelectionReason.setSelection(1)
        spCurrency.setSelection(1)

        etFirstName.setText("Mohd")
        etMiddleName.setText("Moniruzzaman")
        etLastName.setText("Shadhin")
        etAge.setText("33")
        etIdNumber.setText("12")
        etPhoneNumber.setText("01672708329")
        etMonthlyAverageIncome.setText("5000")
        //etSpouseName.setText("Yesmin")

        rgSelectionCriteria.check(R.id.rbA)
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

    }


}
