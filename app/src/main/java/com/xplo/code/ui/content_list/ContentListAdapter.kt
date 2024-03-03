package com.xplo.code.ui.content_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xplo.code.R
import com.xplo.code.data_module.model.content.ContentItem
import com.xplo.code.databinding.RowContentListBinding

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class ContentListAdapter : RecyclerView.Adapter<ContentListAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "ContentListAdapter"
    }

    private var dataset = ArrayList<ContentItem>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickContentItem(getItem(adapterPosition))
            }

        }

        fun bind(item: ContentItem) {
            //binding.tvTitle.text = item.title

            Glide.with(binding.ivThumb.context)
                .load(getImageUrl(item))
                .placeholder(R.drawable.ph_content_landscape)
                .into(binding.ivThumb)

        }

        private fun getImageUrl(item: ContentItem): String? {
            return null
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowContentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun getItem(position: Int): ContentItem {
        return dataset[position]
    }

    fun addItem(item: ContentItem) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: ContentItem) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<ContentItem>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: ContentItem) {
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
        fun onClickContentItem(item: ContentItem)
    }

}