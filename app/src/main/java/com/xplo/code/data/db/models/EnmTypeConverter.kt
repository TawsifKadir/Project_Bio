package com.xplo.code.data.db.models

import androidx.room.TypeConverter
import com.xplo.code.ui.dashboard.enums.BiometricType
import com.xplo.code.ui.dashboard.enums.BiometricUserType
import com.xplo.code.ui.dashboard.enums.CurrencyEnm
import com.xplo.code.ui.dashboard.enums.GenderEnm
import com.xplo.code.ui.dashboard.enums.IncomeSourceEnm
import com.xplo.code.ui.dashboard.enums.LegalStatusEnm
import com.xplo.code.ui.dashboard.enums.MaritalStatusEnm
import com.xplo.code.ui.dashboard.enums.NoFingerprintReasonEnm
import com.xplo.code.ui.dashboard.enums.NonParticipationReasonEnm
import com.xplo.code.ui.dashboard.enums.RelationshipEnm
import com.xplo.code.ui.dashboard.enums.SelectionCriteriaEnm
import com.xplo.code.ui.dashboard.enums.SelectionReasonEnm

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

class EnmTypeConverters {


    @TypeConverter
    fun toGenderEnm(name: String?): GenderEnm? {
        if (name == null) return null
        return GenderEnm.valueOf(name)
    }

    @TypeConverter
    fun fromGenderEnm(enm: GenderEnm?): String? {
        return enm?.name
    }

    @TypeConverter
    fun toLegalStatusEnm(name: String?): LegalStatusEnm? {
        if (name == null) return null
        return LegalStatusEnm.valueOf(name)
    }

    @TypeConverter
    fun fromLegalStatusEnm(enm: LegalStatusEnm?): String? {
        return enm?.name
    }

    @TypeConverter
    fun toMaritalStatusEnm(name: String?): MaritalStatusEnm? {
        if (name == null) return null
        return MaritalStatusEnm.valueOf(name)
    }

    @TypeConverter
    fun fromMaritalStatusEnm(enm: MaritalStatusEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toRelationshipEnm(name: String?): RelationshipEnm? {
        if (name == null) return null
        return RelationshipEnm.valueOf(name)
    }

    @TypeConverter
    fun fromRelationshipEnm(enm: RelationshipEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toIncomeSourceEnm(name: String?): IncomeSourceEnm? {
        if (name == null) return null
        return IncomeSourceEnm.valueOf(name)
    }

    @TypeConverter
    fun fromIncomeSourceEnm(enm: IncomeSourceEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toCurrencyEnm(name: String?): CurrencyEnm? {
        if (name == null) return null
        return CurrencyEnm.valueOf(name)
    }

    @TypeConverter
    fun fromCurrencyEnm(enm: CurrencyEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toSelectionCriteriaEnm(name: String?): SelectionCriteriaEnm? {
        if (name == null) return null
        return SelectionCriteriaEnm.valueOf(name)
    }

    @TypeConverter
    fun fromSelectionCriteriaEnm(enm: SelectionCriteriaEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toSelectionReasonEnm(name: String?): SelectionReasonEnm? {
        if (name == null) return null
        return SelectionReasonEnm.valueOf(name)
    }

    @TypeConverter
    fun fromSelectionReasonEnm(enm: SelectionReasonEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toNonParticipationReasonEnm(name: String?): NonParticipationReasonEnm? {
        if (name == null) return null
        return NonParticipationReasonEnm.valueOf(name)
    }

    @TypeConverter
    fun fromNonParticipationReasonEnm(enm: NonParticipationReasonEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toBiometricType(name: String?): BiometricType? {
        if (name == null) return null
        return BiometricType.valueOf(name)
    }

    @TypeConverter
    fun fromBiometricType(enm: BiometricType?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toNoFingerprintReasonEnm(name: String?): NoFingerprintReasonEnm? {
        if (name == null) return null
        return NoFingerprintReasonEnm.valueOf(name)
    }

    @TypeConverter
    fun fromNoFingerprintReasonEnm(enm: NoFingerprintReasonEnm?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toBiometricUserType(name: String?): BiometricUserType? {
        if (name == null) return null
        return BiometricUserType.valueOf(name)
    }

    @TypeConverter
    fun fromBiometricUserType(enm: BiometricUserType?): String? {
        return enm?.name
    }


}