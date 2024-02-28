package com.xplo.code.data.mapper

import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.OccupationEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum

object FakeMapperValue {

    val documentType  = DocumentTypeEnum.NATIONAL_ID
    val occupation  = OccupationEnum.FORMAL_JOB
    val currency  = CurrencyEnum.SUDANESE_POUND
    val selectionCriteria  = SelectionCriteriaEnum.DIS

    val idNumber  = "12345678901234"
    val nationalId  = "12345678901234"
    val respondentId  = "12345678901234"
    val name  = "fake_data"
    val houseHoldSize = 10
    val selectionReasons = listOf(
        SelectionReasonEnum.DIS_REASON_1,
        SelectionReasonEnum.DIS_REASON_2,
    )

}