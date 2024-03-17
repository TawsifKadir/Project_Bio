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
@Entity(tableName = "sync_beneficiary")
public class SyncBeneficiary implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "beneficiary_name")
    public String beneficiaryName;

}
