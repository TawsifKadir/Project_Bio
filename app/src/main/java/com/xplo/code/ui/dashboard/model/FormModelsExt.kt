package com.xplo.code.ui.dashboard.model

import com.google.gson.GsonBuilder
import com.xplo.code.core.TestConfig
import com.xplo.code.utils.MaritalStatus


fun HhForm2?.getFullName(): String? {
    if (this == null) return null
    var name = "${this.firstName} ${this.middleName} ${this.lastName}"
    return name.replace(" null ", " ")
}

fun HhForm1.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.countryName.isNullOrBlank()) return false
    if (this.stateName.isNullOrBlank()) return false
    if (this.payamName.isNullOrBlank()) return false
    if (this.bomaName.isNullOrBlank()) return false
    if (this.lat == null) return false
    if (this.lon == null) return false

    return true
}

fun HhForm2.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true

    if (this.firstName.isNullOrBlank()) return false
    if (this.lastName.isNullOrBlank()) return false
    //if (this.idNumber.isNullOrBlank()) return false
    if (this.phoneNumber.isNullOrBlank()) {
        return false
    }
    if (this.mainSourceOfIncome.isNullOrBlank()) return false
    if (this.gender.isNullOrBlank()) return false
    if (this.maritalStatus.isNullOrBlank()){
        return false
    }

    if (this.maritalStatus == MaritalStatus.MARRIED.status){
        if(this.spouseFirstName.isNullOrBlank()){
            return false
        }
    if(this.spouseLastName.isNullOrBlank()){
            return false
        }
    }

    if (this.legalStatus.isNullOrBlank()) return false
    //if (this.spouseName.isNullOrBlank()) return false
    if (this.selectionReason.isNullOrBlank()) return false
    if (this.selectionCriteria.isNullOrBlank()) return false
    if (this.monthlyAverageIncome.isNullOrBlank()) return false
    if (this.selectionReason.isNullOrBlank()) return false

    if (this.age == null) return false
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
    if (this.readWriteNumber == null) return false
    if (this.isReadWrite.isNullOrEmpty()) return false

    return true
}

fun HhForm6.isOk(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.isNomineeAdd.isNullOrEmpty()) return false

    if (this.isNomineeAdd.equals("no", true)){
        return this.isOkNoNominee()
    }

    if (this.nominees.size < 1) return false
    if (!this.nominees.isOk()) return false
    return true
}

fun HhForm6.isOkNoNominee(): Boolean {
    if (!TestConfig.isValidationEnabled) return true
    if (this.isNomineeAdd.isNullOrEmpty()) return false
    if (this.noNomineeReason.isNullOrEmpty()) return false
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

    for (item in this){
        if (!item.isOk()) return false
    }

    return true
}


fun AlForm1.isOk(): Boolean {
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

fun HouseholdForm?.toSummary(): String? {
    if (this == null) return null

    var sb = StringBuilder()
        .append("name: " + this.form2.getFullName())
        .append("\nsize: " + this.alternates.size)


    var txt = sb.toString()
    return txt
}