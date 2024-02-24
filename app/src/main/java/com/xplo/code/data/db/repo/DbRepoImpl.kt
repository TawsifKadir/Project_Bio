package com.xplo.code.data.db.repo

import com.xplo.code.data.db.dao.BeneficiaryDao
import com.xplo.code.data.db.dao.HouseholdDao
import com.xplo.code.data.db.dao.PostDao
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.offline.DbCallImpl
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.data.core.CallInfo
import com.xplo.data.core.Resource
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
    private val householdDao: HouseholdDao,
    private val beneficiaryDao: BeneficiaryDao,
) : DbRepo {

    private val TAG = "DbRepoImpl"

    override suspend fun getHousehold(id: String): Resource<HouseholdItem> {
        return try {
            val response = householdDao.readByUuid(id)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun getHouseholds(): Resource<List<HouseholdItem>> {
        return try {
            val response = householdDao.readAll()
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun updateHousehold(item: HouseholdItem): Resource<Unit> {
        return try {
            val response = householdDao.update(item)
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

    override suspend fun getBeneficiary(id: String): Resource<BeneficiaryEntity> {
        return try {
            val response = beneficiaryDao.readByUuid(id)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun getBeneficiaryItems(): Resource<List<BeneficiaryEntity>> {
        return try {
            val response = beneficiaryDao.readAll()
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun insertBeneficiary(item: BeneficiaryEntity): Resource<Unit> {
        return try {
            val response = beneficiaryDao.insert(item)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun updateBeneficiary(item: BeneficiaryEntity): Resource<Unit> {
        return try {
            val response = beneficiaryDao.update(item)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun deleteBeneficiary(item: BeneficiaryEntity): Resource<Unit> {
        return try {
            val response = beneficiaryDao.delete(item)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }
    }

    override suspend fun getOptionItems(
        column: String,
        argColName: String?,
        argColValue: String?
    ): Resource<List<OptionItem>> {

        return try {
            val response = DbCallImpl().getOptionItems(getTable(), column, argColName, argColValue)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }

    }

    override suspend fun getOptionItems2(
        columnCode: String,
        columnTitle: String,
        argColName: String?,
        argColValue: String?
    ): Resource<List<OptionItem>> {

        return try {
            val response = DbCallImpl().getOptionItems2(getTable(), columnCode, columnTitle, argColName, argColValue)
            Resource.Success(response, null)
        } catch (e: Exception) {
            Resource.Failure(CallInfo(-1, e.message))
        }

    }


    private fun getTable(): String {
        return "state"
    }

}