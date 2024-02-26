package com.xplo.code.ui.dashboard.model

import com.xplo.code.core.TestConfig
import com.xplo.data.core.ext.toBool
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

    var id: String,     //uuid
    var hid: String,     // optional, use at some specific case
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
    var monthlyAverageIncome: Int? = null,
    var gender: String? = null,
    var respondentRlt: String? = null,
    var maritalStatus: String? = null,
    var legalStatus: String? = null,
    //var spouseName: String? = null,
    var selectionReason: String? = null,
    var idIsOrNot: String? = null,
    var selectionCriteria: String? = null,
    var itemsSupportType: List<CheckboxItem>? = null
)

fun HhForm2?.getOppositeGender(): String? {
//    if (TestConfig.isNavHackEnabled) {
//        return "Female" // test purpose
//    }
    if (this == null) return null
    if (this.gender.equals("Male", true)) return "Female"
    return "Male"
}

data class HhForm3(
    var householdSize: Int? = null,
    var male0_2: HhMember = HhMember(),
    var male3_5: HhMember = HhMember(),
    var male6_17: HhMember = HhMember(),
    var male18_35: HhMember = HhMember(),
    var male36_64: HhMember = HhMember(),
    var male65p: HhMember = HhMember(),

    var female0_2: HhMember = HhMember(),
    var female3_5: HhMember = HhMember(),
    var female6_17: HhMember = HhMember(),
    var female18_35: HhMember = HhMember(),
    var female36_64: HhMember = HhMember(),
    var female65p: HhMember = HhMember(),
    var readWriteNumber: Int? = null,
    var isReadWrite: String? = null

)

data class HhMember(
    var normal: Int = 0,
    var ill: Int = 0,
    var disable: Int = 0
)

data class HhForm4(
    //var img: String? = null
    var photoData: PhotoData? = null
)

fun HhForm4.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.photoData == null) return false
    if (!this.photoData?.isOk().toBool()) return false

    return true
}

data class HhForm6(

    var isNomineeAdd: String? = null,       // nominee add or not
    var noNomineeReason: String? = null,    // spinner
    var otherReason: String? = null,    // others box
    var nominees: ArrayList<Nominee> = arrayListOf(),

    // under list
    var xIsNomineeAdd: String? = null,
    var xNoNomineeReason: String? = null,
    var xOtherReason: String? = null,

    )

fun HhForm6?.getNomineeNumber(): Int {
    if (this == null) return 1
    return this.nominees.size.plus(1)
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

fun Nominee?.getOppositeGender(): String? {
    if (this == null) return null
    if (this.gender.equals("Male", true)) return "Female"
    return "Male"
}

fun Nominee?.getNomineeHeader(number: Int): String {
    if (this == null) return "Nominee $number"
    when (number) {
        1 -> return "First Nominee"
        2 -> return "Second Nominee"
        3 -> return "Third Nominee"
        4 -> return "Fourth Nominee"
        5 -> return "Fifth Nominee"
        else -> return "Nominee $number"
    }
}

fun Nominee?.toSummary(): String? {
    if (this == null) return null

    var sb = StringBuilder()
        .append("Name: " + this.getFullName())
        .append("\nRelation: " + this.relation)
//        .append("\nAge: " + this.age)
        .append("\nGender: " + this.gender)
//        .append("\nOccupation: " + this.occupation)
//        .append("\nCan read write: " + this.isReadWrite)


    var txt = sb.toString()
    return txt
}

fun Nominee?.toDetails(): String? {
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
    //var fingerData: FingerData? = null
    var fingers: List<Finger> = arrayListOf()
)

fun List<Finger>?.isCaptured(fingerCode: String?): Boolean {
    //if (!TestConfig.isValidationEnabled) return true
    //if (!TestConfig.isFingerPrintRequired) return true
    if (this == null) return false
    if (this.isEmpty()) return false
    if (fingerCode == null) return false

    for (item in this){
        if (fingerCode.equals(item.fingerType, true)) return true
    }

    return false
}

fun HhForm5.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (!TestConfig.isFingerPrintRequired) return true
    if (this.fingers.isEmpty()) return false

//    if (TestConfig.isFingerPrintRequired) {
//        if (fingerItemsStore.isEmpty()) {
//            showAlerter("Warning", "Please Add Fingerprint")
//            return
//        }
//    }

    return true
}

data class FingerData(
    var fingerLT: Finger? = null,
    var fingerLI: Finger? = null,
    var fingerLM: Finger? = null,
    var fingerLR: Finger? = null,
    var fingerLL: Finger? = null,

    var fingerRT: Finger? = null,
    var fingerRI: Finger? = null,
    var fingerRM: Finger? = null,
    var fingerRR: Finger? = null,
    var fingerRL: Finger? = null

) : Serializable

data class Finger(
    var fingerId: String? = null,
    var fingerPrint: String? = null,
    var fingerType: String? = null,
    var userType: String? = null
) : Serializable

fun Finger.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.fingerPrint.isNullOrEmpty()) return false
    if (this.fingerType.isNullOrEmpty()) return false
    if (this.userType.isNullOrEmpty()) return false

    return true
}
data class AlForm1(
    var householdName: String? = null,

    var alternateFirstName: String? = null,
    var alternateMiddleName: String? = null,
    var alternateLastName: String? = null,
    var age: Int? = null,
    var idNumber: String? = null,
    var idNumberType: String? = null,
    var idIsOrNot: String? = null,
    var phoneNumber: String? = null,
    var selectAlternateRlt: String? = null,
    var gender: String? = null
) : Serializable

data class AlForm2(
    var photoData: PhotoData? = null
) : Serializable

fun AlForm2.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (!this.photoData?.isOk().toBool()) return false
    return true
}


data class PhotoData(
    var imgPath: String? = null,
    var img: String? = null,
    var userType: String? = null
) : Serializable

fun PhotoData.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.imgPath == null) return false
    if (this.imgPath.isNullOrEmpty()) return false

    return true
}

data class AlForm3(
    //var fingerData: FingerData? = null
    var fingers: List<Finger> = arrayListOf()
) : Serializable

fun AlForm3.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (!TestConfig.isFingerPrintRequired) return true
    if (this.fingers.isEmpty()) return false

    return true
}


