package com.mmt.record.mvp.model.mvp.contract;

import androidx.fragment.app.Fragment;


import com.luck.picture.lib.entity.LocalMedia;

/**
 * @author：luck
 * @date：2021/11/27 5:44 下午
 * @describe：OnMediaEditInterceptListener
 */
public interface OnMediaEditInterceptListener {

    void onStartMediaEdit(Fragment fragment, LocalMedia currentLocalMedia, int requestCode);
}
