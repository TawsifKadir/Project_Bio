package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.databinding.FragmentHhForm3HhBdBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm3
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
class HhForm3Fragment : BasicFormFragment(), HouseholdContract.Form3View {

    companion object {
        const val TAG = "HhForm3Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm3Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm3Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm3HhBdBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


    private lateinit var etHouseholdSize: EditText
    private lateinit var etMaleHouseholdMembers: EditText
    private lateinit var etFemaleHouseholdMembers: EditText
    private lateinit var etHouseholdMembers0_2: EditText
    private lateinit var etHouseholdMembers3_5: EditText
    private lateinit var etHouseholdMembers6_17: EditText
    private lateinit var etHouseholdMembers18_35: EditText
    private lateinit var etHouseholdMembers36_45: EditText
    private lateinit var etHouseholdMembers46_64: EditText
    private lateinit var etHouseholdMembers65andAbove: EditText
    private lateinit var etHouseholdMembersWithDisability: EditText
    private lateinit var etHouseholdMembersWithChronicallyIll: EditText


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
        binding = FragmentHhForm3HhBdBinding.inflate(inflater, container, false)
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


        etHouseholdSize = binding.etHouseholdSize
        etMaleHouseholdMembers = binding.etMaleHouseholdMembers
        etFemaleHouseholdMembers = binding.etFemaleHouseholdMembers
        etHouseholdMembers0_2 = binding.etHouseholdMembers02
        etHouseholdMembers3_5 = binding.etHouseholdMembers35
        etHouseholdMembers6_17 = binding.etHouseholdMembers617
        etHouseholdMembers18_35 = binding.etHouseholdMembers1835
        etHouseholdMembers36_45 = binding.etHouseholdMembers3645
        etHouseholdMembers46_64 = binding.etHouseholdMembers4664
        etHouseholdMembers65andAbove = binding.etHouseholdMembers65andAbove
        etHouseholdMembersWithDisability = binding.etHouseholdMembersWithDisability
        etHouseholdMembersWithChronicallyIll = binding.etHouseholdMembersWithChronicallyIll

    }

    override fun initView() {

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

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Household Breakdown")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm3?) {
        Log.d(TAG, "onValidated() called with: form = $form")
        //showToast(form.toString())

        val rootForm = interactor?.getRootForm()
        rootForm?.form3 = form
        interactor?.setRootForm(rootForm)


        interactor?.navigateToForm4()
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToForm4()
//        if (!TestConfig.isValidationEnabled) {
//            interactor?.navigateToForm4()
//            return
//        }
        onReadInput()
    }

    override fun onReadInput() {
        Log.d(TAG, "onValidation() called")

        val form = HhForm3()

        form.householdSize = chkEditText(etHouseholdSize, UiData.ER_ET_DF)?.toInt()
        form.maleHouseholdMembers = chkEditText(etMaleHouseholdMembers, UiData.ER_ET_DF)?.toInt()
        form.femaleHouseholdMembers =
            chkEditText(etFemaleHouseholdMembers, UiData.ER_ET_DF)?.toInt()
        form.householdMembers0_2 = chkEditText(etHouseholdMembers0_2, UiData.ER_ET_DF)?.toInt()
        form.householdMembers3_5 = chkEditText(etHouseholdMembers3_5, UiData.ER_ET_DF)?.toInt()
        form.householdMembers6_17 = chkEditText(etHouseholdMembers6_17, UiData.ER_ET_DF)?.toInt()
        form.householdMembers18_35 = chkEditText(etHouseholdMembers18_35, UiData.ER_ET_DF)?.toInt()
        form.householdMembers36_45 = chkEditText(etHouseholdMembers36_45, UiData.ER_ET_DF)?.toInt()
        form.householdMembers46_64 = chkEditText(etHouseholdMembers46_64, UiData.ER_ET_DF)?.toInt()
        form.householdMembers65andAbove =
            chkEditText(etHouseholdMembers65andAbove, UiData.ER_ET_DF)?.toInt()
        form.householdMembersWithDisability =
            chkEditText(etHouseholdMembersWithDisability, UiData.ER_ET_DF)?.toInt()
        form.householdMembersWithChronicallyIll =
            chkEditText(etHouseholdMembersWithChronicallyIll, UiData.ER_ET_DF)?.toInt()

        if (!form.isOk()) {
            return
        }

        onValidated(form)

    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

        etHouseholdSize.setText("12")
        etMaleHouseholdMembers.setText("12")
        etFemaleHouseholdMembers.setText("12")
        etHouseholdMembers0_2.setText("2")
        etHouseholdMembers3_5.setText("4")
        etHouseholdMembers6_17.setText("2")
        etHouseholdMembers18_35.setText("1")
        etHouseholdMembers36_45.setText("1")
        etHouseholdMembers46_64.setText("2")
        etHouseholdMembers65andAbove.setText("12")
        etHouseholdMembersWithDisability.setText("2")
        etHouseholdMembersWithChronicallyIll.setText("2")
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

    }


}
