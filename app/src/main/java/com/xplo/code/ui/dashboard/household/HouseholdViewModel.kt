package com.xplo.code.ui.dashboard.household

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kit.integrationmanager.model.ServerInfo
import com.kit.integrationmanager.model.SyncResult
import com.kit.integrationmanager.model.SyncStatus
import com.kit.integrationmanager.service.OnlineIntegrationManager
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.offline.Column
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.code.data.db.repo.DbRepo
import com.xplo.code.data.mapper.BeneficiaryMapper
import com.xplo.code.data.mapper.EntityMapper
import com.xplo.code.data.mapper.FormMapper
import com.xplo.code.network.fake.Fake
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.toJson
import com.xplo.code.utils.FormAppUtils
import com.xplo.data.core.DispatcherProvider
import com.xplo.data.core.Resource
import com.xplo.data.repo.ContentRepo
import com.xplo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Observable
import java.util.Observer
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val dbRepo: DbRepo,
    private val contentRepo: ContentRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel(), Observer {

    companion object {
        private const val TAG = "HouseholdViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class GetHouseholdItemSuccess(val item: HouseholdItem?) : Event()
        class GetHouseholdItemFailure(val msg: String?) : Event()

        class GetHouseholdItemsSuccess(val items: List<HouseholdItem>?) : Event()
        class GetHouseholdItemsFailure(val msg: String?) : Event()

        class SaveHouseholdFormSuccess(val id: String) : Event()
        class SaveHouseholdFormFailure(val msg: String?) : Event()

        class SaveBeneficiarySuccess(val item: BeneficiaryEntity) : Event()
        class SaveBeneficiaryFailure(val msg: String?) : Event()

        class SubmitHouseholdFormSuccess(val id: String, val pos: Int) : Event()
        class SubmitHouseholdFormFailure(val msg: String?) : Event()

        class SyncFormSuccess(val syncResult: SyncResult, val pos: Int) : Event()
        class SyncFormFailure(val msg: String?) : Event()


        class UpdateHouseholdFormSuccess(val id: String) : Event()
        class UpdateHouseholdFormFailure(val msg: String?) : Event()


        class UpdateHouseholdItemSuccess(val id: String?) : Event()
        class UpdateHouseholdItemFailure(val msg: String?) : Event()

        object DeleteHouseholdItemSuccess : Event()
        class DeleteHouseholdItemFailure(val msg: String?) : Event()

        class GetStateItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetStateItemsFailure(val msg: String?) : Event()

        class GetCountryItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetCountryItemsFailure(val msg: String?) : Event()

        class GetPayamItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetPayamItemsFailure(val msg: String?) : Event()

        class GetBomaItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetBomaItemsFailure(val msg: String?) : Event()


    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun clearEvent() {
        _event.value = Event.Empty
    }

    fun getHouseholdItem(id: String?) {
        Log.d(TAG, "getHouseholdItem() called with: id = $id")
        if (id == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getHousehold(id)) {

                is Resource.Success -> {
                    Log.d(TAG, "getHouseholdItem: success: ${response.data}")
                    _event.value = Event.GetHouseholdItemSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.GetHouseholdItemFailure(response.callInfo?.msg)
                }
            }
        }

    }

    fun getHouseholdItems() {
        Log.d(TAG, "getHouseholdItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getHouseholds()) {

                is Resource.Success -> {
                    Log.d(TAG, "getHouseholdItems: success: ${response.data?.size}")
                    _event.value = Event.GetHouseholdItemsSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getHouseholdItems: failure: ${response.callInfo}")
                    _event.value = Event.GetHouseholdItemsFailure(response.callInfo?.msg)
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
                    Log.d(TAG, "deleteHouseholdItem: success: ${response.data}")
                    _event.value = Event.DeleteHouseholdItemSuccess
                }

                is Resource.Failure -> {
                    Log.d(TAG, "deleteHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.DeleteHouseholdItemFailure(response.callInfo?.msg)
                }
            }
        }


    }

    fun saveHouseholdForm(form: HouseholdForm?) {
        Log.d(TAG, "saveHouseholdForm() called with: form = $form")
        if (form == null) return

        val uuid = UUID.randomUUID().toString()
        var item = HouseholdItem(data = form.toJson(), uuid = uuid)

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.insertHousehold(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "saveHouseholdForm: success: ${response.data}")
                    _event.value = Event.SaveHouseholdFormSuccess(uuid)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "saveHouseholdForm: failure: ${response.callInfo}")
                    _event.value = Event.SaveHouseholdFormFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    fun saveBeneficiary(item: BeneficiaryEntity?) {
        Log.d(TAG, "saveBeneficiary() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.insertBeneficiary(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "saveBeneficiary: success: ${response.data}")
                    _event.value = Event.SaveBeneficiarySuccess(item)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "saveBeneficiary: failure: ${response.callInfo}")
                    _event.value = Event.SaveBeneficiaryFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    fun submitHouseholdForm(form: HouseholdForm?, pos: Int) {
        Log.d(TAG, "saveHouseholdForm() called with: form = $form")
        if (form == null) return

        var item = FormMapper.toFormRqb(form)
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = contentRepo.submitForm(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "submitHouseholdForm: success: ${response.data}")

                    _event.value = Event.SubmitHouseholdFormSuccess(item.applicationId!!, pos)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "submitHouseholdForm: failure: ${response.callInfo}")
                    _event.value = Event.SubmitHouseholdFormFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    fun syncHouseholdForm(context: Context, form: HouseholdForm?, pos: Int) {
        Log.d(TAG, "saveHouseholdForm() called with: form = $form")
        if (form == null) return

        var item = FormMapper.toFormRqb(form)
        if (item == null) return


        val serverInfo = FormAppUtils.getServerInfo()
        val integrationManager = OnlineIntegrationManager(context, this, serverInfo)
        val headers = FormAppUtils.getHeaderForIntegrationManager()
        //val beneficiary = Fake.getABenificiary()
        val entity = EntityMapper.toBeneficiaryEntity(form)
        val beneficiary = BeneficiaryMapper.toBeneficiary(entity)
        integrationManager.syncRecord(beneficiary, headers)

//        viewModelScope.launch(dispatchers.io) {
//            //_event.value = Event.Loading
//            when (val response = contentRepo.submitBeneficiary(beneficiary)) {
//
//                is Resource.Success -> {
        //Log.d(TAG, "syncHouseholdForm: success: ${response.data}")
//                    Log.d(TAG, "syncHouseholdForm() called success")
//                    //_event.value = Event.SubmitHouseholdFormSuccess(item.applicationId!!, pos)
//                }
//
//                is Resource.Failure -> {
        //Log.d(TAG, "syncHouseholdForm: failure: ${response.callInfo}")
//                    Log.d(TAG, "syncHouseholdForm() called failure")
//                   // _event.value = Event.SubmitHouseholdFormFailure(response.callInfo?.msg)
//                }
//
//                else -> {}
//            }
//        }
    }


    fun updateHouseholdForm(form: HouseholdForm?) {
        Log.d(TAG, "saveHouseholdForm() called with: form = $form")
        if (form == null) return

        val uuid = UUID.randomUUID().toString()
        var item = HouseholdItem(data = form.toJson(), uuid = uuid)

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.updateHousehold(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "updateHouseholdForm: success: ${response.data}")
                    _event.value = Event.SaveHouseholdFormSuccess(uuid)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "updateHouseholdForm: failure: ${response.callInfo}")
                    _event.value = Event.SaveHouseholdFormFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    fun updateHouseholdItem(item: HouseholdItem?) {
        Log.d(TAG, "updateHouseholdItem() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.updateHousehold(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "updateHouseholdItem: success: ${response.data}")
                    _event.value = Event.UpdateHouseholdItemSuccess(item.uuid)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "updateHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.UpdateHouseholdItemFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    fun getStateItems() {
        Log.d(TAG, "getStateItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getOptionItems2(Column.s_code.name, Column.state.name, null, null)) {

                is Resource.Success -> {
                    Log.d(TAG, "getStateItems: success: ${response.data?.size}")
                    _event.value = Event.GetStateItemsSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getStateItems: failure: ${response.callInfo}")
                    _event.value = Event.GetStateItemsFailure(response.callInfo?.msg)
                }

            }
        }

    }


    fun getCountryItems(state: String?) {
        Log.d(TAG, "getCountryItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getOptionItems2(Column.c_code.name, Column.county.name, Column.state.name, state)) {

                is Resource.Success -> {
                    Log.d(TAG, "getCountryItems: success: ${response.data?.size}")
                    _event.value = Event.GetCountryItemsSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getCountryItems: failure: ${response.callInfo}")
                    _event.value = Event.GetCountryItemsFailure(response.callInfo?.msg)
                }

            }
        }

    }

    fun getPayamItems(country: String?) {

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getOptionItems2(Column.p_code.name, Column.payam.name, Column.county.name, country)) {

                is Resource.Success -> {
                    Log.d(TAG, "getPayamItems: success: ${response.data?.size}")
                    _event.value = Event.GetPayamItemsSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getPayamItems: failure: ${response.callInfo}")
                    _event.value = Event.GetPayamItemsFailure(response.callInfo?.msg)
                }

            }
        }

    }


    fun getBomaItems(payam: String?) {

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response =
                dbRepo.getOptionItems2(Column.b_code.name, Column.boma.name, Column.payam.name, payam)) {

                is Resource.Success -> {
                    Log.d(TAG, "getBomaItems: success: ${response.data?.size}")
                    _event.value = Event.GetBomaItemsSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getBomaItems: failure: ${response.callInfo}")
                    _event.value = Event.GetBomaItemsFailure(response.callInfo?.msg)
                }

            }
        }

    }

    override fun update(observable: Observable?, arg: Any?) {
        Log.d(TAG, "update() called with: observable = $observable, arg = $arg")
        if (arg == null) return

        val syncResult = arg as SyncResult?
        onGetSyncResult(syncResult)


    }

    private fun onGetSyncResult(syncResult: SyncResult?) {
        Log.d(TAG, "onGetSyncResult() called with: syncResult = ${syncResult?.syncStatus}")
        if (syncResult == null) return

        when (syncResult.syncStatus) {
            SyncStatus.SUCCESS -> onSyncSuccess(syncResult)
            else -> onSyncFailure(syncResult)
        }


    }

    private fun onSyncSuccess(syncResult: SyncResult) {
        Log.d(TAG, "onSyncSuccess() called with: syncResult = $syncResult")
        _event.value = Event.SyncFormSuccess(syncResult, -1)


    }

    private fun onSyncFailure(syncResult: SyncResult) {
        Log.d(TAG, "onSyncFailure() called with: syncResult = $syncResult")
        _event.value = Event.SyncFormFailure("sync failed")

    }


}