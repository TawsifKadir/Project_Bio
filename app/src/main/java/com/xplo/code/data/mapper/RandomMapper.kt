package com.xplo.code.data.mapper

import android.util.Log
import com.google.gson.Gson
import com.kit.integrationmanager.model.AlternatePayee
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.model.Biometric
import com.kit.integrationmanager.model.Nominee
import java.util.UUID

object RandomMapper {
    private const val TAG = "RandomMapper"

    fun assignBeneficiaryARandomId(beneficiary: Beneficiary?): Beneficiary? {
        if (beneficiary == null) return null
        val id = UUID.randomUUID().toString()
        Log.d(TAG, "assignBeneficiaryARandomId: $id")
        return assignBeneficiaryAId(beneficiary, id)
    }

    fun assignBeneficiaryAId(beneficiary: Beneficiary?, id: String?): Beneficiary? {
        Log.d(TAG, "assignBeneficiaryAId() called with: beneficiary = $beneficiary, id = $id")
        if (beneficiary == null) return null
        if (id == null) return beneficiary
        beneficiary.applicationId = id


        beneficiary.householdMember2.applicationId = id
        beneficiary.householdMember5.applicationId = id
        beneficiary.householdMember17.applicationId = id
        beneficiary.householdMember64.applicationId = id
        beneficiary.householdMember65.applicationId = id

        if (beneficiary.alternatePayee1 != null) {
            beneficiary.alternatePayee1 = assignAlternateAId(beneficiary.alternatePayee1, id)
        }

        if (beneficiary.alternatePayee2 != null) {
            beneficiary.alternatePayee2 = assignAlternateAId(beneficiary.alternatePayee2, id)
        }

        beneficiary.nominees = assignNomineesAId(beneficiary.nominees, id)
        beneficiary.biometrics = assignBiometricsAId(beneficiary.biometrics, id)
        val json = Gson().toJson(beneficiary)
        Log.d(TAG, "assignBeneficiaryAId: $json")
        return beneficiary

    }

    private fun assignAlternateAId(item: AlternatePayee?, id: String?): AlternatePayee? {
        if (item == null) return null
        if (id == null) return item
        if (item.biometrics == null) return item
        if (item.biometrics.isEmpty()) return item

        val upElements = arrayListOf<Biometric>()
        for (element in item.biometrics) {
            val upElement = assignBiometricAId(element, id)
            if (upElement != null)
                upElements.add(upElement)
        }
        item.biometrics = upElements
        return item
    }


    private fun assignNomineesAId(items: List<Nominee>?, id: String?): List<Nominee>? {
        if (items == null) return null
        if (items.isEmpty()) return items
        if (id == null) return items

        val upElements = arrayListOf<Nominee>()
        for (element in items) {
            val upElement = assignNomineeAId(element, id)
            if (upElement != null)
                upElements.add(upElement)
        }
        return upElements
    }

    private fun assignNomineeAId(item: Nominee?, id: String?): Nominee? {
        if (item == null) return null
        if (id == null) return item
        item.applicationId = id
        return item
    }


    private fun assignBiometricsAId(items: List<Biometric>?, id: String?): List<Biometric>? {
        if (items == null) return null
        if (items.isEmpty()) return items
        if (id == null) return items

        val upElements = arrayListOf<Biometric>()
        for (element in items) {
            val upElement = assignBiometricAId(element, id)
            if (upElement != null)
                upElements.add(upElement)
        }
        return upElements
    }

    private fun assignBiometricAId(item: Biometric?, id: String?): Biometric? {
        if (item == null) return null
        if (id == null) return item
        item.applicationId = id
        return item
    }


}