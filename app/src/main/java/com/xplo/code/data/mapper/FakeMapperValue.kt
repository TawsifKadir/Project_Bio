package com.xplo.code.data.mapper

import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.OccupationEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum

object FakeMapperValue {

    val currency  = CurrencyEnum.SUDANESE_POUND
    val respondentId  = "12345678901234"
    val name  = "fake_data"
    val isOtherMemberPerticipating = true
    val fakePosition = 2
    val fakeGender = GenderEnum.MALE
    val selectionReasons = listOf(
        SelectionReasonEnum.DIS_REASON_1,
        SelectionReasonEnum.DIS_REASON_2,
    )

}