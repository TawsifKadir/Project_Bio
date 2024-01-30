package com.xplo.code.ui.dashboard.household.forms

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.databinding.FragmentFormDetailsBinding
import com.xplo.code.ui.dashboard.household.HouseholdContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
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
        binding.tvDetails.movementMethod = ScrollingMovementMethod()

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

        binding.tvDetails.text = item.toString()
    }


}