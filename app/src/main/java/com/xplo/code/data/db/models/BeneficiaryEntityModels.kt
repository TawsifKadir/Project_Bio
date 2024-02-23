package com.xplo.code.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.xplo.code.ui.dashboard.enums.CurrencyEnm
import com.xplo.code.ui.dashboard.enums.GenderEnm
import com.xplo.code.ui.dashboard.enums.IncomeSourceEnm
import com.xplo.code.ui.dashboard.enums.LegalStatusEnm
import com.xplo.code.ui.dashboard.enums.MaritalStatusEnm
import com.xplo.code.ui.dashboard.enums.SelectionCriteriaEnm
import com.xplo.code.ui.dashboard.enums.SelectionReasonEnm
import com.xplo.data.model.content.Address
import com.xplo.data.model.content.Alternate
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
    var respondentGender: GenderEnm? = null,

    @ColumnInfo(name = "respondentLegalStatus")
    var respondentLegalStatus: LegalStatusEnm? = null,
    @ColumnInfo(name = "respondentMaritalStatus")
    var respondentMaritalStatus: MaritalStatusEnm? = null,

    @ColumnInfo(name = "respondentId")
    var respondentId: String? = null,
    @ColumnInfo(name = "respondentPhoneNo")
    var respondentPhoneNo: String? = null,


    @ColumnInfo(name = "relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: String? = null,

    @ColumnInfo(name = "currency")
    var currency: CurrencyEnm? = null,
    @ColumnInfo(name = "householdIncomeSource")
    var householdIncomeSource: IncomeSourceEnm? = null,
    @ColumnInfo(name = "householdMonthlyAvgIncome")
    var householdMonthlyAvgIncome: Int = 0,

    @ColumnInfo(name = "selectionCriteria")
    var selectionCriteria: SelectionCriteriaEnm? = null,
    @ColumnInfo(name = "selectionReason")
    var selectionReason: SelectionReasonEnm? = null,

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

    @ColumnInfo(name = "alternates")
    var alternates: ArrayList<Alternate>? = null,
    @ColumnInfo(name = "biometrics")
    var biometrics: ArrayList<Biometric>? = null,


    @ColumnInfo(name = "isOtherMemberPerticipating")
    var isOtherMemberPerticipating: Boolean = false,
    @ColumnInfo(name = "notPerticipationReason")
    var notPerticipationReason: String? = null,
    @ColumnInfo(name = "notPerticipationOtherReason")
    var notPerticipationOtherReason: String? = null,
    @ColumnInfo(name = "nominees")
    var nominees: ArrayList<Nominee>? = arrayListOf()

) : Serializable

fun BeneficiaryEntity?.toJson(): String? {
    return GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this)
}

//data class NomineeMappingTable (
//    val outfitIdRef: Long,
//    @ColumnInfo(index = true)
//    val clothingIdRef: Long
//)



