package com.xplo.code.ui.dashboard.household.list

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
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
class HouseholdListAdapterNew : RecyclerView.Adapter<HouseholdListAdapterNew.ViewHolder>(),
    Filterable {

    companion object {
        private const val TAG = "HouseholdListAdapterNew"
    }

    private var dataset = ArrayList<Beneficiary>()
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

            binding.btDelete.setOnClickListener {
                listener?.onClickHouseholdItemDelete(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

            binding.btSend.setOnClickListener {
                listener?.onClickHouseholdItemSend(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

            binding.btAddAlternate.setOnClickListener {
                listener?.onClickHouseholdItemAddAlternate(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

            binding.btSyncStatus.setOnClickListener {
                listener?.onClickHouseholdItemSave(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Beneficiary) {
            binding.tvId.text = "id: " + item.applicationId
            binding.tvName.text =
                item.respondentFirstName + " " + item.respondentMiddleName + " " + item.respondentLastName
            GenderEnum.getGenderById(item.respondentGender.toInt())
            binding.tvGender.text =
                "Gender: " + GenderEnum.getGenderById(item.respondentGender.toInt() + 1)
            binding.tvAge.text = "age: " + item.respondentAge
            binding.tvNominee.text = "Nominee: " + item.nomineeSize
            binding.tvAlternate.text = "Alternate: " + item.alternateSize
            if (item.applicationStatus == 1) {
                binding.tvStatus.text = "Status: Completed"
            } else if (item.applicationStatus == 0) {
                binding.tvStatus.text = "Status: Draft"
            }
            //binding.btSyncStatus.setImageResource(R.drawable.baseline_cloud_done_24)
            Log.d(TAG, "bind: isSynced ${item.isSynced}")
            if (item.isSynced) {
                // binding.btSend.visibility=View.VISIBLE
                binding.btSyncStatus.setImageResource(R.drawable.baseline_cloud_done_24)
                binding.btAddAlternate.gone()
            } else {
                //  binding.btSend.visibility=View.VISIBLE
                binding.btSyncStatus.setImageResource(R.drawable.sync_saved_locally_24px)
                binding.btAddAlternate.visible()
            }
            loadImage(item.photoPath)
        }

        private fun loadImage(byteArray: ByteArray?) {
            if (byteArray?.isEmpty() == true) return

            val bitmap = byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }


// Use Glide to load the Bitmap into an ImageView
            Glide.with(binding.ivAvatar.context)
                .load(bitmap)
                .placeholder(R.drawable.ic_avatar_3)
                .into(binding.ivAvatar)

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

    fun getItem(position: Int): Beneficiary {
        return dataset[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: Beneficiary) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(position: Int, item: Beneficiary) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(items: List<Beneficiary>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(item: Beneficiary) {
        dataset.remove(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(listener: HouseholdHomeFragment) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClickHouseholdItem(item: Beneficiary, pos: Int)
        fun onClickHouseholdItemDelete(item: Beneficiary, pos: Int)
        fun onClickHouseholdItemSend(item: Beneficiary, pos: Int)
        fun onClickHouseholdItemAddAlternate(item: Beneficiary, pos: Int)
        fun onClickHouseholdItemSave(item: Beneficiary, pos: Int)
    }

    override fun getFilter(): Filter {
        return HouseholdFilter()
    }

    inner class HouseholdFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Beneficiary>()
            if (constraint.isNullOrBlank()) {
                filteredList.addAll(dataset)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in dataset) {
                    if (item.respondentFirstName.lowercase().contains(filterPattern)
                        || item.respondentLastName.lowercase().contains(filterPattern)
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataset.clear()
            if (results?.values != null) {
                @Suppress("UNCHECKED_CAST")
                dataset.addAll(results.values as ArrayList<Beneficiary>)
            }
            notifyDataSetChanged()
        }
    }


}