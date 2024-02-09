package com.xplo.code.ui.dashboard.household.forms


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentFormDetailsBinding
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhForm4
import com.xplo.code.ui.dashboard.model.HhForm5
import com.xplo.code.ui.dashboard.model.HhForm6
import com.xplo.code.ui.dashboard.model.ReportRow
import com.xplo.code.ui.dashboard.model.getReportRows
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
            Log.d(TAG, "newInstance() called with: parent = $parent, item = ${item?.id}")
            val fragment = FormDetailsFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putSerializable(Bk.KEY_ITEM, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentFormDetailsBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: HomeContract.Presenter
    //private var interactor: HouseholdContract.View? = null


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
            val householdItem = arguments?.getSerializable(Bk.KEY_ITEM) as HouseholdItem
            onGetCompleteData(householdItem)
        }
    }

    override fun initObserver() {


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
        if (form == null) return

        addReportForm1(form.form1)
        addReportForm2(form.form2)
        addReportForm3(form.form3)
        addReportForm4(form.form4)
        addReportForm5(form.form5)
        addReportForm6(form.form6)


    }

    private fun addReportForm1(form: HhForm1?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.blockLocation.addView(view)
        }
    }

    private fun addReportForm2(form: HhForm2?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.blockPerInfo.addView(view)
        }
    }

    private fun addReportForm3(form: HhForm3?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.blockHouseholdBreakdown.addView(view)
        }
    }

    private fun addReportForm4(form: HhForm4?) {
        if (form == null) return
        binding.ivAvatar.loadAvatar(form.img)
    }

    private fun addReportForm5(form: HhForm5?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.blockFinger.addView(view)
        }
    }

    private fun addReportForm6(form: HhForm6?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.blockNominee.addView(view)
        }
    }


    private fun getRowView(item: ReportRow?): View {
        Log.d(TAG, "getRowView() called with: item = $item")


        val rowView: View = layoutInflater.inflate(R.layout.row_report_item, null, false)

        if (item == null) return rowView

        val tvTitle: TextView = rowView.findViewById(R.id.tvTitle)
        val tvValue: TextView = rowView.findViewById(R.id.tvValue)
        val tvTitle2: TextView = rowView.findViewById(R.id.tvTitle2)
        val tvValue2: TextView = rowView.findViewById(R.id.tvValue2)
        val llCol2: View = rowView.findViewById(R.id.llCol2)

        tvTitle.text = item.title
        tvValue.text = item.value

        if (item.title2 == null) {
            //llCol2.gone()
            //tvValue2.gone()
            return rowView
        }

        tvTitle2.text = item.title2
        tvValue2.text = item.value2

        return rowView

    }


}