package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.checkRbOpAB
import com.xplo.code.core.ext.checkRbOpABforIDcard
import com.xplo.code.core.ext.getString
import com.xplo.code.databinding.FragmentHhForm2PerInfoBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.list.CheckboxListAdapter
import com.xplo.code.ui.dashboard.model.CheckboxItem
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.checkExtraCases
import com.xplo.code.ui.dashboard.model.isOk
import com.xplo.code.utils.MaritalStatus
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
class HhForm2PerInfoFragment : BasicFormFragment(), HouseholdContract.Form2View , CheckboxListAdapter.OnItemClickListener{

    companion object {
        const val TAG = "HhForm2PerInfoFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm2PerInfoFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm2PerInfoFragment()
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

    private lateinit var spIdType: Spinner
    private lateinit var spMainSourceOfIncome: Spinner

    //private lateinit var spCurrency: Spinner
    private lateinit var spGender: Spinner
    private lateinit var spRespondentRlt: Spinner
    private lateinit var spMaritalStatus: Spinner
    private lateinit var spLegalStatus: Spinner
    private lateinit var spSelectionReason: Spinner
    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etNickName: EditText
    private lateinit var etAge: EditText
    private lateinit var etIdNumber: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etMonthlyAverageIncome: EditText
    private lateinit var etSpouseFirstName: EditText
    private lateinit var etSpouseMiddleName: EditText
    private lateinit var etSpouseLastName: EditText
    private lateinit var etSpouseFourthName : EditText
    private lateinit var rgSelectionCriteria: RadioGroup
    private lateinit var rgId: RadioGroup
    private lateinit var tempFirst : String
    private lateinit var tempSecond : String
    //private lateinit var directRecycler: RecyclerView
    //private lateinit var publicRecycler: RecyclerView

    private var adapterSupportType: CheckboxListAdapter? = null

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


        etSpouseFirstName = binding.etSpouseFirstName
        etSpouseMiddleName = binding.etSpouseMiddleName
        etSpouseLastName = binding.etSpouseLastName
        etSpouseFourthName = binding.etSpouseNickName
        spMainSourceOfIncome = binding.spMainSourceOfIncome
        spIdType = binding.spIdType
        //spCurrency = binding.spCurrency
        spGender = binding.spGender
        spRespondentRlt = binding.spRespondentRlt
        spMaritalStatus = binding.spMaritalStatus
        spLegalStatus = binding.spLegalStatus
        spSelectionReason = binding.spSelectionReason
        etFirstName = binding.etFirstName
        etMiddleName = binding.etMiddleName
        etLastName = binding.etLastName
        etNickName = binding.etNickName
        etAge = binding.etAge
        etIdNumber = binding.etIdNumber
        etPhoneNumber = binding.etPhoneNumber
        etMonthlyAverageIncome = binding.etMonthlyAverageIncome
        //etSpouseName = binding.etSpouseName
        rgSelectionCriteria = binding.rgSelectionCriteria
        rgId = binding.rgId

        //publicRecycler = binding.recycler
        //directRecycler = binding.recycler

        binding.rvSupportType.setHasFixedSize(true)
        binding.rvSupportType.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSupportType.itemAnimator = DefaultItemAnimator()

        adapterSupportType = CheckboxListAdapter()
        adapterSupportType?.setOnItemClickListener(this)
        binding.rvSupportType.adapter = adapterSupportType

        adapterSupportType?.addAll(UiData.getPublicWorks())
    }

    override fun initView() {

        bindSpinnerData(spMainSourceOfIncome, UiData.mainIncomeOptions)
        //bindSpinnerData(spCurrency, UiData.currency)
        bindSpinnerData(spGender, UiData.genderOptions)
        bindSpinnerData(spRespondentRlt, UiData.relationshipOptions)
        bindSpinnerData(spMaritalStatus, UiData.maritalStatusOptions)
        bindSpinnerData(spLegalStatus, UiData.legalStatusOptions)
        bindSpinnerData(spSelectionReason, UiData.selectionReason)
        bindSpinnerData(spIdType, UiData.idType)


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
        spMainSourceOfIncome.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d(TAG,"First item is $parent.getItemAtPosition(0).toString()")
                if ( position == 0 || position == 1 )  {
                    etMonthlyAverageIncome.setText("0")
                    etMonthlyAverageIncome.isEnabled = false
                }
                else{
                    etMonthlyAverageIncome.isEnabled = true
                    //binding.etMonthlyAverageIncome.setText("0")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        spMaritalStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem.equals(MaritalStatus.MARRIED.status,  ignoreCase = true)) {
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

        spIdType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem.equals(UiData.idType[1], ignoreCase = true)) {
                    etIdNumber.inputType = InputType.TYPE_CLASS_TEXT
                } else {
                    etIdNumber.inputType = InputType.TYPE_CLASS_NUMBER
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        binding.rgId.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbYes -> doSomethingForYes()
                R.id.rbNo -> doSomethingForNo()
                else -> {}
            }
        }

        binding.rgSelectionCriteria.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbA -> doSomethingForRbA()
                R.id.rbB -> doSomethingForRbB()
                else -> {}
            }
        }
    }

    fun doSomethingForRbA() {
        adapterSupportType?.addAll(UiData.getPublicWorks())
    }

    fun doSomethingForRbB() {
        adapterSupportType?.addAll(UiData.getDirectIncomeSupport())
    }

    fun doSomethingForYes() {
        binding.llIdType.isVisible = true
        binding.llIdTypeInput.isVisible = true
    }

    fun doSomethingForNo() {
        binding.llIdType.isVisible = false
        binding.llIdTypeInput.isVisible = false
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

        setSpinnerItem(spIdType, UiData.idType, form.idNumberType)
        setSpinnerItem(spMainSourceOfIncome, UiData.mainIncomeOptions, form.mainSourceOfIncome)
        //setSpinnerItem(spCurrency, UiData.countryNameOptions, form.currency)
        setSpinnerItem(spGender, UiData.genderOptions, form.gender)
        setSpinnerItem(spRespondentRlt, UiData.relationshipOptions, form.respondentRlt)
        setSpinnerItem(spMaritalStatus, UiData.maritalStatusOptions, form.maritalStatus)
        setSpinnerItem(spLegalStatus, UiData.legalStatusOptions, form.legalStatus)
        setSpinnerItem(spSelectionReason, UiData.selectionReason, form.selectionReason)

        rgSelectionCriteria.checkRbOpAB(binding.rbA, binding.rbB, form.selectionCriteria)
        rgId.checkRbOpABforIDcard(binding.rbYes, binding.rbNo, form.idIsOrNot)


        if(binding.rbA.isChecked){
            form.itemsSupportType?.let { doSomethingForRbA(it) }
        }else{
            form.itemsSupportType?.let { doSomethingForRbB(it) }
        }

        etFirstName.setText(form.firstName)
        etMiddleName.setText(form.middleName)
        etLastName.setText(form.lastName)
        etNickName.setText(form.nickName)
        etAge.setText(form.age.toString())
        etIdNumber.setText(form.idNumber)
        etPhoneNumber.setText(form.phoneNumber)
        etMonthlyAverageIncome.setText(form.monthlyAverageIncome)
        etSpouseFirstName.setText(form.spouseFirstName)
        etSpouseMiddleName.setText(form.spouseMiddleName)
        etSpouseLastName.setText(form.spouseLastName)
        etSpouseFourthName.setText(form.spouseFourthName)
        //etSpouseName.setText(form.spouseName)

    }
    fun doSomethingForRbA(items: List<CheckboxItem>) {
        var list = UiData.getPublicWorks()
        for (item in list) {
            // Check if the current item's id matches any of the ids in checkedItem
            if (items.any { checkedItem -> checkedItem.id == item.id }) {
                item.isChecked = true
            }
        }
        adapterSupportType?.addAll(list)
    }

    fun doSomethingForRbB(items: List<CheckboxItem>) {
        var list = UiData.getDirectIncomeSupport()
        for (item in list) {
            // Check if the current item's id matches any of the ids in checkedItem
            if (items.any { checkedItem -> checkedItem.id == item.id }) {
                item.isChecked = true
            }
        }
        adapterSupportType?.addAll(list)
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
        //form.currency = chkSpinner(spCurrency, UiData.ER_SP_DF)
        form.gender = chkSpinner(spGender, UiData.ER_SP_DF)
        form.respondentRlt = chkSpinner(spRespondentRlt, UiData.ER_SP_DF)
        form.maritalStatus = chkSpinner(spMaritalStatus, UiData.ER_SP_DF)
        form.legalStatus = chkSpinner(spLegalStatus, UiData.ER_SP_DF)
        form.selectionReason = chkSpinner(spSelectionReason, UiData.ER_SP_DF)

        if (binding.llspouse1.isVisible && binding.llspouse2.isVisible) {
            form.spouseFirstName = chkEditText3Char(etSpouseFirstName, UiData.ER_SP_DF)
            form.spouseMiddleName = chkEditText3Char(etSpouseMiddleName, UiData.ER_SP_DF)
            form.spouseLastName = chkEditText3Char(etSpouseLastName, UiData.ER_SP_DF)
            form.spouseFourthName = chkEditText3Char(etSpouseFourthName,UiData.ER_SP_DF)
        } else {
            form.spouseFirstName = null
            form.spouseMiddleName = null
            form.spouseLastName = null
            form.spouseFourthName = null
        }

        if (binding.llIdTypeInput.isVisible && binding.llIdType.isVisible) {
            form.idNumberType = chkSpinner(spIdType, UiData.ER_SP_DF)
            if(form.idNumberType?.equals("Passport") == true){
                form.idNumber = chkEditTextOnlyNumberAndChar(etIdNumber, UiData.ER_ET_DF)
            }else{
                form.idNumber = chkEditTextOnlyNumber(etIdNumber, UiData.ER_ET_DF)
            }
        } else {
            form.idNumber = null
            form.idNumberType = null
        }

        //Toast.makeText(requireContext(), form.idNumberType, Toast.LENGTH_SHORT).show()
        form.firstName = chkEditText3Char(etFirstName, UiData.ER_ET_DF)
        form.middleName = chkEditText3Char(etMiddleName, UiData.ER_ET_DF)
        form.lastName = chkEditText3Char(etLastName, UiData.ER_ET_DF)
        form.nickName = chkEditText3Char(etNickName, UiData.ER_ET_DF)

        form.age = chkEditTextMax3Digit(etAge, UiData.ER_ET_DF)?.toInt()
        form.phoneNumber = chkEditText(etPhoneNumber, UiData.ER_ET_DF)
        form.monthlyAverageIncome = chkEditTextMonthlyAvgIncome(etMonthlyAverageIncome, UiData.ER_ET_DF)
        //form.spouseName = chkEditText(etSpouseName, UiData.ER_ET_DF)
        form.selectionCriteria = chkRadioGroup(rgSelectionCriteria, UiData.ER_RB_DF)
        form.idIsOrNot = chkRadioGroup(rgId, UiData.ER_RB_DF)

        form.itemsSupportType = adapterSupportType?.getCheckedItems()

        if (!form.isOk()) {
            val checkExtraCases = form.checkExtraCases()
            if (checkExtraCases != null) {
                showAlerter(checkExtraCases, null)
                return
            }
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

        spIdType.setSelection(1)
//        spMainSourceOfIncome.setSelection(1)
//        spGender.setSelection(1)
//        spRespondentRlt.setSelection(1)
//        spMaritalStatus.setSelection(1)
//        spLegalStatus.setSelection(1)
//        spSelectionReason.setSelection(1)
//        //spCurrency.setSelection(1)
//
//        etFirstName.setText("Mohd")
//        etMiddleName.setText("Moniruzzaman")
//        etLastName.setText("Shadhin")
//        etNickName.setText("Bio")
//        etAge.setText("33")
//        etIdNumber.setText("12")
//        etPhoneNumber.setText("01672708329")
//        etMonthlyAverageIncome.setText("5000")
        //etSpouseName.setText("Yesmin")

        rgSelectionCriteria.check(R.id.rbA)
//        adapterSupportType?.addAll(UiData.getPublicWorksDummy())
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        outState.putString("FirstName", etSpouseFirstName.text.toString())
//        Log.d(TAG,etSpouseFirstName.text.toString())
//        outState.putString("SecondName", etSpouseMiddleName.text.toString())
//        outState.putString("ThirdName", etSpouseLastName.text.toString())
//        outState.putString("NickName", etSpouseNickName.text.toString())
//    }

    override fun onStatusChangeCheckboxItem(item: CheckboxItem, pos: Int, isChecked: Boolean) {
        Log.d(
            HhForm2PerInfoFragment.TAG,
            "onStatusChangeCheckboxItem() called with: item = $item, pos = $pos, isChecked = $isChecked"
        )
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        val firstName = savedInstanceState?.getString("FirstName")
//        val secondName = savedInstanceState?.getString("SecondName")
//        val thirdName = savedInstanceState?.getString("ThirdName")
//        val nickName = savedInstanceState?.getString("NickName")
//        if (firstName != null) {
//            Log.d(TAG,firstName)
//        }
//        etSpouseFirstName.setText(firstName)
//        etSpouseMiddleName.setText(secondName)
//        etSpouseLastName.setText(thirdName)
//        etSpouseNickName.setText(nickName)
//
//    }
}
