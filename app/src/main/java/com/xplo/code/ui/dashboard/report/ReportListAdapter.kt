package com.xplo.code.ui.dashboard.report

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kit.integrationmanager.model.GenderEnum
import com.xplo.code.R
import com.xplo.code.core.ext.gone
import com.xplo.code.core.ext.visible
import com.xplo.code.data.db.room.model.Beneficiary
import com.xplo.code.data.db.room.model.SyncBeneficiary
import com.xplo.code.databinding.RowHouseholdItemBinding
import com.xplo.code.ui.dashboard.household.forms.HouseholdHomeFragment


/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class ReportListAdapter : RecyclerView.Adapter<ReportListAdapter.ViewHolder>(),
    Filterable {

    companion object {
        private const val TAG = "HouseholdListAdapterNew"
    }

    private var dataset = ArrayList<SyncBeneficiary>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowHouseholdItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickHouseholdItem(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }


        }

        fun bind(item: SyncBeneficiary) {
            binding.tvId.text = "id: " + item.applicationId
            binding.tvName.text = item.beneficiaryName

        }

        private fun loadImage(byteArray: ByteArray?) {
            if (byteArray?.isEmpty() == true) return

            val bitmap = byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }


// Use Glide to load the Bitmap into an ImageView
            Glide.with(binding.ivAvatar.getContext())
                .load(bitmap)
                .placeholder(R.drawable.ic_avatar_3)
                .into(binding.ivAvatar);

//            Glide.with(binding.ivAvatar.context)
//                .load(url)
//                .placeholder(R.drawable.ic_avatar_3)
//                .into(binding.ivAvatar)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowHouseholdItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get element from your dataset at this position
        //holder.setIsRecyclable(false)
        val item = dataset[position]
        holder.bind(item)


    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getItem(position: Int): SyncBeneficiary {
        return dataset[position]
    }

    fun addItem(item: SyncBeneficiary) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: SyncBeneficiary) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<SyncBeneficiary>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: SyncBeneficiary) {
        dataset.remove(item)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(listener: ReportActivity) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClickHouseholdItem(item: SyncBeneficiary, pos: Int)
    }

    override fun getFilter(): Filter {
        return HouseholdFilter()
    }

    inner class HouseholdFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<SyncBeneficiary>()
            if (constraint.isNullOrBlank()) {
                filteredList.addAll(dataset)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in dataset) {
                    if (item.beneficiaryName.lowercase().contains(filterPattern)
                        || item.applicationId.lowercase().contains(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataset.clear()
            if (results != null && results.values != null) {
                @Suppress("UNCHECKED_CAST")
                dataset.addAll(results.values as ArrayList<SyncBeneficiary>)
            }
            notifyDataSetChanged()
        }
    }


}