package com.xplo.code.ui.dashboard.alternate.forms


import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.api.Distribution.BucketOptions.Linear
import com.kit.integrationmanager.model.IDtypeEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.checkRbOpAB
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.toBool
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentAlForm1PerInfoBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.list.CheckboxListAdapter
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.CheckboxItem
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.code.ui.dashboard.model.isOk

import com.xplo.code.BuildConfig
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
class AlForm1Fragment : BasicFormFragment(), AlternateContract.Form1View , CheckboxListAdapter.OnItemClickListener{

    companion object {
        const val TAG = "AlForm1Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?,
            id: String?
        ): AlForm1Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent, id = $id")
            val fragment = AlForm1Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putString(Bk.KEY_ID, id)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlForm1PerInfoBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null

    private var id: String? = null

    private lateinit var idType: LinearLayout
    private lateinit var alternateOtherRelation: LinearLayout
    private lateinit var etHouseholdName: EditText
    private lateinit var etAlternateFirstName: EditText
    private lateinit var etAlternateMiddleName: EditText
    private lateinit var etAlternateLastName: EditText
    private lateinit var etAlternateNickName : EditText
    private lateinit var spGender: Spinner
    private lateinit var spAlternateRelation: Spinner
    private lateinit var etPhoneNo: EditText
    private lateinit var etIdNumber: EditText
    private lateinit var etAge: EditText
    private lateinit var etIdType: EditText
    private lateinit var etothers: EditText
    private lateinit var spIdType: Spinner
    private lateinit var rgId: RadioGroup
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
        binding = FragmentAlForm1PerInfoBinding.inflate(inflater, container, false)
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

        if (arguments != null) {
            id = arguments?.getString(Bk.KEY_ID)
        }

        spGender = binding.spGender
        spAlternateRelation = binding.spAlternateRelation
        etPhoneNo = binding.etPhoneNo
        etIdNumber = binding.etIdNumber
        etAge = binding.etAge
        spIdType = binding.spIdType
        rgId = binding.rgId
        idType = binding.IdType
        alternateOtherRelation = binding.otherAlternateRelation
        etHouseholdName = binding.etHouseholdName
        etAlternateFirstName = binding.etAlternateFirstName
        etAlternateMiddleName = binding.etAlternateMiddleName
        etAlternateLastName = binding.etAlternateLastName
        etAlternateNickName =binding.etAlternateNickName
        etIdType = binding.etIDType
        etothers = binding.etotherstext
    }

    override fun initView() {

        //etName.setText(id)
        //etName.isEnabled = false

        bindSpinnerData(spGender, UiData.genderOptions)
        bindSpinnerData(spAlternateRelation, UiData.relationshipOptions)
        bindSpinnerData(spIdType, UiData.idType)
        idType.gone()
        // has no id, passed name instead of id
        if (interactor?.isCallForResult().toBool()) {
            binding.etHouseholdName.setText(id)
            return
        }

        viewModel.getHouseholdItem(id)


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemSuccess -> {
                        hideLoading()
                        onGetHouseholdItem(event.item)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemFailure -> {
                        hideLoading()
                        onGetHouseholdItemFailure(event.msg)
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

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }


        binding.rgId.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbYes -> doSomethingForYes()
                R.id.rbNo -> doSomethingForNo()
                else -> {}
            }
        }
        spIdType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                idType.gone()
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem.equals(UiData.idType[2], ignoreCase = true)) { //NationalID selected
//                    etIdNumber.inputType = InputType.TYPE_CLASS_TEXT
                    etIdNumber.inputType = InputType.TYPE_CLASS_NUMBER
                    etIdNumber.setText("")
                } else if (selectedItem.equals(UiData.idType[1], ignoreCase = true)){ // Passport Selected
//                    etIdNumber.inputType = InputType.TYPE_CLASS_NUMBER
                    etIdNumber.inputType = InputType.TYPE_CLASS_TEXT
                    etIdNumber.setText("")
                }else if (selectedItem.equals(UiData.idType[3], ignoreCase = true)) {  ///Other Selected
//                    etIdNumber.inputType = InputType.TYPE_CLASS_NUMBER
                    idType.visible()
                    etIdType.setText("")
                    etIdNumber.inputType = InputType.TYPE_CLASS_TEXT
                    etIdNumber.setText("")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
        spAlternateRelation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem.equals("Other", ignoreCase = true)) {
                    binding.otherAlternateRelation.visible()
                } else {
                    binding.otherAlternateRelation.gone()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    fun doSomethingForYes() {
        binding.llIdType.isVisible = true
        binding.llIdTypeInput.isVisible = true
    }

    fun doSomethingForNo() {
        binding.llIdType.isVisible = false
        binding.llIdTypeInput.isVisible = false
    }
    override fun onPause() {
        super.onPause()
        //EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        //EventBus.getDefault().register(this)
        setToolbarTitle("Alternate Registration")

        binding.viewButtonBackNext.btBack.gone()
        binding.viewButtonBackNext.btNext.visible()


    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
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
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

        val form = AlForm1()

        form.age = chkEditText(etAge, UiData.ER_ET_DF)?.toInt() ?: 0
        form.idNumber = chkEditText(etIdNumber, UiData.ER_ET_DF)

        //form.phoneNumber = chkPhoneNumber(etPhoneNo, UiData.ER_ET_DF)
        form.phoneNumber = etPhoneNo.text.toString()
        if(spAlternateRelation.selectedItem.toString().equals(RelationshipEnum.OTHER.value, ignoreCase = true)){
            form.selectAlternateRlt = etothers.text.toString()
        }else{
            form.selectAlternateRlt = chkSpinner(spAlternateRelation, UiData.ER_SP_DF)
        }
        form.gender = chkSpinner(spGender, UiData.ER_SP_DF)
        Log.d(TAG,"Number = ${form.idNumber}")
        Log.d(TAG,"Edit text = $etIdNumber")

        if (binding.llIdTypeInput.isVisible && binding.llIdType.isVisible) {
            if (spIdType.selectedItem.toString().equals(IDtypeEnum.OTHERS.value, ignoreCase = true)){
                form.idNumberType = etIdType.text.toString()
            }else{
                form.idNumberType = chkSpinner(spIdType, UiData.ER_SP_DF)
            }
            form.idNumber = checkIDNumber(etIdNumber, UiData.ER_ET_DF, form.idNumberType)
        } else {
            form.idNumber = null
            form.idNumberType = null
        }
        form.idIsOrNot = chkRadioGroup(rgId, UiData.ER_RB_DF)

        form.householdName = etHouseholdName.text.toString()

        form.alternateFirstName = chkEditText3Char(etAlternateFirstName, UiData.ER_ET_DF)
        form.alternateMiddleName =  chkEditText3Char(etAlternateMiddleName, UiData.ER_ET_DF)
        form.alternateLastName = chkEditText3Char(etAlternateLastName, UiData.ER_ET_DF)
        form.alternateNickName = chkEditTextNickName3Char(etAlternateNickName,UiData.ER_ET_DF)

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
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

        spGender.setSelection(2)
        spAlternateRelation.setSelection(2)

        //etName.setText("Shadhin")
        etAge.setText("29")
        etIdNumber.setText("122")
        etPhoneNo.setText("01829372012")
        spIdType.setSelection(1)

        //etHouseholdName.setText("Mohd")

        etAlternateFirstName.setText("Mohd")
        etAlternateMiddleName.setText("Moniruzzaman")
        etAlternateLastName.setText("Shadhin")
    }

    override fun onPopulateView() {
        TODO("Not yet implemented")
    }

    override fun onValidated(form: AlForm1?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        //showToast(form.toString())

        val rootForm = interactor?.getRootForm()
        rootForm?.form1 = form
        interactor?.setRootForm(rootForm)

        Log.d(TAG, "onValidated: $rootForm")
        interactor?.navigateToForm2()
    }

    override fun onReinstateData(form: AlForm1?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")
        if (form == null) return

        setSpinnerItem(spGender, UiData.genderOptions, form.gender)
        setSpinnerItem(spAlternateRelation, UiData.relationshipOptions, form.selectAlternateRlt)
        setSpinnerItem(spIdType, UiData.idType, form.idNumberType)
        rgId.checkRbOpAB(binding.rbYes, binding.rbNo, form.idIsOrNot)
        etAge.setText(form.age.toString())
        etIdNumber.setText(form.idNumber)
        etPhoneNo.setText(form.phoneNumber)

        etHouseholdName.setText(form.householdName)

        etAlternateFirstName.setText(form.alternateFirstName)
        etAlternateMiddleName.setText(form.alternateMiddleName)
        etAlternateLastName.setText(form.alternateLastName)
        etAlternateNickName.setText(form.alternateNickName)

    }

    override fun onGetHouseholdItem(item: HouseholdItem?) {
        Log.d(TAG, "onGetHouseholdItem() called with: item = $item")

        interactor?.setHouseholdItem(item)

        binding.etHouseholdName.setText(item.toHouseholdForm()?.form2?.getFullName())
    }

    override fun onGetHouseholdItemFailure(msg: String?) {
        Log.d(TAG, "onGetHouseholdItemFailure() called with: msg = $msg")
    }

    override fun onStatusChangeCheckboxItem(item: CheckboxItem, pos: Int, isChecked: Boolean) {
        TODO("Not yet implemented")
    }

}
