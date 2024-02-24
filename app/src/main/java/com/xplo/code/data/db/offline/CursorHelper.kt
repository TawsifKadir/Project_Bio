package com.xplo.code.data.db.offline

import android.database.Cursor
import android.util.Log

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : Nasif Ahmed
 * Desc     :
 * Comment  :
 */
@Suppress("LocalVariableName")
class CursorHelper {

    companion object {
        private const val TAG = "CursorHelper"


        private var COLUMN_ID = Column._id.name

        private var COLUMN_S_CODE = Column.s_code.name
        private var COLUMN_STATE = Column.state.name

        private var COLUMN_C_CODE = Column.c_code.name
        private var COLUMN_COUNTRY = Column.county.name

        private var COLUMN_P_CODE = Column.p_code.name
        private var COLUMN_PAYAM = Column.payam.name

        private var COLUMN_B_CODE = Column.b_code.name
        private var COLUMN_BOMA = Column.boma.name


        fun toODbItem(cursor: Cursor?): ODbItem? {
            Log.d(TAG, "toODbItem() called with: cursor = $cursor")
            if (cursor == null) return null

            //val INDEX_ID = cursor.getColumnIndex(COLUMN_ID)
            val INDEX_S_CODE = cursor.getColumnIndex(COLUMN_S_CODE)
            val INDEX_STATE = cursor.getColumnIndex(COLUMN_STATE)
            val INDEX_C_CODE = cursor.getColumnIndex(COLUMN_C_CODE)
            val INDEX_COUNTRY = cursor.getColumnIndex(COLUMN_COUNTRY)
            val INDEX_P_CODE = cursor.getColumnIndex(COLUMN_P_CODE)
            val INDEX_PAYAM = cursor.getColumnIndex(COLUMN_PAYAM)
            val INDEX_B_CODE = cursor.getColumnIndex(COLUMN_B_CODE)
            val INDEX_BOMA = cursor.getColumnIndex(COLUMN_BOMA)

            var id = 1
            var stateCode = 0
            var state = ""
            var countryCode = 0
            var country = ""
            var payamCode = 0
            var payam = ""
            var bomaCode = 0
            var boma = ""


            cursor.moveToFirst()

            //if (INDEX_ID != -1) id = cursor.getInt(INDEX_ID)
            if (INDEX_S_CODE != -1) stateCode = cursor.getInt(INDEX_S_CODE)
            if (INDEX_STATE != -1) state = cursor.getString(INDEX_STATE)
            if (INDEX_C_CODE != -1) countryCode = cursor.getInt(INDEX_C_CODE)
            if (INDEX_COUNTRY != -1) country = cursor.getString(INDEX_COUNTRY)
            if (INDEX_P_CODE != -1) payamCode = cursor.getInt(INDEX_P_CODE)
            if (INDEX_PAYAM != -1) payam = cursor.getString(INDEX_PAYAM)
            if (INDEX_B_CODE != -1) bomaCode = cursor.getInt(INDEX_B_CODE)
            if (INDEX_BOMA != -1) boma = cursor.getString(INDEX_BOMA)

            return ODbItem(
                id,
                stateCode,
                state,
                countryCode,
                country,
                payamCode,
                payam,
                bomaCode,
                bomaCode
            )

        }

        fun toODbItems(cursor: Cursor?): ArrayList<ODbItem> {
            Log.d(TAG, "toODbItems() called with: cursor = $cursor")
            val items = ArrayList<ODbItem>()

            if (cursor == null) return items

            //val INDEX_ID = cursor.getColumnIndex(COLUMN_ID)
            val INDEX_S_CODE = cursor.getColumnIndex(COLUMN_S_CODE)
            val INDEX_STATE = cursor.getColumnIndex(COLUMN_STATE)
            val INDEX_C_CODE = cursor.getColumnIndex(COLUMN_C_CODE)
            val INDEX_COUNTRY = cursor.getColumnIndex(COLUMN_COUNTRY)
            val INDEX_P_CODE = cursor.getColumnIndex(COLUMN_P_CODE)
            val INDEX_PAYAM = cursor.getColumnIndex(COLUMN_PAYAM)
            val INDEX_B_CODE = cursor.getColumnIndex(COLUMN_B_CODE)
            val INDEX_BOMA = cursor.getColumnIndex(COLUMN_BOMA)

            var id = 1
            var stateCode = 0
            var state = ""
            var countryCode = 0
            var country = ""
            var payamCode = 0
            var payam = ""
            var bomaCode = 0
            var boma = ""



            cursor.moveToFirst()
            while (!cursor.isAfterLast) {

                //if (INDEX_ID != -1) id = cursor.getInt(INDEX_ID)
                if (INDEX_S_CODE != -1) stateCode = cursor.getInt(INDEX_S_CODE)
                if (INDEX_STATE != -1) state = cursor.getString(INDEX_STATE)
                if (INDEX_C_CODE != -1) countryCode = cursor.getInt(INDEX_C_CODE)
                if (INDEX_COUNTRY != -1) country = cursor.getString(INDEX_COUNTRY)
                if (INDEX_P_CODE != -1) payamCode = cursor.getInt(INDEX_P_CODE)
                if (INDEX_PAYAM != -1) payam = cursor.getString(INDEX_PAYAM)
                if (INDEX_B_CODE != -1) bomaCode = cursor.getInt(INDEX_B_CODE)
                if (INDEX_BOMA != -1) boma = cursor.getString(INDEX_BOMA)


                items.add(
                    ODbItem(
                        id,
                        stateCode,
                        state,
                        countryCode,
                        country,
                        payamCode,
                        payam,
                        bomaCode,
                        bomaCode
                    )
                )
                cursor.moveToNext()

            }

            return items

        }

        fun toOptionItem(cursor: Cursor?, columnCode: String?, columnTitle: String?): ArrayList<OptionItem> {
            Log.d(
                TAG,
                "toOptionItem() called with: cursor = $cursor, columnCode = $columnCode, columnTitle = $columnTitle"
            )
            val items = ArrayList<OptionItem>()

            if (cursor == null) return items

            var code = 0
            var name = ""

            val colIndCode = cursor.getColumnIndex(columnCode)
            val colIndTitle = cursor.getColumnIndex(columnTitle)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                code = cursor.getInt(colIndTitle-1)
                name = cursor.getString(colIndTitle)
                items.add(
                    OptionItem(
                        code,
                        name
                    )
                )

                //if(items.size>10) break
                cursor.moveToNext()

            }
            return items

        }

    }


}