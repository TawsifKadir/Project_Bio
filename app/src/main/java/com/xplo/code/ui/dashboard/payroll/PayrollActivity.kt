package com.xplo.code.ui.dashboard.payroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityPayrollBinding
import com.xplo.code.ui.dashboard.model.PayrollEntry
import com.xplo.code.ui.dashboard.report.ReportContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class PayrollActivity : BaseActivity(), ReportContract.View {
    private lateinit var binding: ActivityPayrollBinding
    private val viewModel by viewModels<PayrollViewModel>()


    companion object {
        private const val TAG = "PayrollActivity"

        @JvmStatic
        fun open(context: Context, parent: String?) {
            Log.d(TAG, "open() called with: context = $context, parent = $parent")
            val bundle = Bundle()
            bundle.putString(Bk.KEY_PARENT, parent)
            val intent = Intent(context, PayrollActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInitial()
        initView()
        initObserver()

    }

    override fun initInitial() {

    }

    override fun initView() {
        setToolBar()

        lifecycleScope.launch{
            val entries = "wsgetbenworkwagesdet.json"
                .loadJsonFromAsset()
                .parseJsonToList()
                .result
            viewModel.entries = entries
        }

    }

    override fun initObserver() {
        viewModel.currentPageEntries.observe(this){entries->
            binding.apply {
                pageNo.text = viewModel.currentPageNo.toString()
                recyclerView.adapter = PayrollEntriesAdapter(
                    dataset = entries,
                    onRootClick = {position->
                        viewModel.currentPageEntry = entries[position]
                        navigateToPayrollDetails(entries[position])
                    },
                    viewModel)
                pre.setOnClickListener {
                    viewModel.changePage(false,entries.first())
                }
                next.setOnClickListener {
                    viewModel.changePage(true,entries.last())
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
        setToolbarTitle("Payroll")
    }

    private fun String.loadJsonFromAsset(): String {
        return assets.open(this).bufferedReader().use { it.readText() }
    }

    private fun String.parseJsonToList(): PayrollEntry {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(this)
    }

}