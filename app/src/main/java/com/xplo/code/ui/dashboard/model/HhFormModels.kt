package com.xplo.code.ui.dashboard.model

import com.google.gson.GsonBuilder
import com.xplo.code.core.TestConfig


data class HouseholdForm(

    var form1: HhForm1? = null,
    var form2: HhForm2? = null,
    var form3: HhForm3? = null,
    var form4: HhForm4? = null,
    var form5: HhForm5? = null,
    var form6: HhForm6? = null,

    var alPerson1: AlternateForm? = null,
    var alPerson2: AlternateForm? = null

//    var altform1: ALTForm1? = null,
//    var altform2: ALTForm2? = null,
//    var altform3: ALTForm3? = null,
)

data class AlternateForm(
    var form1: ALTForm1? = null,
    var form2: ALTForm2? = null,
    var form3: ALTForm3? = null
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

data class ALTForm1(
    var householdName: String? = null,
    var alternateName: String? = null,
    var age: String? = null,
    var idNumber: String? = null,
    var phoneNumber: String? = null,
    var selectAlternateRlt: String? = null,
    var gender: String? = null
)

data class ALTForm2(
    var img: String? = null
)

data class ALTForm3(
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

fun HhForm2?.getFullName(): String? {
    if (this == null) return null
    return "${this.firstName} ${this.middleName} ${this.lastName}"
}

fun HhForm1.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.countryName.isNullOrBlank()) return false
    if (this.stateName.isNullOrBlank()) return false
    if (this.payamName.isNullOrBlank()) return false
    if (this.bomaName.isNullOrBlank()) return false
    return true
}

fun HhForm2.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.firstName.isNullOrBlank()) return false
    if (this.middleName.isNullOrBlank()) return false
    if (this.lastName.isNullOrBlank()) return false
    if (this.idNumber.isNullOrBlank()) return false
    if (this.phoneNumber.isNullOrBlank()) return false
    if (this.mainSourceOfIncome.isNullOrBlank()) return false
    if (this.gender.isNullOrBlank()) return false
    if (this.maritalStatus.isNullOrBlank()) return false
    if (this.legalStatus.isNullOrBlank()) return false
    if (this.spouseName.isNullOrBlank()) return false
    if (this.selectionReason.isNullOrBlank()) return false
    if (this.selectionCriteria.isNullOrBlank()) return false
    if (this.monthlyAverageIncome.isNullOrBlank()) return false
    if (this.spouseName.isNullOrBlank()) return false

    if (this.age == null) return false
    if (this.idNumber == null) return false
    if (this.monthlyAverageIncome == null) return false

    return true
}

fun HhForm3.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.householdSize == null) return false
    if (this.maleHouseholdMembers == null) return false
    if (this.femaleHouseholdMembers == null) return false
    if (this.householdMembers0_2 == null) return false
    if (this.householdMembers3_5 == null) return false
    if (this.householdMembers6_17 == null) return false
    if (this.householdMembers18_35 == null) return false
    if (this.householdMembers36_45 == null) return false
    if (this.householdMembers46_64 == null) return false
    if (this.householdMembers65andAbove == null) return false
    if (this.householdMembersWithDisability == null) return false
    if (this.householdMembersWithChronicallyIll == null) return false
    return true
}

fun ALTForm1.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.householdName.isNullOrBlank()) return false
    if (this.alternateName.isNullOrBlank()) return false
    if (this.age.isNullOrBlank()) return false
    if (this.idNumber.isNullOrBlank()) return false
    if (this.phoneNumber.isNullOrBlank()) return false
    if (this.selectAlternateRlt == null) return false
    if (this.gender == null) return false
    return true
}

fun HouseholdForm?.toJson(): String? {
    //return Gson().toJson(this)
    return GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this)
}