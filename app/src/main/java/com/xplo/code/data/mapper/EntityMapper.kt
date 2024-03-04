package com.xplo.code.data.mapper

import android.util.Log
import com.kit.integrationmanager.model.AlternatePayee
import com.kit.integrationmanager.model.Beneficiary
import com.kit.integrationmanager.model.BiometricType
import com.kit.integrationmanager.model.BiometricUserType
import com.kit.integrationmanager.model.CurrencyEnum
import com.kit.integrationmanager.model.DocumentTypeEnum
import com.kit.integrationmanager.model.GenderEnum
import com.kit.integrationmanager.model.IncomeSourceEnum
import com.kit.integrationmanager.model.LegalStatusEnum
import com.kit.integrationmanager.model.MaritalStatusEnum
import com.kit.integrationmanager.model.NoFingerprintReasonEnum
import com.kit.integrationmanager.model.NonPerticipationReasonEnum
import com.kit.integrationmanager.model.OccupationEnum
import com.kit.integrationmanager.model.RelationshipEnum
import com.kit.integrationmanager.model.SelectionCriteriaEnum
import com.kit.integrationmanager.model.SelectionReasonEnum
import com.xplo.code.core.ext.isYes
import com.xplo.code.data.db.models.BeneficiaryEntity
import com.xplo.code.data.db.models.toJson
import com.xplo.code.data.db.room.model.HouseholdInfo
import com.xplo.code.data_module.model.content.Address
import com.xplo.code.data_module.model.content.Alternate
import com.xplo.code.data_module.model.content.Biometric
import com.xplo.code.data_module.model.content.HouseholdMember
import com.xplo.code.data_module.model.content.Location
import com.xplo.code.ui.dashboard.model.AlternateForm
import com.xplo.code.ui.dashboard.model.Finger
import com.xplo.code.ui.dashboard.model.HhForm3
import com.xplo.code.ui.dashboard.model.HhMember
import com.xplo.code.ui.dashboard.model.HouseholdForm
import com.xplo.code.ui.dashboard.model.Nominee
import com.xplo.code.ui.dashboard.model.PhotoData
import com.xplo.code.ui.dashboard.model.getFullName
import com.xplo.code.ui.dashboard.model.getTotal

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

object EntityMapper {
    private const val TAG = "EntityMapper"

    fun toBeneficiaryModelEntity(item: HouseholdForm?): Beneficiary? {
        Log.d(TAG, "toBeneficiaryEntity() called with: item = $item")
        if (item == null) return null

        val applicationId = item.id

        val form: Beneficiary = Beneficiary()
        form.applicationId = applicationId
        form.respondentFirstName = item.form2?.firstName
        form.respondentMiddleName = item.form2?.middleName
        form.respondentLastName = item.form2?.lastName
        form.respondentNickName = item.form2?.nickName
        form.spouseFirstName = item.form2?.spouseFirstName
        form.spouseMiddleName = item.form2?.spouseMiddleName
        form.spouseLastName = item.form2?.spouseLastName
        form.spouseNickName = item.form2?.spouseNickName
        form.relationshipWithHouseholdHead = RelationshipEnum.find(item.form2?.respondentRlt)
        form.respondentAge = item.form2?.age
        form.respondentGender = GenderEnum.find(item.form2?.gender)
        form.respondentMaritalStatus = MaritalStatusEnum.find(item.form2?.maritalStatus)
        form.respondentLegalStatus = LegalStatusEnum.find(item.form2?.legalStatus)
        form.documentType = DocumentTypeEnum.find(item.form2?.idNumberType)
        //  form.documentTypeOther = item.form2?.firstName
        form.documentTypeOther = "Other"
        form.respondentId = item.form2?.idNumber
        form.respondentPhoneNo = item.form2?.phoneNumber
        form.householdIncomeSource = IncomeSourceEnum.find(item.form2?.mainSourceOfIncome)
        form.householdMonthlyAvgIncome = item.form2?.monthlyAverageIncome
        form.currency = CurrencyEnum.find(item.form2?.currency)
        form.selectionCriteria = SelectionCriteriaEnum.find(item.form2?.selectionCriteria)
        //form.selectionReason = SelectionReasonEnum.find(item.form2?.selectionReason)
        val address = Address()
        address.stateId = item.form1?.state?.id
        address.countyId = item.form1?.county?.id
        address.payamId = item.form1?.payam?.id
        address.bomaId = item.form1?.boma?.id
        form.address = toAddress(address)
        val location = Location()
        location.lat = item.form1?.lat
        location.lon = item.form1?.lon
        form.location = toLocation(location)

        form.householdSize = item.form3?.householdSize
        form.householdMember2 = toHouseholdMember2(applicationId, item.form3)
        form.householdMember5 = toHouseholdMember5(applicationId, item.form3)
        form.householdMember17 = toHouseholdMember17(applicationId, item.form3)
        form.householdMember35 = toHouseholdMember35(applicationId, item.form3)
        form.householdMember64 = toHouseholdMember64(applicationId, item.form3)
        form.householdMember65 = toHouseholdMember65(applicationId, item.form3)
        form.isReadWrite = getReadWrite(item.form3?.isReadWrite)
        // form.isReadWrite = true
        form.memberReadWrite = item.form3?.readWriteNumber
        form.isOtherMemberPerticipating = FakeMapperValue.isOtherMemberPerticipating
        form.notPerticipationReason = NonPerticipationReasonEnum.find(item.form6?.noNomineeReason)
        form.notPerticipationOtherReason = item.form6?.otherReason

        form.nominees = toNomineeItems(item.form6?.nominees)

        //form.biometrics = toBiometricEntities(item.form5?.fingers)
        form.biometrics = toBiometricEntities(item)
        form.alternatePayee1 = getFirstAlternate(item.alternates)
        if (item.alternates.size == 2) {
            form.alternatePayee2 = getSecondAlternate(item.alternates)
        }

        form.createdBy = 0


        //  Log.d(TAG, "toBeneficiaryEntity: return: ${form.toJson()}")
        return form
    }

    fun toAlternateModelEntity(item: AlternateForm?): Beneficiary? {
        Log.d(TAG, "toBeneficiaryEntity() called with: item = $item")
        if (item == null) return null

        val form = Beneficiary()
        form.applicationId = item.appId

        val alternatePayee1 = AlternatePayee()
        alternatePayee1.payeeFirstName = item.form1?.alternateFirstName
        alternatePayee1.payeeMiddleName = item.form1?.alternateMiddleName
        alternatePayee1.payeeLastName = item.form1?.alternateLastName
        alternatePayee1.payeeNickName = item.form1?.alternateNickName
        alternatePayee1.payeeAge = item.form1?.age
        alternatePayee1.payeeGender = GenderEnum.find(item.form1?.gender)
        alternatePayee1.documentType = DocumentTypeEnum.find(item.form1?.idNumberType)
        alternatePayee1.documentTypeOther = item.form1?.documentTypeOther
        alternatePayee1.nationalId = item.form1?.idNumber
        alternatePayee1.payeePhoneNo = item.form1?.phoneNumber
        alternatePayee1.biometrics = toAlternateBiometricEntities(item)

        form.alternatePayee1 = alternatePayee1

        //  form.alternatePayee1 = getFirstAlternate(item.)
//        if (item.alternates.size == 2) {
//            form.alternatePayee2 = getSecondAlternate(item.alternates)
//        }

        //  Log.d(TAG, "toBeneficiaryEntity: return: ${form.toJson()}")
        return form
    }

    fun getReadWrite(value: String?): Boolean {
        return value?.uppercase() == "YES"
    }

    fun getFirstAlternate(items: ArrayList<AlternateForm>): AlternatePayee? {
        if (items.isNullOrEmpty()) return null
        return toAlternate(items[0])
    }

    fun getFirstAlternateLdb(
        items: MutableList<com.xplo.code.data.db.room.model.Alternate>,
        bio_data: com.xplo.code.data.db.room.model.Biometric
    ): AlternatePayee? {
        if (items.isNullOrEmpty()) return null
        return toAlternateLdb(items[0], bio_data)
    }

    fun getSecondAlternateLdb(
        items: MutableList<com.xplo.code.data.db.room.model.Alternate>,
        bio_data: com.xplo.code.data.db.room.model.Biometric
    ): AlternatePayee? {
        if (items.isNullOrEmpty()) return null
        return toAlternateLdb(items[1], bio_data)
    }

    fun getSecondAlternate(items: ArrayList<AlternateForm>): AlternatePayee? {
        if (items.isNullOrEmpty()) return null
        return toAlternate(items[1])
    }

    private fun toAlternate(item: AlternateForm): AlternatePayee? {
        if (item == null) return null
        val alternate = AlternatePayee()
        alternate.nationalId = item.form1?.idNumber
        alternate.documentType = DocumentTypeEnum.find(item.form1?.idNumberType)
        alternate.payeeFirstName = item.form1?.alternateFirstName
        alternate.payeeMiddleName = item.form1?.alternateMiddleName
        alternate.payeeLastName = item.form1?.alternateLastName
        alternate.payeeNickName = item.form1?.alternateNickName
        alternate.payeeAge = item.form1?.age
        //alternate.payeeGender = GenderEnum.find(item.form1?.gender)
        alternate.payeeGender = FakeMapperValue.fakeGender
        alternate.payeePhoneNo = item.form1?.phoneNumber

        alternate.biometrics = toAlternateBiometricEntities(item)

        return alternate
    }

    private fun toAlternateLdb(
        item: com.xplo.code.data.db.room.model.Alternate,
        bio_data: com.xplo.code.data.db.room.model.Biometric
    ): AlternatePayee? {
        if (item == null) return null
        val alternate = AlternatePayee()
        //alternate.documentType = FakeMapperValue.documentType
        // alternate.nationalId = FakeMapperValue.nationalId
        alternate.documentType = DocumentTypeEnum.find(item.documentType.toString() + 1)
        alternate.nationalId = item.nationalId
        alternate.payeeFirstName = item.payeeFirstName
        alternate.payeeMiddleName = item.payeeMiddleName
        alternate.payeeLastName = item.payeeLastName
        alternate.payeeNickName = item.payeeNickName
        alternate.payeeAge = item.payeeAge
        alternate.payeeGender = GenderEnum.find(item.payeeGender.toString() + 1)
        alternate.payeePhoneNo = item.payeePhoneNo
        alternate.biometrics = toBiometricEntityFromdbForBeneficiary(bio_data)

        return alternate
    }

    private fun toBiometricEntity(
        item: Finger?,
        photoData: PhotoData?
    ): com.kit.integrationmanager.model.Biometric? {
        if (item != null) {
            val biometric = com.kit.integrationmanager.model.Biometric()
            biometric.applicationId = ""
            biometric.biometricType = returnFingerPrintEnum(item.fingerType)
            biometric.biometricUserType = item.userType?.let { BiometricUserType.valueOf(it) }
            //  biometric.biometricUserType = BiometricUserType.BENEFICIARY// hard code value

            if (item.fingerPrint == null) {
                biometric.biometricData = null
            } else {
                biometric.biometricData = item.fingerPrint
            }
            biometric.noFingerPrint = item.noFingerprint
            biometric.noFingerprintReason = NoFingerprintReasonEnum.find(item.noFingerprintReason)
            biometric.noFingerprintReasonText = ""

            biometric.biometricUrl = ""
            return biometric
        } else {
            val biometric = com.kit.integrationmanager.model.Biometric()
            biometric.applicationId = ""
            biometric.biometricType = BiometricType.PHOTO
            biometric.biometricUserType = photoData?.userType?.let { BiometricUserType.valueOf(it) }

            if (photoData?.img?.isEmpty() == true) {
                biometric.biometricData = null
            } else {
                biometric.biometricData = photoData?.img
            }
            return biometric
        }
    }


    public fun toBiometricEntityFromdbForBeneficiary(item: com.xplo.code.data.db.room.model.Biometric): List<com.kit.integrationmanager.model.Biometric?> {

        val applicationId = item.applicationId
        val biometrics = mutableListOf<com.kit.integrationmanager.model.Biometric?>()


        if (item.photo != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId

            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }

            biometricData.biometricType = BiometricType.PHOTO
            biometricData.biometricData = item.photo
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        }

        if (item.wsqLt != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LT
            biometricData.biometricData = item.wsqLt
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LT
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLi != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LI
            biometricData.biometricData = item.wsqLi
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LI
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLm != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LM
            biometricData.biometricData = item.wsqLm
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LM
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLr != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LR
            biometricData.biometricData = item.wsqLr
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LR
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLs != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LL
            biometricData.biometricData = item.wsqLs
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LL
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRt != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RT
            biometricData.biometricData = item.wsqRt
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RT
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRi != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RI
            biometricData.biometricData = item.wsqRi
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RI
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRm != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RM
            biometricData.biometricData = item.wsqRm
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RM
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRr != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RR
            biometricData.biometricData = item.wsqRr

            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RR
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRs != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RL
            biometricData.biometricData = item.wsqRs

            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RL
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        return biometrics

    }

    public fun toBiometricEntityFromdbForAlternate(item: com.xplo.code.data.db.room.model.Biometric): List<com.kit.integrationmanager.model.Biometric?> {

        val applicationId = item.applicationId
        val biometrics = mutableListOf<com.kit.integrationmanager.model.Biometric?>()


        if (item.photo != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId

            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }

            biometricData.biometricType = BiometricType.PHOTO
            biometricData.biometricData = item.photo
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        }

        if (item.wsqLt != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LT
            biometricData.biometricData = item.wsqLt
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LT
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLi != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LI
            biometricData.biometricData = item.wsqLi
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LI
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLm != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LM
            biometricData.biometricData = item.wsqLm
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LM
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLr != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LR
            biometricData.biometricData = item.wsqLr
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LR
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        if (item.wsqLs != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LL
            biometricData.biometricData = item.wsqLs
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.LL
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRt != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RT
            biometricData.biometricData = item.wsqRt
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RT
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRi != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RI
            biometricData.biometricData = item.wsqRi
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RI
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRm != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RM
            biometricData.biometricData = item.wsqRm
            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RM
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRr != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RR
            biometricData.biometricData = item.wsqRr

            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RR
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }
        if (item.wsqRs != null) {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RL
            biometricData.biometricData = item.wsqRs

            biometricData.noFingerPrint = false
            biometrics.add(biometricData)
        } else {
            val biometricData = com.kit.integrationmanager.model.Biometric()
            biometricData.applicationId = applicationId
            if (item.biometricUserType != null) {
                biometricData.biometricUserType =
                    BiometricUserType.getBiometricUserTypeById(item.biometricUserType.toInt())
            }
            biometricData.biometricType = BiometricType.RL
            biometricData.noFingerPrint = true
            if (item.noFingerprintReason != null) {
                biometricData.noFingerprintReason =
                    NoFingerprintReasonEnum.getNoFingerprintReasonById(item.noFingerprintReason.toInt())
            }
            biometricData.noFingerprintReasonText = item.noFingerprintReasonText
            biometrics.add(biometricData)
        }

        return biometrics

    }

    private fun toBiometricEntities(items: HouseholdForm?): List<com.kit.integrationmanager.model.Biometric>? {
        //if (items?.form5?.fingers.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Biometric>()

        val photoBiometric = toBiometricEntity(null, items?.form4?.photoData)
        if (photoBiometric != null) list.add(photoBiometric)

        for (item in items?.form5?.fingers!!) {
            val element = toBiometricEntity(item, null)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    //    public fun toBiometricEntityFromdbForBeneficiary(items: MutableList<com.xplo.code.data.db.room.model.Biometric>): List<com.kit.integrationmanager.model.Biometric>? {
//        //if (items?.form5?.fingers.isNullOrEmpty()) return null
//        val list = arrayListOf<com.kit.integrationmanager.model.Biometric>()
//        for (item in items) {
//            val element = toBiometricEntityFromdbForBeneficiary(item)
//            if (element != null) {
//                list.add(element)
//            }
//        }
//        return list
//    }
//    public fun toBiometricEntityFromdbForAlternate(items: MutableList<com.xplo.code.data.db.room.model.Biometric>): List<com.kit.integrationmanager.model.Biometric>? {
//        //if (items?.form5?.fingers.isNullOrEmpty()) return null
//        val list = arrayListOf<com.kit.integrationmanager.model.Biometric>()
//        for (item in items) {
//            val element = toBiometricEntityFromdbForBeneficiary(item)
//            if (element != null) {
//                list.add(element)
//            }
//        }
//        return list
//    }

    private fun toAlternateBiometricEntities(items: AlternateForm?): List<com.kit.integrationmanager.model.Biometric>? {
        //if (items?.form5?.fingers.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Biometric>()

        val photoBiometric = toBiometricEntity(null, items?.form2?.photoData)
        if (photoBiometric != null) list.add(photoBiometric)

        for (item in items?.form3?.fingers!!) {
            val element = toBiometricEntity(item, null)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    private fun toAlternateBiometricEntitiesLdb(items: com.xplo.code.data.db.room.model.Alternate): List<com.kit.integrationmanager.model.Biometric>? {
        //if (items?.form5?.fingers.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Biometric>()

//        val photoBiometric = toBiometricEntity(null, items?.form2?.photoData)
//        if (photoBiometric != null) list.add(photoBiometric)
//
//        for (item in items?.form3?.fingers!!) {
//            val element = toBiometricEntity(item, null)
//            if (element != null) {
//                list.add(element)
//            }
//        }
        return list
    }


    fun toNomineeItemsLdb(items: MutableList<com.xplo.code.data.db.room.model.Nominee>): List<com.kit.integrationmanager.model.Nominee>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Nominee>()
        for (item in items) {
            val element = toNomineeLdb(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    private fun toNomineeLdb(item: com.xplo.code.data.db.room.model.Nominee): com.kit.integrationmanager.model.Nominee? {
        if (item == null) return null
        val nominee = com.kit.integrationmanager.model.Nominee()

        nominee.applicationId = item.applicationId
        nominee.nomineeFirstName = item.nomineeFirstName
        nominee.nomineeLastName = item.nomineeLastName
        nominee.nomineeNickName = item.nomineeNickName
        nominee.nomineeMiddleName = item.nomineeMiddleName

        nominee.nomineeAge = item.nomineeAge
        if (item.nomineeGender != null) {
            nominee.nomineeGender = GenderEnum.getGenderById(item.nomineeGender.toInt() + 1)
        }

        //  nominee.nomineeOccupation = OccupationEnum.valueOf(item.nomineeOccupation.toString())
        nominee.otherOccupation = item.otherOccupation
        if (item.relationshipWithHouseholdHead != null) {
            nominee.relationshipWithHouseholdHead =
                RelationshipEnum.getRelationById(item.relationshipWithHouseholdHead.toInt() + 1)
        }
        nominee.isReadWrite = item.isReadWrite

        Log.d(TAG, "toNomineeLdb: ${item.nomineeFirstName}")

        return nominee
    }


    fun toNomineeItems(items: ArrayList<Nominee>?): List<com.kit.integrationmanager.model.Nominee>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<com.kit.integrationmanager.model.Nominee>()
        for (item in items) {
            val element = toNominee(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    private fun toNominee(item: Nominee): com.kit.integrationmanager.model.Nominee? {
        if (item == null) return null
        val nominee = com.kit.integrationmanager.model.Nominee()

        nominee.applicationId = ""

        nominee.nomineeFirstName = item.firstName
        nominee.nomineeLastName = item.lastName
        nominee.nomineeNickName = item.nickName
        nominee.nomineeMiddleName = item.middleName
//        nominee.nomineeMiddleName = FakeMapperValue.name

        nominee.nomineeAge = item.age
        nominee.nomineeGender = GenderEnum.find(item.gender)

        nominee.nomineeOccupation = OccupationEnum.FORMAL_JOB
        nominee.otherOccupation = "otoc"
        nominee.relationshipWithHouseholdHead = RelationshipEnum.find(item.relation)

        nominee.isReadWrite = getReadWrite(item.isReadWrite)

        return nominee
    }

    fun toAddress(item: Address?): com.kit.integrationmanager.model.Address? {
        if (item == null) return null
        val address = com.kit.integrationmanager.model.Address()
        address.stateId = item.stateId
        address.countyId = item.countyId
        address.payam = item.payamId
        address.boma = item.bomaId
        return address
    }

    fun toLocation(item: Location?): com.kit.integrationmanager.model.Location? {
        if (item == null) return null
        val location = com.kit.integrationmanager.model.Location()
        location.lat = item.lat
        location.lon = item.lon
        return location
    }

    fun toHouseholdMember2(
        id: String,
        item: HhForm3?
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        household.applicationId = id
        household.totalMale = item.mem0TotalMale
        household.totalFemale = item.mem0TotalFemale
        household.femaleChronicalIll = item.mem0IllFemale
        household.femaleDisable = item.mem0DisableFemale
        household.femaleNormal = item.mem0NormalFemale
        household.maleChronicalIll = item.mem0IllMale
        household.maleDisable = item.mem0DisableMale
        household.maleNormal = item.mem0NormalMale
        return household
    }

    fun toHouseholdMember2Ldb(
        id: String,
        item: MutableList<HouseholdInfo>,
        type: String
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        for (value in item) {
            if (value.type == type) {
                household.applicationId = id
                household.totalMale = value.maleTotal
                household.totalFemale = value.femaleTotal
                household.femaleChronicalIll = value.femaleChronicalIll
                household.femaleDisable = value.femaleDisable
                household.femaleNormal = value.femaleNormal
                household.maleChronicalIll = value.maleChronicalIll
                household.maleDisable = value.maleDisable
                household.maleNormal = value.maleNormal
            }
        }
        return household
    }

    private fun toHouseholdMember35(
        id: String,
        item: HhForm3?
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        household.applicationId = id
        household.totalMale = item.mem18TotalMale
        household.totalFemale = item.mem18TotalFemale
        household.femaleChronicalIll = item.mem18IllFemale
        household.femaleDisable = item.mem18DisableFemale
        household.femaleNormal = item.mem18NormalFemale
        household.maleChronicalIll = item.mem18IllMale
        household.maleDisable = item.mem18DisableMale
        household.maleNormal = item.mem18NormalMale
        return household
    }

    private fun toHouseholdMember64(
        id: String,
        item: HhForm3?
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        household.applicationId = id
        household.totalMale = item.mem36TotalMale
        household.totalFemale = item.mem36TotalFemale
        household.femaleChronicalIll = item.mem36IllFemale
        household.femaleDisable = item.mem36DisableFemale
        household.femaleNormal = item.mem36NormalFemale
        household.maleChronicalIll = item.mem36IllMale
        household.maleDisable = item.mem36DisableMale
        household.maleNormal = item.mem36NormalMale
        return household
    }

    private fun toHouseholdMember65(
        id: String,
        item: HhForm3?
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        household.applicationId = id
        household.totalMale = item.mem65TotalMale
        household.totalFemale = item.mem65TotalFemale
        household.femaleChronicalIll = item.mem65IllFemale
        household.femaleDisable = item.mem65DisableFemale
        household.femaleNormal = item.mem65NormalFemale
        household.maleChronicalIll = item.mem65IllMale
        household.maleDisable = item.mem65DisableMale
        household.maleNormal = item.mem65NormalMale
        return household
    }

    private fun toHouseholdMember17(
        id: String,
        item: HhForm3?
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        household.applicationId = id
        household.totalMale = item.mem6TotalMale
        household.totalFemale = item.mem6TotalFemale
        household.femaleChronicalIll = item.mem6IllFemale
        household.femaleDisable = item.mem6DisableFemale
        household.femaleNormal = item.mem6NormalFemale
        household.maleChronicalIll = item.mem6IllMale
        household.maleDisable = item.mem6DisableMale
        household.maleNormal = item.mem6NormalMale
        return household
    }

    private fun toHouseholdMember5(
        id: String,
        item: HhForm3?
    ): com.kit.integrationmanager.model.HouseholdMember? {
        if (item == null) return null
        val household = com.kit.integrationmanager.model.HouseholdMember()
        household.applicationId = id
        household.totalMale = item.mem3TotalMale
        household.totalFemale = item.mem3TotalFemale
        household.femaleChronicalIll = item.mem3IllFemale
        household.femaleDisable = item.mem3DisableFemale
        household.femaleNormal = item.mem3NormalFemale
        household.maleChronicalIll = item.mem3IllMale
        household.maleDisable = item.mem3DisableMale
        household.maleNormal = item.mem3NormalMale
        return household
    }

    fun toBeneficiaryEntity(item: HouseholdForm?): BeneficiaryEntity? {
        Log.d(TAG, "toBeneficiaryEntity() called with: item = $item")
        if (item == null) return null

        val applicationId = item.id

        val form = BeneficiaryEntity(
            id = item.id,
            hid = item.hid,
            isSynced = false,

            applicationId = applicationId,
            address = Address(
                stateId = item.form1?.state?.id,
                countyId = item.form1?.county?.id,
                payamId = item.form1?.payam?.id,
                bomaId = item.form1?.boma?.id
            ),
            location = Location(
                lat = item.form1?.lat,
                lon = item.form1?.lon
            ),

            respondentFirstName = item.form2?.firstName,
            respondentMiddleName = item.form2?.middleName,
            respondentLastName = item.form2?.lastName,

            respondentAge = item.form2?.age ?: 0,
            respondentGender = GenderEnum.find(item.form2?.gender),
            //documentTypeEnum = DocumentTypeEnum.find(item.form2?.idNumberType),

            respondentLegalStatus = LegalStatusEnum.find(item.form2?.legalStatus),
            respondentMaritalStatus = MaritalStatusEnum.find(item.form2?.maritalStatus),

            respondentId = item.form2?.idNumber,
            respondentPhoneNo = item.form2?.phoneNumber,

            relationshipWithHouseholdHead = RelationshipEnum.find(item.form2?.respondentRlt),

            currency = CurrencyEnum.find(item.form2?.currency),
            householdIncomeSource = IncomeSourceEnum.find(item.form2?.mainSourceOfIncome),
            householdMonthlyAvgIncome = item.form2?.monthlyAverageIncome ?: 0,


            selectionCriteria = SelectionCriteriaEnum.find(item.form2?.selectionCriteria),
            selectionReason = SelectionReasonEnum.find(item.form2?.selectionReason),

            spouseFirstName = item.form2?.spouseFirstName,
            spouseLastName = item.form2?.spouseLastName,
            spouseMiddleName = item.form2?.spouseMiddleName,

            householdSize = item.form3?.householdSize ?: 0,

            householdMember2 = toHouseholdMemberEntity(
                item.form3?.male0_2,
                item.form3?.female0_2,
                applicationId
            ),
            householdMember5 = toHouseholdMemberEntity(
                item.form3?.male3_5,
                item.form3?.female3_5,
                applicationId
            ),
            householdMember17 = toHouseholdMemberEntity(
                item.form3?.male6_17,
                item.form3?.female6_17,
                applicationId
            ),
            householdMember35 = toHouseholdMemberEntity(
                item.form3?.male18_35,
                item.form3?.female18_35,
                applicationId
            ),
            householdMember64 = toHouseholdMemberEntity(
                item.form3?.male36_64,
                item.form3?.female36_64,
                applicationId
            ),
            householdMember65 = toHouseholdMemberEntity(
                item.form3?.male65p,
                item.form3?.female65p,
                applicationId
            ),

            alternates = toAlternateEntityItems(item.alternates, applicationId),
            biometrics = toBiometricEntityItemsFromHouseholdForm(item, applicationId),

            isReadWrite = item.form3?.isReadWrite.toBoolean(),
            memberReadWrite = item.form3?.householdSize ?: 0,

            isOtherMemberPerticipating = item.form6?.isNomineeAdd.isYes(),
            notPerticipationReason = NonPerticipationReasonEnum.find(item.form6?.noNomineeReason),
            notPerticipationOtherReason = item.form6?.otherReason,
            nominees = toNomineeEntityItems(item.form6?.nominees, applicationId)

        )


        Log.d(TAG, "toBeneficiaryEntity: return: ${form.toJson()}")
        return form
    }


    fun toBeneficiaryEntityItems(items: List<HouseholdForm>?): List<BeneficiaryEntity>? {
        Log.d(TAG, "toBeneficiaryEntityItems() called with: items = ${items?.size}")
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<BeneficiaryEntity>()
        for (item in items) {
            val element = toBeneficiaryEntity(item)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    fun toHouseholdMemberEntity(
        male: HhMember?,
        female: HhMember?,
        id: String?
    ): HouseholdMember {
        if (male == null || female == null) return HouseholdMember(applicationId = id)
        return HouseholdMember(
            applicationId = id,

            maleNormal = male.normal,
            maleChronicalIll = male.ill,
            maleDisable = male.disable,
            totalMale = male.getTotal(),

            femaleNormal = female.normal,
            femaleChronicalIll = female.ill,
            femaleDisable = female.disable,
            totalFemale = female.getTotal()

        )
    }

    fun toNomineeEntity(
        item: Nominee?,
        id: String?,
    ): com.xplo.code.data_module.model.content.Nominee? {
        if (item == null) return null
        return com.xplo.code.data_module.model.content.Nominee(
            applicationId = id,

            nomineeFirstName = item.firstName,
            nomineeLastName = item.lastName,
            nomineeMiddleName = item.middleName,

            nomineeAge = item.age ?: 0,
            nomineeGender = GenderEnum.find(item.gender),

            nomineeOccupation = OccupationEnum.FORMAL_JOB,
            otherOccupation = item.occupation,
            relationshipWithHouseholdHead = RelationshipEnum.find(item.relation),

            isReadWrite = item.isReadWrite.isYes()
        )
    }

    fun toNomineeEntityItems(
        items: List<Nominee>?,
        id: String?,
    ): ArrayList<com.xplo.code.data_module.model.content.Nominee>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<com.xplo.code.data_module.model.content.Nominee>()
        for (item in items) {
            val element = toNomineeEntity(item, id)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    fun toAlternateEntity(
        item: AlternateForm?,
        id: String?,
    ): Alternate? {
        if (item == null) return null
        return Alternate(
            //applicationId = id,
            nationalId = item.form1?.idNumber,

            payeeName = item.form1?.getFullName(),
            payeeFirstName = item.form1?.alternateFirstName,
            payeeMiddleName = item.form1?.alternateMiddleName,
            payeeLastName = item.form1?.alternateLastName,
            payeeNickName = item.form1?.alternateNickName,
            payeeAge = item.form1?.age ?: 0,
            payeeGender = GenderEnum.find(item.form1?.gender),
            payeePhoneNo = item.form1?.phoneNumber,
            biometrics = toBiometricEntityItemsFromAlternateForm(item, id)
        )
    }

    fun toAlternateEntityItems(
        items: List<AlternateForm>?,
        id: String?,
    ): ArrayList<Alternate>? {
        if (items.isNullOrEmpty()) return null
        val list = arrayListOf<Alternate>()
        for (item in items) {
            val element = toAlternateEntity(item, id)
            if (element != null) {
                list.add(element)
            }
        }
        return list
    }

    fun toBiometricEntityFromFinger(
        item: Finger?,
        id: String?
    ): Biometric? {
        if (item == null) return null
        if (id == null) return null
        //if (item.fingerPrint.isNullOrEmpty()) return null
        // if (item.fingerType.isNullOrEmpty()) return null
        //if (item.userType.isNullOrEmpty()) return null

        //noFingerprintReason = NoFingerprintReasonEnum.find(item.noFingerprintReason)
        return Biometric(
            applicationId = id,
            biometricType = returnFingerPrintEnum(item.fingerType),
            biometricUserType = BiometricUserType.valueOf(item.userType!!),
            biometricData = if (item.fingerPrint == null) null else item.fingerPrint,
            noFingerPrint = item.noFingerprint,
            noFingerprintReason = if (item.noFingerprint) NoFingerprintReasonEnum.NoFinger else null,
            biometricUrl = null
        )
    }

    fun returnFingerPrintEnum(finger: String?): BiometricType {
        if (finger.equals("LT")) {
            return BiometricType.LT
        } else if (finger.equals("LI")) {
            return BiometricType.LI
        } else if (finger.equals("LM")) {
            return BiometricType.LM
        } else if (finger.equals("LR")) {
            return BiometricType.LR
        } else if (finger.equals("LS")) {
            return BiometricType.LL
        } else if (finger.equals("RT")) {
            return BiometricType.RT
        } else if (finger.equals("RI")) {
            return BiometricType.RI
        } else if (finger.equals("RM")) {
            return BiometricType.RM
        } else if (finger.equals("RR")) {
            return BiometricType.RR
        } else if (finger.equals("RS")) {
            return BiometricType.RL
        } else {
            return BiometricType.NA
        }
    }

    fun toBiometricEntityFromPhoto(
        item: PhotoData?,
        id: String?
    ): Biometric? {
        if (item == null) return null
        if (id == null) return null
        if (item.img?.isEmpty() == true) return null
        if (item.userType.isNullOrEmpty()) return null

        return Biometric(
            applicationId = id,
            biometricType = BiometricType.PHOTO,
            biometricUserType = BiometricUserType.valueOf(item.userType!!), //"ALTERNATE",

            biometricData = item.img,
            noFingerPrint = false,
            noFingerprintReason = null,
            noFingerprintReasonText = null,

            biometricUrl = null
        )
    }

    fun toBiometricEntityItemsFromAlternateForm(
        item: AlternateForm?,
        id: String?,
    ): ArrayList<Biometric>? {
        if (item == null) return null
        val items = arrayListOf<Biometric>()

        val fingers = item.form3?.fingers

        fingers?.let {
            for (finger in fingers) {
                val bitem = toBiometricEntityFromFinger(finger, id)
                if (bitem != null) {
                    items.add(bitem)
                }
            }
        }

        val photoBiometric = toBiometricEntityFromPhoto(item.form2?.photoData, id)
        if (photoBiometric != null) items.add(photoBiometric)

        return items
    }


    fun toBiometricEntityItemsFromHouseholdForm(
        item: HouseholdForm?,
        id: String?,
    ): ArrayList<Biometric>? {
        if (item == null) return null
        val items = arrayListOf<Biometric>()

        val fingers = item.form5?.fingers

        val photoBiometric = toBiometricEntityFromPhoto(item.form4?.photoData, id)
        if (photoBiometric != null) items.add(photoBiometric)

        fingers?.let {
            for (finger in fingers) {
                val bitem = toBiometricEntityFromFinger(finger, id)
                if (bitem != null) {
                    items.add(bitem)
                }
            }
        }

        return items
    }

}