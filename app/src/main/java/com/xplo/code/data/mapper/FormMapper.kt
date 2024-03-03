package com.xplo.code.data.mapper

import android.util.Log
import com.xplo.code.data_module.model.content.Address
import com.xplo.code.data_module.model.content.FormRqb
import com.xplo.code.data_module.model.content.Location
import com.xplo.code.data_module.model.content.toJson
import com.xplo.code.ui.dashboard.model.HouseholdForm

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

object FormMapper {
    private const val TAG = "FormMapper"

    fun toFormRqb(item: HouseholdForm?): FormRqb? {
        Log.d(TAG, "toFormRqb() called with: item = $item")
        if (item == null) return null

        val form = FormRqb(
            applicationId = item.id,
            address = Address(
                stateId = item.form1?.state?.id,
                countyId = item.form1?.county?.id,
                payamId = item.form1?.payam?.id,
                bomaId = item.form1?.boma?.id
            ),
            location = Location(
                lat = item.form1?.lat,
                lon = item.form1?.lon
            ),
            respondentFirstName = item.form2?.firstName,
            respondentMiddleName = item.form2?.middleName,
            respondentLastName = item.form2?.lastName
        )
        Log.d(TAG, "toFormRqb: return: ${form.toJson()}")
        return form
    }

    fun toFormsRqb(items: List<HouseholdForm>?): List<FormRqb>? {
        Log.d(TAG, "toFormsRqb() called with: items = ${items?.size}")
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<FormRqb>()
        for (item in items) {
            val element = toFormRqb(item)
            if (element != null){
                list.add(element)
            }
        }
        return list
    }
}