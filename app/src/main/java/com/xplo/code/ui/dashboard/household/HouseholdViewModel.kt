package com.xplo.code.ui.dashboard.household

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.repo.DbRepo
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.toJson
import com.xplo.data.repo.UserRepo
import com.xplo.data.utils.DispatcherProvider
import com.xplo.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val dbRepo: DbRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    companion object {
        private const val TAG = "HouseholdViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class GetHouseholdItemSuccess(val items: List<HouseholdItem>?) : Event()
        class GetHouseholdItemFailure(val msg: String?) : Event()

        object SaveHouseholdFormSuccess : Event()
        class SaveHouseholdFormFailure(val msg: String?) : Event()

        object DeleteHouseholdItemSuccess : Event()
        class DeleteHouseholdItemFailure(val msg: String?) : Event()


    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun getHouseholdItems() {
        Log.d(TAG, "getHouseholdItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getHouseholds()) {

                is Resource.Success -> {
                    _event.value = Event.GetHouseholdItemSuccess(response.data)
                }

                is Resource.Failure -> {
                    _event.value = Event.GetHouseholdItemFailure(response.callInfo?.msg)
                }
            }
        }


    }

    fun deleteHouseholdItem(item: HouseholdItem) {
        Log.d(TAG, "deleteHouseholdItem() called with: item = $item")

        viewModelScope.launch(dispatchers.io) {
            //_event.value = Event.Loading
            when (val response = dbRepo.deleteHousehold(item)) {

                is Resource.Success -> {
                    _event.value = Event.DeleteHouseholdItemSuccess
                }

                is Resource.Failure -> {
                    _event.value = Event.DeleteHouseholdItemFailure(response.callInfo?.msg)
                }
            }
        }


    }

    fun saveHouseholdForm(form: HouseholdForm?) {
        Log.d(TAG, "saveHouseholdForm() called with: form = $form")
        if (form == null) return

        var item = HouseholdItem(data = form.toJson())

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.insertHousehold(item)) {

                is Resource.Success -> {
                    _event.value = Event.SaveHouseholdFormSuccess
                }

                is Resource.Failure -> {
                    _event.value = Event.SaveHouseholdFormFailure(response.callInfo?.msg)
                }

            }
        }


    }


}