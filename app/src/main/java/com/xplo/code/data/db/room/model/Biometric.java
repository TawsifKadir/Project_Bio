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
@Entity(tableName = "biometric")
public class Biometric implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name = "application_id")
    public String applicationId;

    @ColumnInfo(name = "biometric_user_type")
    public Long biometricUserType;

    @ColumnInfo(name = "no_finger_print")
    public boolean noFingerPrint;

    @ColumnInfo(name = "no_finger_print_reason")
    public Long noFingerprintReason;

    @ColumnInfo(name = "no_finger_print_reason_text")
    public String noFingerprintReasonText;

    @ColumnInfo(name = "wsq_lt")
    public byte[] wsqLt;

    @ColumnInfo(name = "wsq_li")
    public byte[] wsqLi;

    @ColumnInfo(name = "wsq_lm")
    public byte[] wsqLm;

    @ColumnInfo(name = "wsq_lr")
    public byte[] wsqLr;

    @ColumnInfo(name = "wsq_ls")
    public byte[] wsqLs;

    @ColumnInfo(name = "wsq_rt")
    public byte[] wsqRt;

    @ColumnInfo(name = "wsq_ri")
    public byte[] wsqRi;

    @ColumnInfo(name = "wsq_rm")
    public byte[] wsqRm;

    @ColumnInfo(name = "wsq_rr")
    public byte[] wsqRr;

    @ColumnInfo(name = "wsq_rs")
    public byte[] wsqRs;

    @ColumnInfo(name = "photo")
    public byte[] photo;
}
