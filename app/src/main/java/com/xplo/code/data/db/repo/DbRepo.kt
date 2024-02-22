package com.xplo.code.data.db.repo

import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.data.core.Resource

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

interface DbRepo {

    suspend fun getHousehold(id: String): Resource<HouseholdItem>
    suspend fun getHouseholds(): Resource<List<HouseholdItem>>
    suspend fun insertHousehold(item: HouseholdItem): Resource<Unit>
    suspend fun updateHousehold(item: HouseholdItem): Resource<Unit>
    suspend fun deleteHousehold(item: HouseholdItem): Resource<Unit>

    suspend fun getOptionItems(
        column: String,
        argColName: String?,
        argColValue: String?
    ): Resource<List<OptionItem>>
}