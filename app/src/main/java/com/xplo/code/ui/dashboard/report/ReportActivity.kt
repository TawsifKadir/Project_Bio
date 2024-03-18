package com.xplo.code.ui.dashboard.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.data.db.room.model.SyncBeneficiary
import com.xplo.code.databinding.ActivityReportBinding
import com.xplo.code.utils.DialogUtil
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
class ReportActivity : BaseActivity(), ReportContract.View, ReportListAdapter.OnItemClickListener {

    companion object {
        private const val TAG = "ReportActivity"

        @JvmStatic
        fun open(context: Context, parent: String?) {
            Log.d(TAG, "open() called with: context = $context, parent = $parent")
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        var STEP = 0

    }

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()

    //private lateinit var toolbar: Toolbar
    private var adapter: ReportListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
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

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = ReportListAdapter()
        adapter?.setOnItemClickListener(this)
        binding.recyclerView.adapter = adapter

//        if (!isNetworkIsConnected) {
//            onNoInternet()
//            return
//        }


    }

    override fun initObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {

                    is ReportViewModel.Event.Loading -> {
                        showLoading()
                    }

                    is ReportViewModel.Event.GetItemSuccess -> {
                        hideLoading()
                        //onGetReports(event.items)
                    }

                    is ReportViewModel.Event.GetItemFailure -> {
                        hideLoading()
                        //onGetReportsFailure(event.msg)
                    }

                    is ReportViewModel.Event.GetSyncDataLocalDb -> {
                        hideLoading()
                        if (event.beneficiary.isEmpty()) {
                            DialogUtil.showLottieDialogFailMsg(
                                this@ReportActivity,
                                "No Data Found."
                            )
                        } else {
                            adapter?.addAll(event.beneficiary)
                            adapter?.notifyDataSetChanged()
                        }

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

        setToolbarTitle("Report")

        viewModel.showBeneficiary(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        //presenter.detach()
        //presenter.onDetachView()
    }

    override fun onClickHouseholdItem(item: SyncBeneficiary, pos: Int) {
        Toast.makeText(this, item.applicationId, Toast.LENGTH_SHORT).show()
    }


}
