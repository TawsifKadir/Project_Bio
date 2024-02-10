package com.xplo.code.ui.dashboard.alternate.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.FragmentAlPreviewBinding
import com.xplo.code.ui.components.ReportViewUtils
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.AlForm1
import com.xplo.code.ui.dashboard.model.AlForm2
import com.xplo.code.ui.dashboard.model.AlForm3
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.ReportRow
import com.xplo.code.ui.dashboard.model.getReportRows
import com.xplo.code.ui.dashboard.model.toJson
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
class AlPreviewFragment : BasicFormFragment(), AlternateContract.PreviewView {

    companion object {
        const val TAG = "AlPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): AlPreviewFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlPreviewFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlPreviewBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: RegistrationContract.Presenter
    private var interactor: AlternateContract.View? = null


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
        binding = FragmentAlPreviewBinding.inflate(inflater, container, false)
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

        generateReport(rootForm)
    }


    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.UpdateHouseholdItemSuccess -> {
                        hideLoading()
                        onUpdateSuccess(event.id)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.UpdateHouseholdItemFailure -> {
                        hideLoading()
                        onUpdateFailure(event.msg)
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

        setToolbarTitle("Alternate Fingerprints")

        binding.viewButtonBackNext.btBack.visible()
        binding.viewButtonBackNext.btNext.visible()
        binding.viewButtonBackNext.btNext.text = "Submit"
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }


    override fun onValidated(form: AlternateForm) {
        Log.d(TAG, "onValidated() called with: form = $form")

    }

    override fun onUpdateSuccess(id: String?) {
        Log.d(TAG, "onUpdateSuccess() called with: id = $id")

        XDialog.Builder(requireActivity().supportFragmentManager)
            .setLayoutId(R.layout.custom_dialog_pnn)
            .setTitle(getString(R.string.fingerprint_enroll_title))
            .setMessage(getString(R.string.fingerprint_enroll_msg))
            .setPosButtonText(getString(R.string.alternate_reg_title))
            .setNegButtonText(getString(R.string.home))
            //.setNeuButtonText(getString(R.string.alternate_reg_title))
            .setThumbId(R.drawable.ic_logo_photo)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    interactor?.navigateToAlternateHome()
                }

                override fun onClickNegativeButton() {
                    requireActivity().finish()
                    //interactor?.navigateToHome()
                }

                override fun onClickNeutralButton() {
                    //interactor?.navigateToAlternate(id)
                }
            })
            .build()
            .show()
    }

    override fun onUpdateFailure(msg: String?) {
        Log.d(TAG, "onUpdateFailure() called with: msg = $msg")

    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")


        val rootForm = interactor?.getRootForm()
        if (rootForm == null) {
            return
        }

        var hhItem = interactor?.getHouseholdItem()
        if (hhItem == null) return

        val hhForm = hhItem.toHouseholdForm()
        hhForm?.alternates?.add(rootForm)
        hhItem.data = hhForm.toJson()

        viewModel.updateHouseholdItem(hhItem)


    }

    override fun onReadInput() {
        Log.d(TAG, "onReadInput() called")

    }

    override fun onLongClickDataGeneration() {

    }

    override fun onGenerateDummyInput() {

    }

    override fun onPopulateView() {

    }


    private fun generateReport(form: AlternateForm?) {
        Log.d(TAG, "generateReport() called with: form = $form")
        if (form == null) return
        addReportForm1(form.form1)
        addReportForm2(form.form2)
        addReportForm3(form.form3)
    }

    private fun addReportForm1(form: AlForm1?) {
        if (form == null) return
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockPerInfo.addView(view)
        }
    }

    private fun addReportForm2(form: AlForm2?) {
        if (form == null) return
        binding.viewPreview.ivAvatar.loadAvatar(form.img)
    }

    private fun addReportForm3(form: AlForm3?) {
        val rows = form.getReportRows()
        for (item in rows) {
            val view = getRowView(item)
            binding.viewPreview.blockFinger.addView(view)
        }
    }

    private fun getRowView(item: ReportRow?): View {
        Log.d(TAG, "getRowView() called with: item = $item")
        return ReportViewUtils.getRowView(requireContext(), layoutInflater, item)
    }


}
