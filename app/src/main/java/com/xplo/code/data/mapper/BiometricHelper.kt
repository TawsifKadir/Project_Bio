package com.xplo.code.data.mapper

import android.content.Intent
import com.faisal.fingerprintcapture.model.FingerprintData
import com.kit.integrationmanager.model.NoFingerprintReasonEnum
import com.xplo.code.ui.dashboard.model.Finger
import java.util.Base64

/**
 * Copyright 2022 (C) xplo
 *
 * Created  : 22/06/28
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object BiometricHelper {

    val fingerNames = arrayOf(
        "right_thumb",
        "right_index",
        "right_middle",
        "right_ring",
        "right_small",
        "left_thumb",
        "left_index",
        "left_middle",
        "left_ring",
        "left_small"
    )

    val fingerCodes = arrayOf(
        "RT",
        "RI",
        "RM",
        "RR",
        "RS",

        "LT",
        "LI",
        "LM",
        "LR",
        "LS"
    )


    fun fingerPrintIntentToFingerItems(intent: Intent?, userType: String?): List<Finger>? {
        if (intent == null) return null

        var reason = fingerPrintIntentToNoFingerprintReason(intent, userType)

        val items = arrayListOf<Finger>()

        for (i in fingerNames.indices) {
            val fingerName = fingerNames[i]

            val fingerprintData = intent.getParcelableExtra(fingerName) as FingerprintData?
//            if (fingerprintData == null) continue
//            if (fingerprintData.fingerprintData == null) continue

            if (fingerprintData == null || fingerprintData.fingerprintData == null) {
                val dfinger = Finger(
                    fingerId = fingerprintData?.fingerprintId.toString(),
                    fingerPrint = null,
                    fingerType = fingerCodes[i],
                    userType = userType,
                    noFingerprint = true,
                    noFingerprintReason = reason
                )
                items.add(dfinger)
                continue
            }

            val finger = Finger(
                fingerId = fingerprintData.fingerprintId.toString(),
                fingerPrint = Base64.getEncoder().encodeToString(fingerprintData.fingerprintData),
                fingerType = fingerCodes[i],
                userType = userType
            )

            items.add(finger)

        }

        return items

    }


    fun fingerPrintIntentToNoFingerprintReason(intent: Intent?, userType: String?): String? {
        if (intent == null) return NoFingerprintReasonEnum.Other.name
        return NoFingerprintReasonEnum.NoFinger.name

    }


//    fun addReasonToBiometricItems(
//        items: List<Biometric>?,
//        reason: String?,
//        id: String?,
//        userType: String
//    ): List<Biometric>? {
//        if (items.isNullOrEmpty()) return items
//        if (reason == null) return items
//
//        val list = arrayListOf<Biometric>()
//
//        val photoBiometric = getABiometricByType(items, BiometricType.PHOTO.name)
//        if (photoBiometric != null) {
//            list.add(photoBiometric)
//        }
//
//        for (i in fingerCodes.indices) {
//            val item = getABiometricByType(items, fingerCodes[i])
//            if (item?.isContainValidBiometric().toBool()) {
//                list.add(item!!)
//            } else {
//                list.add(createABiometricWithOnlyReason(reason, id, fingerCodes[i], userType))
//            }
//
//        }
//        return items
//    }
//
//    fun getABiometricByType(items: List<Biometric>?, biometricType: String?): Biometric? {
//        if (items.isNullOrEmpty()) return null
//        if (biometricType.isNullOrEmpty()) return null
//
//        for (item in items) {
//            if (item.biometricType?.name.equals(biometricType, true)) return item
//        }
//
//        return null
//    }
//
//    fun createABiometricWithOnlyReason(
//        reason: String?,
//        id: String?,
//        biometricType: String?,
//        userType: String
//    ): Biometric {
//        return Biometric(
//            applicationId = id,
//            biometricType = BiometricType.find(biometricType),
//            biometricUserType = BiometricUserType.valueOf(userType),
//            biometricData = null,
//            noFingerPrint = true,
//            noFingerprintReason = NoFingerprintReasonEnum.find(reason),
//            biometricUrl = null
//        )
//    }

}