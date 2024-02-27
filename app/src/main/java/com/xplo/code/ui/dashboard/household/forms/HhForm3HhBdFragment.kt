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
import com.xplo.code.ui.dashboard.model.getIllPerson
import com.xplo.code.ui.dashboard.model.getTotalTable
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

    private lateinit var etMem0TotalFemale : EditText
    private lateinit var etMem3TotalFemale : EditText
    private lateinit var etMem6TotalFemale : EditText
    private lateinit var etMem18TotalFemale : EditText
    private lateinit var etMem36TotalFemale : EditText
    private lateinit var etMem65TotalFemale : EditText

    private lateinit var etMem0TotalMale : EditText
    private lateinit var etMem3TotalMale : EditText
    private lateinit var etMem6TotalMale : EditText
    private lateinit var etMem18TotalMale : EditText
    private lateinit var etMem36TotalMale : EditText
    private lateinit var etMem65TotalMale : EditText

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

        etMem0TotalMale =binding.viewHhTable.etMem0TotalMale
        etMem3TotalMale =binding.viewHhTable.etMem3TotalMale
        etMem6TotalMale =binding.viewHhTable.etMem6TotalMale
        etMem18TotalMale =binding.viewHhTable.etMem18TotalMale
        etMem36TotalMale =binding.viewHhTable.etMem36TotalMale
        etMem65TotalMale =binding.viewHhTable.etMem65TotalMale

        etMem0TotalFemale =binding.viewHhTable.etMem0TotalFemale
        etMem3TotalFemale =binding.viewHhTable.etMem3TotalFemale
        etMem6TotalFemale =binding.viewHhTable.etMem6TotalFemale
        etMem18TotalFemale =binding.viewHhTable.etMem18TotalFemale
        etMem36TotalFemale =binding.viewHhTable.etMem36TotalFemale
        etMem65TotalFemale =binding.viewHhTable.etMem65TotalFemale

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

        etMem0TotalMale.setText(form.mem0TotalMale.toString())
        etMem3TotalMale.setText(form.mem3TotalMale.toString())
        etMem6TotalMale.setText(form.mem6TotalMale.toString())
        etMem18TotalMale.setText(form.mem18TotalMale.toString())
        etMem36TotalMale.setText(form.mem36TotalMale.toString())
        etMem65TotalMale.setText(form.mem65TotalMale.toString())

        etMem0TotalFemale.setText(form.mem0TotalFemale.toString())
        etMem3TotalFemale.setText(form.mem3TotalFemale.toString())
        etMem6TotalFemale.setText(form.mem6TotalFemale.toString())
        etMem18TotalFemale.setText(form.mem18TotalFemale.toString())
        etMem36TotalFemale.setText(form.mem36TotalFemale.toString())
        etMem65TotalFemale.setText(form.mem65TotalFemale.toString())

        etMem0NormalMale.setText(form.male0_2.normal.toString())
        etMem0DisableMale.setText(form.male0_2.disable.toString())
        etMem0IllMale.setText(form.male0_2.ill.toString())

        etMem3NormalMale.setText(form.male3_5.normal.toString())
        etMem3DisableMale.setText(form.male3_5.disable.toString())
        etMem3IllMale.setText(form.male3_5.ill.toString())

        etMem6NormalMale.setText(form.male6_17.normal.toString())
        etMem6DisableMale.setText(form.male6_17.disable.toString())
        etMem6IllMale.setText(form.male6_17.ill.toString())

        etMem18NormalMale.setText(form.male18_35.normal.toString())
        etMem18DisableMale.setText(form.male18_35.disable.toString())
        etMem18IllMale.setText(form.male18_35.ill.toString())

        etMem36NormalMale.setText(form.male36_64.normal.toString())
        etMem36DisableMale.setText(form.male36_64.disable.toString())
        etMem36IllMale.setText(form.male36_64.ill.toString())

        etMem65NormalMale.setText(form.male65p.normal.toString())
        etMem65DisableMale.setText(form.male65p.disable.toString())
        etMem65IllMale.setText(form.male65p.ill.toString())



        etMem0NormalFemale.setText(form.female0_2.normal.toString())
        etMem0DisableFemale.setText(form.female0_2.disable.toString())
        etMem0IllFemale.setText(form.female0_2.ill.toString())

        etMem3NormalFemale.setText(form.female3_5.normal.toString())
        etMem3DisableFemale.setText(form.female3_5.disable.toString())
        etMem3IllFemale.setText(form.female3_5.ill.toString())

        etMem6NormalFemale.setText(form.female6_17.normal.toString())
        etMem6DisableFemale.setText(form.female6_17.disable.toString())
        etMem6IllFemale.setText(form.female6_17.ill.toString())

        etMem18NormalFemale.setText(form.female18_35.normal.toString())
        etMem18DisableFemale.setText(form.female18_35.disable.toString())
        etMem18IllFemale.setText(form.female18_35.ill.toString())

        etMem36NormalFemale.setText(form.female36_64.normal.toString())
        etMem36DisableFemale.setText(form.female36_64.disable.toString())
        etMem36IllFemale.setText(form.female36_64.ill.toString())

        etMem65NormalFemale.setText(form.female65p.normal.toString())
        etMem65DisableFemale.setText(form.female65p.disable.toString())
        etMem65IllFemale.setText(form.female65p.ill.toString())
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

        form.mem0TotalMale=getEditTextInt(etMem0TotalMale)
        form.mem3TotalMale=getEditTextInt(etMem3TotalMale)
        form.mem6TotalMale=getEditTextInt(etMem6TotalMale)
        form.mem18TotalMale=getEditTextInt(etMem18TotalMale)
        form.mem36TotalMale=getEditTextInt(etMem36TotalMale)
        form.mem65TotalMale=getEditTextInt(etMem65TotalMale)

        form.mem0TotalFemale=getEditTextInt(etMem0TotalFemale)
        form.mem3TotalFemale=getEditTextInt(etMem3TotalFemale)
        form.mem6TotalFemale=getEditTextInt(etMem6TotalFemale)
        form.mem18TotalFemale=getEditTextInt(etMem18TotalFemale)
        form.mem36TotalFemale=getEditTextInt(etMem36TotalFemale)
        form.mem65TotalFemale=getEditTextInt(etMem65TotalFemale)

        form.male0_2.normal = getEditTextInt(etMem0NormalMale)
        form.male0_2.disable = getEditTextInt(etMem0DisableMale)
        form.male0_2.ill = getEditTextInt(etMem0IllMale)
        form.male3_5.normal = getEditTextInt(etMem3NormalMale)
        form.male3_5.disable = getEditTextInt(etMem3DisableMale)
        form.male3_5.ill = getEditTextInt(etMem3IllMale)
        form.male6_17.normal = getEditTextInt(etMem6NormalMale)
        form.male6_17.disable = getEditTextInt(etMem6DisableMale)
        form.male6_17.ill = getEditTextInt(etMem6IllMale)
        form.male18_35.normal = getEditTextInt(etMem18NormalMale)
        form.male18_35.disable = getEditTextInt(etMem18DisableMale)
        form.male18_35.ill = getEditTextInt(etMem18IllMale)
        form.male36_64.normal = getEditTextInt(etMem36NormalMale)
        form.male36_64.disable = getEditTextInt(etMem36DisableMale)
        form.male36_64.ill = getEditTextInt(etMem36IllMale)
        form.male65p.normal = getEditTextInt(etMem65NormalMale)
        form.male65p.disable = getEditTextInt(etMem65DisableMale)
        form.male65p.ill = getEditTextInt(etMem65IllMale)


        form.female0_2.normal = getEditTextInt(etMem0NormalFemale)
        form.female0_2.disable = getEditTextInt(etMem0DisableFemale)
        form.female0_2.ill = getEditTextInt(etMem0IllFemale)
        form.female3_5.normal = getEditTextInt(etMem3NormalFemale)
        form.female3_5.disable = getEditTextInt(etMem3DisableFemale)
        form.female3_5.ill = getEditTextInt(etMem3IllFemale)
        form.female6_17.normal = getEditTextInt(etMem6NormalFemale)
        form.female6_17.disable = getEditTextInt(etMem6DisableFemale)
        form.female6_17.ill = getEditTextInt(etMem6IllFemale)
        form.female18_35.normal = getEditTextInt(etMem18NormalFemale)
        form.female18_35.disable = getEditTextInt(etMem18DisableFemale)
        form.female18_35.ill = getEditTextInt(etMem18IllFemale)
        form.female36_64.normal = getEditTextInt(etMem36NormalFemale)
        form.female36_64.disable = getEditTextInt(etMem36DisableFemale)
        form.female36_64.ill = getEditTextInt(etMem36IllFemale)
        form.female65p.normal = getEditTextInt(etMem65NormalFemale)
        form.female65p.disable = getEditTextInt(etMem65DisableFemale)
        form.female65p.ill = getEditTextInt(etMem65IllFemale)
        form.readWriteNumber = getEditTextInt(etReadWriteNumber)
        form.isReadWrite = chkRadioGroup(rgReadWrite, UiData.ER_RB_DF)
        val myIntList = listOf(
            form.mem0TotalMale,
            form.mem3TotalMale,
            form.mem6TotalMale,
            form.mem18TotalMale,
            form.mem36TotalMale,
            form.mem65TotalMale,

            form.mem0TotalFemale,
            form.mem3TotalFemale,
            form.mem6TotalFemale,
            form.mem18TotalFemale,
            form.mem36TotalFemale,
            form.mem65TotalFemale,


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
        val myIntTotalList = listOf(
            form.mem0TotalMale,
            form.mem3TotalMale,
            form.mem6TotalMale,
            form.mem18TotalMale,
            form.mem36TotalMale,
            form.mem65TotalMale,

            form.mem0TotalFemale,
            form.mem3TotalFemale,
            form.mem6TotalFemale,
            form.mem18TotalFemale,
            form.mem36TotalFemale,
            form.mem65TotalFemale,
        )
        val sum = myIntTotalList.sum()
        //val sum = myIntList.sum() // = 9
        //val sum = form.getTotalTable()
        if (form.householdSize != sum) {

            etHouseholdSize.error = "Household Size not matched"
            etHouseholdSize.requestFocus()
            return
        }

        /*val myIntListFor18to35 = listOf(
            form.male18_35.normal,
            form.male18_35.disable,
            form.male18_35.ill,
            form.female18_35.normal,
            form.female18_35.disable,
            form.female18_35.ill
        )*/
        val myIntListFor18to35 = listOf(
            form.mem18TotalMale,
            form.mem18TotalFemale
        )
        val sum18to35 = myIntListFor18to35.sum()
        //val sum18to35 = myIntListFor18to35.sum() // = 9
        if (form.readWriteNumber != sum18to35) {
            etReadWriteNumber.error = "Member Size not matched"
            etReadWriteNumber.requestFocus()
            return
        }
        val myIntListFor0to2Male = listOf(
            form.mem0NormalMale,
            form.mem0DisableMale,
            form.mem0IllMale,

            )
        val sum0to2Male = myIntListFor0to2Male.sum()
        Log.d(TAG,"the real sum 0 to 2 = $sum0to2Male")
        val myIntListFor0to2Female = listOf(
            form.mem0NormalFemale,
            form.mem0DisableFemale,
            form.mem0IllFemale,

            )
        val sum0to2Female = myIntListFor0to2Female.sum()
        Log.d(TAG,"the real sum 0 to 2 = $sum0to2Female")
        //Age 0-2

        Log.d(TAG," total O male is ${form.mem0TotalMale}" )
        Log.d(TAG,"without disablites ${form.male0_2.normal}")
        Log.d(TAG,"sum of o to 2 $sum0to2Female")
        if (form.mem0TotalMale < form.male0_2.normal ||
            form.mem0TotalMale < form.male0_2.disable ||
            form.mem0TotalMale < form.male0_2.ill ) {
            when {

                form.mem0TotalMale < form.male0_2.normal -> {
                    etMem0NormalMale.error = "Member Size not matched"
                    etMem0NormalMale.requestFocus()
                    Log.d(TAG,"Total o male ${form.mem0TotalMale}")
                    Log.d(TAG,"Total o Normal male ${form.mem0NormalMale}")
                }
                form.mem0TotalMale < form.male0_2.disable -> {
                    etMem0DisableMale.error = "Member Size not matched"
                    etMem0DisableMale.requestFocus()
                    Log.d(TAG,"Total o male ${form.mem0TotalMale}")
                    Log.d(TAG,"Total o Disable  male ${form.mem0DisableMale}")
                }
                form.mem0TotalMale < form.male0_2.ill -> {
                    etMem0IllMale.error = "Member Size not matched"
                    etMem0IllMale.requestFocus()
                    Log.d(TAG,"Total o male ${form.mem0TotalMale}")
                    Log.d(TAG,"Total o Ill  male ${form.mem0IllMale}")
                }

            }
            return
        }


        if (form.mem0TotalFemale < form.female0_2.normal ||
            form.mem0TotalFemale < form.female0_2.disable ||
            form.mem0TotalFemale < form.female0_2.ill ) {
            when {
                form.mem0TotalFemale < form.female0_2.normal -> {
                    etMem0NormalFemale.error = "Member Size not matched"
                    etMem0NormalFemale.requestFocus()
                }
                form.mem0TotalFemale < form.female0_2.disable  -> {
                    etMem0DisableFemale.error = "Member Size not matched"
                    etMem0DisableFemale.requestFocus()
                }
                form.mem0TotalFemale < form.female0_2.ill -> {
                    etMem0IllFemale.error = "Member Size not matched"
                    etMem0IllFemale.requestFocus()
                }
                /*form.mem0TotalFemale < sum0to2Female -> {
                    etMem0TotalFemale.error = "Total Female is less than other"
                    etMem0TotalFemale.requestFocus()
                }*/
            }
            return
        }


        //Age 3-5 y

        val myIntListFor3to5Male = listOf(
            form.mem3NormalMale,
            form.mem3DisableMale,
            form.mem3IllMale,

            )
        val sum3to5Male = myIntListFor3to5Male.sum()
        Log.d(TAG,"the real sum 0 to 2 = $sum0to2Male")
        val myIntListFor3to5Female = listOf(
            form.mem3NormalFemale,
            form.mem3DisableFemale,
            form.mem3IllFemale,

            )
        val sum3to5Female = myIntListFor3to5Female.sum()
        Log.d(TAG,"the real sum 0 to 2 = $sum0to2Female")

        if (form.mem3TotalMale < form.male3_5.normal ||
            form.mem3TotalMale < form.male3_5.disable ||
            form.mem3TotalMale < form.male3_5.ill) {
            when {
                form.mem3TotalMale < form.male3_5.normal -> {
                    etMem3NormalMale.error = "Member Size not matched"
                    etMem3NormalMale.requestFocus()
                }
                form.mem3TotalMale < form.male3_5.disable -> {
                    etMem3DisableMale.error = "Member Size not matched"
                    etMem3DisableMale.requestFocus()
                }
                form.mem3TotalMale < form.male3_5.ill -> {
                    etMem3IllMale.error = "Member Size not matched"
                    etMem3IllMale.requestFocus()
                }
                /*form.mem3TotalMale < sum3to5Male -> {
                    etMem0TotalMale.error = "Total Male is less than other"
                    etMem0TotalMale.requestFocus()
                }*/
            }
            return
        }


        if (form.mem3TotalFemale < form.female3_5.normal ||
            form.mem3TotalFemale < form.female3_5.disable ||
            form.mem3TotalFemale < form.female3_5.ill ) {
            when {
                form.mem3TotalFemale < form.female3_5.normal -> {
                    etMem3NormalFemale.error = "Member Size not matched"
                    etMem3NormalFemale.requestFocus()
                }
                form.mem3TotalFemale < form.female3_5.disable -> {
                    etMem3DisableFemale.error = "Member Size not matched"
                    etMem3DisableFemale.requestFocus()
                }
                form.mem3TotalFemale < form.female3_5.ill -> {
                    etMem3IllFemale.error = "Member Size not matched"
                    etMem3IllFemale.requestFocus()
                }
                /*form.mem3TotalFemale < sum3to5Female -> {
                    etMem3TotalFemale.error = "Total Female is less than other"
                    etMem3TotalFemale.requestFocus()
                }*/
            }
            return
        }


        // Age 6 -17
        val myIntListFor6to17Male = listOf(
            form.mem6NormalMale,
            form.mem6DisableMale,
            form.mem6IllMale,

            )
        val sum6to17Male = myIntListFor6to17Male.sum()
        Log.d(TAG,"the real sum 0 to 2 = $sum0to2Male")
        val myIntListFor6to17Female = listOf(
            form.mem6NormalFemale,
            form.mem6DisableFemale,
            form.mem6IllFemale,

            )
        val sum6to17Female = myIntListFor6to17Female.sum()
        Log.d(TAG,"the real sum 0 to 2 = $sum0to2Female")
        if (form.mem6TotalMale < form.male6_17.normal ||
            form.mem6TotalMale < form.male6_17.disable ||
            form.mem6TotalMale < form.male6_17.ill ) {
            when {
                form.mem6TotalMale < form.male6_17.normal -> {
                    etMem6NormalMale.error = "Member Size not matched"
                    etMem6NormalMale.requestFocus()
                }
                form.mem6TotalMale < form.male6_17.disable -> {
                    etMem6DisableMale.error = "Member Size not matched"
                    etMem6DisableMale.requestFocus()
                }
                form.mem6TotalMale < form.male6_17.ill -> {
                    etMem6IllMale.error = "Member Size not matched"
                    etMem6IllMale.requestFocus()
                }
                /*form.mem6TotalMale < sum6to17Male -> {
                    etMem6TotalMale.error = "Total Male is less than other"
                    etMem6TotalMale.requestFocus()
                }*/
            }
            return
        }


        if (form.mem6TotalFemale < form.female6_17.normal ||
            form.mem6TotalFemale < form.female6_17.disable ||
            form.mem6TotalFemale < form.female6_17.ill ) {
            when {
                form.mem6TotalFemale < form.female6_17.normal -> {
                    etMem6NormalFemale.error = "Member Size not matched"
                    etMem6NormalFemale.requestFocus()
                }
                form.mem6TotalFemale < form.female6_17.disable -> {
                    etMem6DisableFemale.error = "Member Size not matched"
                    etMem6DisableFemale.requestFocus()
                }
                form.mem6TotalFemale < form.female6_17.ill -> {
                    etMem6IllFemale.error = "Member Size not matched"
                    etMem6IllFemale.requestFocus()
                }
                /* form.mem6TotalFemale < sum6to17Female -> {
                     etMem6TotalFemale.error = "Total Female is less than other"
                     etMem6TotalFemale.requestFocus()
                 }*/
            }
            return
        }

        // Age 18 -35 y

        val myIntListFor18to35Male = listOf(
            form.mem18NormalMale,
            form.mem18DisableMale,
            form.mem18IllMale,

            )
        val sum18to35Male = myIntListFor18to35Male.sum()
        val myIntListFor18to35Female = listOf(
            form.mem18NormalFemale,
            form.mem18DisableFemale,
            form.mem18IllFemale,

            )
        val sum18to35Female = myIntListFor18to35Female.sum()


        if (form.mem18TotalMale < form.male18_35.normal ||
            form.mem18TotalMale < form.male18_35.disable ||
            form.mem18TotalMale < form.male18_35.ill ) {
            when {
                form.mem18TotalMale < form.male18_35.normal -> {
                    etMem18NormalMale.error = "Member Size not matched"
                    etMem18NormalMale.requestFocus()
                }
                form.mem18TotalMale < form.male18_35.disable -> {
                    etMem18DisableMale.error = "Member Size not matched"
                    etMem18DisableMale.requestFocus()
                }
                form.mem18TotalMale < form.male18_35.ill -> {
                    etMem18IllMale.error = "Member Size not matched"
                    etMem18IllMale.requestFocus()
                }
                /* form.mem18TotalMale < sum18to35Male -> {
                     etMem18TotalMale.error = "Total Male is less than other"
                     etMem18TotalMale.requestFocus()
                 }*/
            }
            return
        }


        if (form.mem18TotalFemale < form.female18_35.normal ||
            form.mem18TotalFemale < form.female18_35.disable ||
            form.mem18TotalFemale < form.female18_35.ill ) {
            when {
                form.mem18TotalFemale < form.female18_35.normal -> {
                    etMem18NormalFemale.error = "Member Size not matched"
                    etMem18NormalFemale.requestFocus()
                }
                form.mem18TotalFemale < form.female18_35.disable -> {
                    etMem18DisableFemale.error = "Member Size not matched"
                    etMem18DisableFemale.requestFocus()
                }
                form.mem18TotalFemale < form.female18_35.ill -> {
                    etMem18IllFemale.error = "Member Size not matched"
                    etMem18IllFemale.requestFocus()
                }
                /*form.mem18TotalFemale < sum18to35Female -> {
                    etMem18TotalFemale.error = "Total Female is less than other"
                    etMem18TotalFemale.requestFocus()
                }*/
            }
            return
        }

        // Age 36-64 year
        val myIntListFor36to64Male = listOf(
            form.mem36NormalMale,
            form.mem36DisableMale,
            form.mem36IllMale,

            )
        val sum36to64Male = myIntListFor36to64Male.sum()
        val myIntListFor36to64Female = listOf(
            form.mem36NormalFemale,
            form.mem36DisableFemale,
            form.mem36IllFemale,

            )
        val sum36to64Female = myIntListFor36to64Female.sum()


        if (form.mem36TotalMale < form.male36_64.normal  ||
            form.mem36TotalMale < form.male36_64.disable ||
            form.mem36TotalMale < form.male36_64.ill  ) {
            when {
                form.mem36TotalMale < form.male36_64.normal -> {
                    etMem36NormalMale.error = "Member Size not matched"
                    etMem36NormalMale.requestFocus()
                }
                form.mem36TotalMale < form.male36_64.disable -> {
                    etMem36DisableMale.error = "Member Size not matched"
                    etMem36DisableMale.requestFocus()
                }
                form.mem36TotalMale < form.male36_64.ill -> {
                    etMem36IllMale.error = "Member Size not matched"
                    etMem36IllMale.requestFocus()
                }
                /*form.mem36TotalMale < sum36to64Male -> {
                    etMem36TotalMale.error = "Total Male is less than other"
                    etMem36TotalMale.requestFocus()
                }*/
            }
            return
        }


        if (form.mem36TotalFemale < form.female36_64.normal ||
            form.mem36TotalFemale < form.female36_64.disable ||
            form.mem36TotalFemale < form.female36_64.ill ) {
            when {
                form.mem36TotalFemale < form.female36_64.normal -> {
                    etMem36NormalFemale.error = "Member Size not matched"
                    etMem36NormalFemale.requestFocus()
                }
                form.mem36TotalFemale < form.female36_64.disable -> {
                    etMem36DisableFemale.error = "Member Size not matched"
                    etMem36DisableFemale.requestFocus()
                }
                form.mem36TotalFemale < form.female36_64.ill -> {
                    etMem36IllFemale.error = "Member Size not matched"
                    etMem36IllFemale.requestFocus()
                }
                /*form.mem36TotalFemale < sum36to64Female -> {
                    etMem36TotalFemale.error = "Total Female is less than other"
                    etMem36TotalFemale.requestFocus()
                }*/
            }
            return
        }

        // Age 65 or above

        val myIntListFor65andAboveMale = listOf(
            form.mem65NormalMale,
            form.mem65DisableMale,
            form.mem65IllMale,


            )
        val sum65andAboveMale = myIntListFor65andAboveMale.sum()
        val myIntListFor65andAboveFemale = listOf(
            form.mem65NormalFemale,
            form.mem65DisableFemale,
            form.mem65IllFemale,

            )
        val sum65andAboveFemale = myIntListFor65andAboveFemale.sum()


        if (form.mem65TotalMale < form.male65p.normal ||
            form.mem65TotalMale < form.male65p.disable ||
            form.mem65TotalMale < form.male65p.ill) {

            //     ||       form.mem65TotalMale < sum65andAboveMale
            when {
                form.mem65TotalMale < form.male65p.normal -> {
                    etMem65NormalMale.error = "Member Size not matched"
                    etMem65NormalMale.requestFocus()
                }
                form.mem65TotalMale < form.male65p.disable -> {
                    etMem65DisableMale.error = "Member Size not matched"
                    etMem65DisableMale.requestFocus()
                }
                form.mem65TotalMale < form.male65p.ill -> {
                    etMem65IllMale.error = "Member Size not matched"
                    etMem65IllMale.requestFocus()
                }
                /*form.mem65TotalMale < sum65andAboveMale -> {
                    etMem65TotalMale.error = "Total Male is less than other"
                    etMem65TotalMale.requestFocus()
                }*/
            }
            return
        }


        if (form.mem65TotalFemale < form.female65p.normal ||
            form.mem65TotalFemale < form.female65p.disable ||
            form.mem65TotalFemale < form.female65p.ill) {
            when {
                form.mem65TotalFemale < form.female65p.normal -> {
                    etMem65NormalFemale.error = "Member Size not matched"
                    etMem65NormalFemale.requestFocus()
                }
                form.mem65TotalFemale < form.female65p.disable -> {
                    etMem65DisableFemale.error = "Member Size not matched"
                    etMem65DisableFemale.requestFocus()
                }
                form.mem65TotalFemale < form.female65p.ill -> {
                    etMem65IllFemale.error = "Member Size not matched"
                    etMem65IllFemale.requestFocus()
                }
                /* form.mem65TotalFemale < sum65andAboveFemale -> {
                     etMem65TotalFemale.error = "Total Female is less than other"
                     etMem65TotalFemale.requestFocus()
                 }*/
            }
            return
        }







        /*if (form.mem0TotalMale < form.mem0NormalMale || form.mem0TotalMale < sum0to2Male){
            etMem0NormalMale.error = "Member Size not matched"
            etMem0NormalMale.requestFocus()
            return
        }
        if (form.mem0TotalMale < form.mem0DisableMale || form.mem0TotalMale < sum0to2Male){
            etMem0DisableMale.error = "Member Size not matched"
            etMem0DisableMale.requestFocus()
            return
        }
        if (form.mem0TotalMale < form.mem0IllMale || form.mem0TotalMale < sum0to2Male){
            etMem0IllMale.error = "Member Size not matched"
            etMem0IllMale.requestFocus()
            return
        }
        if (form.mem0TotalFemale < form.mem0NormalFemale || form.mem0TotalMale < sum0to2Female){
            etMem0NormalFemale.error = "Member Size not matched"
            etMem0NormalMale.requestFocus()
            return
        }
        if (form.mem0TotalFemale < form.mem0DisableFemale || form.mem0TotalMale < sum0to2Female){
            etMem0DisableFemale.error = "Member Size not matched"
            etMem0DisableFemale.requestFocus()
            return
        }
        if (form.mem0TotalFemale < form.mem0IllFemale || form.mem0TotalMale < sum0to2Female){
            etMem0IllFemale.error = "Member Size not matched"
            etMem0IllFemale.requestFocus()
            return
        }*/
        /*if (form.mem0TotalMale < sum0to2Male){
            etMem0TotalMale.error = "Total is Not Greater the Sum of 0-2"
            etMem0TotalMale.requestFocus()
            return
        }
        if (form.mem0TotalMale < sum0to2Female){
            etMem0TotalFemale.error = "Total is Not Greater the Sum of 0-2"
            etMem0TotalFemale.requestFocus()
            return
        }*/

        /*if (form.mem0TotalMale < form.mem0NormalMale || form.mem0TotalMale < form.mem0DisableMale || form.mem0TotalMale < form.mem0IllMale || form.mem0TotalMale < sum0to2){
            etReadWriteNumber.error = "Member Size not matched"
            etReadWriteNumber.requestFocus()
            return

        }*/



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

        etHouseholdSize.setText("6")

        etMem0NormalMale.setText("3")
        etMem0NormalFemale.setText("3")

        etReadWriteNumber.setText("0")

        rgReadWrite.check(binding.rbNo.id)
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")

    }


}
