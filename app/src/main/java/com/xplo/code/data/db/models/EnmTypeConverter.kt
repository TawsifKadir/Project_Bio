package com.xplo.code.data.db.models

import androidx.room.TypeConverter
import com.kit.integrationmanager.model.BiometricType
import com.kit.integrationmanager.model.BiometricUserType
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.IncomeSourceEnum
import com.kit.integrationmanager.model.LegalStatusEnum
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.kit.integrationmanager.model.NoFingerprintReasonEnum
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

class EnumTypeConverters {


    @TypeConverter
    fun toGenderEnum(name: String?): GenderEnum? {
        if (name == null) return null
        return GenderEnum.valueOf(name)
    }

    @TypeConverter
    fun fromGenderEnum(enm: GenderEnum?): String? {
        return enm?.name
    }

    @TypeConverter
    fun toLegalStatusEnum(name: String?): LegalStatusEnum? {
        if (name == null) return null
        return LegalStatusEnum.valueOf(name)
    }

    @TypeConverter
    fun fromLegalStatusEnum(enm: LegalStatusEnum?): String? {
        return enm?.name
    }

    @TypeConverter
    fun toMaritalStatusEnum(name: String?): MaritalStatusEnum? {
        if (name == null) return null
        return MaritalStatusEnum.valueOf(name)
    }

    @TypeConverter
    fun fromMaritalStatusEnum(enm: MaritalStatusEnum?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toRelationshipEnum(name: String?): RelationshipEnum? {
        if (name == null) return null
        return RelationshipEnum.valueOf(name)
    }

    @TypeConverter
    fun fromRelationshipEnum(enm: RelationshipEnum?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toIncomeSourceEnum(name: String?): IncomeSourceEnum? {
        if (name == null) return null
        return IncomeSourceEnum.valueOf(name)
    }

    @TypeConverter
    fun fromIncomeSourceEnum(enm: IncomeSourceEnum?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toCurrencyEnum(name: String?): CurrencyEnum? {
        if (name == null) return null
        return CurrencyEnum.valueOf(name)
    }

    @TypeConverter
    fun fromCurrencyEnum(enm: CurrencyEnum?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toSelectionCriteriaEnum(name: String?): SelectionCriteriaEnum? {
        if (name == null) return null
        return SelectionCriteriaEnum.valueOf(name)
    }

    @TypeConverter
    fun fromSelectionCriteriaEnum(enm: SelectionCriteriaEnum?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toSelectionReasonEnum(name: String?): SelectionReasonEnum? {
        if (name == null) return null
        return SelectionReasonEnum.valueOf(name)
    }

    @TypeConverter
    fun fromSelectionReasonEnum(enm: SelectionReasonEnum?): String? {
        return enm?.name
    }


    @TypeConverter
    fun toNonParticipationReasonEnum(name: String?): NonPerticipationReasonEnum? {
        if (name == null) return null
        return NonPerticipationReasonEnum.valueOf(name)
    }

    @TypeConverter
    fun fromNonParticipationReasonEnum(enm: NonPerticipationReasonEnum?): String? {
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
    fun toNoFingerprintReasonEnum(name: String?): NoFingerprintReasonEnum? {
        if (name == null) return null
        return NoFingerprintReasonEnum.valueOf(name)
    }

    @TypeConverter
    fun fromNoFingerprintReasonEnum(enm: NoFingerprintReasonEnum?): String? {
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