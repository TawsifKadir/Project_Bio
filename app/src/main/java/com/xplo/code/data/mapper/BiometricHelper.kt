package com.xplo.code.data.mapper

import android.content.Intent
import com.faisal.fingerprintcapture.model.FingerprintData
import com.xplo.code.ui.dashboard.model.Finger

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

        val items = arrayListOf<Finger>()

        for (i in fingerNames.indices) {
            val fingerName = fingerNames[i]

            val fingerprintData = intent.getParcelableExtra(fingerName) as FingerprintData?
            if (fingerprintData == null) continue
            if (fingerprintData.fingerprintData == null) continue

            val finger = Finger(
                fingerId = fingerprintData.fingerprintId.toString(),
                fingerPrint = fingerprintData.fingerprintData.toString(),
                fingerType = fingerCodes[i],
                userType = userType
            )

            items.add(finger)

        }

        return items

    }


}