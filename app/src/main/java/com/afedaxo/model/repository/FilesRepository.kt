package com.afedaxo.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*


class FilesRepository (val context: Context) {
    fun getOutputJPEGFile(fileid: String?): File {
        return getOutputMediaFile(fileid, "IMG_", ".jpg")
    }

    fun getBitmapOfFile(filename: String): Bitmap {
        val file = getOutputJPEGFile(filename)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        return BitmapFactory.decodeFile(file.absolutePath, options)
    }

    suspend fun saveBitmapToFile(bitmap: Bitmap): String = withContext(Dispatchers.IO){
        val filename = generateFilename()
        val saveFile = getOutputJPEGFile(filename)
        val fileOutputStream = FileOutputStream(saveFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
            fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        filename
    }

    /** Create a File for saving an image or video  */
    @SuppressLint("SimpleDateFormat")
    private fun getOutputMediaFile(fileid: String?, prefix: String, extension: String): File {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        val mediaStorageDir = context.getFilesDir()
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create a media file name

        return File(mediaStorageDir.getPath() + File.separator + prefix + fileid + extension)
    }


    private fun getMetadataDir(): String {
        val appdir = context.getFilesDir()
        return appdir.getAbsolutePath() + File.separator + "captures"
    }


    fun generateFilename(): String {
        return _generateFilename(getMetadataDir() + File.separator, ".meta")
    }


    private fun _generateFilename(prefix: String, suffix: String): String {
        // Create a media file name
        val timestamp = UUID.randomUUID().toString()
        var generated = timestamp

        var mediaFile: File
        mediaFile = File(prefix + generated + suffix)
        if (mediaFile.exists()) {
            var filenum = 1
            // append a number 2, 3, 4.... to the filename
            while (mediaFile.exists()) {
                filenum++
                generated = timestamp + "_" + filenum
                mediaFile = File(prefix + generated + suffix)
            }
        }
        return generated
    }

    suspend fun deleteIfExists(fullFilename: String?) = withContext(Dispatchers.IO){
        val file = getOutputJPEGFile(fullFilename)
        if (file.exists()) {
            file.delete()
        }
    }
}