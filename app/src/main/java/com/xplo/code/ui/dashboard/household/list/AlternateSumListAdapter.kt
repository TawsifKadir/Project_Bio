package com.xplo.code.ui.dashboard.household.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.core.ext.loadAvatar
import com.xplo.code.databinding.RowAlternateSumItemBinding
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.code.ui.dashboard.model.toSummary

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class AlternateSumListAdapter : RecyclerView.Adapter<AlternateSumListAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "AlternateSumListAdapter"
    }

    private var dataset = ArrayList<AlternateForm>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowAlternateSumItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickAlternateForm(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }

            binding.btDelete.setOnClickListener {
                listener?.onClickAlternateFormDelete(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }
//
//            binding.btAddAlternate.setOnClickListener {
//                listener?.onClickAlternateFormAddAlternate(
//                    getItem(absoluteAdapterPosition),
//                    absoluteAdapterPosition
//                )
//            }

        }

        fun bind(item: AlternateForm) {
            Log.d(TAG, "bind() called with: item = $item")

            //binding.tvId.text = "id: " + item.id.toString()
            binding.tvTitle.text = item.form1.getFullName()
            binding.tvData.text = item.toSummary()

            loadImage(item.form2?.img)

        }

        private fun loadImage(url: String?) {
            if (url.isNullOrEmpty()) return

            binding.ivAvatar.loadAvatar(url)
//            Glide.with(binding.ivAvatar.context)
//                .load(url)
//                .placeholder(R.drawable.ic_avatar_3)
//                .into(binding.ivAvatar)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowAlternateSumItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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

    fun getDataset(): ArrayList<AlternateForm> {
        return dataset
    }

    fun getItem(position: Int): AlternateForm {
        return dataset[position]
    }

    fun addItem(item: AlternateForm) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: AlternateForm) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<AlternateForm>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: AlternateForm) {
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
        fun onClickAlternateForm(item: AlternateForm, pos: Int)
        fun onClickAlternateFormDelete(item: AlternateForm, pos: Int)
    }

}