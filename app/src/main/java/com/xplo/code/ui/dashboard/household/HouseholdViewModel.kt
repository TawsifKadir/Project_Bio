package com.xplo.code.ui.dashboard.household

import android.content.Context
import android.content.SyncResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kit.integrationmanager.model.AlternatePayee
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.model.BiometricType
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.HouseholdMember
import com.kit.integrationmanager.model.IncomeSourceEnum
import com.kit.integrationmanager.model.LegalStatusEnum
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum
import com.kit.integrationmanager.payload.RegistrationResult
import com.kit.integrationmanager.payload.RegistrationStatus
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.HouseholdItem
import com.xplo.code.data.db.offline.Column
import com.xplo.code.data.db.offline.OptionItem
import com.xplo.code.data.db.repo.DbRepo
import com.xplo.code.data.db.room.dao.AddressDao
import com.xplo.code.data.db.room.dao.AlternateDao
import com.xplo.code.data.db.room.dao.BeneficiaryDao
import com.xplo.code.data.db.room.dao.BeneficiaryTransactionDao
import com.xplo.code.data.db.room.dao.BiometricDao
import com.xplo.code.data.db.room.dao.HouseholdInfoDao
import com.xplo.code.data.db.room.dao.LocationDao
import com.xplo.code.data.db.room.dao.NomineeDao
import com.xplo.code.data.db.room.dao.SelectionReasonDao
import com.xplo.code.data.db.room.database.BeneficiaryDatabase
import com.xplo.code.data.db.room.database.DatabaseExecutors
import com.xplo.code.data.db.room.model.Alternate
import com.xplo.code.data.db.room.model.Biometric
import com.xplo.code.data.db.room.model.HouseholdInfo
import com.xplo.code.data.db.room.model.Nominee
import com.xplo.code.data.db.room.model.SelectionReason
import com.xplo.code.data.db.room.model.SyncBeneficiary
import com.xplo.code.data.mapper.BeneficiaryMapper
import com.xplo.code.data.mapper.EntityMapper
import com.xplo.code.data_module.core.DispatcherProvider
import com.xplo.code.data_module.core.Resource
import com.xplo.code.data_module.model.content.Address
import com.xplo.code.data_module.model.content.Location
import com.xplo.code.data_module.repo.UserRepo
import com.xplo.code.ui.dashboard.DashboardFragment
import com.xplo.code.ui.dashboard.household.forms.HhPreviewFragment
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.toJson
import com.xplo.code.utils.DialogUtil
import com.xplo.code.utils.IMHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Observable
import java.util.Observer
import javax.inject.Inject

@HiltViewModel
class HouseholdViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val dbRepo: DbRepo,
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
        class GetHouseholdItemsSuccessMsg(val msg: String?, val appId: MutableList<String>) :
            Event()

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
        class GetDataLocalDb(val beneficiary: MutableList<com.xplo.code.data.db.room.model.Beneficiary>) :
            Event()

        class UpdateDataLocalDb(val msg: Boolean) :
            Event()

        class InsertDataLocalDb(val msg: String) :
            Event()


        class GetDataLocalDbByAppId(val beneficiary: Beneficiary) :
            Event()

        class GetDataLocalDbForBulk(val beneficiaryList: ArrayList<Beneficiary>) :
            Event()

        class GetDataLocalDbByAppIdForView(val beneficiaryViewDetails: Beneficiary) :
            Event()

        class DeleteDataLocalDbByAppId(val beneficiary: Boolean) : Event()

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
        val item = HouseholdItem(data = form.toJson(), id = form.id, hid = form.hid)

        val entity = EntityMapper.toBeneficiaryEntity(form)
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
        val item = HouseholdItem(data = form.toJson(), id = form.id, hid = form.hid)

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

    fun deleteBeneficiary(context: Context, appId: String) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context);
        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()
            val beneficiary = beneficiaryDao.getBeneficiaryByAppId(appId)
            val deleteBene = beneficiaryDao.deleteBeneficiary(beneficiary)

            val alternateDao: AlternateDao = mDatabase.alternateDao()
            alternateDao.deleteAlternate(appId)

            val addressDao: AddressDao = mDatabase.addressDao()
            addressDao.deleteAddreesByAppId(appId)

            val nomineeDao: NomineeDao = mDatabase.nomineeDao()
            nomineeDao.deleteNomineeByAppid(appId)

            val locationDao: LocationDao = mDatabase.locationDao()
            locationDao.deleteLocationByAppId(appId)

            val householdInfoDao: HouseholdInfoDao = mDatabase.householdInfoDao()
            householdInfoDao.deleteHouseholdByAppid(appId)

            val biometricDao: BiometricDao = mDatabase.biometricDao()
            biometricDao.deleteBiomatricByAppId(appId)

            val selectionReasonDao: SelectionReasonDao = mDatabase.selectionReasonDao()
            selectionReasonDao.deleteReasonByAppId(appId)
            _event.value = Event.DeleteDataLocalDbByAppId(true)
        }
    }

    //MappingApi - Prepare for Api call
    fun showBeneficiaryByAppId(context: Context, appId: String) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context)
        val form: Beneficiary = Beneficiary()
        //_event.value = Event.Loading
        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()
            val alternateDao: AlternateDao = mDatabase.alternateDao()
            val addressDao: AddressDao = mDatabase.addressDao()
            val nomineeDao: NomineeDao = mDatabase.nomineeDao()
            val locationDao: LocationDao = mDatabase.locationDao()
            val householdInfoDao: HouseholdInfoDao = mDatabase.householdInfoDao()
            val biometricDao: BiometricDao = mDatabase.biometricDao()
            val selectionReasonDao: SelectionReasonDao = mDatabase.selectionReasonDao()
            val beneficiary = beneficiaryDao.getBeneficiaryByAppId(appId)
            val address = addressDao.getAddressByAppId(appId)
            val location = locationDao.getLocationByAppId(appId)
            val nominee = nomineeDao.getNomineeListByAppId(appId)
            val householdInfo = householdInfoDao.getHouseholdInfoListByAppId(appId)
            val alternateEO = alternateDao.getAlternateList(appId)
            val selectionReason = selectionReasonDao.getSelectionReasonByAppId(appId)
            val biometricBio = biometricDao.getBiometricsListByAppIdForBenaficiary(appId)
            val alternateBio1 = biometricDao.getBiometricsListByAppIdForAlternate1(appId)
            val alternateBio2 = biometricDao.getBiometricsListByAppIdForAlternate2(appId)

            // Data Bind With Api Obj
            form.applicationId = beneficiary.applicationId
            form.respondentFirstName = beneficiary.respondentFirstName
            form.respondentMiddleName = beneficiary.respondentMiddleName
            form.respondentLastName = beneficiary.respondentLastName
            form.respondentNickName = beneficiary.respondentNickName
            form.spouseFirstName = beneficiary.spouseFirstName
            form.spouseMiddleName = beneficiary.spouseMiddleName
            form.spouseLastName = beneficiary.spouseLastName
            form.spouseNickName = beneficiary.spouseNickName
            form.incomeSourceOther = beneficiary.incomeSourceOther
            form.relationshipOther = beneficiary.relationshipOther
            form.documentTypeOther = beneficiary.documentTypeOther
            form.householdSize = beneficiary.householdSize

            if (beneficiary.relationshipWithHouseholdHead != null) {
                form.relationshipWithHouseholdHead =
                    RelationshipEnum.getRelationById(beneficiary.relationshipWithHouseholdHead.toInt() + 1)
            }

            form.respondentAge = beneficiary.respondentAge

            if (beneficiary.respondentGender != null) {
                form.respondentGender =
                    GenderEnum.getGenderById(beneficiary.respondentGender.toInt() + 1)
            }

            if (beneficiary.respondentMaritalStatus != null) {
                form.respondentMaritalStatus =
                    MaritalStatusEnum.getMartialStatusById(beneficiary.respondentMaritalStatus.toInt() + 1)
            }

            if (beneficiary.respondentLegalStatus != null) {
                form.respondentLegalStatus =
                    LegalStatusEnum.getLegalStatusById(beneficiary.respondentLegalStatus.toInt() + 1)
            }
            if (beneficiary.documentType != null) {
                form.documentType =
                    DocumentTypeEnum.getDocumentTypeById(beneficiary.documentType.toInt() + 1)
            }

            if (form.documentType == DocumentTypeEnum.PASSPORT || form.documentType == DocumentTypeEnum.NATIONAL_ID) {
                form.respondentId = beneficiary.respondentId
            } else if (form.documentType == DocumentTypeEnum.OTHER) {
                form.documentTypeOther = beneficiary.documentTypeOther
                form.respondentId = beneficiary.respondentId
            } else {
                form.documentTypeOther = null
            }

            if (beneficiary.respondentPhoneNo.isNullOrEmpty() || beneficiary.respondentPhoneNo.length < 10) {
                form.respondentPhoneNo = null
            } else {
                form.respondentPhoneNo = beneficiary.respondentPhoneNo
            }

            if (beneficiary.householdIncomeSource != null) {
                form.householdIncomeSource =
                    IncomeSourceEnum.getIncomeSourceById(beneficiary.householdIncomeSource.toInt() + 1)
            }
            form.householdMonthlyAvgIncome = beneficiary.householdMonthlyAvgIncome
            if (beneficiary.currency != null) {
                //form.currency = CurrencyEnum.entries.getOrNull(beneficiary.currency.toInt())
//                form.currency = CurrencyEnum.getCurrencyById(beneficiary.currency.toInt() + 1)
                form.currency = CurrencyEnum.SUDANESE_POUND
            }
            if (beneficiary.selectionCriteria != null) {
                form.selectionCriteria =
                    SelectionCriteriaEnum.getSelectionCriteriaById(beneficiary.selectionCriteria.toInt() + 1)
            }

            val selectionReasonList = ArrayList<SelectionReasonEnum>()
            for (item in selectionReason) {
                val reasonId = item.id.toInt() + 1
                val selectionReason = SelectionReasonEnum.getSelectionReasonById(reasonId)
                selectionReasonList.add(selectionReason)
            }

            form.selectionReason = selectionReasonList
            val addressOb = Address()
            addressOb.stateId = address.stateId
            addressOb.countyId = address.countyId
            addressOb.payamId = address.payam
            addressOb.bomaId = address.boma
            form.address = EntityMapper.toAddress(addressOb)
            val locationObj = Location()
            locationObj.lat = location.lat
            locationObj.lon = location.lon
            form.location = EntityMapper.toLocation(locationObj)

            // form.householdSize = householdInfo.size
            form.householdMember2 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M2")
            form.householdMember5 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M5")
            form.householdMember17 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M17")
            form.householdMember35 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M35")
            form.householdMember64 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M64")
            form.householdMember65 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M65")
            form.isReadWrite = beneficiary.isReadWrite
            form.memberReadWrite = beneficiary.memberReadWrite

            form.biometrics = EntityMapper.toBiometricEntityFromdbForBeneficiary(biometricBio)


//            if (beneficiary.notPerticipationReason != null) {
//                form.notPerticipationReason =
//                    NonPerticipationReasonEnum.getNonParticipationById(beneficiary.notPerticipationReason.toInt() + 1)
//            }
//            form.notPerticipationOtherReason = beneficiary.notPerticipationOtherReason

            form.nominees = EntityMapper.toNomineeItemsLdb(nominee)

            if (form.nominees != null && form.nominees.size > 0) {
                form.isOtherMemberPerticipating = true
            } else {
                form.isOtherMemberPerticipating = false
            }
            if (beneficiary.notPerticipationReason != null) {
                form.notPerticipationReason =
                    NonPerticipationReasonEnum.getNonParticipationById(beneficiary.notPerticipationReason.toInt() + 1)
                form.notPerticipationOtherReason = beneficiary.notPerticipationOtherReason
            }

            if (alternateBio1 != null) {
                form.alternatePayee1 = EntityMapper.getFirstAlternateLdb(alternateEO, alternateBio1)
            }
            if (alternateEO.size == 2) {
                form.alternatePayee2 =
                    EntityMapper.getSecondAlternateLdb(alternateEO, alternateBio2)
            }
            form.createdBy = 0
            //Log.d(TAG, "showBeneficiary: ${form.alternatePayee1.payeeAge}")
            Log.d(TAG, "showBeneficiary: ${form.isReadWrite}")
            _event.value = Event.GetDataLocalDbByAppId(form)
        }
    }

    fun bulkBeneficiaryList(context: Context) {
        val dataList: ArrayList<Beneficiary> = ArrayList()

        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context)
        val form: Beneficiary = Beneficiary()
        //_event.value = Event.Loading
        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()
            val alternateDao: AlternateDao = mDatabase.alternateDao()
            val addressDao: AddressDao = mDatabase.addressDao()
            val nomineeDao: NomineeDao = mDatabase.nomineeDao()
            val locationDao: LocationDao = mDatabase.locationDao()
            val householdInfoDao: HouseholdInfoDao = mDatabase.householdInfoDao()
            val biometricDao: BiometricDao = mDatabase.biometricDao()
            val selectionReasonDao: SelectionReasonDao = mDatabase.selectionReasonDao()

            val beneficiaryList = beneficiaryDao.beneficiaryForBulk
            for (beneficiary in beneficiaryList) {
                // Data Bind With Api Obj

                val address = addressDao.getAddressByAppId(beneficiary.applicationId)
                val location = locationDao.getLocationByAppId(beneficiary.applicationId)
                val nominee = nomineeDao.getNomineeListByAppId(beneficiary.applicationId)
                val householdInfo =
                    householdInfoDao.getHouseholdInfoListByAppId(beneficiary.applicationId)
                val alternateEO = alternateDao.getAlternateList(beneficiary.applicationId)
                val selectionReason =
                    selectionReasonDao.getSelectionReasonByAppId(beneficiary.applicationId)
                val biometricBio =
                    biometricDao.getBiometricsListByAppIdForBenaficiary(beneficiary.applicationId)
                val alternateBio1 =
                    biometricDao.getBiometricsListByAppIdForAlternate1(beneficiary.applicationId)
                val alternateBio2 =
                    biometricDao.getBiometricsListByAppIdForAlternate2(beneficiary.applicationId)

                form.applicationId = beneficiary.applicationId
                form.respondentFirstName = beneficiary.respondentFirstName
                form.respondentMiddleName = beneficiary.respondentMiddleName
                form.respondentLastName = beneficiary.respondentLastName
                form.respondentNickName = beneficiary.respondentNickName
                form.spouseFirstName = beneficiary.spouseFirstName
                form.spouseMiddleName = beneficiary.spouseMiddleName
                form.spouseLastName = beneficiary.spouseLastName
                form.spouseNickName = beneficiary.spouseNickName
                form.incomeSourceOther = beneficiary.incomeSourceOther
                form.relationshipOther = beneficiary.relationshipOther
                form.documentTypeOther = beneficiary.documentTypeOther
                form.householdSize = beneficiary.householdSize

                if (beneficiary.relationshipWithHouseholdHead != null) {
                    form.relationshipWithHouseholdHead =
                        RelationshipEnum.getRelationById(beneficiary.relationshipWithHouseholdHead.toInt() + 1)
                }

                form.respondentAge = beneficiary.respondentAge

                if (beneficiary.respondentGender != null) {
                    form.respondentGender =
                        GenderEnum.getGenderById(beneficiary.respondentGender.toInt() + 1)
                }

                if (beneficiary.respondentMaritalStatus != null) {
                    form.respondentMaritalStatus =
                        MaritalStatusEnum.getMartialStatusById(beneficiary.respondentMaritalStatus.toInt() + 1)
                }

                if (beneficiary.respondentLegalStatus != null) {
                    form.respondentLegalStatus =
                        LegalStatusEnum.getLegalStatusById(beneficiary.respondentLegalStatus.toInt() + 1)
                }
                if (beneficiary.documentType != null) {
                    form.documentType =
                        DocumentTypeEnum.getDocumentTypeById(beneficiary.documentType.toInt() + 1)
                }

                when (form.documentType) {
                    DocumentTypeEnum.PASSPORT, DocumentTypeEnum.NATIONAL_ID -> {
                        form.respondentId = beneficiary.respondentId
                    }

                    DocumentTypeEnum.OTHER -> {
                        form.documentTypeOther = beneficiary.documentTypeOther
                        form.respondentId = beneficiary.respondentId
                    }

                    else -> {
                        form.documentTypeOther = null
                    }
                }

                if (beneficiary.respondentPhoneNo.isNullOrEmpty() || beneficiary.respondentPhoneNo.length < 10) {
                    form.respondentPhoneNo = null
                } else {
                    form.respondentPhoneNo = beneficiary.respondentPhoneNo
                }

                if (beneficiary.householdIncomeSource != null) {
                    form.householdIncomeSource =
                        IncomeSourceEnum.getIncomeSourceById(beneficiary.householdIncomeSource.toInt() + 1)
                }
                form.householdMonthlyAvgIncome = beneficiary.householdMonthlyAvgIncome
                if (beneficiary.currency != null) {
                    //form.currency = CurrencyEnum.entries.getOrNull(beneficiary.currency.toInt())
//                form.currency = CurrencyEnum.getCurrencyById(beneficiary.currency.toInt() + 1)
                    form.currency = CurrencyEnum.SUDANESE_POUND
                }
                if (beneficiary.selectionCriteria != null) {
                    form.selectionCriteria =
                        SelectionCriteriaEnum.getSelectionCriteriaById(beneficiary.selectionCriteria.toInt() + 1)
                }

                val selectionReasonList = ArrayList<SelectionReasonEnum>()
                for (item in selectionReason) {
                    val reasonId = item.id.toInt() + 1
                    val selectionReason = SelectionReasonEnum.getSelectionReasonById(reasonId)
                    selectionReasonList.add(selectionReason)
                }

                form.selectionReason = selectionReasonList
                val addressOb = Address()
                addressOb.stateId = address.stateId
                addressOb.countyId = address.countyId
                addressOb.payamId = address.payam
                addressOb.bomaId = address.boma
                form.address = EntityMapper.toAddress(addressOb)
                val locationObj = Location()
                locationObj.lat = location.lat
                locationObj.lon = location.lon
                form.location = EntityMapper.toLocation(locationObj)

                // form.householdSize = householdInfo.size
                form.householdMember2 = EntityMapper.toHouseholdMember2Ldb(
                    beneficiary.applicationId,
                    householdInfo,
                    "M2"
                )
                form.householdMember5 = EntityMapper.toHouseholdMember2Ldb(
                    beneficiary.applicationId,
                    householdInfo,
                    "M5"
                )
                form.householdMember17 = EntityMapper.toHouseholdMember2Ldb(
                    beneficiary.applicationId,
                    householdInfo,
                    "M17"
                )
                form.householdMember35 = EntityMapper.toHouseholdMember2Ldb(
                    beneficiary.applicationId,
                    householdInfo,
                    "M35"
                )
                form.householdMember64 = EntityMapper.toHouseholdMember2Ldb(
                    beneficiary.applicationId,
                    householdInfo,
                    "M64"
                )
                form.householdMember65 = EntityMapper.toHouseholdMember2Ldb(
                    beneficiary.applicationId,
                    householdInfo,
                    "M65"
                )
                form.isReadWrite = beneficiary.isReadWrite
                form.memberReadWrite = beneficiary.memberReadWrite

                form.biometrics = EntityMapper.toBiometricEntityFromdbForBeneficiary(biometricBio)

                form.nominees = EntityMapper.toNomineeItemsLdb(nominee)

                form.isOtherMemberPerticipating = form.nominees != null && form.nominees.size > 0
                if (beneficiary.notPerticipationReason != null) {
                    form.notPerticipationReason =
                        NonPerticipationReasonEnum.getNonParticipationById(beneficiary.notPerticipationReason.toInt() + 1)
                    form.notPerticipationOtherReason = beneficiary.notPerticipationOtherReason
                }

                if (alternateBio1 != null) {
                    form.alternatePayee1 =
                        EntityMapper.getFirstAlternateLdb(alternateEO, alternateBio1)
                }
                if (alternateEO.size == 2) {
                    form.alternatePayee2 =
                        EntityMapper.getSecondAlternateLdb(alternateEO, alternateBio2)
                }
                form.createdBy = 0

                dataList.add(form)

            }

            if (dataList.size > 0) {
                _event.value = Event.GetDataLocalDbForBulk(dataList)
            } else {
                _event.value = Event.GetHouseholdItemsFailure("No Beneficiary Found For Bulk Sync.")
            }

            //Log.d(TAG, "showBeneficiary: ${form.alternatePayee1.payeeAge}")
            Log.d(TAG, "bulkBeneficiaryList: ${beneficiaryList.size}")
        }
    }

    fun showBeneficiaryByAppIdForViewDetails(context: Context, appId: String) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context)
        val form: Beneficiary = Beneficiary()
        //_event.value = Event.Loading
        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()
            val alternateDao: AlternateDao = mDatabase.alternateDao()
            val addressDao: AddressDao = mDatabase.addressDao()
            val nomineeDao: NomineeDao = mDatabase.nomineeDao()
            val locationDao: LocationDao = mDatabase.locationDao()
            val householdInfoDao: HouseholdInfoDao = mDatabase.householdInfoDao()
            val biometricDao: BiometricDao = mDatabase.biometricDao()
            val selectionReasonDao: SelectionReasonDao = mDatabase.selectionReasonDao()
            val beneficiary = beneficiaryDao.getBeneficiaryByAppId(appId)
            val address = addressDao.getAddressByAppId(appId)
            val location = locationDao.getLocationByAppId(appId)
            val nominee = nomineeDao.getNomineeListByAppId(appId)
            val householdInfo = householdInfoDao.getHouseholdInfoListByAppId(appId)
            val alternateEO = alternateDao.getAlternateList(appId)
            val selectionReason = selectionReasonDao.getSelectionReasonByAppId(appId)
            val biometricBio = biometricDao.getBiometricsListByAppIdForBenaficiary(appId)
            val alternateBio1 = biometricDao.getBiometricsListByAppIdForAlternate1(appId)
            val alternateBio2 = biometricDao.getBiometricsListByAppIdForAlternate2(appId)

            // Data Bind With Api Obj
            form.applicationId = beneficiary.applicationId
            form.respondentFirstName = beneficiary.respondentFirstName
            form.respondentMiddleName = beneficiary.respondentMiddleName
            form.respondentLastName = beneficiary.respondentLastName
            form.respondentNickName = beneficiary.respondentNickName
            form.spouseFirstName = beneficiary.spouseFirstName
            form.spouseMiddleName = beneficiary.spouseMiddleName
            form.spouseLastName = beneficiary.spouseLastName
            form.spouseNickName = beneficiary.spouseNickName

            if (beneficiary.relationshipWithHouseholdHead != null) {
                form.relationshipWithHouseholdHead =
                    RelationshipEnum.getRelationById(beneficiary.relationshipWithHouseholdHead.toInt() + 1)
            }

            form.respondentAge = beneficiary.respondentAge

            if (beneficiary.respondentGender != null) {
                form.respondentGender =
                    GenderEnum.getGenderById(beneficiary.respondentGender.toInt() + 1)
            }

            if (beneficiary.respondentMaritalStatus != null) {
                form.respondentMaritalStatus =
                    MaritalStatusEnum.getMartialStatusById(beneficiary.respondentMaritalStatus.toInt() + 1)
            }

            if (beneficiary.respondentLegalStatus != null) {
                form.respondentLegalStatus =
                    LegalStatusEnum.getLegalStatusById(beneficiary.respondentLegalStatus.toInt() + 1)
            }
            if (beneficiary.documentType != null) {
                form.documentType =
                    DocumentTypeEnum.getDocumentTypeById(beneficiary.documentType.toInt() + 1)
            }

            form.documentTypeOther = beneficiary.documentTypeOther
            form.respondentId = beneficiary.respondentId

            if (beneficiary.respondentPhoneNo.isNullOrEmpty()) {
                form.respondentPhoneNo = ""
            } else {
                form.respondentPhoneNo = beneficiary.respondentPhoneNo
            }

            if (beneficiary.householdIncomeSource != null) {
                form.householdIncomeSource =
                    IncomeSourceEnum.getIncomeSourceById(beneficiary.householdIncomeSource.toInt() + 1)
            }
            form.householdMonthlyAvgIncome = beneficiary.householdMonthlyAvgIncome
            if (beneficiary.currency != null) {
                //form.currency = CurrencyEnum.entries.getOrNull(beneficiary.currency.toInt())
                form.currency = CurrencyEnum.getCurrencyById(beneficiary.currency.toInt() + 1)
            }
            if (beneficiary.selectionCriteria != null) {
                form.selectionCriteria =
                    SelectionCriteriaEnum.getSelectionCriteriaById(beneficiary.selectionCriteria.toInt() + 1)
            }

            val selectionReasonList = ArrayList<SelectionReasonEnum>()
            for (item in selectionReason) {
                val reasonId = item.id.toInt() + 1
                val selectionReason = SelectionReasonEnum.getSelectionReasonById(reasonId)
                selectionReasonList.add(selectionReason)
            }

            form.selectionReason = selectionReasonList

            val addressOb = Address()
            addressOb.stateId = address.stateId
            addressOb.countyId = address.countyId
            addressOb.payamId = address.payam
            addressOb.bomaId = address.boma
            form.address = EntityMapper.toAddress(addressOb)
            val locationObj = Location()
            locationObj.lat = location.lat
            locationObj.lon = location.lon
            form.location = EntityMapper.toLocation(locationObj)

            form.householdSize = householdInfo.size
            form.householdMember2 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M2")
            form.householdMember5 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M5")
            form.householdMember17 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M17")
            form.householdMember35 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M35")
            form.householdMember64 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M64")
            form.householdMember65 = EntityMapper.toHouseholdMember2Ldb(appId, householdInfo, "M65")
            form.isReadWrite = beneficiary.isReadWrite
            form.memberReadWrite = beneficiary.memberReadWrite
            form.isOtherMemberPerticipating = beneficiary.isOtherMemberPerticipating
            if (beneficiary.notPerticipationReason != null) {
                form.notPerticipationReason =
                    NonPerticipationReasonEnum.getNonParticipationById(beneficiary.notPerticipationReason.toInt() + 1)
            }
            form.notPerticipationOtherReason = beneficiary.notPerticipationOtherReason
            form.nominees = EntityMapper.toNomineeItemsLdb(nominee)

            form.biometrics = EntityMapper.toBiometricEntityFromdbForBeneficiary(biometricBio)

            if (alternateBio1 != null) {
                form.alternatePayee1 = EntityMapper.getFirstAlternateLdb(alternateEO, alternateBio1)
            }
            if (alternateEO.size == 2) {
                form.alternatePayee2 =
                    EntityMapper.getSecondAlternateLdb(alternateEO, alternateBio2)
            }
            form.createdBy = 0
            //Log.d(TAG, "showBeneficiary: ${form.alternatePayee1.payeeAge}")
            Log.d(TAG, "showBeneficiary: ${form.isReadWrite}")
            _event.value = Event.GetDataLocalDbByAppIdForView(form)
            //   mDatabase.close()
        }
    }

    fun showBeneficiary(context: Context) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context)

        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()
            val nomineeDao = mDatabase.nomineeDao()
            val alternateDao = mDatabase.alternateDao()
            val biometricDao = mDatabase.biometricDao()

            val beneficiaries = beneficiaryDao.allBeneficiaries

            beneficiaries.forEach { beneficiary ->
                val nomineeList = nomineeDao.getNomineeListByAppId(beneficiary.applicationId)
                val alternateList = alternateDao.getAlternateList(beneficiary.applicationId)
                val biometric = biometricDao.getPhoto(beneficiary.applicationId)

                beneficiary.alternateSize = alternateList.size
                beneficiary.nomineeSize = nomineeList.size
                beneficiary.photoPath = biometric.photo

                Log.d(TAG, "showBeneficiary: Nominee count - ${nomineeList.size}")
                Log.d(TAG, "showBeneficiary: Alternate count - ${alternateList.size}")
            }

            Log.d(TAG, "showBeneficiary: Beneficiaries - $beneficiaries")
            _event.value = Event.GetDataLocalDb(beneficiaries)
        }

        // Note: No need to close the database here since you're using Room, which manages database connections automatically.
    }

    fun updateBeneficiary(context: Context, appId: String) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context);
        //_event.value = Event.Loading
        viewModelScope.launch(dispatchers.io) {
            val beneficiaryDao: BeneficiaryDao = mDatabase.beneficiaryDao()
            val beneficiary = beneficiaryDao.updateBeneficiaryByAppId(appId)

            Log.d(TAG, "showBeneficiary: $beneficiary")
            _event.value = Event.UpdateDataLocalDb(true)
        }
        // mDatabase.close()
    }

    fun insertBeneficiary(context: Context, beneficiaryBO: Beneficiary, applicationStatus: Int) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context);
        viewModelScope.launch(dispatchers.io) {

            //MappingDb Mapping data to local db
            Log.d(
                HhPreviewFragment.TAG,
                "insertBeneficiary() called with: beneficiaryBO = $beneficiaryBO"
            )
            try {
                DatabaseExecutors.getInstance().diskIO().execute {
                    val uuid = beneficiaryBO.applicationId
                    val beneficiaryEO: com.xplo.code.data.db.room.model.Beneficiary =
                        if (beneficiaryBO.nominees.isNullOrEmpty()) {
                            prepareBeneficiaryEntity(uuid, beneficiaryBO, 0, applicationStatus)
                        } else {
                            prepareBeneficiaryEntity(
                                uuid,
                                beneficiaryBO,
                                beneficiaryBO.nominees.size,
                                applicationStatus
                            )
                        }

                    val addressEO: com.xplo.code.data.db.room.model.Address =
                        prepareAddressEntity(uuid, beneficiaryBO.address)
                    val locationEO: com.xplo.code.data.db.room.model.Location =
                        prepareLocationEntity(uuid, beneficiaryBO.location)

                    val selectionReasonList: List<SelectionReason> =
                        prepareSelectionReasonEntity(uuid, beneficiaryBO.selectionReason)

                    val alternateList: MutableList<Alternate> =
                        ArrayList<Alternate>()
                    if (beneficiaryBO.alternatePayee1 != null) {
                        val firstAlternateEO: Alternate =
                            prepareAlternateEntity(
                                uuid,
                                beneficiaryBO.alternatePayee1,
                                "ALT1"
                            )
                        alternateList.add(firstAlternateEO)
                    }
                    if (beneficiaryBO.alternatePayee2 != null) {
                        val secondAlternateEO: Alternate =
                            prepareAlternateEntity(
                                uuid,
                                beneficiaryBO.alternatePayee2,
                                "ALT2"
                            )
                        alternateList.add(secondAlternateEO)
                    }

                    var nomineeList: List<Nominee> = ArrayList<Nominee>()
                    if (beneficiaryBO.nominees != null) {
                        nomineeList = prepareNomineeEntity(uuid, beneficiaryBO.nominees)
                    }

                    val householdInfoList: MutableList<HouseholdInfo> =
                        ArrayList<HouseholdInfo>()
                    val householdInfo2EO: HouseholdInfo =
                        prepareHouseholdInfoEntity(
                            uuid,
                            beneficiaryBO.householdMember2,
                            "M2"
                        )
                    householdInfoList.add(householdInfo2EO)
                    val householdInfo5EO: HouseholdInfo =
                        prepareHouseholdInfoEntity(
                            uuid,
                            beneficiaryBO.householdMember5,
                            "M5"
                        )
                    householdInfoList.add(householdInfo5EO)
                    val householdInfo17EO: HouseholdInfo =
                        prepareHouseholdInfoEntity(
                            uuid,
                            beneficiaryBO.householdMember17,
                            "M17"
                        )
                    householdInfoList.add(householdInfo17EO)
                    val householdInfo35EO: HouseholdInfo =
                        prepareHouseholdInfoEntity(
                            uuid,
                            beneficiaryBO.householdMember35,
                            "M35"
                        )
                    householdInfoList.add(householdInfo35EO)
                    val householdInfo64EO: HouseholdInfo =
                        prepareHouseholdInfoEntity(
                            uuid,
                            beneficiaryBO.householdMember64,
                            "M64"
                        )
                    householdInfoList.add(householdInfo64EO)
                    val householdInfo65EO: HouseholdInfo =
                        prepareHouseholdInfoEntity(
                            uuid,
                            beneficiaryBO.householdMember65,
                            "M65"
                        )
                    householdInfoList.add(householdInfo65EO)

                    val biometricList: MutableList<Biometric> =
                        ArrayList<Biometric>()
                    if (beneficiaryBO.biometrics != null) {
                        val beneficiaryBiometric: Biometric =
                            prepareBiometricEntity(uuid, beneficiaryBO.biometrics, "BENE")
                        biometricList.add(beneficiaryBiometric)
                    }
                    if (beneficiaryBO.alternatePayee1 != null && beneficiaryBO.alternatePayee1
                            .biometrics != null
                    ) {
                        val alternate1Biometric: Biometric = prepareBiometricEntity(
                            uuid,
                            beneficiaryBO.alternatePayee1.biometrics, "ALT1"
                        )
                        biometricList.add(alternate1Biometric)
                    }
                    if (beneficiaryBO.alternatePayee2 != null && beneficiaryBO.alternatePayee2
                            .biometrics != null
                    ) {
                        val alternate2Biometric: Biometric = prepareBiometricEntity(
                            uuid,
                            beneficiaryBO.alternatePayee2.biometrics, "ALT2"
                        )
                        biometricList.add(alternate2Biometric)
                    }
                    val beneficiaryTransactionDao: BeneficiaryTransactionDao =
                        mDatabase.beneficiaryTransactionDao()
                    beneficiaryTransactionDao.insertBeneficiaryRecord(
                        beneficiaryEO, addressEO, locationEO,
                        biometricList,
                        householdInfoList, alternateList, nomineeList, selectionReasonList
                    )

                    Log.d(HhPreviewFragment.TAG, "Inserted the beneficiary data")
                    _event.value = Event.InsertDataLocalDb("Inserted the beneficiary data")
                }
            } catch (ex: Exception) {
                _event.value =
                    Event.SaveFormPEntityFailure("Error while sending data : " + ex.message)

                Log.e(HhPreviewFragment.TAG, "Error while sending data : " + ex.message)
                ex.printStackTrace()
            }
        }
        // mDatabase.close()
    }

    fun deleteAndInsertBeneficiary(context: Context, appId: String) {
        val mDatabase: BeneficiaryDatabase = BeneficiaryDatabase.getInstance(context)

        viewModelScope.launch(dispatchers.io) {
            // Delete the beneficiary with the given application ID
            deleteBeneficiary(context, appId)

            // Insert a new beneficiary
            val insertSyncBeneficiary = SyncBeneficiary().apply {
                applicationId = appId
                beneficiaryName = "Test"
            }
            val insertedId =
                mDatabase.syncBeneficiaryDao().insertSyncBeneficiary(insertSyncBeneficiary)

            Log.d(TAG, "Inserted beneficiary with ID: $insertedId")

            _event.value = Event.UpdateDataLocalDb(true)
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

//    override fun sendHouseholdItem(item: HouseholdItem?, pos: Int) {
//        Log.d(TAG, "sendHouseholdItem() called with: item = $item, pos = $pos")
//        if (item == null) return
//        val form = item.toHouseholdForm()
//        var entity = EntityMapper.toBeneficiaryEntity(form)
//        var beneficiary = BeneficiaryMapper.toBeneficiary(entity)
//        if (beneficiary == null) return
//
//        viewModelScope.launch(dispatchers.io) {
//            _event.value = Event.Loading
//            when (val response = contentRepo.sendBeneficiary(beneficiary)) {
//
//                is Resource.Success -> {
//                    Log.d(TAG, "sendHouseholdItem: success: ${response.data}")
//
//                    _event.value = Event.SendHouseholdItemSuccess(item, pos)
//                }
//
//                is Resource.Failure -> {
//                    Log.d(TAG, "sendHouseholdItem: failure: ${response.callInfo}")
//                    _event.value = Event.SendHouseholdItemFailure(response.callInfo?.msg, pos)
//                }
//
//                else -> {}
//            }
//        }
//    }

//    override fun sendHouseholdForm(form: HouseholdForm?, pos: Int) {
//        Log.d(TAG, "sendHouseholdForm() called with: form = $form")
//        if (form == null) return
//
//        var entity = EntityMapper.toBeneficiaryEntity(form)
//        var item = BeneficiaryMapper.toBeneficiary(entity)
//        if (item == null) return
//
//        viewModelScope.launch(dispatchers.io) {
//            _event.value = Event.Loading
//            when (val response = contentRepo.sendBeneficiary(item)) {
//
//                is Resource.Success -> {
//                    Log.d(TAG, "sendHouseholdForm: success: ${response.data}")
//
//                    _event.value = Event.SendHouseholdFormSuccess(item.applicationId, pos)
//                }
//
//                is Resource.Failure -> {
//                    Log.d(TAG, "sendHouseholdForm: failure: ${response.callInfo}")
//                    _event.value = Event.SendHouseholdFormFailure(response.callInfo?.msg, pos)
//                }
//
//                else -> {}
//            }
//        }
//    }

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
                    _event.value =
                        Event.GetBeneficiaryEntityItemsFailure(response.callInfo?.msg)
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

//    override fun sendBeneficiaryEntity(item: BeneficiaryEntity?, pos: Int) {
//        Log.d(TAG, "sendBeneficiaryEntity() called with: item = $item, pos = $pos")
//        if (item == null) return
//
//        val beneficiary = BeneficiaryMapper.toBeneficiary(item)
//
//        viewModelScope.launch(dispatchers.io) {
//            _event.value = Event.Loading
//            when (val response = contentRepo.sendBeneficiary(beneficiary)) {
//
//                is Resource.Success -> {
//                    Log.d(TAG, "sendBeneficiaryEntity: success: ${response.data}")
//                    _event.value = Event.SendBeneficiaryEntitySuccess(item.id, pos)
//                }
//
//                is Resource.Failure -> {
//                    Log.d(TAG, "sendBeneficiaryEntity: failure: ${response.callInfo}")
//                    _event.value = Event.SendBeneficiaryEntityFailure(response.callInfo?.msg)
//                }
//            }
//        }
//    }


//    override fun sendBeneficiary(item: Beneficiary?, pos: Int) {
//        Log.d(TAG, "sendBeneficiary() called with: item = $item, pos = $pos")
//        if (item == null) return
//
//        viewModelScope.launch(dispatchers.io) {
//            _event.value = Event.Loading
//            when (val response = contentRepo.sendBeneficiary(item)) {
//
//                is Resource.Success -> {
//                    Log.d(TAG, "sendBeneficiary: success: ${response.data}")
//                    _event.value = Event.SendBeneficiarySuccess(item.applicationId, pos)
//                }
//
//                is Resource.Failure -> {
//                    Log.d(TAG, "sendBeneficiary: failure: ${response.callInfo}")
//                    _event.value = Event.SendBeneficiaryFailure(response.callInfo?.msg)
//                }
//            }
//        }
//    }


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
        Log.d(
            TAG,
            "syncHouseholdForm() called with: context = $context, form = $form, pos = $pos"
        )
        if (form == null) return


        //val beneficiary = Fake.getABenificiary()
        val entity = EntityMapper.toBeneficiaryEntity(form)
        val beneficiary = BeneficiaryMapper.toBeneficiary(entity)

        val integrationManager = IMHelper.getIntegrationManager(context, this)
        val header = IMHelper.getHeader()
        integrationManager.syncRecord(beneficiary, header)

        //sendBeneficiary(beneficiary, pos)

    }

    fun callRegisterApi(context: Context, beneficiary: Beneficiary?) {
        Log.d(TAG, "callRegisterApi() called with: context = $context, beneficiary = $beneficiary")

        beneficiary?.let { validBeneficiary ->
            // Uncomment the line below if you need to log the JSON representation of the payee phone number
            // val json = Gson().toJson(validBeneficiary.alternatePayee1?.payeePhoneNo)
            // Log.d(TAG, "JSON representation of payee phone number: $json")

            val integrationManager = IMHelper.getIntegrationManager(context, this)
            val header = IMHelper.getHeader()
            integrationManager.syncRecord(validBeneficiary, header)
        } ?: run {
            Log.e(TAG, "Beneficiary is null.")
        }
    }


    fun callRegisterApiBulk(context: Context, beneficiaries: ArrayList<Beneficiary>?) {
        Log.d(
            TAG,
            "callRegisterApiBulk - Context: $context, Beneficiary Count: ${beneficiaries?.size}"
        )

        beneficiaries?.let { beneficiaryList ->
            val integrationManager = IMHelper.getIntegrationManager(context, this)
            val header = IMHelper.getHeader()
            integrationManager.syncRecords(beneficiaryList, header)
        } ?: run {
            Log.e(TAG, "Beneficiaries list is null or empty.")
        }
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
        viewModelScope.launch(dispatchers.io) {
            arg?.let { registrationResult ->
                if (registrationResult.syncStatus == RegistrationStatus.SUCCESS) {
                    Log.d(DashboardFragment.TAG, "Registration Successful")
                    val appIds = registrationResult.applicationIds
                    if (appIds.isNullOrEmpty()) {
                        Log.e(DashboardFragment.TAG, "No beneficiary list received. Returning ...")
                        _event.value =
                            Event.GetHouseholdItemsFailure("No beneficiary list received.")
                    } else {
                        Log.d(DashboardFragment.TAG, "Registered following users:")
                        appIds.forEach { nowId ->
                            Log.d(DashboardFragment.TAG, "Beneficiary ID: $nowId")
                        }
                        _event.value =
                            Event.GetHouseholdItemsSuccessMsg("Registration Successful", appIds)
                    }
                } else {
                    val errorCode = registrationResult.syncStatus?.errorCode ?: "Unknown"
                    val errorMsg = registrationResult.syncStatus?.errorMsg ?: "Unknown"
                    val failureMsg = "Error code: $errorCode, Error Msg: $errorMsg"
                    Log.d(DashboardFragment.TAG, "Registration Failed: $failureMsg")
                    _event.value = Event.GetHouseholdItemsFailure(failureMsg)
                }
            } ?: run {
                Log.d(DashboardFragment.TAG, "Received null parameter. Returning...")
                _event.value = Event.GetHouseholdItemsFailure("Received null parameter.")
            }
        }
    }


//    private fun onGetSyncResult(arg: RegistrationResult?) {
//        viewModelScope.launch(dispatchers.io) {
//            if (arg == null) {
//                _event.value =
//                    Event.GetHouseholdItemsFailure("Received null parameter in update. Returning...")
//            } else {
//                var appId = ""
//                val registrationResult = arg as? RegistrationResult
//                if (registrationResult?.syncStatus == RegistrationStatus.SUCCESS) {
//                    Log.d(DashboardFragment.TAG, "Registration Successful")
//
//                    val appIds = registrationResult.applicationIds
//                    if (appIds == null) {
//                        Log.e(
//                            DashboardFragment.TAG,
//                            "No beneficiary list received. Returning ... "
//                        )
//                        //_event.value = Event.GetHouseholdItemsSuccessMsg("No beneficiary list received. Returning ... ")
//                    }
//                    Log.d(DashboardFragment.TAG, "Registered following users: ")
//                    for (nowId in appIds) {
//                        // _event.value = Event.GetHouseholdItemsSuccessMsg("Beneficiary ID : $nowId")
//                        Log.d(DashboardFragment.TAG, "Beneficiary ID : $nowId")
////                        if (appIds.size == 1) {
////                            appId = nowId
////                        }
//                    }
//                    _event.value =
//                        Event.GetHouseholdItemsSuccessMsg("Registration Successful", appIds)
//
//                } else {
//                    _event.value =
//                        Event.GetHouseholdItemsFailure("Error code : ${registrationResult?.syncStatus?.errorCode}" + " Error Msg : ${registrationResult?.syncStatus?.errorMsg}")
//                    Log.d(DashboardFragment.TAG, "Registration Failed")
//                    Log.d(
//                        DashboardFragment.TAG,
//                        "Error code : ${registrationResult?.syncStatus?.errorCode}"
//                    )
//                    Log.d(
//                        DashboardFragment.TAG,
//                        "Error Msg : ${registrationResult?.syncStatus?.errorMsg}"
//                    )
//                }
//            }
//
//        }
////        try {
////
////            Log.d(DashboardFragment.TAG, "Received update>>>>")
////            if (arg == null) {
////                Log.d(DashboardFragment.TAG, "Received null parameter in update. Returning...")
////                return
////            } else {
////                Log.d(DashboardFragment.TAG, "Received parameter in update.")
////                val registrationResult = arg as? RegistrationResult
////                if (registrationResult?.syncStatus == RegistrationStatus.SUCCESS) {
////                    Log.d(DashboardFragment.TAG, "Registration Successful")
////
////                    val appIds = registrationResult.applicationIds
////                    if (appIds == null) {
////                        Log.e(DashboardFragment.TAG, "No beneficiary list received. Returning ... ")
////                        return
////                    }
////
////                    Log.d(DashboardFragment.TAG, "Registered following users: ")
////                    for (nowId in appIds) {
////                        Log.d(DashboardFragment.TAG, "Beneficiary ID : $nowId")
////                    }
////                } else {
////                    Log.d(DashboardFragment.TAG, "Registration Failed")
////                    Log.d(DashboardFragment.TAG, "Error code : ${registrationResult?.syncStatus?.errorCode}")
////                    Log.d(DashboardFragment.TAG, "Error Msg : ${registrationResult?.syncStatus?.errorMsg}")
////                }
////            }
////        } catch (exc: Exception) {
////            Log.e(DashboardFragment.TAG, "Error while processing update : ${exc.message}")
////        }
//    }

    private fun onSyncSuccess(syncResult: SyncResult) {
        Log.d(TAG, "onSyncSuccess() called with: syncResult = $syncResult")
        _event.value = Event.SyncFormSuccess(syncResult, -1)


    }

    private fun onSyncFailure(syncResult: SyncResult) {
        Log.d(TAG, "onSyncFailure() called with: syncResult = $syncResult")
        _event.value = Event.SyncFormFailure("sync failed")

    }


    fun prepareBiometricEntity(
        appId: String?,
        biometricList: List<com.kit.integrationmanager.model.Biometric?>?,
        type: String
    ): Biometric {
        val nowBiometricEO = Biometric()
        if (biometricList != null) {
            for (nowBiometric in biometricList) {
                if (nowBiometric != null) {
                    nowBiometricEO.applicationId = appId
                    nowBiometricEO.type = type
                    //  nowBiometricEO.biometricUserType = nowBiometric.biometricUserType.id.toLong()
                    nowBiometricEO.biometricUserType = nowBiometric.biometricUserType?.id?.toLong()
                    if (nowBiometric.noFingerprintReasonText != null) {
                        nowBiometricEO.noFingerprintReasonText =
                            nowBiometric.noFingerprintReasonText
                    }
                    if (nowBiometric.noFingerPrint != null) {
                        nowBiometricEO.noFingerPrint = nowBiometric.noFingerPrint
                    }
                    if (nowBiometric.noFingerprintReason != null) {
                        nowBiometricEO.noFingerprintReason =
                            nowBiometric.noFingerprintReason.id.toLong()
                    }
                    if (nowBiometric.biometricType == BiometricType.PHOTO && nowBiometric.biometricData != null) {
                        nowBiometricEO.photo = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LT && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLt = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LI && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLi = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LM && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLm = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LR && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLr = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.LL && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqLs = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RT && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRt = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RI && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRi = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RM && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRm = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RR && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRr = nowBiometric.biometricData
                    } else if (nowBiometric.biometricType == BiometricType.RL && nowBiometric.biometricData != null) {
                        nowBiometricEO.wsqRs = nowBiometric.biometricData
                    }
                }
            }
        }
        return nowBiometricEO
    }

    fun prepareAddressEntity(
        appId: String?,
        addressBO: com.kit.integrationmanager.model.Address?
    ): com.xplo.code.data.db.room.model.Address {
        val addressEO = com.xplo.code.data.db.room.model.Address()
        if (addressBO != null) {
            addressEO.applicationId = appId
            //addressEO.setAddressLine(addressBO.getAddressLine());
            addressEO.stateId = addressBO.stateId
            addressEO.countyId = addressBO.countyId
            addressEO.payam = addressBO.payam
            addressEO.boma = addressBO.boma
        }
        return addressEO
    }

    fun prepareLocationEntity(
        appId: String?,
        locationBO: com.kit.integrationmanager.model.Location?
    ): com.xplo.code.data.db.room.model.Location {
        val locationEO = com.xplo.code.data.db.room.model.Location()
        if (locationBO != null) {
            locationEO.lat = locationBO.lat
            locationEO.lon = locationBO.lon
            locationEO.applicationId = appId
        }
        return locationEO
    }

    fun prepareAlternateEntity(
        appId: String?,
        alternateBO: AlternatePayee,
        type: String
    ): Alternate {
        val alternateEO = Alternate()
        alternateEO.applicationId = appId
        alternateEO.payeeFirstName = alternateBO.payeeFirstName
        alternateEO.payeeMiddleName = alternateBO.payeeMiddleName
        alternateEO.payeeLastName = alternateBO.payeeLastName
        alternateEO.payeeNickName = alternateBO.payeeNickName
        alternateEO.payeeGender =
            if (alternateBO.payeeGender != null) alternateBO.payeeGender.ordinal.toLong() else null
        alternateEO.payeeAge = alternateBO.payeeAge
        alternateEO.documentType =
            if (alternateBO.documentType != null) alternateBO.documentType.ordinal.toLong() else 3
        alternateEO.documentTypeOther = alternateBO.documentTypeOther
        alternateEO.nationalId = alternateBO.nationalId
        alternateEO.payeePhoneNo = alternateBO.payeePhoneNo
        alternateEO.type = type
        alternateEO.relationshipWithHousehold = alternateBO.relationshipWithHouseholdHead.name
        alternateEO.relationshipOther = alternateBO.relationshipOther
        return alternateEO
    }

    fun prepareNomineeEntity(
        appId: String?,
        nomineeList: List<com.kit.integrationmanager.model.Nominee>?
    ): List<Nominee> {
        val nominees: MutableList<Nominee> = java.util.ArrayList()
        if (nomineeList != null) {
            for (nominee in nomineeList) {
                val nomineeEO = Nominee()
                nomineeEO.applicationId = appId
                nomineeEO.nomineeFirstName = nominee.nomineeFirstName
                nomineeEO.nomineeMiddleName = nominee.nomineeMiddleName
                nomineeEO.nomineeLastName = nominee.nomineeLastName
                nomineeEO.nomineeNickName = nominee.nomineeNickName
                nomineeEO.relationshipWithHouseholdHead =
                    if (nominee.relationshipWithHouseholdHead != null) nominee.relationshipWithHouseholdHead.ordinal.toLong() else null
                nomineeEO.relationshipOther = nominee.relationshipOther
                nomineeEO.nomineeAge = nominee.nomineeAge
                nomineeEO.nomineeGender =
                    if (nominee.nomineeGender != null) nominee.nomineeGender.ordinal.toLong() else null
                nomineeEO.isReadWrite = nominee.isReadWrite
                nomineeEO.nomineeOccupation =
                    if (nominee.nomineeOccupation != null) nominee.nomineeOccupation.ordinal.toLong() else null
                nomineeEO.otherOccupation = nominee.otherOccupation
                nominees.add(nomineeEO)
            }
        }
        return nominees
    }

    fun prepareHouseholdInfoEntity(
        appId: String?,
        member: HouseholdMember?,
        type: String
    ): HouseholdInfo {
        val householdInfoEO = HouseholdInfo()
        if (member != null) {
            householdInfoEO.applicationId = appId
            householdInfoEO.maleTotal = member.maleTotal
            householdInfoEO.femaleTotal = member.femaleTotal
            householdInfoEO.maleDisable = member.maleDisable
            householdInfoEO.femaleDisable = member.femaleDisable
            householdInfoEO.maleChronicalIll = member.maleChronicalIll
            householdInfoEO.femaleChronicalIll = member.femaleChronicalIll
            householdInfoEO.femaleBoth = member.femaleBoth
            householdInfoEO.maleBoth = member.maleBoth
            householdInfoEO.type = type
        }
        return householdInfoEO
    }

    fun prepareSelectionReasonEntity(
        appId: String,
        reasons: List<SelectionReasonEnum>
    ): List<SelectionReason> {
        //  Log.d(TAG, "Reason List: " + appId + reasons!![0].value)
        val selectionReasons: MutableList<SelectionReason> = java.util.ArrayList()
        for (nowReason in reasons) {
            val nowSelectionReason = SelectionReason()
            nowSelectionReason.applicationId = appId
            //  Log.d(TAG, "prepareSelectionReasonEntity: ${nowReason.name}")
            //  Log.d(TAG, "prepareSelectionReasonEntity: ${nowReason.value}")
            Log.d(HhPreviewFragment.TAG, "prepareSelectionReasonEntity: ${reasons.size}")
            try {
                if (nowReason.name.isNotEmpty()) {
                    nowSelectionReason.selectionReasonName = nowReason.name
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            selectionReasons.add(nowSelectionReason)
        }
        return selectionReasons
    }

    fun prepareBeneficiaryEntity(
        appId: String?,
        beneficiaryBO: Beneficiary,
        nomineSize: Int,
        applicationStatus: Int
    ): com.xplo.code.data.db.room.model.Beneficiary {
        val beneficiaryEO = com.xplo.code.data.db.room.model.Beneficiary()
        beneficiaryEO.applicationId = appId
        beneficiaryEO.applicationStatus = applicationStatus
        beneficiaryEO.respondentFirstName = beneficiaryBO.respondentFirstName
        beneficiaryEO.respondentMiddleName = beneficiaryBO.respondentMiddleName
        beneficiaryEO.respondentLastName = beneficiaryBO.respondentLastName
        beneficiaryEO.respondentNickName = beneficiaryBO.respondentNickName

        beneficiaryEO.spouseFirstName = beneficiaryBO.spouseFirstName
        beneficiaryEO.spouseMiddleName = beneficiaryBO.spouseMiddleName
        beneficiaryEO.spouseLastName = beneficiaryBO.spouseLastName
        beneficiaryEO.spouseNickName = beneficiaryBO.spouseNickName

        beneficiaryEO.incomeSourceOther = beneficiaryBO.incomeSourceOther
        beneficiaryEO.relationshipOther = beneficiaryBO.relationshipOther
        beneficiaryEO.documentTypeOther = beneficiaryBO.documentTypeOther

        beneficiaryEO.relationshipWithHouseholdHead =
            if (beneficiaryBO.relationshipWithHouseholdHead != null) beneficiaryBO.relationshipWithHouseholdHead.ordinal.toLong() else null
        beneficiaryEO.respondentAge = beneficiaryBO.respondentAge
        beneficiaryEO.respondentGender =
            if (beneficiaryBO.respondentGender != null) beneficiaryBO.respondentGender.ordinal.toLong() else null
        beneficiaryEO.respondentMaritalStatus =
            if (beneficiaryBO.respondentMaritalStatus != null) beneficiaryBO.respondentMaritalStatus.ordinal.toLong() else null
        beneficiaryEO.respondentLegalStatus =
            if (beneficiaryBO.respondentLegalStatus != null) beneficiaryBO.respondentLegalStatus.ordinal.toLong() else null
        beneficiaryEO.documentType =
            if (beneficiaryBO.documentType != null) beneficiaryBO.documentType.ordinal.toLong() else 4

        beneficiaryEO.respondentId = beneficiaryBO.respondentId
        beneficiaryEO.respondentPhoneNo = beneficiaryBO.respondentPhoneNo
        beneficiaryEO.householdIncomeSource =
            if (beneficiaryBO.householdIncomeSource != null) beneficiaryBO.householdIncomeSource.ordinal.toLong() else null
        beneficiaryEO.householdMonthlyAvgIncome = beneficiaryBO.householdMonthlyAvgIncome
        beneficiaryEO.householdSize = beneficiaryBO.householdSize
        beneficiaryEO.isOtherMemberPerticipating = beneficiaryBO.isOtherMemberPerticipating
        beneficiaryEO.isReadWrite = beneficiaryBO.isReadWrite
        beneficiaryEO.memberReadWrite = beneficiaryBO.memberReadWrite

        if (beneficiaryBO.notPerticipationOtherReason != null) {
            beneficiaryEO.notPerticipationOtherReason = beneficiaryBO.notPerticipationOtherReason
        }
        beneficiaryEO.notPerticipationReason =
            if (beneficiaryBO.notPerticipationReason != null) beneficiaryBO.notPerticipationReason.ordinal.toLong() else null

        beneficiaryEO.createdBy = beneficiaryBO.createdBy
        beneficiaryEO.selectionCriteria =
            if (beneficiaryBO.selectionCriteria != null) beneficiaryBO.selectionCriteria.ordinal.toLong() else null
        beneficiaryEO.currency =
            if (beneficiaryBO.currency != null) beneficiaryBO.currency.ordinal.toLong() else null
        beneficiaryEO.isOtherMemberPerticipating = nomineSize > 0
        beneficiaryEO.creation_date = getCurrentDateTimeInMillis()


        return beneficiaryEO
    }

    fun getCurrentDateTimeInMillis(): Long {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        val formattedDateTime = currentDateTime.format(formatter)
        return formattedDateTime.toLong()
    }


}