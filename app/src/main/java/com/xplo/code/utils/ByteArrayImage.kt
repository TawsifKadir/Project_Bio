package com.xplo.code.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
object ImageUtils {
    fun convertDrawableToByteArray(context: Context, drawableId: Int): ByteArray {
        // Read the image from the drawable resource
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

        // Convert Bitmap to ByteArray
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}