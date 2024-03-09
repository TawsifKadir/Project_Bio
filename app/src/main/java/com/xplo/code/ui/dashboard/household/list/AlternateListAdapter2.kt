package com.xplo.code.ui.dashboard.household.list

import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kit.integrationmanager.model.GenderEnum
import com.xplo.code.R
import com.xplo.code.data.db.room.model.Beneficiary
import com.xplo.code.databinding.RowAlternateItemBinding


/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
class AlternateListAdapter2 : RecyclerView.Adapter<AlternateListAdapter2.ViewHolder>() {

    companion object {
        private const val TAG = "AlternateListAdapter2"
    }

    private var dataset = ArrayList<Beneficiary>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(private val binding: RowAlternateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener?.onClickBeneficiaryItem(
                    getItem(absoluteAdapterPosition),
                    absoluteAdapterPosition
                )
            }
        }

        fun bind(item: Beneficiary) {
            //Log.d(TAG, "bind() called with: item = $item")
            //   val form = item.toHouseholdForm()
            //   if (form == null) return

            binding.tvId.text = "id: " + item.applicationId
            binding.tvName.text =
                item.respondentFirstName + " " + item.respondentMiddleName + " " + item.respondentLastName

            binding.tvGender.text =
                "Gender: " + GenderEnum.getGenderById(item.respondentGender.toInt() + 1)
            binding.tvAge.text = "age: " + item.respondentAge

            binding.tvAlternate.text = "Alternate: " + item.alternateSize

            if (item.alternateSize >= 2) {
                binding.root.isClickable = false
                binding.tvAlternate.setTypeface(null, Typeface.BOLD)
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
                .into(binding.ivAvatar);

//            Glide.with(binding.ivAvatar.context)
//                .load(url)
//                .placeholder(R.drawable.ic_avatar_3)
//                .into(binding.ivAvatar)

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

    fun getItem(position: Int): Beneficiary {
        return dataset[position]
    }

    fun addItem(item: Beneficiary) {
        dataset.add(item)
        notifyDataSetChanged()
    }

    fun addItem(position: Int, item: Beneficiary) {
        dataset.add(position, item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<Beneficiary>) {
        dataset.clear()
        dataset.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: Beneficiary) {
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
        fun onClickBeneficiaryItem(item: Beneficiary, pos: Int)
    }

}