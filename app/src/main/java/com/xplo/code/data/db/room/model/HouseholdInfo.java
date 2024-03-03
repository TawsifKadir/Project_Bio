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
@Entity(tableName = "household_info")
public class HouseholdInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "male_total")
    public Integer maleTotal;

    @ColumnInfo(name = "female_total")
    public Integer femaleTotal;

    @ColumnInfo(name = "male_disable")
    public Integer maleDisable;

    @ColumnInfo(name = "male_chronically_ill")
    public Integer maleChronicalIll;
    @ColumnInfo(name = "male_normal")
    public Integer maleNormal;

    @ColumnInfo(name = "female_normal")
    public Integer femaleNormal;

    @ColumnInfo(name = "female_disable")
    public Integer femaleDisable;

    @ColumnInfo(name = "female_chronically_ill")
    public Integer femaleChronicalIll;

    @ColumnInfo(name = "type")
    public String type;

}
