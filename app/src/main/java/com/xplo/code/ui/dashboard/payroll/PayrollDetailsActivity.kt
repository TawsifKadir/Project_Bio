package com.xplo.code.ui.dashboard.payroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.xplo.code.R
import com.xplo.code.base.BaseActivity
import com.xplo.code.databinding.ActivityPayrollDetailsBinding
import com.xplo.code.ui.dashboard.model.PayrollEntry
import com.xplo.code.ui.dashboard.report.ReportContract

class PayrollDetailsActivity : BaseActivity(), ReportContract.View {
    private lateinit var binding: ActivityPayrollDetailsBinding

    companion object {
        private const val TAG = "PayrollDetailsActivity"

        @JvmStatic
        fun open(context: Context, data: PayrollEntry.Result) {
            Log.d(TAG, "open() called with: context = $context, data = $data")
            val bundle = Bundle()
//            bundle.putString(Bk.KEY_PARENT, parent)
            bundle.putParcelable("data", data)
            val intent = Intent(context, PayrollDetailsActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayrollDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInitial()
        initView()
        initObserver()

    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle("Payroll Details")
    }

    override fun initInitial() {}

    @Suppress("DEPRECATION")
    override fun initView() {
        setToolBar()
        binding.apply {
            val currentPageEntry = intent.extras?.getParcelable<PayrollEntry.Result>("data")
            currentPageEntry?.let {
                intWagePaymentReqid.text =
                    "Wage Payment Req Id: ${currentPageEntry.intWagePaymentReqid}"
                ReqFromDate.text = "Req From Date: ${currentPageEntry.reqFromDate}"
                ReqToDate.text = "Req To Date: ${currentPageEntry.reqToDate}"
                Exchangerateattime.text =
                    "Exchange Rate at time: ${currentPageEntry.exchangerateattime}"
                WageRateUSD.text = "Wage Rate USD: ${currentPageEntry.wageRateUSD}"
                ReqNo.text = "Req No: ${currentPageEntry.reqNo}"
                intWagesReqBenTransid.text =
                    "Wages Req Trans Id: ${currentPageEntry.intWagesReqBenTransid}"
                intBenid.text = "Ben id: ${currentPageEntry.intBenid}"
                EnrollmentNo.text = "Enrollment No: ${currentPageEntry.enrollmentNo}"
                householdNumber.text = "Household Number: ${currentPageEntry.householdNumber}"
                intWorkid.text = "Work Id: ${currentPageEntry.intWorkid}"
                WorkCode.text = "WorkCode: ${currentPageEntry.workCode}"
                TotAttendance.text = "TotAttendance: ${currentPageEntry.totAttendance}"
                WageRateUSDTotal.text = "Wage Rate USD Total: ${currentPageEntry.wageRateUSDTotal}"
                DateofCreation.text = "Date Of Creation: ${currentPageEntry.dateofCreation}"
                Status.text = "Status: ${currentPageEntry.status}"
                PaymentCycle.text = "Payment Cycle: ${currentPageEntry.paymentCycle}"
                WageRateSSPTotal.text = "Wage Rate SSP Total: ${currentPageEntry.wageRateSSPTotal}"
            }


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

}