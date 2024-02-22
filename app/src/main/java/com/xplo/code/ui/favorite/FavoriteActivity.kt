package com.xplo.code.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.PaginationListener
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityFavoriteBinding
import com.xplo.data.model.content.ContentItem
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
class FavoriteActivity : BaseActivity(), FavoriteView,
    FavoriteAdapter.OnItemClickListener {

    companion object {
        private const val TAG = "FavoriteActivity"

        @JvmStatic
        fun open(context: Context, parent: String?) {
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, FavoriteActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        var STEP = 0

    }

    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()
    //private lateinit var toolbar: Toolbar

    private var adapter: FavoriteAdapter? = null

    // pagination
    private var offset = 0
    private val LIMIT = 10
    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInitial()
        initView()
        initObserver()


    }

    override fun initInitial() {

    }

    override fun initView() {
        setToolBar()

        val parent = intent.getStringExtra(Bk.KEY_PARENT)
        Log.d(TAG, "initView: parent: $parent")

//        if (!isNetworkIsConnected) {
//            onNoInternet()
//            return
//        }

        //binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        adapter = FavoriteAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

        loadData(offset)


    }

    override fun initObserver() {

        binding.recyclerView.addOnScrollListener(object : PaginationListener() {
            override fun onLoadMore() {
                Log.d(TAG, "onLoadMore() called")
                loadData(adapter!!.itemCount)
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun getPageSize(): Int {
                return LIMIT
            }

        })

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is FavoriteViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is FavoriteViewModel.Event.GetItemSuccess -> {
                        hideLoading()
                        onGetFavorites(event.items)
                    }

                    is FavoriteViewModel.Event.GetItemFailure -> {
                        hideLoading()
                        onGetFavoritesFailure(event.msg)
                    }

                    is FavoriteViewModel.Event.UpdateFavoriteSuccess -> {
                        hideLoading()
                        onRemoveFromFavoriteSuccess(event.position)
                    }

                    is FavoriteViewModel.Event.UpdateFavoriteFailure -> {
                        hideLoading()
                        onGetFavoritesFailure(event.msg)
                    }

                    else -> Unit
                }
            }
        }

    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        //presenter.detach()
        //presenter.onDetachView()
    }

    override fun navigateToContentDetails(content: ContentItem) {
        Log.d(TAG, "navigateToContentDetails() called with: content = $content")

    }

    override fun onRefreshUI() {
        if (adapter == null || adapter!!.itemCount < 1) {
            onEmptyList()
        }
    }

    override fun onEmptyList() {
        Log.d(TAG, "onEmptyList() called")
        //binding.tvTitle.text = getString(R.string.you_have_no_favourite_videos)
    }

    override fun onNoMoreItems() {
        Log.d(TAG, "onNoMoreItems() called")
        isLastPage = true
    }

    override fun isListHasItems(): Boolean {
        Log.d(TAG, "isListHasItems() called")
        if (adapter == null) return false
        if (adapter!!.itemCount > 0) return true
        return false
    }

    override fun loadData(offset: Int) {
        Log.d(TAG, "loadData() called with: offset = $offset")
        isLoading = true
        //presenter.getFavorites2(offset, LIMIT)

        viewModel.getFavorites(offset, LIMIT)

    }

    override fun onGetFavorites(items: List<ContentItem>?) {
        Log.d(TAG, "onGetFavorites2() called with: items = $items")

        isLoading = false

        if (isListHasItems() && items.isNullOrEmpty()) {
            onNoMoreItems()
            return
        }

        if (items == null || items.isEmpty()) {
            onEmptyList()
            return
        }

        adapter?.addAll(items)
    }

    override fun onGetFavoritesFailure(msg: String?) {
        Log.d(TAG, "onGetFavoritesFailure() called with: msg = $msg")
        isLoading = false
        if (isListHasItems()) {
            onNoMoreItems()
            return
        }
    }

    override fun onRemoveFromFavoriteSuccess(position: Int) {
        Log.d(TAG, "onRemoveFromFavoriteSuccess() called with: position = $position")
        // favorite removed from db, now remove from list
        //adapter?.remove(position)
        onRefreshUI()

    }

    override fun onUpdateFavoriteStatusFailure(msg: String?) {
        Log.d(TAG, "onUpdateFavoriteStatusFailure() called with: msg = $msg")

    }

//    override fun onNoInternet() {
//        showMessage(getString(R.string.network_error_msg))
//    }

    override fun onClickFavoriteItem(item: ContentItem) {
        Log.d(TAG, "onClickFavoriteItem() called with: item = $item")
        navigateToContentDetails(item)
    }

    override fun onClickFavoriteIcon(item: ContentItem, position: Int) {
        Log.d(TAG, "onClickFavoriteIcon() called with: item = $item, position = $position")


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
