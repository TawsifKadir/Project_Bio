package com.xplo.code.ui.dashboard.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityReportBinding
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
class ReportActivity : BaseActivity(), ReportContract.View {

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

    }

    override fun onDestroy() {
        super.onDestroy()
        //presenter.detach()
        //presenter.onDetachView()
    }


}
