package com.xplo.code.data.db.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = "address", indices = {@Index(value = {"application_id"}, unique = true)})
public class Address implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "address_line")
    public String addressLine;

    @ColumnInfo(name = "state_id")
    public Integer stateId;

    @ColumnInfo(name = "county_id")
    public Integer countyId;

    @ColumnInfo(name = "payam_id")
    public Integer payam;

    @ColumnInfo(name = "boma_id")
    public Integer boma;
}
