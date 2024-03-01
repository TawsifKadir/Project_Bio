package com.xplo.code.ui.dashboard.model

import com.google.gson.GsonBuilder
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.xplo.code.core.TestConfig
import com.xplo.code.core.ext.isNo
import com.xplo.code.core.ext.toBool
import com.xplo.code.ui.dashboard.UiData



fun HhForm2?.getFullName(): String? {
    if (this == null) return null
    var name = "${this.firstName} ${this.middleName} ${this.lastName} ${this.nickName}"
    return name.replace(" null ", " ")
}

fun HhForm2?.getSpouseFullName(): String? {
    if (this!!.spouseFirstName == null) return null
    return "${spouseFirstName?.replace("null ", " ")}" +
            " ${spouseMiddleName?.replace("null ", " ")} " +
            "${spouseLastName?.replace("null ", " ")} "+
            "${spouseNickName?.replace("null ", " ")}"
}

fun Nominee?.getFullName(): String? {
    if (this == null) return null
    var name = "${this.firstName} ${this.middleName} ${this.lastName} ${this.nickName}"
    return name.replace(" null ", " ")
}

fun AlForm1?.getFullName(): String? {
    if (this == null) return null
    var name = "${this.alternateFirstName} ${this.alternateMiddleName} ${this.alternateLastName} ${this.alternateNickName}"
    return name.replace(" null ", " ")
}


fun HhForm1.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (!this.county?.isOk().toBool()) return false
    if (!this.state?.isOk().toBool()) return false
    if (!this.payam?.isOk().toBool()) return false
    if (!this.boma?.isOk().toBool()) return false

//    if (this.county?.name.isNullOrBlank()) return false
//    if (this.state?.name.isNullOrBlank()) return false
//    if (this.payam?.name.isNullOrBlank()) return false
//    if (this.boma?.name.isNullOrBlank()) return false
    //if (this.lat == null) return false
    //if (this.lon == null) return false

    return true
}
fun Area.isOk(): Boolean {

    if (this.id == null) return false
    if (this.name.isNullOrBlank()) return false

    return true
}

fun HhForm2.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.firstName.isNullOrBlank()) return false
    if (this.middleName.isNullOrBlank()) return false
    if (this.lastName.isNullOrBlank()) return false
    //if (this.idNumber.isNullOrBlank()) return false
    if (this.mainSourceOfIncome.isNullOrBlank()) return false
    if (this.gender.isNullOrBlank()) return false
    if (this.maritalStatus.isNullOrBlank()) {
        return false
    }
    if (this.phoneNumber == null) return false
    
    if (this.maritalStatus == MaritalStatusEnum.MARRIED.value) {
        if (this.spouseFirstName.isNullOrBlank()) {
            return false
        }
        if (this.spouseMiddleName.isNullOrBlank()) {
            return false
        }
        if (this.spouseLastName.isNullOrBlank()) {
            return false
        }
    }

    if (this.idIsOrNot == null) {
        if (this.idNumber.isNullOrBlank()) {
            return false
        }
        if (this.idNumberType.isNullOrBlank()) {
            return false
        }
    }

    if (this.legalStatus.isNullOrBlank()) return false
    if (this.respondentRlt.isNullOrBlank()) return false
    //if (this.spouseName.isNullOrBlank()) return false
//    if (this.selectionReason.isNullOrBlank()) return false
    if (this.selectionCriteria.isNullOrBlank()) return false
    if (this.monthlyAverageIncome == null) return false
//    if (this.selectionReason.isNullOrBlank()) return false

    if (this.age == null) return false
    if (this.monthlyAverageIncome == null) return false

    if ((this.itemsSupportType?.count() ?: 0) == 0) return false

    return true
}

fun HhForm2.checkExtraCases(): String? {
    if (!TestConfig.isValidationEnabled) return null
    //if (this == null) return null
    if (!this.isOkCaseSizeMinSupportType()) return "Please Select at least one Support Type"
    return null
}

fun HhForm2.isOkCaseSizeMinSupportType(): Boolean {
    //if (!TestConfig.isValidationEnabled) return true
    //if (this == null) return false
    if (this.itemsSupportType.isNullOrEmpty()) return false
    return true
}

fun HhForm3.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.householdSize == null) return false

    if (this.readWriteNumber == null) return false
    if (this.isReadWrite.isNullOrEmpty()) return false

    return true
}

fun HhForm6.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.isNomineeAdd.isNullOrEmpty()) return false

    if (this.isNomineeAdd.equals("no", true)) {
        return this.isOkNoNominee()
    }

//    if (this.nominees.size < 2) return false
//    if (this.nominees.size > 5) return false
//
//    var isFemaleExist = false
//    for (nominee in this.nominees){
//         if(nominee.gender == "Female"){
//             isFemaleExist = true
//         }
//     }
//
//    if(isFemaleExist == false){
//        return false
//    }

    if (!this.nominees.isOk()) return false
    return true
}

fun HhForm6.isExtraNomineeOk(): Boolean {

    if (this.xIsNomineeAdd.isNo()) {
        if (this.xNoNomineeReason.isNullOrEmpty()) return false
        if (this.xNoNomineeReason?.contains(UiData.otherSpecify, true).toBool()) {
            if (this.xOtherReason.isNullOrEmpty()) return false
        }
    }

    return true
}


fun HhForm6.isOk2(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.isNomineeAdd.isNullOrEmpty()) return false

    if (this.isNomineeAdd.equals("no", true)) {
        return this.isOkNoNominee()
    }

//    if (this.nominees.size < 2) return false
//    if (this.nominees.size > 5) return false
//
//    var isFemaleExist = false
//    for (nominee in this.nominees){
//         if(nominee.gender == "Female"){
//             isFemaleExist = true
//         }
//     }
//
//    if(isFemaleExist == false){
//        return false
//    }

    if (!this.nominees.isOk()) return false
    return true
}

fun HhForm6.checkExtraCases(): String? {
    if (!TestConfig.isValidationEnabled) return null
    if (this.isNomineeAdd.isNo()) return null  // no extra case for no nominee selection
    if (!this.isOkCaseSizeMin()) return "Minimum 1 nominee required"
    if (!this.isOkCaseSizeMax()) return "Maximum 5 nominee allowed"
    //if (!this.isOkCaseMaleFemale()) return "1 female nominee required"
    return null
}

fun HhForm6.isOkCaseSizeMin(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.nominees.size < 1) return false
    //if (this.nominees.size > 5) return false
    return true
}

fun HhForm6.isOkCaseSizeMax(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    //if (this.nominees.size < 2) return false
    if (this.nominees.size > 5) return false
    return true
}

fun HhForm6.isOkCaseMaleFemale(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.nominees.isEmpty()) return false
    for (nominee in this.nominees) {
        if (nominee.gender.equals("Female", true))
            return true
    }
    return false
}


fun HhForm6.isOkNoNominee(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.isNomineeAdd.isNullOrEmpty()) return false
    if (this.noNomineeReason.isNullOrEmpty()) return false
    if (this.noNomineeReason?.contains(UiData.otherSpecify, true).toBool()) {
        if (this.otherReason.isNullOrEmpty()) return false
    }
    return true
}

fun Nominee?.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this == null) return false
    if (this.firstName.isNullOrBlank()) return false
    if (this.lastName.isNullOrBlank()) return false
    if (this.relation.isNullOrBlank()) return false
    if (this.age == null) return false
    if (this.gender.isNullOrEmpty()) return false
    if (this.occupation.isNullOrEmpty()) return false

    //var isReadWrite: Boolean? = null
    return true
}

fun List<Nominee>?.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this == null) return false

    for (item in this) {
        if (!item.isOk()) return false
    }

    return true
}


fun AlForm1.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.householdName.isNullOrBlank()) return false
    if (this.alternateFirstName.isNullOrBlank()) return false
    if (this.alternateMiddleName.isNullOrBlank()) return false
    if (this.alternateLastName.isNullOrBlank()) return false
    if (this.age == null) return false
    if (this.selectAlternateRlt == null) return false
    if (this.gender == null) return false
    if (this.idIsOrNot == null) {
        if (this.idNumber.isNullOrBlank()) {
            return false
        }
        if (this.idNumberType.isNullOrBlank()) {
            return false
        }
    }
    return true
}

fun HouseholdForm?.toJson(): String? {
    //return Gson().toJson(this)
    return GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this)
}

fun HouseholdForm?.toSummary(): String? {
    if (this == null) return null

    var sb = StringBuilder()
        .append("Name: " + this.form2.getFullName())
        .append("\nAlternate Added: " + this.alternates.size)


    var txt = sb.toString()
    return txt
}

fun AlternateForm?.toSummary(): String? {
    if (this == null) return null

    var sb = StringBuilder()
        .append("Name: " + this.form1.getFullName())
        .append("\nGender: " + this.form1?.gender)
        .append("\nAge: " + this.form1?.age)


    var txt = sb.toString()
    return txt
}