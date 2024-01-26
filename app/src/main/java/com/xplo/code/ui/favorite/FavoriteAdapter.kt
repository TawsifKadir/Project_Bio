package com.xplo.code.ui.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xplo.code.BuildConfig
import com.xplo.code.databinding.RowFavoriteBinding
import com.xplo.data.model.content.ContentItem

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "FavoriteAdapter"
    }

    private var dataset = ArrayList<ContentItem>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickFavoriteItem(getItem(adapterPosition))
            }

            binding.ivFavorite.setOnClickListener {
                listener?.onClickFavoriteIcon(getItem(adapterPosition), adapterPosition)
            }

        }

        fun bind(item: ContentItem) {
            //binding.tvTitle.text = item.title

//            Glide.with(binding.ivThumb.context)
//                .load(getImageUrl(item))
//                .placeholder(R.drawable.ph_content_landscape)
//                .into(binding.ivThumb)


        }

        private fun setPrimeTagVisibility(isPremium: Boolean, isTvod: Boolean) {
            Log.d(
                TAG,
                "setPrimeTagVisibility() called with: isPremium = $isPremium, isTvod = $isTvod"
            )

            viewHide(binding.viewPrimeTag.viewPrimeTag)
            viewHide(binding.viewTvodTag.viewTvodTag)

//            if (isTvod && SubscriptionUtils.isTvodFeatureEnabled()) {
//                viewShow(binding.viewTvodTag.viewTvodTag)
//                return
//            }
            if (isPremium) {
                viewShow(binding.viewPrimeTag.viewPrimeTag)
                return
            }
        }

        private fun viewHide(view: View?) {
            if (view == null) return
            view.visibility = View.GONE
        }

        private fun viewShow(view: View?) {
            if (view == null) return
            view.visibility = View.VISIBLE
        }

        private fun getImageUrl(item: ContentItem): String? {

            return null
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //FavoriteAdapterThemeGenerator(binding).render()
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get element from your dataset at this position
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
        //dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: ContentItem) {
        dataset.remove(item)
        notifyDataSetChanged()
        dbgDataset()
    }

    fun remove(position: Int) {
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun dbgDataset() {
        if (!BuildConfig.DEBUG) return
        for (item in dataset) {
            //Log.d(TAG, "dbgDataset: ${item.title}")
        }
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onClickFavoriteItem(item: ContentItem)
        fun onClickFavoriteIcon(item: ContentItem, position: Int)
    }

}