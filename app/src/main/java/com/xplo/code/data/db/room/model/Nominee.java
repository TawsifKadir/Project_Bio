package com.xplo.code.data.db.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = "nominee")
public class Nominee implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "nominee_first_name")
    public String nomineeFirstName;

    @ColumnInfo(name = "nominee_middle_name")
    public String nomineeMiddleName;

    @ColumnInfo(name = "nominee_last_name")
    public String nomineeLastName;

    @ColumnInfo(name = "nominee_nick_name")
    public String nomineeNickName;

    @ColumnInfo(name = "relationship_with_household")
    public Long relationshipWithHouseholdHead;

    @ColumnInfo(name = "nominee_age")
    public Integer nomineeAge;

    @ColumnInfo(name = "nominee_gender")
    public Long nomineeGender;

    @ColumnInfo(name = "is_read_write")
    public Boolean isReadWrite;

    @ColumnInfo(name = "nominee_occupation")
    public Long nomineeOccupation;

    @ColumnInfo(name = "other_occupation")
    public String otherOccupation;

    @ColumnInfo(name = "relationship_other")
    public String relationshipOther;

}
