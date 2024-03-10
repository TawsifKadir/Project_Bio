package com.xplo.code.data.db.room.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = "beneficiary", indices = {@Index(value = {"application_id"}, unique = true)})
public class Beneficiary implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "is_synced")
    public Boolean isSynced = false;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "respondent_first_name")
    public String respondentFirstName;

    @ColumnInfo(name = "respondent_middle_name")
    public String respondentMiddleName;

    @ColumnInfo(name = "respondent_last_name")
    public String respondentLastName;

    @ColumnInfo(name = "respondent_nick_name")
    public String respondentNickName;

    @ColumnInfo(name = "spouse_first_name")
    public String spouseFirstName;

    @ColumnInfo(name = "spouse_middle_name")
    public String spouseMiddleName;

    @ColumnInfo(name = "spouse_last_name")
    public String spouseLastName;

    @ColumnInfo(name = "spouse_nick_name")
    public String spouseNickName;

    @ColumnInfo(name = "relationship_with_household")
    public Long relationshipWithHouseholdHead;

    @ColumnInfo(name = "respondent_age")
    public Integer respondentAge;

    @ColumnInfo(name = "respondent_gender")
    public Long respondentGender;

    @ColumnInfo(name = "respondent_marital_status")
    public Long respondentMaritalStatus;

    @ColumnInfo(name = "respondent_legal_status")
    public Long respondentLegalStatus;

    @ColumnInfo(name = "document_type")
    public Long documentType;

    @ColumnInfo(name = "document_type_other")
    public String documentTypeOther;

    @ColumnInfo(name = "respondent_id")
    public String respondentId;

    @ColumnInfo(name = "respondent_phone_no")
    public String respondentPhoneNo;

    @ColumnInfo(name = "household_income_source")
    public Long householdIncomeSource;

    @ColumnInfo(name = "household_monthly_avg_income")
    public Integer householdMonthlyAvgIncome;

    @ColumnInfo(name = "currency")
    public Long currency;

    @ColumnInfo(name = "selection_criteria")
    public Long selectionCriteria;

    @ColumnInfo(name = "household_size")
    public Integer householdSize;

    @ColumnInfo(name = "is_read_write")
    public Boolean isReadWrite;

    @ColumnInfo(name = "member_read_write")
    public Integer memberReadWrite;

    @ColumnInfo(name = "is_other_member_perticipating")
    public Boolean isOtherMemberPerticipating;

    @ColumnInfo(name = "non_perticipation_other_reason")
    public String notPerticipationOtherReason;

    @ColumnInfo(name = "creation_date")
    public Long creation_date;

    @ColumnInfo(name = "created_by")
    public Long createdBy;

    @ColumnInfo(name = "non_perticipation_reason")
    public Long notPerticipationReason;

//    @ColumnInfo(name = "non_perticipation_other_reason_text")
//    public String notPerticipationOtherReasonText;

    @ColumnInfo(name = "income_source_other")
    public String incomeSourceOther;

    @ColumnInfo(name = "relationship_other")
    public String relationshipOther;

    @Ignore
    public int nomineeSize = 0;
    @Ignore
    public int alternateSize = 0;
    @Ignore
    public byte[] photoPath;


    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "address_id", referencedColumnInfoName = "id")
    Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "location_id", referencedColumnInfoName = "id")
    Location location;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "household_member_2", referencedColumnInfoName = "id")
    HouseholdInfo householdMember2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "household_member_5", referencedColumnInfoName = "id")
    HouseholdInfo householdMember5;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "household_member_17", referencedColumnInfoName = "id")
    HouseholdInfo householdMember17;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "household_member_35", referencedColumnInfoName = "id")
    HouseholdInfo householdMember35;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "household_member_64", referencedColumnInfoName = "id")
    HouseholdInfo householdMember64;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "household_member_65", referencedColumnInfoName = "id")
    HouseholdInfo householdMember65;



    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Nominee> nominees;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "biometric_id", referencedColumnInfoName = "id")
    Biometric biometric;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "alternate_payee_1", referencedColumnInfoName = "id")
    Alternate alternatePayee1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumnInfo(name = "alternate_payee_2", referencedColumnInfoName = "id")
    Alternate alternatePayee2;
	*/

}
