package com.xplo.code.ui.content_list

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
import com.xplo.code.data_module.model.content.ContentItem
import com.xplo.code.databinding.FragmentContentListBinding
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
class ContentListFragment : BaseFragment(), ContentListContract.View,
    ContentListAdapter.OnItemClickListener {

    companion object {
        const val TAG = "ContentListFragment"

        @JvmStatic
        fun newInstance(parent: String?): ContentListFragment {
            Log.d(TAG, "newInstance() called with: parent = $parent")
            val fragment = ContentListFragment()
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentContentListBinding
    private val viewModel: ContentListViewModel by viewModels()
    //private lateinit var presenter: HomeContract.Presenter

    private var adapter: ContentListAdapter? = null

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
        binding = FragmentContentListBinding.inflate(inflater, container, false)
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

        adapter = ContentListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

        viewModel.getContents(0, 10)


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is ContentListViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is ContentListViewModel.Event.GetItemSuccess -> {
                        hideLoading()
                        onGetContentList(event.items)
                    }

                    is ContentListViewModel.Event.GetItemFailure -> {
                        hideLoading()
                        onGetContentListFailure(event.msg)
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

    override fun navigateToContentDetails(content: ContentItem) {
        Log.d(TAG, "navigateToContentDetails() called with: content = $content")

    }

    override fun onGetContentList(items: List<ContentItem>?) {
        Log.d(TAG, "onGetContentList() called with: items = $items")
        if (items == null) return
        adapter?.addAll(items)
    }

    override fun onGetContentListFailure(msg: String?) {
        Log.d(TAG, "onGetContentListFailure() called with: msg = $msg")
        //showMessage(msg)
    }

    override fun onClickContentItem(item: ContentItem) {
        Log.d(TAG, "onClickHistoryItem() called with: item = $item")
        //dToast(item.title)
        navigateToContentDetails(item)
    }


}