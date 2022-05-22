package com.mmt.record.mvp.model.mvp.contract;

import android.content.Context;

import com.luck.picture.lib.R;

/**
 * @author：luck
 * @date：2021/12/23 10:33 上午
 * @describe：OnInjectLayoutResourceListener
 */
public interface OnInjectLayoutResourceListener {

    int getLayoutResourceId(Context context, int resourceSource);
}
