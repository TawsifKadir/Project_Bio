package com.xplo.code.ui.dashboard.household

import android.content.Context
import android.content.SyncResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.payload.RegistrationResult
import com.kit.integrationmanager.payload.RegistrationStatus
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.models.toHouseholdForm
import com.xplo.code.data.db.offline.Column
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.code.data.db.repo.DbRepo
import com.xplo.code.data.mapper.BeneficiaryMapper
import com.xplo.code.data.mapper.EntityMapper
import com.xplo.code.ui.dashboard.DashboardFragment
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.toJson
import com.xplo.code.utils.IMHelper
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
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val dbRepo: DbRepo,
    private val contentRepo: ContentRepo,
    private val dispatchers: DispatcherProvider
) : ViewModel(), HouseholdContract.Presenter, Observer {


    companion object {
        private const val TAG = "HouseholdViewModel"
    }

    sealed class Event {

        object Loading : Event()
        object Empty : Event()

        class SaveFormPEntitySuccess(val id: String?) : Event()
        class SaveFormPEntityFailure(val msg: String?) : Event()

        // household form
        class SaveHouseholdFormSuccess(val id: String?) : Event()
        class SaveHouseholdFormFailure(val msg: String?) : Event()

        class GetHouseholdItemSuccess(val item: HouseholdItem?) : Event()
        class GetHouseholdItemFailure(val msg: String?) : Event()

        class GetHouseholdItemsSuccess(val items: List<HouseholdItem>?) : Event()
        class GetHouseholdItemsFailure(val msg: String?) : Event()

        class UpdateHouseholdItemSuccess(val id: String?) : Event()
        class UpdateHouseholdItemFailure(val msg: String?) : Event()

        class DeleteHouseholdItemSuccess(val id: String?) : Event()
        class DeleteHouseholdItemFailure(val msg: String?) : Event()

        class SendHouseholdItemSuccess(val item: HouseholdItem?, val pos: Int) : Event()
        class SendHouseholdItemFailure(val msg: String?, val pos: Int) : Event()

        class SendHouseholdFormSuccess(val id: String?, val pos: Int) : Event()
        class SendHouseholdFormFailure(val msg: String?, val pos: Int) : Event()

        // beneficiary entity
        class SaveBeneficiaryEntitySuccess(val id: String?) : Event()
        class SaveBeneficiaryEntityFailure(val msg: String?) : Event()

        class GetBeneficiaryEntitySuccess(val item: BeneficiaryEntity?) : Event()
        class GetBeneficiaryEntityFailure(val msg: String?) : Event()

        class GetBeneficiaryEntityItemsSuccess(val items: List<BeneficiaryEntity>?) : Event()
        class GetBeneficiaryEntityItemsFailure(val msg: String?) : Event()

        class UpdateBeneficiaryEntitySuccess(val id: String?) : Event()
        class UpdateBeneficiaryEntityFailure(val msg: String?) : Event()

        class DeleteBeneficiaryEntitySuccess(val id: String?) : Event()
        class DeleteBeneficiaryEntityFailure(val msg: String?) : Event()

        class SendBeneficiaryEntitySuccess(val id: String?, val pos: Int) : Event()
        class SendBeneficiaryEntityFailure(val msg: String?) : Event()

        class SendBeneficiarySuccess(val id: String?, val pos: Int) : Event()
        class SendBeneficiaryFailure(val msg: String?) : Event()

        // spinner
        class GetStateItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetStateItemsFailure(val msg: String?) : Event()

        class GetCountryItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetCountryItemsFailure(val msg: String?) : Event()

        class GetPayamItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetPayamItemsFailure(val msg: String?) : Event()

        class GetBomaItemsSuccess(val items: List<OptionItem>?) : Event()
        class GetBomaItemsFailure(val msg: String?) : Event()

        // sync result
        class SyncFormSuccess(val syncResult: SyncResult, val pos: Int) : Event()
        class SyncFormFailure(val msg: String?) : Event()


    }

    private val _event = MutableStateFlow<Event>(Event.Empty)
    val event: StateFlow<Event> = _event

    fun clearEvent() {
        _event.value = Event.Empty
    }

    override fun saveFormPEntity(form: HouseholdForm?) {
        Log.d(TAG, "saveFormPEntity() called with: form = $form")
        if (form == null) return

//        val uuid = UUID.randomUUID().toString()
//        val hid = HIDGenerator.getHID()
        var item = HouseholdItem(data = form.toJson(), id = form.id, hid = form.hid)

        var entity = EntityMapper.toBeneficiaryEntity(form)
        if (entity == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading

            when (val response = dbRepo.insertFormPEntity(item, entity)) {

                is Resource.Success -> {
                    Log.d(TAG, "saveFormPEntity:1 success: ${response.data}")
                    _event.value = Event.SaveFormPEntitySuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "saveFormPEntity:1 failure: ${response.callInfo}")
                    _event.value = Event.SaveFormPEntityFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }


    override fun saveHouseholdFormAsHouseholdItem(form: HouseholdForm?) {
        Log.d(TAG, "saveHouseholdFormAsHouseholdItem() called with: form = $form")
        if (form == null) return

//        val uuid = UUID.randomUUID().toString()
//        val hid = HIDGenerator.getHID()
        var item = HouseholdItem(data = form.toJson(), id = form.id, hid = form.hid)

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.insertHousehold(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "saveHouseholdFormAsHouseholdItem: success: ${response.data}")
                    _event.value = Event.SaveHouseholdFormSuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "saveHouseholdFormAsHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.SaveHouseholdFormFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    override fun getHouseholdItem(id: String?) {
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

    override fun getHouseholdItems() {

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

    override fun updateHouseholdItem(item: HouseholdItem?) {

        Log.d(TAG, "updateHouseholdItem() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.updateHousehold(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "updateHouseholdItem: success: ${response.data}")
                    _event.value = Event.UpdateHouseholdItemSuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "updateHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.UpdateHouseholdItemFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    override fun deleteHouseholdItem(item: HouseholdItem?) {
        Log.d(TAG, "deleteHouseholdItem() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            //_event.value = Event.Loading
            when (val response = dbRepo.deleteHousehold(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "deleteHouseholdItem: success: ${response.data}")
                    _event.value = Event.DeleteHouseholdItemSuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "deleteHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.DeleteHouseholdItemFailure(response.callInfo?.msg)
                }
            }
        }


    }

    override fun sendHouseholdItem(item: HouseholdItem?, pos: Int) {
        Log.d(TAG, "sendHouseholdItem() called with: item = $item, pos = $pos")
        if (item == null) return
        val form = item.toHouseholdForm()
        var entity = EntityMapper.toBeneficiaryEntity(form)
        var beneficiary = BeneficiaryMapper.toBeneficiary(entity)
        if (beneficiary == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = contentRepo.sendBeneficiary(beneficiary)) {

                is Resource.Success -> {
                    Log.d(TAG, "sendHouseholdItem: success: ${response.data}")

                    _event.value = Event.SendHouseholdItemSuccess(item, pos)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "sendHouseholdItem: failure: ${response.callInfo}")
                    _event.value = Event.SendHouseholdItemFailure(response.callInfo?.msg, pos)
                }

                else -> {}
            }
        }
    }

    override fun sendHouseholdForm(form: HouseholdForm?, pos: Int) {
        Log.d(TAG, "sendHouseholdForm() called with: form = $form")
        if (form == null) return

        var entity = EntityMapper.toBeneficiaryEntity(form)
        var item = BeneficiaryMapper.toBeneficiary(entity)
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = contentRepo.sendBeneficiary(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "sendHouseholdForm: success: ${response.data}")

                    _event.value = Event.SendHouseholdFormSuccess(item.applicationId, pos)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "sendHouseholdForm: failure: ${response.callInfo}")
                    _event.value = Event.SendHouseholdFormFailure(response.callInfo?.msg, pos)
                }

                else -> {}
            }
        }
    }

    override fun saveBeneficiaryEntity(item: BeneficiaryEntity?) {
        Log.d(TAG, "saveBeneficiaryEntity() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.insertBeneficiary(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "saveBeneficiaryEntity: success: ${response.data}")
                    _event.value = Event.SaveBeneficiaryEntitySuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "saveBeneficiaryEntity: failure: ${response.callInfo}")
                    _event.value = Event.SaveBeneficiaryEntityFailure(response.callInfo?.msg)
                }

                else -> {}
            }
        }
    }

    override fun getBeneficiaryEntity(id: String?) {
        Log.d(TAG, "getBeneficiaryEntity() called with: id = $id")
        if (id.isNullOrEmpty()) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getBeneficiary(id)) {

                is Resource.Success -> {
                    Log.d(TAG, "getBeneficiaryEntity: success: ${response.data}")
                    _event.value = Event.GetBeneficiaryEntitySuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getBeneficiaryEntity: failure: ${response.callInfo}")
                    _event.value = Event.GetBeneficiaryEntityFailure(response.callInfo?.msg)
                }
            }
        }
    }

    override fun getBeneficiaryEntityItems() {
        Log.d(TAG, "getBeneficiaryEntityItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getBeneficiaryItems()) {

                is Resource.Success -> {
                    Log.d(TAG, "getBeneficiaryEntity: success: ${response.data}")
                    _event.value = Event.GetBeneficiaryEntityItemsSuccess(response.data)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "getBeneficiaryEntity: failure: ${response.callInfo}")
                    _event.value = Event.GetBeneficiaryEntityItemsFailure(response.callInfo?.msg)
                }
            }
        }
    }

    override fun updateBeneficiaryEntity(item: BeneficiaryEntity?) {
        Log.d(TAG, "updateBeneficiaryEntity() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.updateBeneficiary(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "updateBeneficiaryEntity: success: ${response.data}")
                    _event.value = Event.UpdateBeneficiaryEntitySuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "updateBeneficiaryEntity: failure: ${response.callInfo}")
                    _event.value = Event.UpdateBeneficiaryEntityFailure(response.callInfo?.msg)
                }
            }
        }

    }

    override fun deleteBeneficiaryEntity(item: BeneficiaryEntity?) {
        Log.d(TAG, "deleteBeneficiaryEntity() called with: item = $item")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.deleteBeneficiary(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "deleteBeneficiaryEntity: success: ${response.data}")
                    _event.value = Event.DeleteBeneficiaryEntitySuccess(item.id)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "deleteBeneficiaryEntity: failure: ${response.callInfo}")
                    _event.value = Event.DeleteBeneficiaryEntityFailure(response.callInfo?.msg)
                }
            }
        }
    }

    override fun sendBeneficiaryEntity(item: BeneficiaryEntity?, pos: Int) {
        Log.d(TAG, "sendBeneficiaryEntity() called with: item = $item, pos = $pos")
        if (item == null) return

        val beneficiary = BeneficiaryMapper.toBeneficiary(item)

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = contentRepo.sendBeneficiary(beneficiary)) {

                is Resource.Success -> {
                    Log.d(TAG, "sendBeneficiaryEntity: success: ${response.data}")
                    _event.value = Event.SendBeneficiaryEntitySuccess(item.id, pos)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "sendBeneficiaryEntity: failure: ${response.callInfo}")
                    _event.value = Event.SendBeneficiaryEntityFailure(response.callInfo?.msg)
                }
            }
        }
    }


    override fun sendBeneficiary(item: Beneficiary?, pos: Int) {
        Log.d(TAG, "sendBeneficiary() called with: item = $item, pos = $pos")
        if (item == null) return

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = contentRepo.sendBeneficiary(item)) {

                is Resource.Success -> {
                    Log.d(TAG, "sendBeneficiary: success: ${response.data}")
                    _event.value = Event.SendBeneficiarySuccess(item.applicationId, pos)
                }

                is Resource.Failure -> {
                    Log.d(TAG, "sendBeneficiary: failure: ${response.callInfo}")
                    _event.value = Event.SendBeneficiaryFailure(response.callInfo?.msg)
                }
            }
        }
    }


    override fun getStateItems() {
        Log.d(TAG, "getStateItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response =
                dbRepo.getOptionItems2(Column.s_code.name, Column.state.name, null, null)) {

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

    override fun getCountryItems(state: String?) {
        Log.d(TAG, "getCountryItems() called")

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getOptionItems2(
                Column.c_code.name,
                Column.county.name,
                Column.state.name,
                state
            )) {

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

    override fun getPayamItems(country: String?) {

        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response = dbRepo.getOptionItems2(
                Column.p_code.name,
                Column.payam.name,
                Column.county.name,
                country
            )) {

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


    override fun getBomaItems(payam: String?) {


        viewModelScope.launch(dispatchers.io) {
            _event.value = Event.Loading
            when (val response =
                dbRepo.getOptionItems2(
                    Column.b_code.name,
                    Column.boma.name,
                    Column.payam.name,
                    payam
                )) {

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


    override fun syncHouseholdForm(context: Context, form: HouseholdForm?, pos: Int) {
        Log.d(TAG, "syncHouseholdForm() called with: context = $context, form = $form, pos = $pos")
        if (form == null) return


        //val beneficiary = Fake.getABenificiary()
        val entity = EntityMapper.toBeneficiaryEntity(form)
        val beneficiary = BeneficiaryMapper.toBeneficiary(entity)

        val integrationManager = IMHelper.getIntegrationManager(context, this)
        val header = IMHelper.getHeader()
        integrationManager.syncRecord(beneficiary, header)

        //sendBeneficiary(beneficiary, pos)

    }

    override fun syncBeneficiaryEntity(context: Context, entity: BeneficiaryEntity?, pos: Int) {
        Log.d(
            TAG,
            "syncBeneficiaryEntity() called with: context = $context, entity = $entity, pos = $pos"
        )
        if (entity == null) return

        //val beneficiary = Fake.getABenificiary()
        val beneficiary = BeneficiaryMapper.toBeneficiary(entity)

        val integrationManager = IMHelper.getIntegrationManager(context, this)
        val header = IMHelper.getHeader()
        integrationManager.syncRecord(beneficiary, header)

        //sendBeneficiary(beneficiary, pos)

    }


    override fun syncBeneficiary(context: Context, beneficiary: Beneficiary?, pos: Int) {
        Log.d(
            TAG,
            "syncBeneficiary() called with: context = $context, beneficiary = $beneficiary, pos = $pos"
        )
        if (beneficiary == null) return

        //val beneficiary = Fake.getABenificiary()

        val integrationManager = IMHelper.getIntegrationManager(context, this)
        val header = IMHelper.getHeader()
        integrationManager.syncRecord(beneficiary, header)

        //sendBeneficiary(beneficiary, pos)


    }

    override fun update(observable: Observable?, arg: Any?) {
        Log.d(TAG, "update() called with: observable = $observable, arg = $arg")
        if (arg == null) return

        val syncResult = arg as RegistrationResult?
        onGetSyncResult(syncResult)


    }

    private fun onGetSyncResult(arg: RegistrationResult?) {
//        Log.d(TAG, "onGetSyncResult() called with: syncResult = ${syncResult?.syncStatus}")
//        if (syncResult == null) return
//
//        when (syncResult.syncStatus) {
//            SyncStatus.SUCCESS -> onSyncSuccess(syncResult)
//            else -> onSyncFailure(syncResult)

        try {
            Log.d(DashboardFragment.TAG, "Received update>>>>")
            if (arg == null) {
                Log.d(DashboardFragment.TAG, "Received null parameter in update. Returning...")
                return
            } else {
                Log.d(DashboardFragment.TAG, "Received parameter in update.")
                val registrationResult = arg as? RegistrationResult
                if (registrationResult?.syncStatus == RegistrationStatus.SUCCESS) {
                    Log.d(DashboardFragment.TAG, "Registration Successful")

                    val appIds = registrationResult.applicationIds
                    if (appIds == null) {
                        Log.e(DashboardFragment.TAG, "No beneficiary list received. Returning ... ")
                        return
                    }

                    Log.d(DashboardFragment.TAG, "Registered following users: ")
                    for (nowId in appIds) {
                        Log.d(DashboardFragment.TAG, "Beneficiary ID : $nowId")
                    }
                } else {
                    Log.d(DashboardFragment.TAG, "Registration Failed")
                    Log.d(DashboardFragment.TAG, "Error code : ${registrationResult?.syncStatus?.errorCode}")
                    Log.d(DashboardFragment.TAG, "Error Msg : ${registrationResult?.syncStatus?.errorMsg}")
                }
            }
        } catch (exc: Exception) {
            Log.e(DashboardFragment.TAG, "Error while processing update : ${exc.message}")
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