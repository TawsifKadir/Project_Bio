package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.isNo
import com.xplo.code.core.ext.isYes
import com.xplo.code.core.ext.plusOne
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhForm6NomineeBinding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.nominee.NomineeListAdapter
import com.xplo.code.ui.dashboard.household.forms.nominee.NomineeModal
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.checkExtraCases
import com.xplo.code.ui.dashboard.model.isOk
import com.xplo.data.BuildConfig
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
class HhForm6NomineeFragment : BasicFormFragment(), HouseholdContract.Form5View,
    NomineeListAdapter.OnItemClickListener {

    companion object {
        const val TAG = "HhForm6NomineeFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm6NomineeFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm6NomineeFragment()
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

    private var adapter: NomineeListAdapter? = null

    //private lateinit var layoutList: LinearLayout


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


    }

    override fun initView() {

        bindSpinnerData(binding.spReasonNoNominee, UiData.whyNot)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = NomineeListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter


    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        binding.rgNomineeAdd.setOnCheckedChangeListener { radioGroup, id ->
            Log.d(TAG, "initObserver() called with: radioGroup = $radioGroup, id = $id")
            when (id) {
                R.id.rbYes -> onEnableDisableNominee(true)
                R.id.rbNo -> onEnableDisableNominee(false)
            }
        }

        binding.btAdd.setOnClickListener {
            onClickAddNominee()
        }

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
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

            binding.rgNomineeAdd.check(binding.rbYes.id)
            onEnableDisableNominee(true)
            adapter?.addAll(form.nominees)

        } else {
            binding.rgNomineeAdd.check(binding.rbNo.id)
            onEnableDisableNominee(false)
            setSpinnerItem(
                binding.spReasonNoNominee,
                UiData.stateNameOptions,
                form.noNomineeReason
            )
        }


    }

    override fun onEnableDisableNominee(isNomineeAdd: Boolean) {
        Log.d(TAG, "onEnableDisableNominee() called with: isNomineeAdd = $isNomineeAdd")

        if (isNomineeAdd) {
            binding.viewNomineeAdd.visible()
            binding.viewNomineeNotAdd.gone()
        } else {
            binding.viewNomineeAdd.gone()
            binding.viewNomineeNotAdd.visible()
        }
    }

    override fun onClickAddNominee() {
        val rootForm = interactor?.getRootForm()
        if (rootForm == null) return

        NomineeModal.Builder(requireActivity().supportFragmentManager)
            .listener(this)
            .parent(null)
            .no(getNomineeNo())
            .gender(null)
            .build()
            .show()

    }

    private fun getNomineeNo(): Int {
        return adapter?.itemCount.plusOne()
    }

    override fun onGetANomineeFromPopup(nominee: Nominee?) {
        Log.d(TAG, "onGetANomineeFromPopup() called with: nominee = $nominee")
        if (nominee == null) return

        adapter?.addItem(nominee)

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

        val form = HhForm6()
        form.isNomineeAdd = chkRadioGroup(binding.rgNomineeAdd, UiData.ER_ET_DF)

        if (form.isNomineeAdd.isNo()) {
            form.noNomineeReason = chkSpinner(binding.spReasonNoNominee, UiData.ER_SP_DF)
            if (form.isOk()) {
                onValidated(form)
                return
            }
        }

        form.nominees = readNomineeInputsFromList()

        if (form.isOk()) {
            val checkExtraCases = form.checkExtraCases()
            if (checkExtraCases != null) {
                showAlerter(checkExtraCases, null)
                return
            }
            onValidated(form)
        }


    }

    private fun readNomineeInputsFromList(): ArrayList<Nominee> {
        var nominees = adapter?.getDataset()
        return nominees ?: arrayListOf()
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


    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")
    }

    override fun onClickNomineeItem(item: Nominee, pos: Int) {
        Log.d(TAG, "onClickNomineeItem() called with: item = $item, pos = $pos")

    }

    override fun onClickNomineeDelete(item: Nominee, pos: Int) {
        Log.d(TAG, "onClickNomineeDelete() called with: item = $item, pos = $pos")
        adapter?.remove(pos)
    }

    override fun onNomineeModalNomineeInputSuccess(item: Nominee?) {
        super.onNomineeModalNomineeInputSuccess(item)
        onGetANomineeFromPopup(item)
    }
}
