package com.xplo.code.ui.dashboard.household.forms.nominee

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.fragment.app.viewModels
import com.kit.integrationmanager.model.OccupationEnum
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.xplo.code.BuildConfig
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.toBool
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.BsdNomineeInputBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.HhForm6Nominee2Fragment
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.isOk

import dagger.hilt.android.AndroidEntryPoint

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/15/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class NomineeInputFragment : BasicFormFragment(), NomineeModalContract.InputView {

    companion object {
        const val TAG = "NomineeInputFragment"

        @JvmStatic
        fun newInstance(
            parent: String?,
            no: Int,
            gender: String?
        ): NomineeInputFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent, no = $no, gender = $gender")
            val fragment = NomineeInputFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putInt(Bk.KEY_NO, no)
            bundle.putString(Bk.KEY_GENDER, gender)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: BsdNomineeInputBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: NomineeModal? = null

    private lateinit var otherRelation: LinearLayout
    private lateinit var etOtherRelation: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etMiddleName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etNickName: EditText
    private lateinit var otherWork: LinearLayout
    private lateinit var etOtherWork: EditText
    private lateinit var etAge: EditText
    private lateinit var spRelation: Spinner
    private lateinit var spGender: Spinner
    private lateinit var spOccupation: Spinner
    private lateinit var rgReadWrite: RadioGroup
    private lateinit var rbReadWriteYes: RadioButton
    private lateinit var rbReadWriteNo: RadioButton

    private var nomineeNo = 0
    private var gender: String? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

//        if (context is NomineeModal) {
//            interactor = context as NomineeModal
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsdNomineeInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        interactor = this.parentFragment as NomineeModal

        arguments?.let {
            nomineeNo = it.getInt(Bk.KEY_NO)
            gender = it.getString(Bk.KEY_GENDER)
        }

        etFirstName = binding.include.etFirstName
        etMiddleName = binding.include.etMiddleName
        etLastName = binding.include.etLastName
        etNickName = binding.include.etNickName
        etAge = binding.include.etAge
        spRelation = binding.include.spRelation
        spGender = binding.include.spGender
        spOccupation = binding.include.spOccupation
        rgReadWrite = binding.include.rgReadWrite
        rbReadWriteYes = binding.include.rbReadWriteYes
        rbReadWriteNo = binding.include.rbReadWriteNo
        etOtherRelation = binding.include.etOtherRelation
        otherRelation = binding.include.otherRelation
        otherWork = binding.include.otherWork
        etOtherWork = binding.include.etOtherWork
    }

    override fun initView() {

        binding.tvHeader.text = getNomineeHeader(nomineeNo)

        bindSpinnerData(spRelation, UiData.relationshipOptions)
        bindSpinnerData(spGender, UiData.genderOptions)
        bindSpinnerData(spOccupation, UiData.nomineeOccupation)
    }

    override fun initObserver() {
        binding.btNext.setOnClickListener {
            onReadInput()
        }
        binding.include.rgReadWrite.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbReadWriteYes -> {
                    (group.getChildAt(group.childCount - 1) as RadioButton).error = null
                }
                R.id.rbReadWriteNo -> {
                    (group.getChildAt(group.childCount - 1) as RadioButton).error = null
                }
            }
        }

        spRelation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if(selectedItem.equals(NonPerticipationReasonEnum.REASON_OTHER.value, ignoreCase = true)){
                    otherRelation.visible()
                }else{
                    Log.d(HhForm6Nominee2Fragment.TAG,"Selected Spinner Other not selected")
                    etOtherRelation.setText("")
                    otherRelation.gone()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spOccupation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if(selectedItem.contains("Other", ignoreCase = true)){
                    otherWork.visible()
                }else{
                    Log.d(NomineeInputFragment.TAG,"Selected Spinner Other not selected")
                    etOtherWork.setText("")
                    otherWork.gone()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        onLongClickDataGeneration()
        onGenerateDummyInput()

        if (gender?.isNotBlank().toBool()) {
            setSpinnerItem(spGender, UiData.genderOptions, gender)
            spGender.isEnabled = false
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


    override fun onGetNomineeSuccess(item: Nominee?) {
        Log.d(TAG, "onGetNomineeSuccess() called with: item = $item")
        interactor?.onCompleteModal(item)
    }

    override fun onGetNomineeFailure(msg: String?) {
        Log.d(TAG, "onGetNomineeFailure() called with: msg = $msg")
        interactor?.onCompleteModal(null)
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

        var nominee = Nominee()

        nominee.firstName = chkEditText3Char(etFirstName, UiData.ER_ET_DF)
        nominee.middleName = chkEditText3Char(etMiddleName, UiData.ER_ET_DF)
        nominee.lastName = chkEditText3Char(etLastName, UiData.ER_ET_DF)
        nominee.nickName = chkEditTextNickName3Char(etNickName, UiData.ER_ET_DF)
        nominee.age = chkAge(etAge, UiData.ER_ET_DF)?.toInt()
        nominee.relation = chkSpinner(spRelation, UiData.ER_SP_DF)
        if(nominee.relation.equals(RelationshipEnum.OTHER.value, ignoreCase = true)){
            nominee.relationOthers = chkEditText3CharAllowSpace(etOtherRelation, UiData.ER_ET_DF)
        }
        nominee.gender = chkSpinner(spGender, UiData.ER_SP_DF)
        nominee.occupation = chkSpinner(spOccupation, UiData.ER_SP_DF)
        if(nominee.occupation.equals(OccupationEnum.OTHER.value, ignoreCase = true)){
            nominee.occupationOthers = chkEditText3CharAllowSpace(etOtherWork, UiData.ER_ET_DF)
        }
        nominee.isReadWrite = chkRadioGroup(rgReadWrite, UiData.ER_RB_DF)

        if (nominee.isOk()) {
            onGetNomineeSuccess(nominee)
        }

        //onGetNomineeSuccess(Nominee(firstName = "jalal"))
    }

    override fun onLongClickDataGeneration() {
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isLongClickDGEnabled) return

        binding.btNext.setOnLongClickListener {
            onGenerateDummyInput()
            return@setOnLongClickListener true
        }
    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

//        etFirstName.setText("Ruben")
//        etMiddleName.setText("middle")
//        etLastName.setText("Dias")
//        etAge.setText("20")
//        spRelation.setSelection(2)
//        spGender.setSelection(2)
//        spOccupation.setSelection(2)
//        rgReadWrite.check(rbReadWriteNo.id)
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")
    }

    private fun getNomineeHeader(number: Int): String {
        when (number) {
            1 -> return "First Nominee"
            2 -> return "Second Nominee"
            3 -> return "Third Nominee"
            4 -> return "Fourth Nominee"
            5 -> return "Fifth Nominee"
            else -> return "Nominee $number"
        }
    }
}
