package com.mmt.record.mvp.model.entity
import androidx.annotation.DrawableRes




/**
 * 图片选择
 */
data class ImageSelect(
    var path: String,
    var isCheckout: Boolean
)

/**
 * 图片文件选择
 */
data class FolderSelect(
    var name: String,  //名字
    var path: String,  //路径
    var firstImagePath: String,  //第一张图
    var imageList: MutableList<ImageSelect> = ArrayList(),
    var isCheckout: Boolean = false
)
/**
 * 视频选择
 */
data class VideoInfo(
    var ThumbPath: String, var path: String, var Title: String, var DisplayName: String, var MimeType: String
    , var duration: String, var size: String, var isCheckout: Boolean = false
)