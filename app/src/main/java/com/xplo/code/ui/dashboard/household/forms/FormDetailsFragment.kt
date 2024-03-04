package com.xplo.code.ui.dashboard.household.forms


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.data.db.room.model.Beneficiary
import com.xplo.code.databinding.FragmentFormDetailsBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.ReportRow
import com.xplo.code.ui.dashboard.model.getReportRows
import com.xplo.code.ui.dashboard.model.getReportRowsAltSummary
import com.xplo.code.ui.testing_lab.FormPGActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@AndroidEntryPoint
class FormDetailsFragment : BaseFragment(), HouseholdContract.FormDetailsView {

    companion object {
        const val TAG = "FormDetailsFragment"

        @JvmStatic
        fun newInstance(parent: String?, item: HouseholdItem?): FormDetailsFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent, item = ${item?.hid}")
            val fragment = FormDetailsFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putSerializable(Bk.KEY_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
        @JvmStatic
        fun newInstance(parent: String?, item: com.kit.integrationmanager.model.Beneficiary?): FormDetailsFragment {
           // Log.d(TAG, "newInstance() called with: parent = $parent, item = ${item?.hid}")
            val fragment = FormDetailsFragment()
            fragment.item = item?.applicationId
            //val bundle = Bundle()
            //bundle.putString(Bk.KEY_PARENT, parent)
            //bundle.putSerializable(Bk.KEY_ITEM, item)
            //fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentFormDetailsBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: HomeContract.Presenter
    //private var interactor: HouseholdContract.View? = null

    private var householdItem : HouseholdItem? = null
    private var item : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInitial()
        initView()
        initObserver()

    }


    override fun initInitial() {
        //binding.tvDetails.movementMethod = ScrollingMovementMethod()

    }

    override fun initView() {

        if (arguments != null) {
            householdItem = arguments?.getSerializable(Bk.KEY_ITEM) as HouseholdItem
            onGetCompleteData(householdItem)
        }
    }

    override fun initObserver() {

        binding.tvPage.setOnLongClickListener {
            //JvActivity.open(requireContext(), null, householdItem?.data)
            FormPGActivity.open(requireContext(), null, householdItem?.id)
            return@setOnLongClickListener true
        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Form Details")

    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onGetCompleteData(item: HouseholdItem?) {
        Log.d(TAG, "onGetCompleteData() called with: item = $item")

        //binding.tvDetails.text = item.toString()

        val form = item?.toHouseholdForm()

        generateReport(form)

    }

    private fun generateReport(form: HouseholdForm?) {
        Log.d(TAG, "generateReport() called with: form = $form")
        if (form == null) return
        addReportForm1(form.form1)
        addReportForm2(form.form2)
        addReportForm3(form.form3)
        addReportForm4(form.form4)
        addReportForm5(form.form5)
        addReportForm6(form.form6)
        addReportAlternate(form)
    }

    private fun addReportForm1(form: HhForm1?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockLocation.addView(view)
        }
    }

    private fun addReportForm2(form: HhForm2?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockPerInfo.addView(view)
        }
    }

    private fun addReportForm3(form: HhForm3?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockHouseholdBreakdown.addView(view)
        }
    }

    private fun addReportForm4(form: HhForm4?) {
        if (form == null) return
        binding.viewPreview.ivAvatar.loadAvatar(form.photoData?.imgPath)
    }

    private fun addReportForm5(form: HhForm5?) {
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockFinger.addView(view)
        }
    }

    private fun addReportForm6(form: HhForm6?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockNominee.addView(view)
        }
    }

    private fun addReportAlternate(form: HouseholdForm?) {
        if (form == null) return
        val rows = form.getReportRowsAltSummary()

        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockAlternate.addView(view)
        }

        for (item in form.alternates){
            val view = getAltRowView(item)
            binding.viewPreview.blockAlternate.addView(view)
        }

    }


    private fun getRowView(item: ReportRow?): View {
        Log.d(TAG, "getRowView() called with: item = $item")
        return ReportViewUtils.getRowView(requireContext(), layoutInflater, item)
    }

    private fun getAltRowView(item: AlternateForm?): View {
        Log.d(TAG, "getAltRowView() called with: item = $item")
        return ReportViewUtils.getAltFormView(requireContext(), layoutInflater, item)
    }


}