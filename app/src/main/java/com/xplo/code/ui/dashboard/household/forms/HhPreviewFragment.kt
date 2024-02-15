package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.R
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhPreviewBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
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
class HhPreviewFragment : BaseFragment(), HouseholdContract.PreviewView {

    companion object {
        const val TAG = "HhPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhPreviewFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhPreviewFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhPreviewBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: HouseholdContract.View? = null


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
        binding = FragmentHhPreviewBinding.inflate(inflater, container, false)
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
    }

    override fun initView() {
        val rootForm = interactor?.getRootForm()
        Log.d(TAG, "initView: $rootForm")
        //binding.tvDetails.text = rootForm.toJson()

        generateReport(rootForm)

    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.SaveHouseholdFormSuccess -> {
                        hideLoading()
                        onSaveSuccess(event.id)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.SaveHouseholdFormFailure -> {
                        hideLoading()
                        onSaveFailure(event.msg)
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
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle("Preview")

        binding.viewButtonBackNext.btBack.visible()
        binding.viewButtonBackNext.btNext.visible()
        binding.viewButtonBackNext.btNext.text = "Save"
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onGetCompleteData(data: HouseholdForm?) {
        Log.d(TAG, "onGetCompleteData() called with: data = $data")
    }

    override fun onSaveSuccess(id: String?) {
        Log.d(TAG, "onSaveSuccess() called with: id = $id")

        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle("Data Saved")
            .setMessage("Household successfully saved. Do you want register another household?")
            .setPosButtonText("Another Household")
            .setNegButtonText(getString(R.string.cancel))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToAnotherHousehold(interactor?.getRootForm()?.form1)
                }

                override fun onClickNegativeButton() {
                    requireActivity().finish()

                }

                override fun onClickNeutralButton() {

                }
            })
            .build()
            .show()

//        XDialog.Builder(requireActivity().supportFragmentManager)
//            .setLayoutId(R.layout.custom_dialog_pnn)
//            .setTitle(getString(R.string.review_complete_reg))
//            .setMessage(getString(R.string.review_complete_reg_msg))
//            .setPosButtonText("Alternate Registration")
//            .setNegButtonText(getString(R.string.home))
//            .setNeuButtonText("Household Registration")
//            .setThumbId(R.drawable.ic_logo_photo)
//            .setCancelable(false)
//            .setListener(object : XDialog.DialogListener {
//                override fun onClickPositiveButton() {
//                    requireActivity().finish()
//                    interactor?.navigateToAlternate(id)
//                }
//
//                override fun onClickNegativeButton() {
//                    requireActivity().finish()
//
//                }
//
//                override fun onClickNeutralButton() {
//                    //interactor?.navigateToHousehold()
//                    interactor?.navigateToAnotherHousehold(interactor?.getRootForm()?.form1)
//                }
//            })
//            .build()
//            .show()
    }

    override fun onSaveFailure(msg: String?) {
        Log.d(TAG, "onSaveFailure() called with: msg = $msg")
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        //interactor?.navigateToPreview()


        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.review_complete_reg))
            .setMessage(getString(R.string.review_complete_reg_msg))
            .setPosButtonText("Save")
            .setNegButtonText(getString(R.string.cancel))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(true)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    val rootForm = interactor?.getRootForm()
                    viewModel.saveHouseholdForm(rootForm)
                }

                override fun onClickNegativeButton() {

                }

                override fun onClickNeutralButton() {

                }
            })
            .build()
            .show()


    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")
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
//        binding.viewPreview.llAlternate.visibility = View.GONE
//        if((form.form6?.nominees?.size ?: 0) != 0){
//            addReportForm6(form.form6)
//        }else{
//            binding.viewPreview.llNominee.visibility = View.GONE
//        }
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
        binding.viewPreview.ivAvatar.loadAvatar(form.img)
    }

    private fun addReportForm5(form: HhForm5?) {
        if (form == null) return
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
    }


    private fun getRowView(item: ReportRow?): View {
        Log.d(TAG, "getRowView() called with: item = $item")
        return ReportViewUtils.getRowView(requireContext(), layoutInflater, item)
    }


}
