package com.xplo.code.ui.components

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xplo.code.R
import com.xplo.code.core.ext.loadImage
import com.xplo.code.ui.dashboard.household.forms.FormDetailsFragment
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.ReportRow
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
object ReportViewUtils {

    fun getRowView(context: Context, layoutInflater: LayoutInflater, item: ReportRow?): View {
        Log.d(FormDetailsFragment.TAG, "getRowView() called with: item = $item")


        val rowView: View = layoutInflater.inflate(R.layout.row_report_item, null, false)

        if (item == null) return rowView

        val tvTitle: TextView = rowView.findViewById(R.id.tvTitle)
        val tvValue: TextView = rowView.findViewById(R.id.tvValue)
        val tvTitle2: TextView = rowView.findViewById(R.id.tvTitle2)
        val tvValue2: TextView = rowView.findViewById(R.id.tvValue2)
        val llCol2: View = rowView.findViewById(R.id.llCol2)

        tvTitle.text = item.title
        tvValue.text = item.value

        if (item.title2 == null) {
            //llCol2.gone()
            //tvValue2.gone()
            return rowView
        }

        tvTitle2.text = item.title2
        tvValue2.text = item.value2

        return rowView

    }

    fun getAltFormView(context: Context, layoutInflater: LayoutInflater, item: AlternateForm?): View {
        Log.d(FormDetailsFragment.TAG, "getRowView() called with: item = $item")


        val rowView: View = layoutInflater.inflate(R.layout.row_alternate_report_item, null, false)

        if (item == null) return rowView

        val tvTitle: TextView = rowView.findViewById(R.id.tvTitle)
        val tvData: TextView = rowView.findViewById(R.id.tvData)
        val img: ImageView = rowView.findViewById(R.id.ivAvatar)


        img.loadImage(item.form2?.photoData?.imgPath)
        tvTitle.text = item.form1.getFullName()
        tvData.text = item.toSummary()

        return rowView

    }


}