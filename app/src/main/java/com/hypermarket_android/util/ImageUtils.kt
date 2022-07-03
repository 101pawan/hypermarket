package com.app.zaoplus.utils

import android.content.Context
import android.os.Environment
import com.hypermarket_android.util.Compressor
import java.io.File
import java.io.IOException

object ImageUtils {
    fun changeFileSize(path: String, appName: String,context:Context): String {

        var scaledImagePath: String

        val filePath = Environment.getExternalStorageDirectory().toString() + "/$appName/Media/Images"
        val file = File(filePath)
        file.mkdirs()
        try {
            val file2 = Compressor(context)
                .setDestinationDirectoryPath(filePath)
                .compressToFile(File(path))
            scaledImagePath = file2.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            scaledImagePath = path
        }

        return scaledImagePath
    }
}