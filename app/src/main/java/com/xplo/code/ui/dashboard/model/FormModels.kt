package com.xplo.code.ui.dashboard.model

import java.io.Serializable

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

data class HouseholdForm(

    var id: Long = -1L,     // optional, use at some specific case
    var uuid: String? = null,

    var form1: HhForm1? = null,
    var form2: HhForm2? = null,
    var form3: HhForm3? = null,
    var form4: HhForm4? = null,
    var form5: HhForm5? = null,
    var form6: HhForm6? = null,
    var alternates: ArrayList<AlternateForm> = arrayListOf(),
    var consentStatus: ConsentStatus = ConsentStatus()
)

data class ConsentStatus(
    var isConsentGivenHhHome: Boolean = false,
    var isConsentGivenNominee: Boolean = false
)

data class AlternateForm(
    var form1: AlForm1? = null,
    var form2: AlForm2? = null,
    var form3: AlForm3? = null
) : Serializable

data class HhForm1(
    var state: Area? = Area(),
    var county: Area? = Area(),
    var payam: Area? = Area(),
    var boma: Area? = Area(),
    var lat: Double? = null,
    var lon: Double? = null
)

data class Area(
    var id: Int? = null,
    var name: String? = null
)

data class HhForm2(
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var spouseFirstName: String? = null,
    var spouseMiddleName: String? = null,
    var spouseLastName: String? = null,
    var age: Int? = null,
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
    var idIsOrNot: String? = null,
    var selectionCriteria: String? = null,
    var itemsSupportType : List<CheckboxItem>? = null
)

data class HhForm3(
    var householdSize: Int? = null,

    var mem0NormalMale: Int = 0,
    var mem0DisableMale: Int = 0,
    var mem0IllMale: Int = 0,
    var mem3NormalMale: Int = 0,
    var mem3DisableMale: Int = 0,
    var mem3IllMale: Int = 0,
    var mem6NormalMale: Int = 0,
    var mem6DisableMale: Int = 0,
    var mem6IllMale: Int = 0,
    var mem18NormalMale: Int = 0,
    var mem18DisableMale: Int = 0,
    var mem18IllMale: Int = 0,
    var mem36NormalMale: Int = 0,
    var mem36DisableMale: Int = 0,
    var mem36IllMale: Int = 0,
    var mem65NormalMale: Int = 0,
    var mem65DisableMale: Int = 0,
    var mem65IllMale: Int = 0,

    var mem0NormalFemale: Int = 0,
    var mem0DisableFemale: Int = 0,
    var mem0IllFemale: Int = 0,
    var mem3NormalFemale: Int = 0,
    var mem3DisableFemale: Int = 0,
    var mem3IllFemale: Int = 0,
    var mem6NormalFemale: Int = 0,
    var mem6DisableFemale: Int = 0,
    var mem6IllFemale: Int = 0,
    var mem18NormalFemale: Int = 0,
    var mem18DisableFemale: Int = 0,
    var mem18IllFemale: Int = 0,
    var mem36NormalFemale: Int = 0,
    var mem36DisableFemale: Int = 0,
    var mem36IllFemale: Int = 0,
    var mem65NormalFemale: Int = 0,
    var mem65DisableFemale: Int = 0,
    var mem65IllFemale: Int = 0,

    var readWriteNumber: Int? = null,
    var isReadWrite: String? = null

)

data class HhForm4(
    var img: String? = null
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
    if (this == null) return 1
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

fun Nominee?.getNomineeHeader(number: Int): String {
    when (number) {
        1 -> return "First Nominee"
        2 -> return "Second Nominee"
        3 -> return "Third Nominee"
        4 -> return "Fourth Nominee"
        5 -> return "Fifth Nominee"
    }
    return "First Nominee"
}

fun Nominee?.toSummary(): String? {
    if (this == null) return null

    var sb = StringBuilder()
        .append("Name: " + this.getFullName())
        .append("\nRelation: " + this.relation)
        .append("\nAge: " + this.age)
        .append("\nGender: " + this.gender)
        .append("\nOccupation: " + this.occupation)
        .append("\nCan read write: " + this.isReadWrite)


    var txt = sb.toString()
    return txt
}


data class HhForm5(
    var finger: Finger? = null
)

data class AlForm1(
    var householdName: String? = null,
//    var householdMiddleName: String? = null,
//    var householdLastName: String? = null,
    var alternateFirstName: String? = null,
    var alternateMiddleName: String? = null,
    var alternateLastName: String? = null,
    var alternateName: String? = null,
    var age: Int? = null,
    var idNumber: String? = null,
    var idNumberType: String? = null,
    var idIsOrNot: String? = null,
    var phoneNumber: String? = null,
    var selectAlternateRlt: String? = null,
    var gender: String? = null
) : Serializable

data class AlForm2(
    var img: String? = null
) : Serializable

data class AlForm3(
    var finger: Finger? = null
) : Serializable

data class Finger(
    var fingerRT: String? = null,
    var fingerRI: String? = null,
    var fingerRM: String? = null,
    var fingerRR: String? = null,
    var fingerRL: String? = null,
    var fingerLT: String? = null,
    var fingerLI: String? = null,
    var fingerLM: String? = null,
    var fingerLR: String? = null,
    var fingerLL: String? = null
) : Serializable

