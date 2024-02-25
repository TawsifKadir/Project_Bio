package com.xplo.code.data.mapper

import android.util.Log
import com.kit.integrationmanager.model.BiometricType
import com.kit.integrationmanager.model.BiometricUserType
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.IncomeSourceEnum
import com.kit.integrationmanager.model.LegalStatusEnum
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum
import com.xplo.code.core.ext.isYes
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.toJson
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.Finger
import com.xplo.code.ui.dashboard.model.HhMember
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.PhotoData
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.code.ui.dashboard.model.getTotal
import com.xplo.data.model.content.Address
import com.xplo.data.model.content.Alternate
import com.xplo.data.model.content.Biometric
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
            hid = item.hid,
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
            respondentGender = GenderEnum.find(item.form2?.gender),

            respondentLegalStatus = LegalStatusEnum.find(item.form2?.legalStatus),
            respondentMaritalStatus = MaritalStatusEnum.find(item.form2?.maritalStatus),

            respondentId = item.form2?.idNumber,
            respondentPhoneNo = item.form2?.phoneNumber,

            relationshipWithHouseholdHead = RelationshipEnum.find(item.form2?.respondentRlt),

            currency = CurrencyEnum.find(item.form2?.currency),
            householdIncomeSource = IncomeSourceEnum.find(item.form2?.mainSourceOfIncome),
            householdMonthlyAvgIncome = item.form2?.monthlyAverageIncome ?: 0,


            selectionCriteria = SelectionCriteriaEnum.find(item.form2?.selectionCriteria),
            selectionReason = SelectionReasonEnum.find(item.form2?.selectionReason),

            spouseFirstName = item.form2?.spouseFirstName,
            spouseLastName = item.form2?.spouseLastName,
            spouseMiddleName = item.form2?.spouseMiddleName,

            householdSize = item.form3?.householdSize ?: 0,

            householdMember2 = toHouseholdMemberEntity(
                item.form3?.male0_2,
                item.form3?.female0_2,
                applicationId
            ),
            householdMember5 = toHouseholdMemberEntity(
                item.form3?.male3_5,
                item.form3?.female3_5,
                applicationId
            ),
            householdMember17 = toHouseholdMemberEntity(
                item.form3?.male6_17,
                item.form3?.female6_17,
                applicationId
            ),
            householdMember35 = toHouseholdMemberEntity(
                item.form3?.male18_35,
                item.form3?.female18_35,
                applicationId
            ),
            householdMember64 = toHouseholdMemberEntity(
                item.form3?.male36_64,
                item.form3?.female36_64,
                applicationId
            ),
            householdMember65 = toHouseholdMemberEntity(
                item.form3?.male65p,
                item.form3?.female65p,
                applicationId
            ),

            alternates = toAlternateEntityItems(item.alternates, applicationId),
            biometrics = toBiometricEntityItemsFromHouseholdForm(item, applicationId),

            isReadWrite = item.form3?.isReadWrite.toBoolean(),
            memberReadWrite = item.form3?.householdSize ?: 0,

            isOtherMemberPerticipating = item.form6?.isNomineeAdd.isYes(),
            notPerticipationReason = NonPerticipationReasonEnum.find(item.form6?.noNomineeReason),
            notPerticipationOtherReason = item.form6?.otherReason,
            nominees = toNomineeEntityItems(item.form6?.nominees, applicationId)

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

    fun toHouseholdMemberEntity(
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
            totalMale = male.getTotal(),

            femaleNormal = female.normal,
            femaleChronicalIll = female.ill,
            femaleDisable = female.disable,
            totalFemale = female.getTotal()

        )
    }

    fun toNomineeEntity(
        item: Nominee?,
        id: String?,
    ): com.xplo.data.model.content.Nominee? {
        if (item == null) return null
        return com.xplo.data.model.content.Nominee(
            applicationId = id,

            nomineeFirstName = item.firstName,
            nomineeLastName = item.lastName,
            nomineeMiddleName = item.middleName,

            nomineeAge = item.age ?: 0,
            nomineeGender = GenderEnum.find(item.gender),

            //nomineeOccupation = item.occupation,
            otherOccupation = item.occupation,
            relationshipWithHouseholdHead = RelationshipEnum.find(item.relation),

            isReadWrite = item.isReadWrite.isYes()
        )
    }

    fun toNomineeEntityItems(
        items: List<Nominee>?,
        id: String?,
    ): ArrayList<com.xplo.data.model.content.Nominee>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<com.xplo.data.model.content.Nominee>()
        for (item in items) {
            val element = toNomineeEntity(item, id)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    fun toAlternateEntity(
        item: AlternateForm?,
        id: String?,
    ): Alternate? {
        if (item == null) return null
        return Alternate(
            //applicationId = id,
            nationalId = item.form1?.idNumber,

            payeeName = item.form1?.getFullName(),
            payeeAge = item.form1?.age ?: 0,
            payeeGender = GenderEnum.find(item.form1?.gender),
            payeePhoneNo = item.form1?.phoneNumber,
            biometrics = toBiometricEntityItemsFromAlternateForm(item, id)
        )
    }

    fun toAlternateEntityItems(
        items: List<AlternateForm>?,
        id: String?,
    ): ArrayList<Alternate>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<Alternate>()
        for (item in items) {
            val element = toAlternateEntity(item, id)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    fun toBiometricEntityFromFinger(
        item: Finger?,
        id: String?
    ): Biometric? {
        if (item == null) return null
        if (id == null) return null
        if (item.fingerPrint.isNullOrEmpty()) return null
        if (item.fingerType.isNullOrEmpty()) return null
        if (item.userType.isNullOrEmpty()) return null

        return Biometric(
            applicationId = id,
            biometricType = BiometricType.find(item.fingerType),
            biometricUserType = BiometricUserType.valueOf(item.userType!!),

            noFingerPrint = null,
            noFingerprintReason = null,
            noFingerprintReasonText = null,

            biometricUrl = null
        )
    }


    fun toBiometricEntityFromPhoto(
        item: PhotoData?,
        id: String?
    ): Biometric? {
        if (item == null) return null
        if (id == null) return null
        if (item.img.isNullOrEmpty()) return null
        if (item.userType.isNullOrEmpty()) return null

        return Biometric(
            applicationId = id,
            biometricType = BiometricType.PHOTO,
            biometricUserType = BiometricUserType.valueOf(item.userType!!), //"ALTERNATE",

            noFingerPrint = null,
            noFingerprintReason = null,
            noFingerprintReasonText = null,

            biometricUrl = null
        )
    }

    fun toBiometricEntityItemsFromAlternateForm(
        item: AlternateForm?,
        id: String?,
    ): ArrayList<Biometric>? {
        if (item == null) return null
        val items = arrayListOf<Biometric>()

        val fingers = item.form3?.fingers

        fingers?.let {
            for (finger in fingers) {
                val bitem = toBiometricEntityFromFinger(finger, id)
                if (bitem != null) {
                    items.add(bitem)
                }
            }
        }

        val photoBiometric = toBiometricEntityFromPhoto(item.form2?.photoData, id)
        if (photoBiometric != null) items.add(photoBiometric)

        return items
    }


    fun toBiometricEntityItemsFromHouseholdForm(
        item: HouseholdForm?,
        id: String?,
    ): ArrayList<Biometric>? {
        if (item == null) return null
        val items = arrayListOf<Biometric>()

        val fingers = item.form5?.fingers

        fingers?.let {
            for (finger in fingers) {
                val bitem = toBiometricEntityFromFinger(finger, id)
                if (bitem != null) {
                    items.add(bitem)
                }
            }
        }

        val photoBiometric = toBiometricEntityFromPhoto(item.form4?.photoData, id)
        if (photoBiometric != null) items.add(photoBiometric)

        return items
    }

}