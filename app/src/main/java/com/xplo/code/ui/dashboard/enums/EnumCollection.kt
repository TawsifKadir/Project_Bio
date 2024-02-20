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
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}

enum class RelationshipEnm(val value: String) {

    SELECT("Select Relationship"),
    HOUSEHOLD_HEAD("HOUSEHOLD_HEAD"),
    SPOUSE("SPOUSE"),
    SON_DAUGHTER("SON_DAUGHTER"),
    SON_DAUGHTER_IN_LAW("SON_DAUGHTER_IN_LAW"),
    GRANDCHILD("GRANDCHILD"),
    PARENT("PARENT"),
    PARENT_IN_LAW("PARENT_IN_LAW"),
    SIBLING("SIBLING"),
    OTHER("OTHER"),
    DOMESTIC_WORKER("DOMESTIC_WORKER"),
    NO_RELATION("NO_RELATION"),
    UNKNOWN("UNKNOWN");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class MaritalStatusEnm(val value: String) {

    SELECT("Select Marital Status"),
    SINGLE("SINGLE"),
    MARRIED("MARRIED"),
    WIDOW("WIDOW"),
    SEPARATED("SEPARATED"),
    DIVORCE("DIVORCE");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class LegalStatusEnm(val value: String) {

    SELECT("Select Legal Status"),
    HOST("HOST"),
    RETURNEE("RETURNEE"),
    REFUGEE("REFUGEE"),
    IDP("IDP");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class IncomeSourceEnm(val value: String) {

    SELECT("Select Income Source"),
    NONE("NONE"),
    SELLING_OF_FARM("SELLING_OF_FARM"),
    SELLING_OF_NON_FARM("SELLING_OF_NON_FARM"),
    CAUSAL_LABOUR("CAUSAL_LABOUR"),
    FORMAL_EMPLOYMENT("FORMAL_EMPLOYMENT"),
    REMITTANCES("REMITTANCES"),
    GIFT("GIFT"),
    FROM_GOVT("FROM_GOVT"),
    FROM_NGO("FROM_NGO"),
    PENSION("PENSION"),
    OTHER("OTHER");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class CurrencyEnm(val value: String) {

    SELECT("Select Currency"),
    SUDANESE_POUND("SUDANESE_POUND"),
    USD("USD"),
    POUND("POUND"),
    EURO("EURO");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class SelectionCriteriaEnm(val value: String) {

    SELECT("Select Selection Criteria"),
    LIPW("LIPW"),
    DIS("DIS");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class SelectionReasonLipwEnm(val value: String) {

    SELECT("Select Selection Reason LIPW"),
    LIPW_REASON_1("Poor household with no sufficient income to sustain the household"),
    LIPW_REASON_2("Household contain able bodied youth member (18-35)"),
    LIPW_REASON_3("Household headed by young men and women between the ages of 18 and 35"),
    LIPW_REASON_4("Many members who are dependents (HH with dependants greater than 3)"),
    LIPW_REASON_5("Poor household which have persons with severe disabilities");

    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}

enum class SelectionReasonDisEnm(val value: String) {

    SELECT("Select Selection Reason DIS"),
    DIS_REASON_1("Child headed households with no alternate income support"),
    DIS_REASON_2("Elderly headed household lacking alternate income support and able bodied member"),
    DIS_REASON_3("Persons with disability headed household lacking alternate income support and able bodied member"),
    DIS_REASON_4("Chronically ill headed household lacking alternate income and able bodied member"),
    DIS_REASON_5("Female headed household lacking alternate income support and able-bodied member");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}


enum class NonParticipationReasonEnm(val value: String) {

    SELECT("Select Non Participation Reason"),
    REASON_1("All eligible household members have other commitments that occupy their time"),
    REASON_2("I am uncertain about the ability of the household members to comply with the program's expectations or conditions"),
    REASON_3("The health or physical condition of the eligible household members prevents me from participating"),
    REASON_4("I am not convinced that the program will provide meaningful benefits"),
    REASON_5("The program activities don't align with my interests"),
    REASON_OTHER("Other");


    override fun toString(): String {
        return value
    }

    companion object {
        fun getArray(): Array<String> {
            return entries.map { it.value }.toTypedArray()
        }
    }

}









