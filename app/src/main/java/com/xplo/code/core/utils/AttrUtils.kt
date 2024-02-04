package com.xplo.code.core.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue

/**
 * Copyright 2020 (C) Xplo
 *
 * Created  : 2020/09/02
 * Author   : xplo
 * Desc     :
 * Comment  :
 */
object AttrUtils {

    private const val TAG = "AttrUtils"


    fun getAttrColor(context: Context, attributeId: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attributeId, typedValue, true)
        val resourceId = typedValue.resourceId
        var res = -1
        try {
            res = context.resources.getColor(resourceId)
        } catch (e: Resources.NotFoundException) {
            Log.e(
                TAG,
                "Not found resource by id: $resourceId"
            )
        }
        return res
    }

    fun getAttrDrawable(context: Context, attributeId: Int): Drawable? {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attributeId, typedValue, true)
        val resourceId = typedValue.resourceId
        var res: Drawable? = null
        try {
            res = context.resources.getDrawable(resourceId)
        } catch (e: Resources.NotFoundException) {
            Log.e(
                TAG,
                "Not found resource by id: $resourceId"
            )
        }
        return res
    }


}
