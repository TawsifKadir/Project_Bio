package com.xplo.code.data_module.model.content

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
    var alternates: List<Alternate>? = null,
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



    @SerializedName("isReadWrite")
    var isReadWrite: Boolean = false,
    @SerializedName("memberReadWrite")
    var memberReadWrite: Int = 0,


    @SerializedName("isOtherMemberPerticipating")
    var isOtherMemberPerticipating: Boolean = false,
    @SerializedName("notPerticipationReason")
    var notPerticipationReason: String? = null,
    @SerializedName("notPerticipationOtherReason")
    var notPerticipationOtherReason: String? = null,
    @SerializedName("nominees")
    var nominees: List<Nominee>? = listOf(),

    @SerializedName("relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: String? = null


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