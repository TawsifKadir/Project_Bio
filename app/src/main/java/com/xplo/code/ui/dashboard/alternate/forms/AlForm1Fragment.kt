package com.xplo.code.ui.dashboard.alternate.forms


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentAlForm1RegSetupBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.getFullName
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
class AlForm1Fragment : BasicFormFragment(), AlternateContract.Form1View {

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

    private lateinit var binding: FragmentAlForm1RegSetupBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null

    private var id: String? = null

    private lateinit var spGender: Spinner
    private lateinit var spAlternateRelation: Spinner
    private lateinit var etPhoneNo: EditText
    private lateinit var etIdNumber: EditText
    private lateinit var etAge: EditText
    private lateinit var etAlternateName: EditText
    private lateinit var etName: EditText


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
        binding = FragmentAlForm1RegSetupBinding.inflate(inflater, container, false)
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
        etAlternateName = binding.etAlternateName
        etName = binding.etName
    }

    override fun initView() {

        //etName.setText(id)
        etName.isEnabled = false

        bindSpinnerData(spGender, UiData.genderOptions)
        bindSpinnerData(spAlternateRelation, UiData.legalStatusOptions)

//        bindSpinnerData(binding.spCountryName, UiData.countryNameOptions)

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

        onGenerateDummyInput()
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

        form.householdName = chkEditText(etName, UiData.ER_ET_DF)
        form.alternateName = chkEditText(etAlternateName, UiData.ER_ET_DF)
        form.age = chkEditText(etAge, UiData.ER_ET_DF)
        form.idNumber = chkEditText(etIdNumber, UiData.ER_ET_DF)
        form.phoneNumber = chkEditText(etPhoneNo, UiData.ER_ET_DF)
        form.selectAlternateRlt = chkSpinner(spAlternateRelation, UiData.ER_SP_DF)
        form.gender = chkSpinner(spGender, UiData.ER_SP_DF)


        if (!form.isOk()) {
            return
        }

        onValidated(form)
    }

    override fun onGenerateDummyInput() {

        if (!BuildConfig.DEBUG) return
        if (!TestConfig.isDummyDataEnabled) return

        //etName.setText("Shadhin")
        etAge.setText("29")
        etIdNumber.setText("122")
        etPhoneNo.setText("01829372012")
        etAlternateName.setText("Nasif Ahmed")
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

    override fun onGetHouseholdItem(item: HouseholdItem?) {
        Log.d(TAG, "onGetHouseholdItem() called with: item = $item")
        binding.etName.setText(item.toHouseholdForm()?.form2.getFullName())
    }

    override fun onGetHouseholdItemFailure(msg: String?) {
        Log.d(TAG, "onGetHouseholdItemFailure() called with: msg = $msg")
    }

}
