package com.xplo.code.ui.dashboard.alternate.forms

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xplo.code.base.BaseFragment
import com.xplo.code.core.Bk
import com.xplo.code.core.ext.gone
import com.xplo.code.data.db.room.model.Beneficiary
import com.xplo.code.databinding.FragmentAlternateHomeBinding
import com.xplo.code.ui.dashboard.alternate.AlternateContract
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import com.xplo.code.ui.dashboard.household.list.AlternateListAdapter2
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
class AlternateHomeFragment : BaseFragment(), AlternateContract.HomeView,
    AlternateListAdapter2.OnItemClickListener {

    companion object {
        const val TAG = "AlternateHomeFragment"

        @JvmStatic
        fun newInstance(parent: String?): AlternateHomeFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = AlternateHomeFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAlternateHomeBinding
    private val viewModel: HouseholdViewModel by viewModels()

    //private lateinit var presenter: HomeContract.Presenter
    private var interactor: AlternateContract.View? = null

    private var adapter: AlternateListAdapter2? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AlternateContract.View) {
            interactor = activity as AlternateContract.View
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlternateHomeBinding.inflate(inflater, container, false)
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

        //binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        adapter = AlternateListAdapter2()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

        //viewModel.getHouseholdItems()

        viewModel.showBeneficiary(requireContext())
    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        //showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsSuccess -> {
                        hideLoading()
                        //onGetHouseholdList(event.items)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemsFailure -> {
                        hideLoading()
                        onGetHouseholdListFailure(event.msg)
                        viewModel.clearEvent()
                    }

                    is HouseholdViewModel.Event.GetDataLocalDb -> {
                        hideLoading()
                        onGetHouseholdList(event.beneficiary)
                        viewModel.clearEvent()
                    }

                    else -> Unit
                }
            }
        }

//        binding.btAddAlternate.setOnClickListener {
//            interactor?.navigateToForm1(null, false, false)
//        }

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Alternate Home")

        binding.llFooter.gone()

    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun navigateToHouseholdDetails(content: Beneficiary) {
        Log.d(TAG, "navigateToHouseholdDetails() called with: content = $content")
        //interactor?.navigateToFormDetails(content)
    }

    override fun onGetHouseholdList(items: List<Beneficiary>?) {
        Log.d(TAG, "onGetHouseholdList() called with: items = ${items?.size}")
        if (items == null) return
        //adapter?.addAll(items)

        val pItems = arrayListOf<Beneficiary>()
        for (item in items){
            if (!item.isSynced) {
                pItems.add(item)
            }
        }
        adapter?.addAll(pItems)

    }

    override fun onGetHouseholdListFailure(msg: String?) {
        Log.d(TAG, "onGetHouseholdListFailure() called with: msg = $msg")
        //showMessage(msg)
    }

    override fun onClickBeneficiaryItem(item: Beneficiary, pos: Int) {
        Log.d(TAG, "onClickBeneficiaryItem() called with: item = $item, pos = $pos")
        //interactor?.navigateToForm1(item.applicationId, item.respondentFirstName, null, true, false)

        val fname = item.respondentFirstName + " " + item.respondentMiddleName + " " + item.respondentLastName
        interactor?.navigateToForm1(item.applicationId, fname, "V", true, false)
    }

}