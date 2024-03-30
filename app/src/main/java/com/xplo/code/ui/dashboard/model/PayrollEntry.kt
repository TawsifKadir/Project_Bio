package com.xplo.code.ui.dashboard.model


import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class PayrollEntry(
    @JsonProperty("BenfWorkWagesDetails")
    val benfWorkWagesDetails: String = "",
    @JsonProperty("result")
    val result: List<Result> = listOf(),
    @JsonProperty("status")
    val status: String = ""
): Parcelable {
    @Parcelize
    data class Result(
        @JsonProperty("DateofCreation")
        val dateofCreation: String = "",
        @JsonProperty("EnrollmentNo")
        val enrollmentNo: String = "",
        @JsonProperty("Exchangerateattime")
        val exchangerateattime: Int = 0,
        @JsonProperty("household_number")
        val householdNumber: String = "",
        @JsonProperty("intBenid")
        val intBenid: Int = 0,
        @JsonProperty("intWagePaymentReqid")
        val intWagePaymentReqid: Int = 0,
        @JsonProperty("intWagesReqBenTransid")
        val intWagesReqBenTransid: Int = 0,
        @JsonProperty("intWorkid")
        val intWorkid: Int = 0,
        @JsonProperty("PaymentCycle")
        val paymentCycle: String = "",
        @JsonProperty("ReqFromDate")
        val reqFromDate: String = "",
        @JsonProperty("ReqNo")
        val reqNo: String = "",
        @JsonProperty("ReqToDate")
        val reqToDate: String = "",
        @JsonProperty("Status")
        val status: String = "",
        @JsonProperty("TotAttendance")
        val totAttendance: Int = 0,
        @JsonProperty("WageRateSSPTotal")
        val wageRateSSPTotal: Int = 0,
        @JsonProperty("WageRateUSD")
        val wageRateUSD: Double = 0.0,
        @JsonProperty("WageRateUSDTotal")
        val wageRateUSDTotal: Int = 0,
        @JsonProperty("WorkCode")
        val workCode: String = ""
    ): Parcelable
}