package com.xplo.code.ui.dashboard.payroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.core.Bk
import com.xplo.code.databinding.ActivityPayrollBinding
import com.xplo.code.ui.dashboard.model.PayrollEntry
import com.xplo.code.ui.dashboard.report.ReportContract

class PayrollActivity : BaseActivity(), ReportContract.View {

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

    private lateinit var binding: ActivityPayrollBinding
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

        val entries = "wsgetbenworkwagesdet.json"
            .loadJsonFromAsset()
            .parseJsonToList()
            .result


        binding.apply {
            recyclerView.adapter = PayrollEntriesAdapter(entries)
        }

    }

    override fun initObserver() {

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
        setToolbarTitle("Payroll Entries")
    }

    private fun String.loadJsonFromAsset(): String {
        return assets.open(this).bufferedReader().use { it.readText() }
    }

    private fun String.parseJsonToList(): PayrollEntry {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(this)
    }
}