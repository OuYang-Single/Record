package com.mmt.record.mvp.model.mvp.util


import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log

import com.mmt.record.mvp.model.entity.FolderSelect
import com.mmt.record.mvp.model.entity.ImageSelect
import com.mmt.record.mvp.model.entity.VideoInfo
import java.io.File


object MediaUtil {


    fun queryFolderAlbum(context: Context): MutableList<FolderSelect> {
        var albums: MutableList<FolderSelect> = ArrayList()
        val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val mContentResolver = context.contentResolver

        val mCursor = mContentResolver.query(
            mImageUri, null,
            MediaStore.Images.Media.MIME_TYPE + "=? or " +
                    MediaStore.Images.Media.MIME_TYPE + "=? or " +
                    MediaStore.Images.Media.MIME_TYPE + "=?",
            arrayOf("image/jpeg", "image/png", "image/jpg"),
            null
        )
            ?: return albums


        while (mCursor.moveToNext()) {
            var images: MutableList<ImageSelect> = ArrayList()  //总的图片列表

            var path: String = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            var image: ImageSelect? = null




            if (fileExist(path) && !TextUtils.isEmpty(path)) {
                image = ImageSelect(path, false)
                images.add(image)
            }

            if (fileExist(path)) {
                // get all folder data
                val folderFile = File(path).parentFile
                if (folderFile != null && folderFile.exists()) {


                    val fp = folderFile.absolutePath
                    val f: FolderSelect = getFolderByPath(fp, albums)!!
                    if (f.name == "no_image") {
                        var firstImage: String? = ""
                        // 拿到第一张图片的路径
                        if (firstImage == "") {
                            firstImage = path
                        }
                        var folder: FolderSelect? = null
                        var imageList: MutableList<ImageSelect> = ArrayList()
                        imageList.add(image!!)
                        folder = FolderSelect(folderFile.name, fp, firstImage!!, imageList, false)
                        folder.imageList = imageList

                        albums.add(folder)
                    } else {
                        f.imageList.add(image!!)
                    }
                }
            }
        }

        return albums
    }

    fun queryAlbum(context: Context): List<String> {
        var albums = ArrayList<String>()
        val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val mContentResolver = context.contentResolver

        val mCursor = mContentResolver.query(
            mImageUri, null,
            MediaStore.Images.Media.MIME_TYPE + "=? or " +
                    MediaStore.Images.Media.MIME_TYPE + "=? or " +
                    MediaStore.Images.Media.MIME_TYPE + "=?",
            arrayOf("image/jpeg", "image/png", "image/jpg"),
            null
        )
            ?: return albums


        var i = 0
        while (mCursor.moveToNext()) {
            i++
            val path =
                mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))// 1.获取图片的路径
            albums.add(path)

            Log.e("tag", "----------- album path  $path")

        }
        Log.e("tag", "----------- album count  $i")

        return albums.reversed()
    }

    /**
     * Video attribute.
     */
    private val VIDEOS = arrayOf(
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.LATITUDE,
        MediaStore.Video.Media.LONGITUDE,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DURATION
    )

    fun queryVideo(context: Context) {
        var list = ArrayList<String>()
        val cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEOS, null, null, null)

        try {

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val path = cursor.getString(0)
                    list.add(path)
                }
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
        }
    }

    private fun fileExist(path: String): Boolean {
        return if (!TextUtils.isEmpty(path)) {
            File(path).exists()
        } else false
    }

    private fun getFolderByPath(path: String?, images: MutableList<FolderSelect>): FolderSelect? {
        if (images != null) {
            for (folder in images) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder
                }
            }
        }
        return FolderSelect("no_image", "1", "1", ArrayList(), false)
    }

    fun setVideoList(activity: Activity): MutableList<VideoInfo> {
        var mVideoList: MutableList<VideoInfo> = ArrayList()
        val thumbColumns = arrayOf(MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID)

        val mediaColumns = arrayOf(
            MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.SIZE
        )
        var cursor: Cursor? = activity!!.managedQuery(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            mediaColumns, null, null, null
        ) ?: return mVideoList
        if (cursor!!.moveToFirst()) {
            do {

                val id = cursor.getInt(
                    cursor.getColumnIndex(MediaStore.Video.Media._ID)
                )
                var thumbCursor = activity!!.contentResolver.query(
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                            + "=" + id, null, null
                )
                if (thumbCursor!!.moveToFirst()) {
                    var mThumbPath = thumbCursor.getString(
                        thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)
                    )
                }
                var mPath = cursor.getString(
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                )
                var mTitle = cursor.getString(
                    cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                )
                var mDisplayName = cursor.getString(
                    cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                )
                var mMimeType = cursor
                    .getString(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
                    )
                var mDuration = cursor
                    .getInt(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                    ).toString()
                var mSize = cursor
                    .getInt(
                        cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                    ).toString()

                val info = VideoInfo("", mPath, mTitle, mDisplayName, mMimeType, mDuration, mSize, false)
                mVideoList.add(info)
            } while (cursor.moveToNext())

        }
        return mVideoList
    }
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
    /**
     * uri转换为file文件
     * 返回值为file类型
     * @param uri
     * @return
     */
    fun uri2File(uri: Uri, activity: Activity): File {
        val imgPath: String?
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val anticlimaxes: Cursor = activity.managedQuery(uri, proj, null,
            null, null)
        imgPath = if (anticlimaxes == null) {
            uri.path
        } else {
            val actualImageColumnIndex: Int = anticlimaxes
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            anticlimaxes.moveToFirst()
            anticlimaxes
                .getString(actualImageColumnIndex)
        }
        return File(imgPath)
    }

     fun getSDFreeSize(): Long {
        //取得SD卡文件路径
        val path: File = Environment.getExternalStorageDirectory()
        val sf = StatFs(path.path)
        //获取单个数据块的大小(Byte)
        val blockSize = sf.blockSize.toLong()
        //空闲的数据块的数量
        val freeBlocks = sf.availableBlocks.toLong()
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return freeBlocks * blockSize / 1024 //单位MB
    }
    /**
     * 获取总容量
     */
     fun getSDAllSize(): Long {
        //取得SD卡文件路径
        val path = Environment.getExternalStorageDirectory()
        val sf = StatFs(path.path)
        //获取单个数据块的大小(Byte)
        val blockSize = sf.blockSize.toLong()
        //获取所有数据块数
        val allBlocks = sf.blockCount.toLong()
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return allBlocks * blockSize / 1024 //单位MB
    }
}