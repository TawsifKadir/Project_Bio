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
/*
enum class GenderEnum(val value: String) {

    SELECT("Select Gender"),
    MALE("Male"),
    FEMALE("Female");

    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): GenderEnum? {
            return entries.find { it.value == value }
        }
    }

}

enum class RelationshipEnum(val value: String) {

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

    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): RelationshipEnum? {
            return entries.find { it.value == value }
        }
    }

}


enum class MaritalStatusEnum(val value: String) {

    SELECT("Select Marital Status"),
    SINGLE("Single"),
    MARRIED("Married"),
    WIDOW("Widow"),
    SEPARATED("Separated"),
    DIVORCE("Divorce");

    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): MaritalStatusEnum? {
            return entries.find { it.value == value }
        }
    }

}


enum class LegalStatusEnum(val value: String) {

    SELECT("Select Legal Status"),
    HOST("Host"),
    RETURNEE("Returnee"),
    REFUGEE("Refugee"),
    IDP("Idp");




    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): LegalStatusEnum? {
            return entries.find { it.value == value }
        }
    }

}


enum class IncomeSourceEnum(val value: String) {

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

    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): IncomeSourceEnum? {
            return entries.find { it.value == value }
        }
    }

}


enum class CurrencyEnum(val value: String) {

    SELECT("Select Currency"),
    SUDANESE_POUND("Sudanese pound"),
    USD("Usd"),
    POUND("Pound"),
    EURO("Euro");

    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): CurrencyEnum? {
            return entries.find { it.value == value }
        }
    }

}


enum class SelectionCriteriaEnum(val value: String) {

    SELECT("Select Selection Criteria"),
    LIPW("LIPW"),
    DIS("DIS");




    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): SelectionCriteriaEnum? {
            return entries.find { it.value == value }
        }
    }

}

enum class SelectionReasonEnum(val value: String) {

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



    companion object {
        @JvmStatic
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


        @JvmStatic
        fun find(value: String?): SelectionReasonEnum? {
            return entries.find { it.value == value }
        }
    }

}


//enum class SelectionReasonLipwEnum(val value: String) {
//
//    SELECT("Select Selection Reason LIPW"),
//    LIPW_REASON_1("Poor household with no sufficient income to sustain the household"),
//    LIPW_REASON_2("Household contain able bodied youth member (18-35)"),
//    LIPW_REASON_3("Household headed by young men and women between the ages of 18 and 35"),
//    LIPW_REASON_4("Many members who are dependents (HH with dependants greater than 3)"),
//    LIPW_REASON_5("Poor household which have persons with severe disabilities");
//
//    override fun toString(): String {
//        return value
//    }
//
//    companion object {
//        @JvmStatic
//        fun getArray(): Array<String> {
//            return entries.map { it.value }.toTypedArray()
//        }
//
//@JvmStatic
//        fun find(value: String?): SelectionReasonLipwEnum? {
//            return entries.find { it.value == value }
//        }
//    }
//
//}
//
//enum class SelectionReasonDisEnum(val value: String) {
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
//        return value
//    }
//
//    companion object {
//        @JvmStatic
//        fun getArray(): Array<String> {
//            return entries.map { it.value }.toTypedArray()
//        }
//
//@JvmStatic
//        fun find(value: String?): SelectionReasonDisEnum? {
//            return entries.find { it.value == value }
//        }
//    }
//
//}


enum class NonParticipationReasonEnum(val value: String) {

    SELECT("Select Non Participation Reason"),
    REASON_1("All eligible household members have other commitments that occupy their time"),
    REASON_2("I am uncertain about the ability of the household members to comply with the program's expectations or conditions"),
    REASON_3("The health or physical condition of the eligible household members prevents me from participating"),
    REASON_4("I am not convinced that the program will provide meaningful benefits"),
    REASON_5("The program activities don't align with my interests"),
    REASON_OTHER("Other");




    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): NonParticipationReasonEnum? {
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



    companion object {
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): BiometricType? {
            return entries.find { it.value == value }
        }
    }
}

enum class NoFingerprintReasonEnum(val id: Int, val value: String) {
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
        @JvmStatic
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }

        @JvmStatic
        fun find(value: String?): NoFingerprintReasonEnum? {
            return entries.find { it.value == value }
        }

        @JvmStatic
        fun find(id: Int): NoFingerprintReasonEnum? {
            return entries.find { it.id == id }
        }
    }
}

enum class BiometricUserType {
    BENEFICIARY,
    NOMINEE,
    ALTERNATE
}








*/





