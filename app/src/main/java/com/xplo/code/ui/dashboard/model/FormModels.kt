package com.xplo.code.ui.dashboard.model


data class HouseholdForm(

    var id: Long = -1L,     // optional, use at some specific case

    var form1: HhForm1? = null,
    var form2: HhForm2? = null,
    var form3: HhForm3? = null,
    var form4: HhForm4? = null,
    var form5: HhForm5? = null,
    var form6: HhForm6? = null,

//    var alPerson1: AlternateForm? = null,
//    var alPerson2: AlternateForm? = null

    var alternates: ArrayList<AlternateForm> = arrayListOf()
)

data class AlternateForm(
    var form1: AlForm1? = null,
    var form2: AlForm2? = null,
    var form3: AlForm3? = null
)

data class HhForm1(
    var stateName: String? = null,
    var countryName: String? = null,
    var payamName: String? = null,
    var bomaName: String? = null,
    var lat: Double? = null,
    var lon: Double? = null
)

data class HhForm2(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var spouseFirstName: String? = null,
    var spouseMiddleName: String? = null,
    var spouseLastName: String? = null,
    var age: String? = null,
    var idNumber: String? = null,
    var idNumberType: String? = null,
    var phoneNumber: String? = null,
    var mainSourceOfIncome: String? = null,
    var currency: String? = null,
    var monthlyAverageIncome: String? = null,
    var gender: String? = null,
    var respondentRlt: String? = null,
    var maritalStatus: String? = null,
    var legalStatus: String? = null,
    var spouseName: String? = null,
    var selectionReason: String? = null,
    var selectionCriteria: String? = null,
    var idIsOrNot: String? = null
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
    var householdMembersWithChronicallyIll: Int? = null,
    var readWriteNumber: Int? = null,
    var isReadWrite: String? = null

)


data class HhForm4(
    var img: String? = null
)

data class HhForm5(
    var finger: String? = null
)

data class HhForm6(

    var isNomineeAdd: String? = null,
    var noNomineeReason: String? = null,
//    var nominee1: Nominee? = null,
//    var nominee2: Nominee? = null,
//    var nominee3: Nominee? = null,
//    var nominee4: Nominee? = null,
//    var nominee5: Nominee? = null
    var nominees: ArrayList<Nominee> = arrayListOf()
)

fun HhForm6?.getNomineeNumber(): Int {
    if (this == null)  return 1
    return this.nominees.size.plus(1) ?: 1
}

data class Nominee(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var relation: String? = null,
    var age: Int? = null,
    var gender: String? = null,
    var occupation: String? = null,
    var isReadWrite: String? = null
)

fun Nominee?.toSummary() : String? {
    if (this == null) return null
    return this.toString()
}

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
