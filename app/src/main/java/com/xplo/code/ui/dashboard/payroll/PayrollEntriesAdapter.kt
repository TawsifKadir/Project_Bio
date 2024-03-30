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
    private var dataset: List<PayrollEntry.Result>,
    private val onRootClick: (position: Int) -> Unit,
    private val viewModel: PayrollViewModel
) : RecyclerView.Adapter<PayrollEntriesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PayrollEntryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: PayrollEntry.Result, position: Int) {
            binding.apply {
                tvHouseHold.text = "Household Number:\n${item.householdNumber}"
                tvPaymentCycle.text = item.paymentCycle
                tvWageRate.text = "Wage Rate: $${item.wageRateUSD}"
                tvWorkCode.text = "${position+1+(viewModel.currentPageNo-1)*10}. "+item.workCode
                root.setOnClickListener {
                    onRootClick.invoke(position)
                }
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
        holder.bind(item, position)


    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}