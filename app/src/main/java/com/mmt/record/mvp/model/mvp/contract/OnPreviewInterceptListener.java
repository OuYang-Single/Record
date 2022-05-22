package com.mmt.record.mvp.model.mvp.contract;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;


public interface OnPreviewInterceptListener {
    /**
     * Custom preview event
     *
     * @param context
     * @param position         preview current position
     * @param totalNum         source total num
     * @param page             page
     * @param currentBucketId  current source bucket id
     * @param currentAlbumName current album name
     * @param isShowCamera     current album show camera
     * @param data             preview source
     * @param isBottomPreview  from bottomNavBar preview mode
     */
    void onPreview(Context context, int position, int totalNum, int page,
                   long currentBucketId, String currentAlbumName, boolean isShowCamera,
                   ArrayList<LocalMedia> data, boolean isBottomPreview);
}
