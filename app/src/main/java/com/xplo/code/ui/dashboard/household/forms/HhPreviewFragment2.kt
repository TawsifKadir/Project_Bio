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
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhPreview2Binding
import com.xplo.code.ui.components.XDialog
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm1
import com.xplo.code.ui.dashboard.model.HhForm2
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.getFullName

import com.xplo.code.BuildConfig
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
class HhPreviewFragment2 : BaseFragment(), HouseholdContract.PreviewView {

    companion object {
        const val TAG = "HhPreviewFragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhPreviewFragment2 {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhPreviewFragment2()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHhPreview2Binding
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
        binding = FragmentHhPreview2Binding.inflate(inflater, container, false)
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

        binding.tvForm1Location.text = getForm1Text(rootForm?.form1)
        binding.tvForm2PerInfo.text = getForm2Text(rootForm?.form2)

        //loadImage(rootForm?.form4?.img)
    }

    private fun getForm1Text(form: HhForm1?): String {
        Log.d(TAG, "getForm1Text() called with: form = $form")
        var txt = StringBuilder()
        if (form == null) return txt.toString()

        txt.append(getFormattedText("State:", form.state?.name, "County: ", form.county?.name))
            .append("\n" + getFormattedText("Payam:", form.state?.name, "Boma: ", form.county?.name))

        return txt.toString()

    }

    private fun getForm2Text(form: HhForm2?): String {
        Log.d(TAG, "getForm1Text() called with: form = $form")
        var txt = StringBuilder()
        if (form == null) return txt.toString()

        txt.append(getFormattedText("Name:", form.getFullName(), "Gender: ", form.gender))
            .append("\n" + getFormattedText("Gender:", form.gender, "Age: ", form.age.toString()))

        return txt.toString()

    }

    private fun getFormattedText(key: String?, value: String?): String {
        return String.format("%-10s - %s", key, value)
    }

    private fun getFormattedText(
        key: String?,
        value: String?,
        key2: String?,
        value2: String?,
    ): String {
        return String.format("%-20s %-20s %-50s %-20s", key, value, key2, value2)
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
        binding.viewButtonBackNext.btNext.text = "Submit"
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
            .setTitle(getString(R.string.review_complete_reg))
            .setMessage(getString(R.string.review_complete_reg_msg))
            .setPosButtonText("Alternate Registration")
            .setNegButtonText(getString(R.string.home))
            .setNeuButtonText("Household Registration")
            .setThumbId(R.drawable.logo_splash)
            .setCancelable(false)
            .setListener(object : XDialog.DialogListener {
                override fun onClickPositiveButton() {
                    requireActivity().finish()
                    interactor?.navigateToAlternate(id)
                }

                override fun onClickNegativeButton() {
                    requireActivity().finish()

                }

                override fun onClickNeutralButton() {
                    //interactor?.navigateToHousehold()
                    interactor?.navigateToAnotherHousehold(interactor?.getRootForm()?.form1)
                }
            })
            .build()
            .show()
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

        val rootForm = interactor?.getRootForm()

        viewModel.saveHouseholdFormAsHouseholdItem(rootForm)

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

    private fun loadImage(url: String?) {
        if (url.isNullOrEmpty()) return

//        Glide.with(this)
//            .load(url)
//            .into(this.binding.imgPhoto)
//
//        binding.imgPhoto.setColorFilter(
//            ContextCompat.getColor(
//                requireContext(),
//                android.R.color.transparent
//            )
//        )

    }

}
