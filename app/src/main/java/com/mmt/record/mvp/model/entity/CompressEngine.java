package com.mmt.record.mvp.model.entity;

import android.content.Context;

import com.luck.picture.lib.entity.LocalMedia;
import com.mmt.record.mvp.model.mvp.contract.OnCallbackListener;

import java.util.ArrayList;


@Deprecated
public interface CompressEngine {
    /**
     * Custom compression engine
     * <p>
     * Users can implement this interface, and then access their own compression framework to plug
     * the compressed path into the {@link LocalMedia} object;
     *
     * </p>
     *
     * <p>
     * 1、LocalMedia media = new LocalMedia();
     * media.setCompressed(true);
     * media.setCompressPath("Your compressed path");
     * </p>
     * <p>
     * 2、listener.onCall( "you result" );
     * </p>
     *
     * @param context
     * @param list
     * @param listener
     */
    void onStartCompress(Context context, ArrayList<LocalMedia> list, OnCallbackListener<ArrayList<LocalMedia>> listener);
}
