package com.xplo.code.network.fake
import android.annotation.SuppressLint
import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.ObjectMapper
import com.kit.integrationmanager.model.Beneficiary
import com.xplo.code.R
import com.xplo.code.core.Contextor
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Copyright 2020 (C) xplo
 *
 * Created  : 3/16/20
 * Updated  :
 * Author   : xplo
 * Desc     :
 * Comment  :
 */

@SuppressLint("StaticFieldLeak")
object Fake {

    var context = Contextor.getInstance().context
    fun getABenificiary(): Beneficiary? {

        val inputStream: InputStream = context.resources.openRawResource(R.raw.single_reg)
        val br = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val mapper = ObjectMapper()
        val beneficiary: Beneficiary = mapper.readValue(br, Beneficiary::class.java)
        return beneficiary

//        val txt = context.resources.openRawResource(R.raw.single_reg_mod)
//            .bufferedReader().use { it.readText() }
//
//        val beneficiary: Beneficiary = Gson().fromJson(txt, Beneficiary::class.java)
//
//
//        return null
    }

}