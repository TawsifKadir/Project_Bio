package com.xplo.code.ui.dashboard.household.forms

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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.tapadoo.alerter.Alerter
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.clearStatus
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.isNo
import com.xplo.code.core.ext.isYes
import com.xplo.code.core.ext.plusOne
import com.xplo.code.core.ext.toBool
import com.xplo.code.core.ext.visible
import com.xplo.code.core.utils.AttrUtils
import com.xplo.code.databinding.FragmentHhForm6Nominee2Binding
import com.xplo.code.ui.dashboard.UiData
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.forms.nominee.NomineeListAdapter
import com.xplo.code.ui.dashboard.household.forms.nominee.NomineeModal
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.checkExtraCases
import com.xplo.code.ui.dashboard.model.getOppositeGender
import com.xplo.code.ui.dashboard.model.isExtraNomineeOk
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
class HhForm6Nominee2Fragment : BasicFormFragment(), HouseholdContract.Form62View,
    AdapterView.OnItemSelectedListener,
    RadioGroup.OnCheckedChangeListener,
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
    private lateinit var questionBox: LinearLayout
    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null

    private var adapter: NomineeListAdapter? = null
    private lateinit var spReasonNoNominee: Spinner
    private lateinit var llParentOtherReason: View
    private lateinit var etOtherReason: EditText
    private lateinit var questionText : TextView
    //private lateinit var btAdd: Button
    //private lateinit var btAddAnother: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var rbNo : RadioButton


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
        questionText = binding.questionText
        //btAdd = binding.viewNomineeAddNominee.btAdd
        //btAddAnother = binding.viewNomineeAddNominee.btAddAnother
        recyclerView = binding.recyclerView
        rbNo = binding.rbNo
        questionBox = binding.questionBox
    }

    override fun initView() {

        bindSpinnerData(spReasonNoNominee, UiData.nonParticipationReason)
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

        spReasonNoNominee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG,"Entered Spinner Item Listener" + NonPerticipationReasonEnum.REASON_OTHER.ordinal)
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if(selectedItem.equals(NonPerticipationReasonEnum.REASON_OTHER.value, ignoreCase = true)){
                    llParentOtherReason.visible()
                }else{
                    Log.d(TAG,"Selected Spinner Other not selected")
                    etOtherReason.setText("")
                    llParentOtherReason.gone()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

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

        binding.rgNomineeAdd.setOnCheckedChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
        binding.rgNomineeAdd.setOnCheckedChangeListener(null)
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
            //checkRbYes(binding.rgNomineeAdd, binding.rbYes, binding.rbNo)

            //onEnableDisableNominee(true)
            adapter?.addAll(form.nominees)
            if(adapter != null){
                if(adapter?.getDataset()?.size==5){
                    questionBox.gone()
                }
            }
            if (form.xIsNomineeAdd.isNo()) {
                checkRbNo(binding.rgNomineeAdd, binding.rbYes, binding.rbNo)
                onEnableDisableNominee(false)
                setSpinnerItem(spReasonNoNominee, UiData.nonParticipationReason, form.xNoNomineeReason)
                if (isOtherSpecify(form.xNoNomineeReason)) {
                    llParentOtherReason.visible()
                    etOtherReason.setText(form.xOtherReason)
                }
            }

        } else {
            // no

            //binding.rgNomineeAdd.check(binding.rbNo.id)
            checkRbNo(binding.rgNomineeAdd, binding.rbYes, binding.rbNo)

            onEnableDisableNominee(false)
            setSpinnerItem(spReasonNoNominee, UiData.nonParticipationReason, form.noNomineeReason)
            if (isOtherSpecify(form.noNomineeReason)) {
                llParentOtherReason.visible()
                etOtherReason.setText(form.otherReason)
            }
        }


    }

    override fun onChangeRGNomineeAdd(id: Int) {
        Log.d(TAG, "onChangeRGNomineeAdd() called with: id = $id")
        when (id) {
            R.id.rbYes -> {
                if(checkOppositeGender()){
                    onChooseNomineeAdd(null)
                }else{
                    onRGNomineeAddYes()
                }
            }
            R.id.rbNo -> {
                if(checkOppositeGender()){
                    onChooseNomineeNotAdd()
                }else{
                    onRGNomineeAddNo()
                }

            }
        }
    }

    override fun onRGNomineeAddYes() {
        Log.d(TAG, "onRGNomineeAddYes() called")
        val dataset = readNomineeInputsFromList()
        if(dataset.size == 4){
            onRGNomineeAddDialogYes()
        }else{
            onChooseNomineeAdd(null)
        }
    }
    private fun onRGNomineeAddDialogYes() {
        Log.d(TAG, "onRGNomineeAddNo() called")
        val targetGender = getTargetGender()
        val targetGenderTitle = getTargetGenderTitle(targetGender)
        val txt = getString(R.string.nominee_objective, targetGenderTitle)

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

    override fun onRGNomineeAddNo() {
        Log.d(TAG, "onRGNomineeAddNo() called")
        //onChooseNomineeNotAdd()

        val targetGender = getTargetGender()
        val targetGenderTitle = getTargetGenderTitle(targetGender)
        val txt = getString(R.string.nominee_objective, targetGenderTitle)

        AlertDialog.Builder(requireContext())
            .setMessage(txt)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->
                onChooseNomineeAdd(targetGender)
            }
            .setNegativeButton("No") { dialog, which ->
                onChooseNomineeNotAdd()
            }
            .create()
            .show()

    }

    override fun onRGNomineeAddStatusClear() {
        //        binding.rgNomineeAdd.setOnCheckedChangeListener(null)
//        binding.rgNomineeAdd.clearCheck()
//        binding.rgNomineeAdd.setOnCheckedChangeListener(this)
        binding.rgNomineeAdd.clearStatus(this)
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
        if(adapter != null){
            if (adapter!!.getDataset().size == 5){
                questionBox.gone()
            }
        }
        questionText.setText(R.string.would_anyone_else_interested)
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
        if (item.equals(NonPerticipationReasonEnum.REASON_OTHER.value, ignoreCase = true)) {
            llParentOtherReason.visible()
        }else{
            llParentOtherReason.gone()
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
        form.nominees = readNomineeInputsFromList()

        if (form.nominees.isNotEmpty()) {

            form.isNomineeAdd = "Yes"
            form.noNomineeReason = null
            form.otherReason = null

            form.xIsNomineeAdd = getRadioGroup(binding.rgNomineeAdd)
            if (form.xIsNomineeAdd.isNo()) {
                form.xNoNomineeReason = chkSpinner(spReasonNoNominee, UiData.ER_SP_DF)
                if (isOtherSpecify(form.xNoNomineeReason)) {
                    form.xOtherReason = chkEditText(etOtherReason, UiData.ER_ET_DF)
                }
            }

        } else {
            // empty
            form.isNomineeAdd = chkRadioGroup(binding.rgNomineeAdd, UiData.ER_ET_DF)

            if (form.isNomineeAdd.isNo()) {
                form.noNomineeReason = chkSpinner(spReasonNoNominee, UiData.ER_SP_DF)
                if (isOtherSpecify(form.noNomineeReason)) {
                    form.otherReason = chkEditText(etOtherReason, UiData.ER_ET_DF)
                }
            } else {
                // yes
                // already covered
            }

        }

        if (!form.isExtraNomineeOk()) return

        if (form.isOk()) {
            val text = "The project’s objective is to achieve a 50–50 percent distribution between male and female youth in the program. To ensure this, we kindly request your consent to nominate a participant of the opposite gender. If you prefer not to nominate, please select NO and provide your reason."

            val checkExtraCases = form.checkExtraCases()
            if (checkExtraCases != null) {
                showAlerter(checkExtraCases, null)
                return
            }
            if(form.nominees.size == 5){
                onValidated(form)
            }else if(rbNo.isChecked){
                onValidated(form)
            }else{
                //R.string.nominee_objective_alerter_msg
                showAlerterLong("Add Nominee",text)
            }
        }


    }
    //Checks for consecutives in the dataset
    private fun checkOppositeGender(): Boolean {
        val dataset = readNomineeInputsFromList()
        var length = dataset.size
        if(length == 0) return true
        if(length == 1) return false
        else{
            for(i in 0..<length-1){
                if(dataset[i].gender?.equals(dataset[i+1].getOppositeGender(), ignoreCase = true) == true){
                    return true
                }
            }
        }
        return false
    }


    override fun onLongClickDataGeneration() {
        if (!TestConfig.isLongClickDGEnabled) return

        binding.viewButtonBackNext.btNext.setOnLongClickListener {
            onGenerateDummyInput()
            return@setOnLongClickListener true
        }
    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
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
        if(readNomineeInputsFromList().size == 1){
            questionText.setText(R.string.would_anyone_from_your_household_including_yourself_be_interested_in_participating)
        }else if(readNomineeInputsFromList().size == 5){
            questionBox.visible()
        }
        adapter?.remove(pos)
    }

    override fun onNomineeModalCancel() {
        super.onNomineeModalCancel()
        Log.d(TAG, "onNomineeModalCancel() called")
        onRGNomineeAddStatusClear()

    }

    override fun onNomineeModalNomineeInputSuccess(item: Nominee?) {
        super.onNomineeModalNomineeInputSuccess(item)
        Log.d(TAG, "onNomineeModalNomineeInputSuccess() called with: item = $item")
        onGetANomineeFromPopup(item)
        onRGNomineeAddStatusClear()
    }

    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        Log.d(
            TAG,
            "onCheckedChanged() called with: radioGroup = $radioGroup, checkedId = $checkedId"
        )

        onChangeRGNomineeAdd(checkedId)

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

    private fun isListContainsData(): Boolean {
        if (adapter == null) return false
        return adapter!!.itemCount > 0
    }

    private fun shouldShowCrossGenderAlerter(form: HhForm6): Boolean {
        if (isCrossGenderExist()) return false
        if (form.isNomineeAdd.isNo()) return false
        if (form.xIsNomineeAdd.isNo()) return false
        return true
    }

    private fun readNomineeInputsFromList(): ArrayList<Nominee> {
        var nominees = adapter?.getDataset()
        return nominees ?: arrayListOf()
    }

    private fun isOtherSpecify(txt: String?): Boolean {
        return txt.equals(NonPerticipationReasonEnum.REASON_OTHER.value, ignoreCase = true)
    }

    private fun getOppositeGender(string: String): String {
//    if (TestConfig.isNavHackEnabled) {
//        return "Female" // test purpose
//    }
        if (string.equals("Male", true)) return "Female"
        return "Male"
    }

    private fun getTargetGender(): String? {
        if (!isListContainsData()) return null

//        if ((adapter?.itemCount ?: 0) == 0) {
//            val form2 = interactor?.getRootForm()?.form2
//            return form2.getOppositeGender()
//        }

//        if ((adapter?.itemCount ?: 0) == 1) {
//            val nominee = adapter?.getDataset()?.get(0)
//            return nominee?.getOppositeGender()
//        }

        return adapter?.getItem(0)?.gender?.let { getOppositeGender(it) }

    }

    private fun getTargetGenderTitle(targetGender: String?): String {
        return if (targetGender.equals("Male", true)) "man" else "woman"
    }

    private fun isCrossGenderExist(): Boolean {
        //val beneficiaryGender = interactor?.getRootForm()?.form2?.gender
        val listItems = adapter?.getDataset()
        if (listItems.isNullOrEmpty()) return false

        val oppositeGender = interactor?.getRootForm()?.form2?.getOppositeGender()

        for (item in listItems) {
            if (item.gender.equals(oppositeGender, true)) return true
        }
        return false
    }

    private fun getGenderForCross(): String? {
        return interactor?.getRootForm()?.form2.getOppositeGender()
    }


    private fun showAlerterLong(title: String?, msg: String?) {
        Alerter.create(requireActivity())
            .setTitle(title ?: "")
            .setText(msg ?: "")
            .enableSwipeToDismiss()
            .setDuration(6000)      // 3s default
            .setBackgroundColorInt(
                AttrUtils.getAttrColor(
                    requireContext(),
                    R.attr.colorWarning
                )
            ) // or setBackgroundColorInt(Color.CYAN)
            .setTextAppearance(R.style.AlertTextAppr_Text)
            .setTitleAppearance(R.style.AlertTextAppr_Title)
            .show()
    }


}
