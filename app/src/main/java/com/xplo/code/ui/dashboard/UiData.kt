package com.xplo.code.ui.dashboard

import android.content.res.Resources
import com.xplo.code.R
import com.xplo.code.core.Contextor
import com.xplo.code.ui.dashboard.model.CheckboxItem

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
    val ER_RB_DF = "Please select an item"

    val otherSpecify = "other (specify)"

    val relationshipOptions = getStringArray(R.array.respondent_relationship_to_the_household_head_array)
    val genderOptions = getStringArray(R.array.respondent_gender_array)
    val maritalStatusOptions = getStringArray(R.array.respondent_marital_status_array)
    val legalStatusOptions = getStringArray(R.array.respondent_legal_status_array)
    val mainIncomeOptions = getStringArray(R.array.household_main_source_of_income_array)
    val countryNameOptions = getStringArray(R.array.country_nam_array)
    val stateNameOptions = getStringArray(R.array.state_name_array)
    val bomaNameOptions = getStringArray(R.array.boma_name_array)
    val payaamNameOptions = getStringArray(R.array.payam_name_array)
    val currency = getStringArray(R.array.currency_array)
    val selectionReason = getStringArray(R.array.selection_reason_array)
    val whyNot = getStringArray(R.array.why_not)
    val whyNotShortened = getStringArray(R.array.why_not_shortened)
    val idType = getStringArray(R.array.id_type)
    val nomineeOccupation = getStringArray(R.array.what_does_currently_do_for_a_living_array)
    val publicWorks = getStringArray(R.array.support_type_public_work_array)
    val directIncomeSupport = getStringArray(R.array.support_type_direct_income_array)

    fun getReason(): List<CheckboxItem> {
        var array = whyNot
        var items = arrayListOf<CheckboxItem>()
        for (i in array.indices) {
            items.add(CheckboxItem(i, array[i], false))
        }
        return items
    }

    fun getPublicWorks(): List<CheckboxItem> {
        var array = publicWorks
        var items = arrayListOf<CheckboxItem>()
        for (i in array.indices) {
            items.add(CheckboxItem(i, array[i], false))
        }
        return items
    }

    fun getPublicWorksDummy(): List<CheckboxItem> {
        var array = publicWorks
        var items = arrayListOf<CheckboxItem>()
        for (i in array.indices) {
            if (i ==1){
                items.add(CheckboxItem(i, array[i], true))
                continue
            }
            items.add(CheckboxItem(i, array[i], false))
        }
        return items
    }

    fun getDirectIncomeSupport(): List<CheckboxItem> {
        var array = directIncomeSupport
        var items = arrayListOf<CheckboxItem>()
        for (i in array.indices) {
            items.add(CheckboxItem(i, array[i], false))
        }
        return items
    }

    private fun getString(resId: Int): String {
        return Contextor.getInstance().context.getString(resId)
    }

    private fun getStringArray(resId: Int): Array<String> {
        return resources.getStringArray(resId)

    }

}
