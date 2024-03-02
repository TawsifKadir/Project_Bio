package com.xplo.code.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.IncomeSourceEnum
import com.kit.integrationmanager.model.LegalStatusEnum
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum
import com.xplo.code.data_module.model.content.Address
import com.xplo.code.data_module.model.content.Alternate
import com.xplo.code.data_module.model.content.Biometric
import com.xplo.code.data_module.model.content.HouseholdMember
import com.xplo.code.data_module.model.content.Location
import com.xplo.code.data_module.model.content.Nominee
import java.io.Serializable

@Entity(tableName = "beneficiary_items")
data class BeneficiaryEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,             //uuid
    @ColumnInfo(name = "hid")
    var hid: String? = null,

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
    @ColumnInfo(name = "respondentNickName")
    var respondentNickName: String? = null,

    @ColumnInfo(name = "respondentAge")
    var respondentAge: Int = 0,
    @ColumnInfo(name = "respondentGender")
    var respondentGender: GenderEnum? = null,

    @ColumnInfo(name = "respondentLegalStatus")
    var respondentLegalStatus: LegalStatusEnum? = null,

    @ColumnInfo(name = "documentTypeEnum")
    var documentTypeEnum: DocumentTypeEnum? = null,

    @ColumnInfo(name = "respondentMaritalStatus")
    var respondentMaritalStatus: MaritalStatusEnum? = null,

    @ColumnInfo(name = "respondentId")
    var respondentId: String? = null,
    @ColumnInfo(name = "respondentPhoneNo")
    var respondentPhoneNo: String? = null,


    @ColumnInfo(name = "relationshipWithHouseholdHead")
    var relationshipWithHouseholdHead: RelationshipEnum? = null,

    @ColumnInfo(name = "currency")
    var currency: CurrencyEnum? = null,
    @ColumnInfo(name = "householdIncomeSource")
    var householdIncomeSource: IncomeSourceEnum? = null,
    @ColumnInfo(name = "householdMonthlyAvgIncome")
    var householdMonthlyAvgIncome: Int = 0,

    @ColumnInfo(name = "selectionCriteria")
    var selectionCriteria: SelectionCriteriaEnum? = null,
    @ColumnInfo(name = "selectionReason")
    var selectionReason: SelectionReasonEnum? = null,

    @ColumnInfo(name = "spouseFirstName")
    var spouseFirstName: String? = null,
    @ColumnInfo(name = "spouseLastName")
    var spouseLastName: String? = null,
    @ColumnInfo(name = "spouseMiddleName")
    var spouseMiddleName: String? = null,
    @ColumnInfo(name = "spouseNickName")
    var spouseNickName: String? = null,


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
    var notPerticipationReason: NonPerticipationReasonEnum? = null,
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



