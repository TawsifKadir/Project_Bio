package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
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
class HhForm3HhBdFragment : BasicFormFragment(), HouseholdContract.Form3View {

    companion object {
        const val TAG = "HhForm3HhBdFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm3HhBdFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm3HhBdFragment()
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

    private lateinit var etMem0NormalMale: EditText
    private lateinit var etMem0DisableMale: EditText
    private lateinit var etMem0IllMale: EditText
    private lateinit var etMem3NormalMale: EditText
    private lateinit var etMem3DisableMale: EditText
    private lateinit var etMem3IllMale: EditText
    private lateinit var etMem6NormalMale: EditText
    private lateinit var etMem6DisableMale: EditText
    private lateinit var etMem6IllMale: EditText
    private lateinit var etMem18NormalMale: EditText
    private lateinit var etMem18DisableMale: EditText
    private lateinit var etMem18IllMale: EditText
    private lateinit var etMem36NormalMale: EditText
    private lateinit var etMem36DisableMale: EditText
    private lateinit var etMem36IllMale: EditText
    private lateinit var etMem65NormalMale: EditText
    private lateinit var etMem65DisableMale: EditText
    private lateinit var etMem65IllMale: EditText

    private lateinit var etMem0NormalFemale: EditText
    private lateinit var etMem0DisableFemale: EditText
    private lateinit var etMem0IllFemale: EditText
    private lateinit var etMem3NormalFemale: EditText
    private lateinit var etMem3DisableFemale: EditText
    private lateinit var etMem3IllFemale: EditText
    private lateinit var etMem6NormalFemale: EditText
    private lateinit var etMem6DisableFemale: EditText
    private lateinit var etMem6IllFemale: EditText
    private lateinit var etMem18NormalFemale: EditText
    private lateinit var etMem18DisableFemale: EditText
    private lateinit var etMem18IllFemale: EditText
    private lateinit var etMem36NormalFemale: EditText
    private lateinit var etMem36DisableFemale: EditText
    private lateinit var etMem36IllFemale: EditText
    private lateinit var etMem65NormalFemale: EditText
    private lateinit var etMem65DisableFemale: EditText
    private lateinit var etMem65IllFemale: EditText

    private lateinit var etReadWriteNumber: EditText
    private lateinit var rgReadWrite: RadioGroup
    private lateinit var rbYes: RadioButton
    private lateinit var rbNo: RadioButton


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
        binding = FragmentHhForm3HhBdBinding.inflate(inflater, container, false)
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


        etHouseholdSize = binding.etHouseholdSize

        etMem0NormalMale = binding.viewHhTable.etMem0NormalMale
        etMem0DisableMale = binding.viewHhTable.etMem0DisableMale
        etMem0IllMale = binding.viewHhTable.etMem0IllMale
        etMem3NormalMale = binding.viewHhTable.etMem3NormalMale
        etMem3DisableMale = binding.viewHhTable.etMem3DisableMale
        etMem3IllMale = binding.viewHhTable.etMem3IllMale
        etMem6NormalMale = binding.viewHhTable.etMem6NormalMale
        etMem6DisableMale = binding.viewHhTable.etMem6DisableMale
        etMem6IllMale = binding.viewHhTable.etMem6IllMale
        etMem18NormalMale = binding.viewHhTable.etMem18NormalMale
        etMem18DisableMale = binding.viewHhTable.etMem18DisableMale
        etMem18IllMale = binding.viewHhTable.etMem18IllMale
        etMem36NormalMale = binding.viewHhTable.etMem36NormalMale
        etMem36DisableMale = binding.viewHhTable.etMem36DisableMale
        etMem36IllMale = binding.viewHhTable.etMem36IllMale
        etMem65NormalMale = binding.viewHhTable.etMem65NormalMale
        etMem65DisableMale = binding.viewHhTable.etMem65DisableMale
        etMem65IllMale = binding.viewHhTable.etMem65IllMale

        etMem0NormalFemale = binding.viewHhTable.etMem0NormalFemale
        etMem0DisableFemale = binding.viewHhTable.etMem0DisableFemale
        etMem0IllFemale = binding.viewHhTable.etMem0IllFemale
        etMem3NormalFemale = binding.viewHhTable.etMem3NormalFemale
        etMem3DisableFemale = binding.viewHhTable.etMem3DisableFemale
        etMem3IllFemale = binding.viewHhTable.etMem3IllFemale
        etMem6NormalFemale = binding.viewHhTable.etMem6NormalFemale
        etMem6DisableFemale = binding.viewHhTable.etMem6DisableFemale
        etMem6IllFemale = binding.viewHhTable.etMem6IllFemale
        etMem18NormalFemale = binding.viewHhTable.etMem18NormalFemale
        etMem18DisableFemale = binding.viewHhTable.etMem18DisableFemale
        etMem18IllFemale = binding.viewHhTable.etMem18IllFemale
        etMem36NormalFemale = binding.viewHhTable.etMem36NormalFemale
        etMem36DisableFemale = binding.viewHhTable.etMem36DisableFemale
        etMem36IllFemale = binding.viewHhTable.etMem36IllFemale
        etMem65NormalFemale = binding.viewHhTable.etMem65NormalFemale
        etMem65DisableFemale = binding.viewHhTable.etMem65DisableFemale
        etMem65IllFemale = binding.viewHhTable.etMem65IllFemale


        etReadWriteNumber = binding.etReadWriteNumber
        rgReadWrite = binding.rgReadWrite
        rbYes = binding.rbYes
        rbNo = binding.rbNo

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

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Household Breakdown")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()

        onReinstateData(interactor?.getRootForm()?.form3)
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

    override fun onReinstateData(form: HhForm3?) {
        Log.d(TAG, "onReinstateData() called with: form = $form")
        if (form == null) return

        etHouseholdSize.setText(form.householdSize.toString())

        etMem0NormalMale.setText(form.mem0NormalMale.toString())
        etMem0DisableMale.setText(form.mem0DisableMale.toString())
        etMem0IllMale.setText(form.mem0IllMale.toString())
        etMem3NormalMale.setText(form.mem3NormalMale.toString())
        etMem3DisableMale.setText(form.mem3DisableMale.toString())
        etMem3IllMale.setText(form.mem3IllMale.toString())
        etMem6NormalMale.setText(form.mem6NormalMale.toString())
        etMem6DisableMale.setText(form.mem6DisableMale.toString())
        etMem6IllMale.setText(form.mem6IllMale.toString())
        etMem18NormalMale.setText(form.mem18NormalMale.toString())
        etMem18DisableMale.setText(form.mem18DisableMale.toString())
        etMem18IllMale.setText(form.mem18IllMale.toString())
        etMem36NormalMale.setText(form.mem36NormalMale.toString())
        etMem36DisableMale.setText(form.mem36DisableMale.toString())
        etMem36IllMale.setText(form.mem36IllMale.toString())
        etMem65NormalMale.setText(form.mem65NormalMale.toString())
        etMem65DisableMale.setText(form.mem65DisableMale.toString())
        etMem65IllMale.setText(form.mem65IllMale.toString())

        etMem0NormalFemale.setText(form.mem0NormalFemale.toString())
        etMem0DisableFemale.setText(form.mem0DisableFemale.toString())
        etMem0IllFemale.setText(form.mem0IllFemale.toString())
        etMem3NormalFemale.setText(form.mem3NormalFemale.toString())
        etMem3DisableFemale.setText(form.mem3DisableFemale.toString())
        etMem3IllFemale.setText(form.mem3IllFemale.toString())
        etMem6NormalFemale.setText(form.mem6NormalFemale.toString())
        etMem6DisableFemale.setText(form.mem6DisableFemale.toString())
        etMem6IllFemale.setText(form.mem6IllFemale.toString())
        etMem18NormalFemale.setText(form.mem18NormalFemale.toString())
        etMem18DisableFemale.setText(form.mem18DisableFemale.toString())
        etMem18IllFemale.setText(form.mem18IllFemale.toString())
        etMem36NormalFemale.setText(form.mem36NormalFemale.toString())
        etMem36DisableFemale.setText(form.mem36DisableFemale.toString())
        etMem36IllFemale.setText(form.mem36IllFemale.toString())
        etMem65NormalFemale.setText(form.mem65NormalFemale.toString())
        etMem65DisableFemale.setText(form.mem65DisableFemale.toString())
        etMem65IllFemale.setText(form.mem65IllFemale.toString())


        etReadWriteNumber.setText(form.readWriteNumber.toString())

        checkRbPosNeg(rgReadWrite, rbYes, rbNo, form.isReadWrite)

    }

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

        val form = HhForm3()

        form.householdSize = chkEditText(etHouseholdSize, UiData.ER_ET_DF)?.toInt()

        form.mem0NormalMale = getEditTextInt(etMem0NormalMale)
        form.mem0DisableMale = getEditTextInt(etMem0DisableMale)
        form.mem0IllMale = getEditTextInt(etMem0IllMale)
        form.mem3NormalMale = getEditTextInt(etMem3NormalMale)
        form.mem3DisableMale = getEditTextInt(etMem3DisableMale)
        form.mem3IllMale = getEditTextInt(etMem3IllMale)
        form.mem6NormalMale = getEditTextInt(etMem6NormalMale)
        form.mem6DisableMale = getEditTextInt(etMem6DisableMale)
        form.mem6IllMale = getEditTextInt(etMem6IllMale)
        form.mem18NormalMale = getEditTextInt(etMem18NormalMale)
        form.mem18DisableMale = getEditTextInt(etMem18DisableMale)
        form.mem18IllMale = getEditTextInt(etMem18IllMale)
        form.mem36NormalMale = getEditTextInt(etMem36NormalMale)
        form.mem36DisableMale = getEditTextInt(etMem36DisableMale)
        form.mem36IllMale = getEditTextInt(etMem36IllMale)
        form.mem65NormalMale = getEditTextInt(etMem65NormalMale)
        form.mem65DisableMale = getEditTextInt(etMem65DisableMale)
        form.mem65IllMale = getEditTextInt(etMem65IllMale)


        form.mem0NormalFemale = getEditTextInt(etMem0NormalFemale)
        form.mem0DisableFemale = getEditTextInt(etMem0DisableFemale)
        form.mem0IllFemale = getEditTextInt(etMem0IllFemale)
        form.mem3NormalFemale = getEditTextInt(etMem3NormalFemale)
        form.mem3DisableFemale = getEditTextInt(etMem3DisableFemale)
        form.mem3IllFemale = getEditTextInt(etMem3IllFemale)
        form.mem6NormalFemale = getEditTextInt(etMem6NormalFemale)
        form.mem6DisableFemale = getEditTextInt(etMem6DisableFemale)
        form.mem6IllFemale = getEditTextInt(etMem6IllFemale)
        form.mem18NormalFemale = getEditTextInt(etMem18NormalFemale)
        form.mem18DisableFemale = getEditTextInt(etMem18DisableFemale)
        form.mem18IllFemale = getEditTextInt(etMem18IllFemale)
        form.mem36NormalFemale = getEditTextInt(etMem36NormalFemale)
        form.mem36DisableFemale = getEditTextInt(etMem36DisableFemale)
        form.mem36IllFemale = getEditTextInt(etMem36IllFemale)
        form.mem65NormalFemale = getEditTextInt(etMem65NormalFemale)
        form.mem65DisableFemale = getEditTextInt(etMem65DisableFemale)
        form.mem65IllFemale = getEditTextInt(etMem65IllFemale)

        form.readWriteNumber = getEditTextInt(etReadWriteNumber)
        form.isReadWrite = chkRadioGroup(rgReadWrite, UiData.ER_RB_DF)

        val myIntList = listOf(
            form.mem0NormalMale,
            form.mem0DisableMale,
            form.mem0IllMale,
            form.mem3NormalMale,
            form.mem3DisableMale,
            form.mem3IllMale,
            form.mem6NormalMale,
            form.mem6DisableMale,
            form.mem6IllMale,
            form.mem18NormalMale,
            form.mem18DisableMale,
            form.mem18IllMale,
            form.mem36NormalMale,
            form.mem36DisableMale,
            form.mem36IllMale,
            form.mem65NormalMale,
            form.mem65DisableMale,
            form.mem65IllMale,
            form.mem0NormalFemale,
            form.mem0DisableFemale,
            form.mem0IllFemale,
            form.mem3NormalFemale,
            form.mem3DisableFemale,
            form.mem3IllFemale,
            form.mem6NormalFemale,
            form.mem6DisableFemale,
            form.mem6IllFemale,
            form.mem18NormalFemale,
            form.mem18DisableFemale,
            form.mem18IllFemale,
            form.mem36NormalFemale,
            form.mem36DisableFemale,
            form.mem36IllFemale,
            form.mem65NormalFemale,
            form.mem65DisableFemale,
            form.mem65IllFemale,
        )
        val sum = myIntList.sum() // = 9

        if (form.householdSize != sum) {
            etHouseholdSize.error = "Household Size not matched"
            etHouseholdSize.requestFocus()
            return
        }

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

        etHouseholdSize.setText("12")

        etMem0NormalMale.setText("3")

        etReadWriteNumber.setText("2")

        rgReadWrite.check(binding.rbNo.id)
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

    }


}
