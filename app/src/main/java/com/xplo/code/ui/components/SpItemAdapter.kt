package com.xplo.code.ui.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.xplo.code.R


class SpItemAdapter(
    context: Context,
    private val items: List<String>,
    private val layoutId: Int = R.layout.row_spinner_item
) : ArrayAdapter<String>(context, layoutId, items) {
    private val context: Context

    init {
        this.context = context
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = inflater.inflate(layoutId, parent, false)
        val label = row.findViewById(R.id.tvTitle) as TextView
        label.text = items[position]
//        if (position == 0) { //Special style for dropdown header
//            label.setTextColor(context.getResources().getColor(R.color.text_hint_color))
//        }
        return row
    }
}