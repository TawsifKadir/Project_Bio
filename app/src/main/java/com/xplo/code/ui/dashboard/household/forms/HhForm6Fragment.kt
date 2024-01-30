package com.xplo.code.ui.dashboard.household.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.R
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.visible
import com.xplo.code.databinding.FragmentHhForm6NomineeBinding
import com.xplo.code.ui.dashboard.base.BasicFormFragment
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.model.HhForm6
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
class HhForm6Fragment : BasicFormFragment(), HouseholdContract.Form6View {

    companion object {
        const val TAG = "HhForm6Fragment"

        @JvmStatic
        fun newInstance(
            parent: String?
        ): HhForm6Fragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HhForm6Fragment()
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

        val rootForm = interactor?.getRootForm()
        Log.d(TAG, "initView: $rootForm")
    }

    override fun initObserver() {

        binding.viewButtonBackNext.btBack.setOnClickListener {
            onClickBackButton()
        }

        binding.viewButtonBackNext.btNext.setOnClickListener {
            onClickNextButton()
        }

        binding.rgNomineeAdd.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.rbYes -> onDecisionAddNominee(true)
                R.id.rbNo -> onDecisionAddNominee(false)
            }
        }

        binding.btAdd.setOnClickListener {

        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Nominee")

//        binding.viewButtonBackNext.btBack.visible()
//        binding.viewButtonBackNext.btNext.visible()
    }

    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun onValidated(form: HhForm6?) {
        Log.d(TAG, "onValidated() called with: form = $form")
    }

    override fun onDecisionAddNominee(isAdd: Boolean) {
        Log.d(TAG, "onDecisionAddNominee() called with: isAdd = $isAdd")

        if (isAdd) {
            binding.viewNominee.visible()
            binding.btAdd.visible()
            binding.viewReasonNoNominee.gone()
        } else {
            binding.viewNominee.gone()
            binding.btAdd.gone()
            binding.viewReasonNoNominee.visible()
        }

    }

    override fun onClickAddNominee() {

    }

    override fun onAddNominee(number: Int) {
        when (number) {
            1 -> binding.nominee1.root.visible()
            2 -> binding.nominee2.root.visible()
            3 -> binding.nominee3.root.visible()
            4 -> binding.nominee4.root.visible()
            5 -> binding.nominee5.root.visible()
        }
    }

    override fun onHideNominee(number: Int) {
        when (number) {
            1 -> binding.nominee1.root.gone()
            2 -> binding.nominee2.root.gone()
            3 -> binding.nominee3.root.gone()
            4 -> binding.nominee4.root.gone()
            5 -> binding.nominee5.root.gone()
        }
    }

    override fun onClickBackButton() {
        Log.d(TAG, "onClickBackButton() called")
        interactor?.onBackButton()
    }

    override fun onClickNextButton() {
        Log.d(TAG, "onClickNextButton() called")
        interactor?.navigateToPreview()
    }

    override fun onReadInput() {
        Log.d(TAG, "onValidation() called")

        val form = HhForm6()


    }

    override fun onGenerateDummyInput() {
        Log.d(TAG, "onGenerateDummyInput() called")
    }

    override fun onPopulateView() {
        Log.d(TAG, "onPopulateView() called")
    }
}
