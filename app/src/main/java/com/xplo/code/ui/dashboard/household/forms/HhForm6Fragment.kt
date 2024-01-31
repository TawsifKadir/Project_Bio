package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.isNo
import com.xplo.code.core.ext.isYes
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhForm6NomineeBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.getNomineeNumber
import com.xplo.code.ui.dashboard.model.isOk
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 5/15/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
@AndroidEntryPoint
class HhForm6Fragment : BasicFormFragment(), HouseholdContract.Form6View {

    companion object {
        const val TAG = "HhForm6Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm6Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm6Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm6NomineeBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


    private lateinit var layoutList: LinearLayout
    //private lateinit var btAdd: Button
    //var buttonSubmitList: Button? = null

    //var teamList: List<String> = arrayListOf()
    //var cricketersList: ArrayList<Cricketer> = ArrayList<Cricketer>()


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
        binding = FragmentHhForm6NomineeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {

        layoutList = binding.viewNominee
    }

    override fun initView() {

        bindSpinnerData(binding.spReasonNoNominee, UiData.stateNameOptions)

    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        binding.rgNomineeAdd.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.rbYes -> onDecisionAddNominee(true)
                R.id.rbNo -> onDecisionAddNominee(false)
            }
        }

        binding.btAdd.setOnClickListener {
            onClickAddNominee()
        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Nominee")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()

        onReinstateData(interactor?.getRootForm()?.form6)
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm6?) {
        Log.d(TAG, "onValidated() called with: form = $form")

        val rootForm = interactor?.getRootForm()
        rootForm?.form6 = form
        interactor?.setRootForm(rootForm)

        Log.d(TAG, "onValidated: $rootForm")

        interactor?.navigateToPreview()
    }

    override fun onReinstateData(form: HhForm6?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")
        if (form == null) return

        if (form.isNomineeAdd.isYes()) {

            onDecisionAddNominee(true)
            //onClickAddNominee()

            addAllNomineeViews(form.nominees)


        } else {
            onDecisionAddNominee(false)
        }

    }

    private fun addAllNomineeViews(nominees: ArrayList<Nominee>?) {
        Log.d(TAG, "addAllNomineeViews() called with: nominees = ${nominees?.size}")
        if (nominees.isNullOrEmpty()) return
        for ((i, item) in nominees.withIndex()) {
            addNomineeView(i + 1, item)
        }
    }

    override fun onDecisionAddNominee(isAdd: Boolean) {
        Log.d(TAG, "onDecisionAddNominee() called with: isAdd = $isAdd")

        if (isAdd) {
            binding.viewNominee.visible()
            binding.btAdd.visible()
            binding.viewReasonNoNominee.gone()
        } else {
            binding.viewNominee.gone()
            binding.btAdd.gone()
            binding.viewReasonNoNominee.visible()
        }

    }

    override fun onClickAddNominee() {
        val rootForm = interactor?.getRootForm()
        if (rootForm == null) return

        addNomineeView(rootForm.form6.getNomineeNumber(), null)
    }

    override fun onAddNominee(number: Int) {
//        when (number) {
//            1 -> binding.nominee1.root.visible()
//            2 -> binding.nominee2.root.visible()
//            3 -> binding.nominee3.root.visible()
//            4 -> binding.nominee4.root.visible()
//            5 -> binding.nominee5.root.visible()
//        }
    }

    override fun onHideNominee(number: Int) {
//        when (number) {
//            1 -> binding.nominee1.root.gone()
//            2 -> binding.nominee2.root.gone()
//            3 -> binding.nominee3.root.gone()
//            4 -> binding.nominee4.root.gone()
//            5 -> binding.nominee5.root.gone()
//        }
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()
        onReadInput()
    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

        val form = HhForm6()
        form.isNomineeAdd = chkRadioGroup(binding.rgNomineeAdd, UiData.ER_ET_DF)

        if (form.isNomineeAdd.isNo()) {
            form.noNomineeReason = chkSpinner(binding.spReasonNoNominee, UiData.ER_SP_DF)
            if (form.isOk()) {
                onValidated(form)
                return
            }
        }

        for (i in 0 until layoutList.childCount) {
            Log.d(TAG, "onReadInput: i: $i")

            val rowView = layoutList.getChildAt(i)

            val etFirstName: EditText = rowView.findViewById(R.id.etFirstName)
            val etMiddleName: EditText = rowView.findViewById(R.id.etMiddleName)
            val etLastName: EditText = rowView.findViewById(R.id.etLastName)
            val etAge: EditText = rowView.findViewById(R.id.etAge)

            val spRelation: Spinner = rowView.findViewById(R.id.spRelation)
            val spGender: Spinner = rowView.findViewById(R.id.spGender)
            val spOccupation: Spinner = rowView.findViewById(R.id.spOccupation)

            val rgReadWrite: RadioGroup = rowView.findViewById(R.id.rgReadWrite)
            val rbReadWriteYes: RadioButton = rowView.findViewById(R.id.rbReadWriteYes)
            val rbReadWriteNo: RadioButton = rowView.findViewById(R.id.rbReadWriteNo)

            //val firstName = etFirstName.text.toString()

            var nominee = Nominee()

            nominee.firstName = chkEditText(etFirstName, UiData.ER_ET_DF)
            nominee.lastName = chkEditText(etLastName, UiData.ER_ET_DF)
            nominee.age = chkEditText(etAge, UiData.ER_ET_DF)?.toInt()

            nominee.relation = chkSpinner(spRelation, UiData.ER_SP_DF)
            nominee.gender = chkSpinner(spGender, UiData.ER_SP_DF)
            nominee.occupation = chkSpinner(spOccupation, UiData.ER_SP_DF)

//            if (firstName.isNotEmpty()) {
//                nominee = Nominee(firstName = firstName)
//            }

            if (nominee.isOk()) {
                form.nominees.add(nominee)
            }


        }


        if (form.isOk()) {
            onValidated(form)
        }


    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")
    }


    private fun addNomineeView(number: Int, nominee: Nominee?) {

        val rowView: View = layoutInflater.inflate(R.layout.row_nominee_add, null, false)

        val btRemove: ImageButton = rowView.findViewById(R.id.btRemove)
        val tvHeader: TextView = rowView.findViewById(R.id.tvHeader)

        val etFirstName: EditText = rowView.findViewById(R.id.etFirstName)
        val etMiddleName: EditText = rowView.findViewById(R.id.etMiddleName)
        val etLastName: EditText = rowView.findViewById(R.id.etLastName)
        val etAge: EditText = rowView.findViewById(R.id.etAge)

        val spRelation: Spinner = rowView.findViewById(R.id.spRelation)
        val spGender: Spinner = rowView.findViewById(R.id.spGender)
        val spOccupation: Spinner = rowView.findViewById(R.id.spOccupation)

        val rgReadWrite: RadioGroup = rowView.findViewById(R.id.rgReadWrite)
        val rbReadWriteYes: RadioButton = rowView.findViewById(R.id.rbReadWriteYes)
        val rbReadWriteNo: RadioButton = rowView.findViewById(R.id.rbReadWriteNo)

        bindSpinnerData(spRelation, UiData.relationshipOptions)
        bindSpinnerData(spGender, UiData.genderOptions)
        bindSpinnerData(spOccupation, UiData.genderOptions)

        btRemove.setOnClickListener {
            removeView(rowView)
        }

        when (number) {
            1 -> {
                tvHeader.text = "First Nominee"
            }

            2 -> tvHeader.text = "Second Nominee"
            3 -> tvHeader.text = "Third Nominee"
            4 -> tvHeader.text = "Fourth Nominee"
            5 -> tvHeader.text = "Fifth Nominee"
        }

        updateView(
            nominee,
            etFirstName,
            etMiddleName,
            etLastName,
            etAge,
            spRelation,
            spGender,
            spOccupation,
            rgReadWrite,
            rbReadWriteYes,
            rbReadWriteNo
        )
        layoutList.addView(rowView)
    }

    private fun removeView(view: View) {
        layoutList.removeView(view)
    }

    private fun updateView(
        nominee: Nominee?,
        etFirstName: EditText,
        etMiddleName: EditText,
        etLastName: EditText,
        etAge: EditText,
        spRelation: Spinner,
        spGender: Spinner,
        spOccupation: Spinner,
        rgReadWrite: RadioGroup,
        rbReadWriteYes: RadioButton,
        rbReadWriteNo: RadioButton
    ) {
        Log.d(TAG, "updateView() called with: nominee = $nominee, etFirstName = $etFirstName")
        if (nominee == null) return

        etFirstName.setText(nominee.firstName)
        etMiddleName.setText(nominee.middleName)
        etLastName.setText(nominee.lastName)
        etAge.setText(nominee.age.toString())

        setSpinnerItem(spRelation, UiData.relationshipOptions, nominee.relation)
        setSpinnerItem(spGender, UiData.genderOptions, nominee.gender)
        setSpinnerItem(spOccupation, UiData.genderOptions, nominee.occupation)

        //checkRbPosNeg()
    }
}
