package com.xplo.code.data.db.repo

import com.xplo.code.data.db.dao.HouseholdDao
import com.xplo.code.data.db.dao.PostDao
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.data.utils.CallInfo
import com.xplo.data.utils.Resource
import javax.inject.Inject

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 6/2/20
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

class DbRepoImpl @Inject constructor(
    private val dao: PostDao,
    private val householdDao: HouseholdDao
) : DbRepo {

    private val TAG = "DbRepoImpl"


    override suspend fun getHouseholds(): Resource<List<HouseholdItem>> {
        return try {
            val response = householdDao.readAll()
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun insertHousehold(item: HouseholdItem): Resource<Unit> {
        return try {
            val response = householdDao.insert(item)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun deleteHousehold(item: HouseholdItem): Resource<Unit> {
        return try {
            val response = householdDao.delete(item)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

}