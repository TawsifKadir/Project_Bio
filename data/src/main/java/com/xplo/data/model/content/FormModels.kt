package com.xplo.data.model.content

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

data class FormRqb(

    @SerializedName("applicationId")
    var applicationId: String? = null,

    @SerializedName("address")
    var address: Address? = null,
    @SerializedName("location")
    var location: Location? = null,

    @SerializedName("respondentFirstName")
    var respondentFirstName: String? = null,
    @SerializedName("respondentMiddleName")
    var respondentMiddleName: String? = null,
    @SerializedName("respondentLastName")
    var respondentLastName: String? = null,

    @SerializedName("respondentAge")
    var respondentAge: Int = 0,
    @SerializedName("respondentGender")
    var respondentGender: String? = null,

    @SerializedName("respondentLegalStatus")
    var respondentLegalStatus: String? = null,
    @SerializedName("respondentMaritalStatus")
    var respondentMaritalStatus: String? = null,

    @SerializedName("respondentId")
    var respondentId: String? = null,
    @SerializedName("respondentPhoneNo")

    var respondentPhoneNo: String? = null,
    @SerializedName("selectionCriteria")
    var selectionCriteria: String? = null,
    @SerializedName("selectionReason")
    var selectionReason: String? = null,
    @SerializedName("spouseFirstName")
    var spouseFirstName: String? = null,
    @SerializedName("spouseLastName")
    var spouseLastName: String? = null,
    @SerializedName("spouseMiddleName")
    var spouseMiddleName: String? = null,

    @SerializedName("alternates")
    var alternates: List<AlternatePayee>? = null,
    @SerializedName("biometrics")
    var biometrics: List<Biometric>? = null,

    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("householdIncomeSource")
    var householdIncomeSource: String? = null,
    @SerializedName("householdMonthlyAvgIncome")
    var householdMonthlyAvgIncome: Int = 0,


    @SerializedName("householdSize")
    var householdSize: Int = 0,
    @SerializedName("householdMember2")
    var householdMember2: HouseholdMember? = null,
    @SerializedName("householdMember5")
    var householdMember5: HouseholdMember? = null,
    @SerializedName("householdMember17")
    var householdMember17: HouseholdMember? = null,
    @SerializedName("householdMember35")
    var householdMember35: HouseholdMember? = null,
    @SerializedName("householdMember64")
    var householdMember64: HouseholdMember? = null,
    @SerializedName("householdMember65")
    var householdMember65: HouseholdMember? = null,


    @SerializedName("isOtherMemberPerticipating")
    var isOtherMemberPerticipating: Boolean = false,
    @SerializedName("isReadWrite")
    var isReadWrite: Boolean = false,
    @SerializedName("memberReadWrite")
    var memberReadWrite: Int = 0,

    @SerializedName("nominees")
    var nominees: List<Nominee>? = listOf(),
    @SerializedName("notPerticipationOtherReason")
    var notPerticipationOtherReason: String? = null,
    @SerializedName("notPerticipationReason")
    var notPerticipationReason: String? = null,
    @SerializedName("relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: String? = null


)

data class Address(
    @SerializedName("stateId")
    var stateId: Int? = null,
    @SerializedName("countyId")
    var countyId: Int? = null,
    @SerializedName("payam")
    var payamId: Int? = null,
    @SerializedName("boma")
    var bomaId: Int? = null
)

data class Location(
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lon")
    var lon: Double? = null
)


data class AlternatePayee(
    @SerializedName("nationalId")
    var nationalId: String? = null,
    @SerializedName("payeeName")
    var payeeName: String? = null,
    @SerializedName("payeeAge")
    var payeeAge: Int? = null,
    @SerializedName("payeeGender")
    var payeeGender: String? = null,
    @SerializedName("payeePhoneNo")
    var payeePhoneNo: String? = null,
    @SerializedName("biometrics")
    var biometrics: List<Biometric>? = listOf(),
)


data class Nominee(
    @SerializedName("applicationId")
    var applicationId: String? = null,

    @SerializedName("nomineeFirstName")
    var nomineeFirstName: String? = null,
    @SerializedName("nomineeLastName")
    var nomineeLastName: String? = null,
    @SerializedName("nomineeMiddleName")
    var nomineeMiddleName: String? = null,

    @SerializedName("nomineeAge")
    var nomineeAge: Int = 0,
    @SerializedName("nomineeGender")
    var nomineeGender: String? = null,

    @SerializedName("nomineeOccupation")
    var nomineeOccupation: String? = null,
    @SerializedName("otherOccupation")
    var otherOccupation: String? = null,
    @SerializedName("relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: String? = null,

    @SerializedName("isReadWrite")
    var isReadWrite: Boolean = false
)

data class HouseholdMember(
    @SerializedName("applicationId")
    var applicationId: String? = null,
    @SerializedName("femaleChronicalIll")
    var femaleChronicalIll: Int = 0,
    @SerializedName("femaleDisable")
    var femaleDisable: Int = 0,
    @SerializedName("femaleNormal")
    var femaleNormal: Int = 0,
    @SerializedName("maleChronicalIll")
    var maleChronicalIll: Int = 0,
    @SerializedName("maleDisable")
    var maleDisable: Int = 0,
    @SerializedName("maleNormal")
    var maleNormal: Int = 0
)

data class Biometric(
    @SerializedName("applicationId")
    var applicationId: String? = null,
    @SerializedName("biometricType")
    var biometricType: String? = null,
    @SerializedName("biometricUrl")
    var biometricUrl: String? = null
)

data class FormRsp(

    @SerializedName("id")
    val id: String? = null

)

data class FormsRqb(

    @SerializedName("beneficiaries")
    val items: List<FormsRqb>? = null

)

data class FormsRsp(

    @SerializedName("id")
    val id: String? = null

)


fun FormRqb?.toJson(): String? {
    return GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this)
}