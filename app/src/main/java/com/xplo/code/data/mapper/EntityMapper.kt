package com.xplo.code.data.mapper

import android.util.Log
import com.xplo.code.core.ext.isYes
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.toJson
import com.xplo.code.ui.dashboard.model.HhMember
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.data.model.content.Address
import com.xplo.data.model.content.HouseholdMember
import com.xplo.data.model.content.Location

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

object EntityMapper {
    private const val TAG = "EntityMapper"

    fun toBeneficiaryEntity(item: HouseholdForm?): BeneficiaryEntity? {
        Log.d(TAG, "toBeneficiaryEntity() called with: item = $item")
        if (item == null) return null

        val applicationId = item.uuid

        val form = BeneficiaryEntity(
            id = item.id,
            uuid = item.uuid,
            isSynced = false,

            applicationId = applicationId,
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
            respondentLastName = item.form2?.lastName,

            respondentAge = item.form2?.age ?: 0,
            respondentGender = item.form2?.gender,

            respondentLegalStatus = item.form2?.legalStatus,
            respondentMaritalStatus = item.form2?.maritalStatus,

            respondentId = item.form2?.idNumber,
            respondentPhoneNo = item.form2?.phoneNumber,

            relationshipWithHouseholdHead = item.form2?.respondentRlt,

            currency = item.form2?.currency,
            householdIncomeSource = item.form2?.mainSourceOfIncome,
            householdMonthlyAvgIncome = item.form2?.monthlyAverageIncome ?: 0,


            selectionCriteria = item.form2?.selectionCriteria,
            selectionReason = item.form2?.selectionReason,

            spouseFirstName = item.form2?.spouseFirstName,
            spouseLastName = item.form2?.spouseLastName,
            spouseMiddleName = item.form2?.spouseMiddleName,


            householdSize = item.form3?.householdSize ?: 0,

            householdMember2 = toHouseholdMember(
                item.form3?.male0_2,
                item.form3?.female0_2,
                applicationId
            ),
            householdMember5 = toHouseholdMember(
                item.form3?.male3_5,
                item.form3?.male3_5,
                applicationId
            ),
            householdMember17 = toHouseholdMember(
                item.form3?.male6_17,
                item.form3?.male6_17,
                applicationId
            ),
            householdMember35 = toHouseholdMember(
                item.form3?.male18_35,
                item.form3?.male18_35,
                applicationId
            ),
            householdMember64 = toHouseholdMember(
                item.form3?.male36_64,
                item.form3?.male36_64,
                applicationId
            ),
            householdMember65 = toHouseholdMember(
                item.form3?.male65p,
                item.form3?.male65p,
                applicationId
            ),

            isReadWrite = item.form3?.isReadWrite.toBoolean(),
            memberReadWrite = item.form3?.householdSize ?: 0,


            isOtherMemberPerticipating = item.form6?.isNomineeAdd.isYes(),
            notPerticipationReason = item.form6?.noNomineeReason,
            notPerticipationOtherReason = item.form6?.otherReason,


            )


        Log.d(TAG, "toBeneficiaryEntity: return: ${form.toJson()}")
        return form
    }

    fun toBeneficiaryEntityItems(items: List<HouseholdForm>?): List<BeneficiaryEntity>? {
        Log.d(TAG, "toBeneficiaryEntityItems() called with: items = ${items?.size}")
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<BeneficiaryEntity>()
        for (item in items) {
            val element = toBeneficiaryEntity(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    private fun toHouseholdMember(
        male: HhMember?,
        female: HhMember?,
        id: String?
    ): HouseholdMember {
        if (male == null || female == null) return HouseholdMember()
        return HouseholdMember(
            applicationId = id,
            maleNormal = male.normal,
            maleChronicalIll = male.ill,
            maleDisable = male.disable,

            femaleNormal = female.normal,
            femaleChronicalIll = female.ill,
            femaleDisable = female.disable

        )
    }
}