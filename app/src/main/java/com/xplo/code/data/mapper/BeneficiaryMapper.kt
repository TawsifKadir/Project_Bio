package com.xplo.code.data.mapper

import android.util.Log
import com.google.gson.Gson
import com.kit.integrationmanager.model.AlternatePayee
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.data.model.content.Address
import com.xplo.data.model.content.Alternate
import com.xplo.data.model.content.Biometric
import com.xplo.data.model.content.HouseholdMember
import com.xplo.data.model.content.Location
import com.xplo.data.model.content.Nominee

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

object BeneficiaryMapper {
    private const val TAG = "BeneficiaryMapper"

    fun toBeneficiary(item: BeneficiaryEntity?): Beneficiary? {
        Log.d(TAG, "toBeneficiary() called with: item = $item")
        if (item == null) return null

        val beneficiary = Beneficiary()

        beneficiary.applicationId = item.applicationId
        beneficiary.address = toAddress(item.address)
        beneficiary.location = toLocation(item.location)

        beneficiary.respondentFirstName = item.respondentFirstName
        beneficiary.respondentMiddleName = item.respondentMiddleName
        beneficiary.respondentLastName = item.respondentLastName
        //beneficiary.respondentNickName = item.respondentNickName


        beneficiary.respondentAge = item.respondentAge
        beneficiary.respondentGender = item.respondentGender

        beneficiary.respondentLegalStatus = item.respondentLegalStatus
        beneficiary.respondentMaritalStatus = item.respondentMaritalStatus

        beneficiary.documentType = DocumentTypeEnum.NATIONAL_ID
        //beneficiary.respondentId = item.respondentId
        beneficiary.respondentId = FakeMapperValue.respondentId
        beneficiary.respondentPhoneNo = item.respondentPhoneNo


        beneficiary.relationshipWithHouseholdHead = item.relationshipWithHouseholdHead

        //beneficiary.currency = item.currency
        beneficiary.currency = FakeMapperValue.currency
        beneficiary.householdIncomeSource = item.householdIncomeSource
        beneficiary.householdMonthlyAvgIncome = item.householdMonthlyAvgIncome

        //beneficiary.selectionCriteria = item.selectionCriteria
        beneficiary.selectionCriteria = FakeMapperValue.selectionCriteria
        //beneficiary.selectionReason = item.selectionReason
        beneficiary.selectionReason = FakeMapperValue.selectionReasons

        beneficiary.spouseFirstName = item.spouseFirstName
        beneficiary.spouseLastName = item.spouseLastName
        beneficiary.spouseMiddleName = item.spouseMiddleName


        beneficiary.householdSize = FakeMapperValue.houseHoldSize

        beneficiary.householdMember2 = toHouseholdMember(item.householdMember2, item.id)
        beneficiary.householdMember5 = toHouseholdMember(item.householdMember5, item.id)
        beneficiary.householdMember17 = toHouseholdMember(item.householdMember17, item.id)
        beneficiary.householdMember35 = toHouseholdMember(item.householdMember35, item.id)
        beneficiary.householdMember64 = toHouseholdMember(item.householdMember64, item.id)
        beneficiary.householdMember65 = toHouseholdMember(item.householdMember65, item.id)
        beneficiary.isReadWrite = item.isReadWrite
        beneficiary.memberReadWrite = item.memberReadWrite

        beneficiary.alternatePayee1 = getFirstAlternate(item.alternates)
        //beneficiary.alternatePayee2 = getSecondAlternate(item.alternates)
        beneficiary.alternatePayee2 = getFirstAlternate(item.alternates)
        beneficiary.biometrics = toBiometricEntities(item.biometrics)

        //beneficiary.isOtherMemberPerticipating = item.isOtherMemberPerticipating
        beneficiary.isOtherMemberPerticipating = FakeMapperValue.isOtherMemberPerticipating
        beneficiary.notPerticipationReason = item.notPerticipationReason
        beneficiary.notPerticipationOtherReason = item.notPerticipationOtherReason
        beneficiary.nominees = toNomineeItems(item.nominees)

        val json = Gson().toJson(beneficiary)
        Log.d(TAG, "toBeneficiary: return: ${json}")
        return beneficiary
    }

    fun toBeneficiaryItems(items: List<BeneficiaryEntity>?): List<Beneficiary>? {
        Log.d(TAG, "toBeneficiaryItems() called with: items = ${items?.size}")
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<Beneficiary>()
        for (item in items) {
            val element = toBeneficiary(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    private fun toAddress(item: Address?): com.kit.integrationmanager.model.Address? {
        if (item == null) return null
        val address = com.kit.integrationmanager.model.Address()
        address.stateId = item.stateId
        address.countyId = item.countyId
        address.payam = item.payamId
        address.boma = item.bomaId
        return address
    }

    private fun toLocation(item: Location?): com.kit.integrationmanager.model.Location? {
        if (item == null) return null
        val location = com.kit.integrationmanager.model.Location()
        location.lat = item.lat
        location.lon = item.lon
        return location
    }

    private fun toHouseholdMember(item: HouseholdMember?, id: String?): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val member = com.kit.integrationmanager.model.HouseholdMember()
        member.applicationId = id

        member.maleNormal = item.maleNormal
        member.maleDisable = item.maleDisable
        member.maleChronicalIll = item.maleChronicalIll
        member.totalMale = item.totalMale

        member.femaleNormal = item.femaleNormal
        member.femaleDisable = item.femaleDisable
        member.femaleChronicalIll = item.femaleChronicalIll
        member.totalFemale = item.totalFemale

        return member

    }

    private fun getFirstAlternate(items: List<Alternate>?): AlternatePayee? {
        if (items.isNullOrEmpty()) return null
        return toAlternate(items[0])
    }

    private fun getSecondAlternate(items: List<Alternate>?): AlternatePayee? {
        if (items.isNullOrEmpty()) return null
        if (items.size < 2) return null
        return toAlternate(items[1])
    }

    private fun toAlternate(item: Alternate?): AlternatePayee? {
        if (item == null) return null
        val alternate = AlternatePayee()
        alternate.documentType = FakeMapperValue.documentType
        alternate.nationalId = FakeMapperValue.nationalId
        //alternate.documentType = item.documentType
        //alternate.nationalId = item.nationalId
        alternate.payeeFirstName = item.payeeName
        alternate.payeeMiddleName = item.payeeName
        alternate.payeeLastName = item.payeeName
        alternate.payeeNickName = FakeMapperValue.name
        alternate.payeeAge = item.payeeAge
        alternate.payeeGender = item.payeeGender
        alternate.payeePhoneNo = item.payeePhoneNo
        alternate.biometrics = toBiometricEntities(item.biometrics)

        return alternate
    }

    private fun toNominee(item: Nominee?): com.kit.integrationmanager.model.Nominee? {
        if (item == null) return null
        val nominee = com.kit.integrationmanager.model.Nominee()

        nominee.applicationId = item.applicationId

        nominee.nomineeFirstName = item.nomineeFirstName
        nominee.nomineeLastName = item.nomineeLastName
        //nominee.nomineeMiddleName = item.nomineeMiddleName
        nominee.nomineeMiddleName = FakeMapperValue.name

        nominee.nomineeAge = item.nomineeAge
        nominee.nomineeGender = item.nomineeGender

        nominee.nomineeOccupation = item.nomineeOccupation
        nominee.otherOccupation = item.otherOccupation
        nominee.relationshipWithHouseholdHead = item.relationshipWithHouseholdHead

        nominee.isReadWrite = item.isReadWrite

        return nominee
    }

    fun toNomineeItems(items: List<Nominee>?): List<com.kit.integrationmanager.model.Nominee>? {
        Log.d(TAG, "toNomineeItems() called with: items = ${items?.size}")
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Nominee>()
        for (item in items) {
            val element = toNominee(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    private fun toBiometricEntity(item: Biometric?): com.kit.integrationmanager.model.Biometric? {
        if (item == null) return null
        val biometric = com.kit.integrationmanager.model.Biometric()

        biometric.applicationId = item.applicationId
        biometric.biometricType = item.biometricType
        biometric.biometricUserType = item.biometricUserType


        if(item.biometricData?.equals("") == true) {
            biometric.biometricData = null
        } else {
            biometric.biometricData = item.biometricData
        }

        biometric.noFingerPrint = item.noFingerPrint
        biometric.noFingerprintReason = item.noFingerprintReason
        biometric.noFingerprintReasonText = item.noFingerprintReasonText

        biometric.biometricUrl = item.biometricUrl

        return biometric
    }

    private fun toBiometricEntities(items: List<Biometric>?): List<com.kit.integrationmanager.model.Biometric>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Biometric>()
        for (item in items) {
            val element = toBiometricEntity(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

}