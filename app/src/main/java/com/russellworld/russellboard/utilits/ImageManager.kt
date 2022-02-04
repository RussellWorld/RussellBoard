package com.russellworld.russellboard.utilits

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageManager {
    private const val MAX_IMAGE_SIZE = 1280
    const val WIDTH = 0
    const val HEIGHT = 1

    fun getImageSize(uri: String): ArrayList<Int> {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(uri, options)

        return if (imageRotation(uri) == 90)
            arrayListOf(options.outHeight, options.outWidth)
        else arrayListOf(options.outWidth, options.outHeight)
    }

    private fun imageRotation(uri: String): Int {
        val rotation: Int
        val imageFile = File(uri)
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        rotation = if (orientation ==
            ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270
        ) {
            90
        } else {
            0
        }
        return rotation
    }

    suspend fun imageResize(uris: ArrayList<String>): String = withContext(Dispatchers.IO){
        val tempList = ArrayList<ArrayList<Int>>()
        for (n in uris.indices) {
            val size = getImageSize(uris[n])
            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat()

            if (imageRatio > 1) {
                if (size[WIDTH] > MAX_IMAGE_SIZE) {
                    tempList.add(arrayListOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
                } else {
                    tempList.add(arrayListOf(size[WIDTH], size[HEIGHT]))
                }
            } else {
                if (size[HEIGHT] > MAX_IMAGE_SIZE) {
                    tempList.add(arrayListOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
                } else {
                    tempList.add(arrayListOf(size[WIDTH], size[HEIGHT]))
                }
            }
        }
        return@withContext "Done"

    }
}