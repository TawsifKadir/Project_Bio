package com.xplo.code.ui.dashboard.report

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xplo.code.data.db.room.dao.BeneficiaryDao
import com.xplo.code.data.db.room.dao.SyncBeneficiaryDao
import com.xplo.code.data.db.room.database.BeneficiaryDatabase
import com.xplo.code.data.db.room.model.SyncBeneficiary
import com.xplo.code.data_module.core.DispatcherProvider
import com.xplo.code.data_module.model.content.ContentItem
import com.xplo.code.data_module.repo.UserRepo
import com.xplo.code.ui.dashboard.household.HouseholdViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repo: UserRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "ReportViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class GetItemSuccess(val items: List<ContentItem>) : Event()
        class GetItemFailure(val msg: String?) : Event()

        class UpdateFavoriteSuccess(val position: Int) : Event()
        class UpdateFavoriteFailure(val msg: String?) : Event()

        class GetSyncDataLocalDb(val beneficiary: MutableList<SyncBeneficiary>) :
            Event()

    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun getFavorites(offset: Int, limit: Int) {

//        viewModelScope.launch(dispatchers.io) {
//            _event.value = Event.Loading
//            when (val response = repo.getFavorites(offset, limit)) {
//
//                is Resource.Failure -> {
//                    _event.value = Event.GetItemFailure(response.callInfo?.msg)
//                }
//
//                is Resource.Success -> {
//                    //_data.value = Event.Success(response.data?.items)
//                    val items = response.data?.items
//                    if (items.isNullOrEmpty()) {
//                        _event.value = Event.GetItemFailure(response.callInfo?.msg)
//                    } else {
//                        _event.value = Event.GetItemSuccess(items)
//                    }
//                }
//            }
//        }
//

    }

    fun showBeneficiary(context: Context) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context)

        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: SyncBeneficiaryDao = mDatabase.syncBeneficiaryDao()

            val beneficiaries = beneficiaryDao.allSyncBeneficiaries

            _event.value = Event.GetSyncDataLocalDb(beneficiaries)
        }

        // Note: No need to close the database here since you're using Room, which manages database connections automatically.
    }


}