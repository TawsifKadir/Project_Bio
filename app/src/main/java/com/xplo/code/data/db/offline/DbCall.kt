package com.xplo.code.data.db.offline

import com.xplo.code.core.RspCallback

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
interface DbCall {


    fun getItem(table: String, id: Int): ODbItem?

    fun getItemsByTable(
        table: String,
        callback: RspCallback<List<ODbItem>>?
    )

    fun getItemsByColumn(
        table: String,
        column: String,
        columnValue: String,
        callback: RspCallback<List<ODbItem>>?
    )

    fun getItemsBySql(
        sql: String,
        args: Array<String>?,
        callback: RspCallback<List<ODbItem>>?
    )

    fun getOptionItems(
        table: String,
        column: String,
        argColName: String?,
        argColValue: String?
    ): List<OptionItem>

    fun getOptionItems2(
        table: String,
        columnCode: String,
        columnTitle: String,
        argColName: String?,
        argColValue: String?
    ): List<OptionItem>


}