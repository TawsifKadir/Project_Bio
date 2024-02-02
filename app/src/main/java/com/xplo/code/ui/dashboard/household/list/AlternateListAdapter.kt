package com.xplo.code.ui.dashboard.household.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toSummary
import com.xplo.code.databinding.RowAlternateItemBinding

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class AlternateListAdapter : RecyclerView.Adapter<AlternateListAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "AlternateListAdapter"
    }

    private var dataset = ArrayList<HouseholdItem>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowAlternateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickHouseholdItem(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

//            binding.btDelete.setOnClickListener {
//                listener?.onClickHouseholdItemDelete(
//                    getItem(absoluteAdapterPosition),
//                    absoluteAdapterPosition
//                )
//            }
//
//            binding.btAddAlternate.setOnClickListener {
//                listener?.onClickHouseholdItemAddAlternate(
//                    getItem(absoluteAdapterPosition),
//                    absoluteAdapterPosition
//                )
//            }

        }

        fun bind(item: HouseholdItem) {
            //Log.d(TAG, "bind() called with: item = $item")
            //val form = item.toHouseholdForm()
            binding.tvId.text = item.id.toString()
            binding.tvData.text = item.toSummary()
            //binding.tvStatus.text = item.isSynced.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowAlternateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun getItem(position: Int): HouseholdItem {
        return dataset[position]
    }

    fun addItem(item: HouseholdItem) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: HouseholdItem) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<HouseholdItem>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: HouseholdItem) {
        dataset.remove(item)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClickHouseholdItem(item: HouseholdItem, pos: Int)
        fun onClickHouseholdItemDelete(item: HouseholdItem, pos: Int)
        fun onClickHouseholdItemSend(item: HouseholdItem, pos: Int)
        fun onClickHouseholdItemAddAlternate(item: HouseholdItem, pos: Int)
    }

}