package com.xplo.data.model.content

import com.google.gson.annotations.SerializedName

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */


data class Address(
    @SerializedName("stateId")
    var stateId: Int? = null,
    @SerializedName("countyId")
    var countyId: Int? = null,
    @SerializedName("payam")
    var payamId: Int? = null,
    @SerializedName("boma")
    var bomaId: Int? = null
)

data class Location(
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lon")
    var lon: Double? = null
)


data class Alternate(
    @SerializedName("nationalId")
    var nationalId: String? = null,
    @SerializedName("payeeName")
    var payeeName: String? = null,
    @SerializedName("payeeAge")
    var payeeAge: Int = 0,
    @SerializedName("payeeGender")
    var payeeGender: String? = null,
    @SerializedName("payeePhoneNo")
    var payeePhoneNo: String? = null,
    @SerializedName("biometrics")
    var biometrics: List<Biometric>? = listOf(),
)


data class Nominee(
    @SerializedName("applicationId")
    var applicationId: String? = null,

    @SerializedName("nomineeFirstName")
    var nomineeFirstName: String? = null,
    @SerializedName("nomineeLastName")
    var nomineeLastName: String? = null,
    @SerializedName("nomineeMiddleName")
    var nomineeMiddleName: String? = null,

    @SerializedName("nomineeAge")
    var nomineeAge: Int = 0,
    @SerializedName("nomineeGender")
    var nomineeGender: String? = null,

    @SerializedName("nomineeOccupation")
    var nomineeOccupation: String? = null,
    @SerializedName("otherOccupation")
    var otherOccupation: String? = null,
    @SerializedName("relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: String? = null,

    @SerializedName("isReadWrite")
    var isReadWrite: Boolean = false
)

data class HouseholdMember(
    @SerializedName("applicationId")
    var applicationId: String? = null,

    @SerializedName("maleNormal")
    var maleNormal: Int = 0,
    @SerializedName("maleChronicalIll")
    var maleChronicalIll: Int = 0,
    @SerializedName("maleDisable")
    var maleDisable: Int = 0,
    @SerializedName("totalMale")
    var totalMale: Int = 0,

    @SerializedName("femaleNormal")
    var femaleNormal: Int = 0,
    @SerializedName("femaleChronicalIll")
    var femaleChronicalIll: Int = 0,
    @SerializedName("femaleDisable")
    var femaleDisable: Int = 0,
    @SerializedName("totalFemale")
    var totalFemale: Int = 0

)

data class Biometric(
    @SerializedName("applicationId")
    var applicationId: String? = null,
    @SerializedName("biometricType")
    var biometricType: String? = null,
    @SerializedName("biometricUserType")
    var biometricUserType: String? = null,
    @SerializedName("biometricData")
    var biometricData: String? = null,


    @SerializedName("noFingerPrint")
    var noFingerPrint: Boolean? = null,
    @SerializedName("noFingerprintReason")
    var noFingerprintReason: String? = null,
    @SerializedName("noFingerprintReasonText")
    var noFingerprintReasonText: String? = null,

    @SerializedName("biometricUrl")
    var biometricUrl: String? = null
)

