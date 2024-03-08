package com.xplo.code.ui.dashboard.household.list

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xplo.code.R
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.databinding.RowAlternateItemBinding
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.code.utils.DialogHandler

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

            val form = item.toHouseholdForm()
            if (form == null) return

            binding.tvId.text = "id: " + item.hid.toString()
            binding.tvName.text = form.form2.getFullName()
            binding.tvGender.text = form.form2?.gender
            binding.tvAge.text = "age: " + form.form2?.age
            binding.tvAlternate.text = "Alternate added: " + form.alternates.size

            if(form.alternates.size >= 2){
                binding.root.isClickable = false
                binding.tvAlternate.setTypeface(null, Typeface.BOLD)
            }
            loadImage(form.form4?.photoData?.imgPath)

        }

        private fun loadImage(url: String?) {
            if (url.isNullOrEmpty()) return

            Glide.with(binding.ivAvatar.context)
                .load(url)
                .placeholder(R.drawable.ic_avatar_3)
                .into(binding.ivAvatar)

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