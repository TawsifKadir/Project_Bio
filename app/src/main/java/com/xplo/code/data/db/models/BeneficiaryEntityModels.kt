package com.xplo.code.data.db.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.xplo.data.model.content.Address
import com.xplo.data.model.content.AlternatePayee
import com.xplo.data.model.content.Biometric
import com.xplo.data.model.content.HouseholdMember
import com.xplo.data.model.content.Location
import com.xplo.data.model.content.Nominee
import java.io.Serializable

@Entity(tableName = "beneficiary_items")
data class BeneficiaryEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "uuid")
    var uuid: String? = null,
    @ColumnInfo(name = "isSynced")
    var isSynced: Boolean = false,


    @ColumnInfo(name = "applicationId")
    var applicationId: String? = null,

    @Embedded(prefix = "address_")
    //@ColumnInfo(name = "address")
    var address: Address? = null,

    @Embedded(prefix = "location_")
    //@ColumnInfo(name = "location")
    var location: Location? = null,

    @ColumnInfo(name = "respondentFirstName")
    var respondentFirstName: String? = null,
    @ColumnInfo(name = "respondentMiddleName")
    var respondentMiddleName: String? = null,
    @ColumnInfo(name = "respondentLastName")
    var respondentLastName: String? = null,

    @ColumnInfo(name = "respondentAge")
    var respondentAge: Int = 0,
    @ColumnInfo(name = "respondentGender")
    var respondentGender: String? = null,

    @ColumnInfo(name = "respondentLegalStatus")
    var respondentLegalStatus: String? = null,
    @ColumnInfo(name = "respondentMaritalStatus")
    var respondentMaritalStatus: String? = null,

    @ColumnInfo(name = "respondentId")
    var respondentId: String? = null,
    @ColumnInfo(name = "respondentPhoneNo")
    var respondentPhoneNo: String? = null,


    @ColumnInfo(name = "relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: String? = null,

    @ColumnInfo(name = "currency")
    var currency: String? = null,
    @ColumnInfo(name = "householdIncomeSource")
    var householdIncomeSource: String? = null,
    @ColumnInfo(name = "householdMonthlyAvgIncome")
    var householdMonthlyAvgIncome: Int = 0,

    @ColumnInfo(name = "selectionCriteria")
    var selectionCriteria: String? = null,
    @ColumnInfo(name = "selectionReason")
    var selectionReason: String? = null,

    @ColumnInfo(name = "spouseFirstName")
    var spouseFirstName: String? = null,
    @ColumnInfo(name = "spouseLastName")
    var spouseLastName: String? = null,
    @ColumnInfo(name = "spouseMiddleName")
    var spouseMiddleName: String? = null,


    @ColumnInfo(name = "householdSize")
    var householdSize: Int = 0,

    @ColumnInfo(name = "householdMember2")
    var householdMember2: HouseholdMember? = null,
    @ColumnInfo(name = "householdMember5")
    var householdMember5: HouseholdMember? = null,
    @ColumnInfo(name = "householdMember17")
    var householdMember17: HouseholdMember? = null,
    @ColumnInfo(name = "householdMember35")
    var householdMember35: HouseholdMember? = null,
    @ColumnInfo(name = "householdMember64")
    var householdMember64: HouseholdMember? = null,
    @ColumnInfo(name = "householdMember65")
    var householdMember65: HouseholdMember? = null,
    @ColumnInfo(name = "isReadWrite")
    var isReadWrite: Boolean = false,
    @ColumnInfo(name = "memberReadWrite")
    var memberReadWrite: Int = 0,

//    @ColumnInfo(name = "alternates")
//    var alternates: List<AlternatePayee>? = null,
//    @ColumnInfo(name = "biometrics")
//    var biometrics: List<Biometric>? = null,


    @ColumnInfo(name = "isOtherMemberPerticipating")
    var isOtherMemberPerticipating: Boolean = false,
    @ColumnInfo(name = "notPerticipationReason")
    var notPerticipationReason: String? = null,
    @ColumnInfo(name = "notPerticipationOtherReason")
    var notPerticipationOtherReason: String? = null,
//    @ColumnInfo(name = "nominees")
//    var nominees: List<Nominee>? = listOf(),
    @ColumnInfo(name = "xtra")
    var xtra: Boolean = false,

) : Serializable

fun BeneficiaryEntity?.toJson(): String? {
    return GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this)
}



