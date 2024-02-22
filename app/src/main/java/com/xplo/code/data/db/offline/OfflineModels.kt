package com.xplo.code.data.db.offline


import java.io.Serializable

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
data class ODbItem(
    var id: Int = 0,

    var stateCode: Int,
    var state: String,

    var countryCode: Int,
    var country: String,

    var payamCode: Int,
    var payam: String,

    var bomaCode: Int,
    var boma: Int

) : Serializable


data class OptionItem(

    var code: Int,
    var name: String

) : Serializable


fun List<OptionItem>?.toStringArray() : Array<String>   {
    if (this.isNullOrEmpty()) return arrayOf()
    return this.map { it.name }.toTypedArray()
}

fun List<OptionItem>?.addSpinnerHeader(txt: String = "Select an option") : List<OptionItem>   {
    if (this.isNullOrEmpty()) return listOf()
    val items = this.toMutableList()
    items.add(0, OptionItem(0, txt))
    return items
}

fun List<OptionItem>?.toSpinnerOptions() : Array<String>   {
    if (this.isNullOrEmpty()) return arrayOf()
    val items = this.map { it.name }.toTypedArray()
    return items
}



