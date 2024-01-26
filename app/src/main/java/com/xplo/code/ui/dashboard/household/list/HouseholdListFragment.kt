package com.xplo.code.ui.dashboard.household.list

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
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.databinding.FragmentHouseholdListBinding
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
class HouseholdListFragment : BaseFragment(), HouseholdContract.HouseholdListView,
    HouseholdListAdapter.OnItemClickListener {

    companion object {
        const val TAG = "HouseholdListFragment"

        @JvmStatic
        fun newInstance(parent: String?): HouseholdListFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = HouseholdListFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHouseholdListBinding
    private val viewModel: HouseholdViewModel by viewModels()
    //private lateinit var presenter: HomeContract.Presenter

    private var adapter: HouseholdListAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

//        if (context is ProfileContract.View) {
//            interactor = activity as ProfileContract.View
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHouseholdListBinding.inflate(inflater, container, false)
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

        adapter = HouseholdListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

        viewModel.getHouseholdItems()


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is HouseholdViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemSuccess -> {
                        hideLoading()
                        onGetHouseholdList(event.items)
                    }

                    is HouseholdViewModel.Event.GetHouseholdItemFailure -> {
                        hideLoading()
                        onGetHouseholdListFailure(event.msg)
                    }

                    else -> Unit
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()

    }


    override fun onDestroy() {
        //presenter.onDetachView()
        super.onDestroy()
    }

    override fun navigateToHouseholdDetails(content: HouseholdItem) {
        Log.d(TAG, "navigateToHouseholdDetails() called with: content = $content")

    }

    override fun onGetHouseholdList(items: List<HouseholdItem>?) {
        Log.d(TAG, "onGetHouseholdList() called with: items = $items")
        if (items == null) return
        adapter?.addAll(items)
    }

    override fun onGetHouseholdListFailure(msg: String?) {
        Log.d(TAG, "onGetHouseholdListFailure() called with: msg = $msg")
        //showMessage(msg)
    }

    override fun onClickHouseholdItem(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItem() called with: item = $item, pos = $pos")
        //dToast(item.title)
        navigateToHouseholdDetails(item)
    }

    override fun onClickHouseholdItemDelete(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItemDelete() called with: item = $item, pos = $pos")
        viewModel.deleteHouseholdItem(item)
        adapter?.remove(pos)
    }

    override fun onClickHouseholdItemSend(item: HouseholdItem, pos: Int) {
        Log.d(TAG, "onClickHouseholdItemSend() called with: item = $item, pos = $pos")

    }


}