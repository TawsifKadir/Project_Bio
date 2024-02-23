package com.xplo.code.ui.dashboard.enums

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

enum class GenderEnm(val value: String) {

    SELECT("Select Gender"),
    MALE("Male"),
    FEMALE("Female");

    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): GenderEnm? {
            return entries.find { it.value == value }
        }
    }

}

enum class RelationshipEnm(val value: String) {

    SELECT("Select Relationship"),
    HOUSEHOLD_HEAD("Household head"),
    SPOUSE("Spouse"),
    SON_DAUGHTER("Son/daughter"),
    SON_DAUGHTER_IN_LAW("Son/daughter in law"),
    GRANDCHILD("Grandchild"),
    PARENT("Parent"),
    PARENT_IN_LAW("Parent in law"),
    SIBLING("Sibling"),
    OTHER("Other"),
    DOMESTIC_WORKER("Domestic worker"),
    NO_RELATION("No relation"),
    UNKNOWN("Unknown");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): RelationshipEnm? {
            return entries.find { it.value == value }
        }
    }

}


enum class MaritalStatusEnm(val value: String) {

    SELECT("Select Marital Status"),
    SINGLE("Single"),
    MARRIED("Married"),
    WIDOW("Widow"),
    SEPARATED("Separated"),
    DIVORCE("Divorce");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): MaritalStatusEnm? {
            return entries.find { it.value == value }
        }
    }

}


enum class LegalStatusEnm(val value: String) {

    SELECT("Select Legal Status"),
    HOST("Host"),
    RETURNEE("Returnee"),
    REFUGEE("Refugee"),
    IDP("Idp");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): LegalStatusEnm? {
            return entries.find { it.value == value }
        }
    }

}


enum class IncomeSourceEnm(val value: String) {

    SELECT("Select Income Source"),
    NONE("None"),
    SELLING_OF_FARM("Selling of farm"),
    SELLING_OF_NON_FARM("Selling of non farm"),
    CAUSAL_LABOUR("Causal labour"),
    FORMAL_EMPLOYMENT("Formal employment"),
    REMITTANCES("Remittances"),
    GIFT("Gift"),
    FROM_GOVT("From govt"),
    FROM_NGO("From ngo"),
    PENSION("Pension"),
    OTHER("Other");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): IncomeSourceEnm? {
            return entries.find { it.value == value }
        }
    }

}


enum class CurrencyEnm(val value: String) {

    SELECT("Select Currency"),
    SUDANESE_POUND("Sudanese pound"),
    USD("Usd"),
    POUND("Pound"),
    EURO("Euro");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): CurrencyEnm? {
            return entries.find { it.value == value }
        }
    }

}


enum class SelectionCriteriaEnm(val value: String) {

    SELECT("Select Selection Criteria"),
    LIPW("LIPW"),
    DIS("DIS");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): SelectionCriteriaEnm? {
            return entries.find { it.value == value }
        }
    }

}

enum class SelectionReasonEnm(val value: String) {

    SELECT("Select Selection Reason"),
    LIPW_REASON_1("Poor household with no sufficient income to sustain the household"),
    LIPW_REASON_2("Household contain able bodied youth member (18-35)"),
    LIPW_REASON_3("Household headed by young men and women between the ages of 18 and 35"),
    LIPW_REASON_4("Many members who are dependents (HH with dependants greater than 3)"),
    LIPW_REASON_5("Poor household which have persons with severe disabilities"),
    DIS_REASON_1("Child headed households with no alternate income support"),
    DIS_REASON_2("Elderly headed household lacking alternate income support and able bodied member"),
    DIS_REASON_3("Persons with disability headed household lacking alternate income support and able bodied member"),
    DIS_REASON_4("Chronically ill headed household lacking alternate income and able bodied member"),
    DIS_REASON_5("Female headed household lacking alternate income support and able-bodied member");

    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun getArrayLipw(): Array<String> {
            val items = entries.filter {
                it.name.contains("lipw_", true) || it.name.contains(SELECT.name, true)
            }
            return items.map { it.value }.toTypedArray()
        }

        fun getArrayDis(): Array<String> {
            val items = entries.filter {
                it.name.contains("dis_", true) || it.name.contains(SELECT.name, true)
            }
            return items.map { it.value }.toTypedArray()
        }


        fun find(value: String?): SelectionReasonEnm? {
            return entries.find { it.value == value }
        }
    }

}


//enum class SelectionReasonLipwEnm(val value: String) {
//
//    SELECT("Select Selection Reason LIPW"),
//    LIPW_REASON_1("Poor household with no sufficient income to sustain the household"),
//    LIPW_REASON_2("Household contain able bodied youth member (18-35)"),
//    LIPW_REASON_3("Household headed by young men and women between the ages of 18 and 35"),
//    LIPW_REASON_4("Many members who are dependents (HH with dependants greater than 3)"),
//    LIPW_REASON_5("Poor household which have persons with severe disabilities");
//
//    override fun toString(): String {
//        return "$ordinal: $name, $value"
//    }
//
//    companion object {
//        fun getArray(): Array<String> {
//            return entries.map { it.value }.toTypedArray()
//        }
//
//        fun find(value: String?): SelectionReasonLipwEnm? {
//            return entries.find { it.value == value }
//        }
//    }
//
//}
//
//enum class SelectionReasonDisEnm(val value: String) {
//
//    SELECT("Select Selection Reason DIS"),
//    DIS_REASON_1("Child headed households with no alternate income support"),
//    DIS_REASON_2("Elderly headed household lacking alternate income support and able bodied member"),
//    DIS_REASON_3("Persons with disability headed household lacking alternate income support and able bodied member"),
//    DIS_REASON_4("Chronically ill headed household lacking alternate income and able bodied member"),
//    DIS_REASON_5("Female headed household lacking alternate income support and able-bodied member");
//
//
//    override fun toString(): String {
//        return "$ordinal: $name, $value"
//    }
//
//    companion object {
//        fun getArray(): Array<String> {
//            return entries.map { it.value }.toTypedArray()
//        }
//
//        fun find(value: String?): SelectionReasonDisEnm? {
//            return entries.find { it.value == value }
//        }
//    }
//
//}


enum class NonParticipationReasonEnm(val value: String) {

    SELECT("Select Non Participation Reason"),
    REASON_1("All eligible household members have other commitments that occupy their time"),
    REASON_2("I am uncertain about the ability of the household members to comply with the program's expectations or conditions"),
    REASON_3("The health or physical condition of the eligible household members prevents me from participating"),
    REASON_4("I am not convinced that the program will provide meaningful benefits"),
    REASON_5("The program activities don't align with my interests"),
    REASON_OTHER("Other");


    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): NonParticipationReasonEnm? {
            return entries.find { it.value == value }
        }
    }

}


enum class BiometricType(val value: String) {

    PHOTO("Photo"),
    LT("Left Thumb"),
    LI("Left Index"),
    LM("Left Middle"),
    LR("Left Ring"),
    LL("Left Little"),
    RT("Right Thumb"),
    RI("Right Index"),
    RM("Right Middle"),
    RR("Right Ring"),
    RL("Right Little");

    override fun toString(): String {
        return "$ordinal: $name, $value"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): BiometricType? {
            return entries.find { it.value == value }
        }
    }
}

enum class NoFingerprintReasonEnm(val id: Int, val value: String) {
    SELECT(0, "Select Reason"),
    NoFingerprintImpression(1, "No Fingerprint"),
    NoFinger(2, "Missing Finger"),
    NoLeftHand(3, "Missing Left Hand"),
    NoRightHand(4, "Missing Right Hand"),
    NoBothHand(5, "Missing Both Hand"),
    Other(6, "Other (Specify)");

    override fun toString(): String {
        return "$ordinal: $name, $value, $id"
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        fun find(value: String?): NoFingerprintReasonEnm? {
            return entries.find { it.value == value }
        }

        fun find(id: Int): NoFingerprintReasonEnm? {
            return entries.find { it.id == id }
        }
    }
}

enum class BiometricUserType {
    BENEFICIARY,
    NOMINEE,
    ALTERNATE
}














