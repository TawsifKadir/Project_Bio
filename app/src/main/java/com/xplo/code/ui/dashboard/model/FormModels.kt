package com.xplo.code.ui.dashboard.model

import com.google.gson.GsonBuilder
import com.xplo.code.core.TestConfig


data class HouseholdForm(

    var id: Long = -1L,     // optional, use at some specific case

    var form1: HhForm1? = null,
    var form2: HhForm2? = null,
    var form3: HhForm3? = null,
    var form4: HhForm4? = null,
    var form5: HhForm5? = null,
    var form6: HhForm6? = null,

    var alPerson1: AlternateForm? = null,
    var alPerson2: AlternateForm? = null
)

data class AlternateForm(
    var form1: AlForm1? = null,
    var form2: AlForm2? = null,
    var form3: AlForm3? = null
)

data class HhForm1(
    var countryName: String? = null,
    var stateName: String? = null,
    var payamName: String? = null,
    var bomaName: String? = null,
    var lat: Double? = null,
    var lon: Double? = null
)

data class HhForm2(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var age: String? = null,
    var idNumber: String? = null,
    var phoneNumber: String? = null,
    var mainSourceOfIncome: String? = null,
    var monthlyAverageIncome: String? = null,
    var gender: String? = null,
    var maritalStatus: String? = null,
    var legalStatus: String? = null,
    var spouseName: String? = null,
    var selectionReason: String? = null,
    var selectionCriteria: String? = null
)

data class HhForm3(
    var householdSize: Int? = null,
    var maleHouseholdMembers: Int? = null,
    var femaleHouseholdMembers: Int? = null,
    var householdMembers0_2: Int? = null,
    var householdMembers3_5: Int? = null,
    var householdMembers6_17: Int? = null,
    var householdMembers18_35: Int? = null,
    var householdMembers36_45: Int? = null,
    var householdMembers46_64: Int? = null,
    var householdMembers65andAbove: Int? = null,
    var householdMembersWithDisability: Int? = null,
    var householdMembersWithChronicallyIll: Int? = null
)


data class HhForm4(
    var img: String? = null
)

data class HhForm5(
    var finger: String? = null
)

data class HhForm6(
    var nominees: ArrayList<Nominee>? = arrayListOf()
)

data class Nominee(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null
)

data class AlForm1(
    var householdName: String? = null,
    var alternateName: String? = null,
    var age: String? = null,
    var idNumber: String? = null,
    var phoneNumber: String? = null,
    var selectAlternateRlt: String? = null,
    var gender: String? = null
)

data class AlForm2(
    var img: String? = null
)

data class AlForm3(
    var fingerRT: String? = null,
    var fingerRI: String? = null,
    var fingerRM: String? = null,
    var fingerRR: String? = null,
    var fingerRL: String? = null,
    var fingerLT: String? = null,
    var fingerLI: String? = null,
    var fingerLM: String? = null,
    var fingerLR: String? = null,
    var fingerLL: String? = null,
)
