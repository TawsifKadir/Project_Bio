package com.xplo.code.ui.dashboard.household.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.databinding.RowCheckboxItemBinding
import com.xplo.code.ui.dashboard.model.CheckboxItem

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class CheckboxListAdapter : RecyclerView.Adapter<CheckboxListAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "CheckboxListAdapter"
    }

    private var dataset = ArrayList<CheckboxItem>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowCheckboxItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

//            binding.root.setOnClickListener {
//                listener?.onClickCheckboxItem(
//                    getItem(absoluteAdapterPosition),
//                    absoluteAdapterPosition
//                )
//            }

            binding.checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->

                setItemChecked(absoluteAdapterPosition, isChecked)
                listener?.onStatusChangeCheckboxItem(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition,
                    isChecked
                )
            }

        }

        fun bind(item: CheckboxItem) {
            //Log.d(TAG, "bind() called with: item = $item")
            binding.checkbox.text = item.title
            binding.checkbox.isChecked = item.isChecked
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowCheckboxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun getItem(position: Int): CheckboxItem {
        return dataset[position]
    }

    fun addItem(item: CheckboxItem) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: CheckboxItem) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<CheckboxItem>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: CheckboxItem) {
        dataset.remove(item)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyDataSetChanged()
    }

    fun getCheckedPositions(): List<Int> {
        val items = arrayListOf<Int>()
        for (i in dataset.indices) {
            if (dataset[i].isChecked) {
                items.add(i)
            }
        }
        return items
    }

    fun getCheckedIds(): List<Int> {
        val items = arrayListOf<Int>()
        for (i in dataset.indices) {
            if (dataset[i].isChecked) {
                items.add(dataset[i].id)
            }
        }
        return items
    }

    fun getCheckedItems(): List<CheckboxItem> {
        val items = arrayListOf<CheckboxItem>()
        for (item in dataset) {
            if (item.isChecked) {
                items.add(item)
            }
        }
        return items
    }

    fun setItemChecked(position: Int, isChecked: Boolean) {
        dataset[position].isChecked = isChecked
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        //        fun onClickCheckboxItem(item: CheckboxItem, pos: Int)
        fun onStatusChangeCheckboxItem(item: CheckboxItem, pos: Int, isChecked: Boolean)
    }

}