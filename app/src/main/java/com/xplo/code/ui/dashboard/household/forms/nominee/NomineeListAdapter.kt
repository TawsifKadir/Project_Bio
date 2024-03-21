package com.xplo.code.ui.dashboard.household.forms.nominee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.databinding.RowNomineeItemBinding
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.toSummary

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class NomineeListAdapter : RecyclerView.Adapter<NomineeListAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "NomineeListAdapter"
    }

    private var dataset = ArrayList<Nominee>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowNomineeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickNomineeItem(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

            binding.btDelete.setOnClickListener {
                listener?.onClickNomineeDelete(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

        }

        fun bind(item: Nominee) {
            binding.tvData.text = item.toSummary()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowNomineeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun getItem(position: Int): Nominee {
        return dataset[position]
    }

    fun addItem(item: Nominee) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: Nominee) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, item: Nominee) {
        dataset[position] = item
        notifyItemChanged(position)
    }

    fun addAll(items: List<Nominee>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: Nominee) {
        dataset.remove(item)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyDataSetChanged()
    }

    fun getDataset(): ArrayList<Nominee> {
        return dataset
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClickNomineeItem(item: Nominee, pos: Int)
        fun onClickNomineeDelete(item: Nominee, pos: Int)
    }

}