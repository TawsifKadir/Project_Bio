package com.xplo.code.data.db.offline

import android.util.Log
import com.xplo.code.core.Contextor
import com.xplo.code.core.RspCallback
import com.xplo.code.core.utils.DbHelper


/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
class DbCallImpl : DbCall {

    companion object {
        private const val TAG = "DbCallImpl"

    }


    private val dbHelper = DbHelper(Contextor.getInstance().context, "odbv2.db")

    override fun getItem(table: String, id: Int): ODbItem? {
        Log.d(TAG, "getItem() called with: table = $table, id = $id")

        val sql = "select * from $table where _id = ?"
        val args = arrayOf(id.toString())

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, args)
            return CursorHelper.toODbItem(cursor)
        } catch (e: Exception) {
            return null
        } finally {
            dbHelper.close()
        }
    }

    fun getItemName(columnValue: String, peram: String, id: Int, columnCode: Int): String? {
        val sql = "SELECT DISTINCT $columnValue FROM state WHERE $peram = ?"
        val args = arrayOf(id.toString())
        var value: String? = null

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, args)
            if (cursor.moveToNext()) {
                value = cursor.getString(columnCode)
                Log.d(TAG, "getItemName: $value")
            }
        } catch (e: Exception) {
            // Handle the exception, if necessary
            Log.e(TAG, "Error retrieving data from the database", e)
        } finally {
            dbHelper.close()
        }

        return value
    }


    override fun getItemsByTable(table: String, callback: RspCallback<List<ODbItem>>?) {
        Log.d(TAG, "getItemsByTable() called with: table = $table, callback = $callback")

        val sql = "select * from $table"
        val args = arrayOf("0")

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, args)
            val items = CursorHelper.toODbItems(cursor)
            callback?.onSuccess(items)
        } catch (e: Exception) {
            callback?.onFailure(Throwable(e))
        } finally {
            dbHelper.close()
        }
    }

    override fun getItemsByColumn(
        table: String,
        column: String,
        columnValue: String,
        callback: RspCallback<List<ODbItem>>?
    ) {
        Log.d(
            TAG,
            "getItemsByColumn() called with: table = $table, column = $column, columnValue = $columnValue, callback = $callback"
        )

        val sql = "select * from $table where $column = ?"
        val args = arrayOf(columnValue)

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, args)
            val items = CursorHelper.toODbItems(cursor)
            callback?.onSuccess(items)
        } catch (e: Exception) {
            callback?.onFailure(Throwable(e))
        } finally {
            dbHelper.close()
        }

    }

    override fun getItemsBySql(
        sql: String,
        args: Array<String>?,
        callback: RspCallback<List<ODbItem>>?
    ) {
        Log.d(TAG, "getItemsBySql() called with: sql = $sql, args = $args, callback = $callback")

//        String sql = "select * from " + table + " where category = ?";
//        String[] selectionArgs = {category};

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, args)
            val items = CursorHelper.toODbItems(cursor)
            callback?.onSuccess(items)
        } catch (e: Exception) {
            callback?.onFailure(Throwable(e))
        } finally {
            dbHelper.close()
        }


    }

    override fun getOptionItems(
        table: String,
        column: String,
        argColName: String?,
        argColValue: String?
    ): List<OptionItem> {

        //var sql = "select distinct $column from $table"
        var sql = "select distinct $column from $table where $argColName = '$argColValue'"

        if (argColName.isNullOrEmpty() || argColValue.isNullOrEmpty()) {
            sql = "select distinct $column from $table"
        }

        Log.d(TAG, "getOptionItems: $sql")

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, null)

            val items = CursorHelper.toOptionItem(cursor, column, column)
            return items
        } catch (e: Exception) {
            return listOf()
        } finally {
            dbHelper.close()
        }
    }


    override fun getOptionItems2(
        table: String,
        columnCode: String,
        columnTitle: String,
        argColName: String?,
        argColValue: String?
    ): List<OptionItem> {

        //var sql = "select distinct $column from $table"
        var sql =
            "select distinct $columnCode, $columnTitle from $table where $argColName = '$argColValue'"

        if (argColName.isNullOrEmpty() || argColValue.isNullOrEmpty()) {
            sql = "select distinct $columnCode, $columnTitle from $table"
        }

        Log.d(TAG, "getOptionItems: $sql")

        dbHelper.openDataBase()
        try {
            val cursor = dbHelper.runQueryRaw(sql, null)
            val items = CursorHelper.toOptionItem(cursor, columnCode, columnTitle)
            return items
        } catch (e: Exception) {
            return listOf()
        } finally {
            dbHelper.close()
        }
    }


}