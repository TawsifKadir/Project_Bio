package com.xplo.code.data.mapper

import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.OccupationEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum

object FakeMapperValue {

    val currency  = CurrencyEnum.SUDANESE_POUND
    val isOtherMemberPerticipating = true
    val selectionReasons = listOf(
        SelectionReasonEnum.DIS_REASON_1,
        SelectionReasonEnum.DIS_REASON_2,
    )

}