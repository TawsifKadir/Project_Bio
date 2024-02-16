package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.isNo
import com.xplo.code.core.ext.isYes
import com.xplo.code.core.ext.plusOne
import com.xplo.code.core.ext.toBool
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhForm6Nominee2Binding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.nominee.NomineeListAdapter
import com.xplo.code.ui.dashboard.household.forms.nominee.NomineeModal
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.checkExtraCases
import com.xplo.code.ui.dashboard.model.getOppositeGender
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
class HhForm6Nominee2Fragment : BasicFormFragment(), HouseholdContract.Form62View,
    AdapterView.OnItemSelectedListener,
    NomineeListAdapter.OnItemClickListener {

    companion object {
        const val TAG = "HhForm6Nominee2Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm6Nominee2Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm6Nominee2Fragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhForm6Nominee2Binding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null

    private var adapter: NomineeListAdapter? = null

    private lateinit var spReasonNoNominee: Spinner
    private lateinit var llParentOtherReason: View
    private lateinit var etOtherReason: EditText

    //private lateinit var btAdd: Button
    //private lateinit var btAddAnother: Button
    private lateinit var recyclerView: RecyclerView


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
        binding = FragmentHhForm6Nominee2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {
        spReasonNoNominee = binding.viewNomineeNoNominee.spReasonNoNominee
        llParentOtherReason = binding.viewNomineeNoNominee.llParentOtherReason
        etOtherReason = binding.viewNomineeNoNominee.etOtherReason

        //btAdd = binding.viewNomineeAddNominee.btAdd
        //btAddAnother = binding.viewNomineeAddNominee.btAddAnother
        recyclerView = binding.recyclerView


    }

    override fun initView() {

        bindSpinnerData(spReasonNoNominee, UiData.whyNot)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = NomineeListAdapter()
        adapter?.setOnItemClickListener(this)
        recyclerView.adapter = adapter


        onReinstateData(interactor?.getRootForm()?.form6)


    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        binding.rgNomineeAdd.setOnCheckedChangeListener { radioGroup, id ->
            onChangeRGNomineeAdd(id)
        }

        spReasonNoNominee.onItemSelectedListener = this

//        btAdd.setOnClickListener {
//            onClickAddNominee()
//        }
//
//        btAddAnother.setOnClickListener {
//            onClickAddNominee()
//        }

        onLongClickDataGeneration()
        if (TestConfig.isAutoDGEnabled) {
            onGenerateDummyInput()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
        setToolbarTitle("Nominee")
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

            //binding.rgNomineeAdd.check(binding.rbYes.id)
            checkRbYes(binding.rgNomineeAdd, binding.rbYes, binding.rbNo)

            onEnableDisableNominee(true)
            adapter?.addAll(form.nominees)

        } else {
            //binding.rgNomineeAdd.check(binding.rbNo.id)
            checkRbNo(binding.rgNomineeAdd, binding.rbYes, binding.rbNo)

            onEnableDisableNominee(false)
            setSpinnerItem(spReasonNoNominee, UiData.stateNameOptions, form.noNomineeReason)
            if (isOtherSpecify(form.noNomineeReason)) {
                llParentOtherReason.visible()
                etOtherReason.setText(form.noNomineeReasonOther)
            }
        }


    }

    override fun onChangeRGNomineeAdd(id: Int) {
        Log.d(TAG, "onChangeRGNomineeAdd() called with: id = $id")
        when (id) {
            R.id.rbYes -> {
                //onChooseNomineeAdd(null)

                val targetGender = getTargetGender()
                val txt = getString(R.string.nominee_objective, targetGender)

                AlertDialog.Builder(requireContext())
                    .setMessage(txt)
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, which ->
                        onChooseNomineeAdd(targetGender)
                    }
                    .setNegativeButton("No") { dialog, which ->
                        onChooseNomineeAdd(null)
                    }
                    .create()
                    .show()
            }

            R.id.rbNo -> {
                onChooseNomineeNotAdd()
            }
        }
    }

    override fun onChooseNomineeAdd(gender: String?) {
        Log.d(TAG, "onChooseNomineeAdd() called")
        onEnableDisableNominee(true)
        onAddNominee(gender)
    }

    override fun onChooseNomineeNotAdd() {
        Log.d(TAG, "onChooseNomineeNotAdd() called")
        onEnableDisableNominee(false)
    }

    override fun onEnableDisableNominee(isNomineeAdd: Boolean) {
        Log.d(TAG, "onEnableDisableNominee() called with: isNomineeAdd = $isNomineeAdd")

        if (isNomineeAdd) {
            //binding.viewNomineeAddNominee.viewNomineeAddNominee.visible()
            binding.viewNomineeNoNominee.viewNomineeNoNominee.gone()
        } else {
            //binding.viewNomineeAddNominee.viewNomineeAddNominee.gone()
            binding.viewNomineeNoNominee.viewNomineeNoNominee.visible()
        }
    }

    override fun onClickAddNominee() {
        Log.d(TAG, "onClickAddNominee() called")
        val targetGender = getTargetGender()
        onAddNominee(targetGender)
    }

    override fun onAddNominee(gender: String?) {
        Log.d(TAG, "onAddNominee() called with: gender = $gender")
        val rootForm = interactor?.getRootForm()
        if (rootForm == null) return

        NomineeModal.Builder(requireActivity().supportFragmentManager)
            .listener(this)
            .parent(null)
            .no(getNomineeNo())
            .gender(gender)
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
        onRefreshViewWhenListUpdated()

    }

    override fun onRefreshViewWhenListUpdated() {
        Log.d(TAG, "onRefreshViewWhenListUpdated() called")
        if (adapter?.getDataset()?.isNotEmpty().toBool()) {
            onListHasData()
        } else {
            onListEmpty()
        }
    }

    override fun onListHasData() {
        Log.d(TAG, "onListHasData() called")
        //btAdd.gone()
        //btAddAnother.visible()
    }

    override fun onListEmpty() {
        //btAdd.visible()
        //btAddAnother.gone()
    }

    override fun onSelectNoNomineeReason(item: String?) {
        Log.d(TAG, "onSelectNoNomineeItems() called with: item = $item")
        if (item.isNullOrEmpty()) return
        llParentOtherReason.gone()
        if (isOtherSpecify(item)) {
            llParentOtherReason.visible()
        }
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

            form.noNomineeReason = chkSpinner(spReasonNoNominee, UiData.ER_SP_DF)

            if (isOtherSpecify(form.noNomineeReason)) {
                form.noNomineeReasonOther = chkEditText(etOtherReason, UiData.ER_ET_DF)
            }

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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d(TAG, "onItemSelected() called with: p0 = $p0, p1 = $p1, p2 = $p2, p3 = $p3")
        onSelectSpinnerItem(p0, p1, p2)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d(TAG, "onNothingSelected() called with: p0 = $p0")

    }

    override fun onSelectSpinnerItem(parent: AdapterView<*>?, view: View?, position: Int) {
        super.onSelectSpinnerItem(parent, view, position)
        Log.d(TAG, "onSelectSpinnerItem() called with: view = , position = $position")
        if (position == 0) return

        when (parent?.id) {
            R.id.spReasonNoNominee -> {
                val txt = spReasonNoNominee.selectedItem.toString()
                onSelectNoNomineeReason(txt)
            }


        }
    }

    private fun isOtherSpecify(txt: String?): Boolean {
        return txt?.contains(UiData.otherSpecify, true).toBool()
    }

    private fun getTargetGender(): String? {

        if ((adapter?.itemCount ?: 0) == 0) {
            val form2 = interactor?.getRootForm()?.form2
            return form2.getOppositeGender()
        }

        if ((adapter?.itemCount ?: 0) == 1) {
            val nominee = adapter?.getDataset()?.get(0)
            return nominee?.getOppositeGender()
        }

        return null
    }

}
