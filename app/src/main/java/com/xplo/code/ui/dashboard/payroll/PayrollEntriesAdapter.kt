package com.xplo.code.ui.dashboard.payroll

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.databinding.PayrollEntryItemBinding
import com.xplo.code.ui.dashboard.model.PayrollEntry

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class PayrollEntriesAdapter(
    private var dataset: List<PayrollEntry.Result>
) : RecyclerView.Adapter<PayrollEntriesAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "AlternateListAdapter"
    }

    inner class ViewHolder(private val binding: PayrollEntryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: PayrollEntry.Result) {
            binding.apply {
                tvAttendance.text = "Total Attendance: ${item.totAttendance}"
                tvCreationDate.text = "Created Date: ${item.dateofCreation}"
                tvDate.text = "Request From: ${item.reqFromDate}- Request To: ${item.reqToDate}"
                tvHouseHoldNo.text = "Household Number: ${item.householdNumber}"
                tvPaymentCycle.text = "Payment Cycle: ${item.paymentCycle}"
                tvReqNo.text = "Request No: ${item.reqNo}"
                tvWageRate.text = "Wage Rate: $${item.wageRateUSD}"
                tvWorkCode.text = "Work Code: ${item.workCode}"
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PayrollEntryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataset[position]
        holder.bind(item)


    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}