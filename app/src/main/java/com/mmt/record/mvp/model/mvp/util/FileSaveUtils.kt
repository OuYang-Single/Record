package com.mmt.record.mvp.model.mvp.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import java.io.File

object FileSaveUtils {
    /**
     * 保存图片
     * @param context
     * @param file
     */
    fun saveImage(context: Context, file: File) {
        val localContentResolver = context.contentResolver
        val localContentValues = getImageContentValues(file, System.currentTimeMillis())
        localContentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            localContentValues
        )

        val localIntent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
        val localUri = Uri.fromFile(file)
        localIntent.data = localUri
        context.sendBroadcast(localIntent)
    }

    private fun getImageContentValues(
        paramFile: File,
        paramLong: Long
    ): ContentValues {
        val localContentValues = ContentValues()
        localContentValues.put("title", paramFile.name)
        localContentValues.put("_display_name", paramFile.name)
        localContentValues.put("mime_type", "image/jpeg")
        localContentValues.put("datetaken", paramLong)
        localContentValues.put("date_modified", paramLong)
        localContentValues.put("date_added", paramLong)
        localContentValues.put("orientation", Integer.valueOf(0))
        localContentValues.put("_data", paramFile.absolutePath)
        localContentValues.put("_size", paramFile.length())
        return localContentValues
    }

    /**
     * 保存视频
     * @param context
     * @param file
     */
    fun saveVideo(context: Context, file: File) {
        //是否添加到相册
        val localContentResolver = context.contentResolver
        val localContentValues = getVideoContentValues(file, System.currentTimeMillis())
        val localUri = localContentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            localContentValues
        )
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri))
    }

    private fun getVideoContentValues(
        paramFile: File,
        paramLong: Long
    ): ContentValues {
        val localContentValues = ContentValues()
        localContentValues.put("title", paramFile.name)
        localContentValues.put("_display_name", paramFile.name)
        localContentValues.put("mime_type", "video/mp4")
        localContentValues.put("datetaken", paramLong)
        localContentValues.put("date_modified", paramLong)
        localContentValues.put("date_added", paramLong)
        localContentValues.put("_data", paramFile.absolutePath)
        localContentValues.put("_size", paramFile.length())
        return localContentValues
    }
}