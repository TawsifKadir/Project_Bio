package com.xplo.code.ui.dashboard

import android.content.res.Resources
import com.xplo.code.R
import com.xplo.code.core.Contextor

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object UiData {

    private const val TAG = "UiData"

    private var resources: Resources = Contextor.getInstance().context.resources

    val ER_ET_DF = "Please enter valid data"
    val ER_SP_DF = "Please select an item"

    val relationshipOptions = getStringArray(R.array.respondent_relationship_to_the_household_head_array)
    val genderOptions = getStringArray(R.array.respondent_gender_array)
    val maritalStatusOptions = getStringArray(R.array.respondent_marital_status_array)
    val legalStatusOptions = getStringArray(R.array.respondent_legal_status_array)
    val mainIncomeOptions = getStringArray(R.array.household_main_source_of_income_array)
    val countryNameOptions = getStringArray(R.array.country_nam_array)
    val stateNameOptions = getStringArray(R.array.state_name_array)


    private fun getString(resId: Int): String {
        return Contextor.getInstance().context.getString(resId)
    }

    private fun getStringArray(resId: Int): Array<String> {
        return resources.getStringArray(resId)

    }

}
