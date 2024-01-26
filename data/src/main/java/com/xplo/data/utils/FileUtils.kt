package com.xplo.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileUtils {

    fun reduceFileSize(file: File): File {
        try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()


            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                outputStream
            )
            return file
        } catch (e: Exception) {
            //Log.e("error",e.getMessage());
            return file
        }
    }
}
