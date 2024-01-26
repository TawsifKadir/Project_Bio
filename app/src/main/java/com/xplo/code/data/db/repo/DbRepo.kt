package com.xplo.code.data.db.repo

import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.data.utils.Resource

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

interface DbRepo {

    suspend fun getHouseholds(): Resource<List<HouseholdItem>>
    suspend fun insertHousehold(item: HouseholdItem): Resource<Unit>
    suspend fun deleteHousehold(item: HouseholdItem): Resource<Unit>

}