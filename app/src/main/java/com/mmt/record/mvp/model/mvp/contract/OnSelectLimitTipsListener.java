package com.mmt.record.mvp.model.mvp.contract;

import android.content.Context;

import com.luck.picture.lib.config.PictureSelectionConfig;

/**
 * @author：luck
 * @date：2022/1/8 2:12 下午
 * @describe：OnSelectLimitTipsListener
 */
public interface OnSelectLimitTipsListener {

    boolean onSelectLimitTips(Context context, PictureSelectionConfig config, int limitType);
}
