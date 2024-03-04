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
@Entity(tableName = "alternate")
public class Alternate implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "payee_first_name")
    public String payeeFirstName;

    @ColumnInfo(name = "payee_middle_name")
    public String payeeMiddleName;

    @ColumnInfo(name = "payee_last_name")
    public String payeeLastName;

    @ColumnInfo(name = "payee_nick_name")
    public String payeeNickName;

    @ColumnInfo(name = "payee_gender")
    public Long payeeGender;

    @ColumnInfo(name = "payee_age")
    public Integer payeeAge;

    @ColumnInfo(name = "document_type")
    public Long documentType;

    @ColumnInfo(name = "document_type_other")
    public String documentTypeOther;

    @ColumnInfo(name = "national_id")
    public String nationalId;

    @ColumnInfo(name = "payee_phone_no")
    public String payeePhoneNo;

}
