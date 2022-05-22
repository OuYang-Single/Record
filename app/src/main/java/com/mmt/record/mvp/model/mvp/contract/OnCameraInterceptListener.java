package com.mmt.record.mvp.model.mvp.contract;

import androidx.fragment.app.Fragment;


/**
 * @author：luck
 * @date：2021/11/23 10:41 上午
 * @describe：OnCameraInterceptListener
 */
public interface OnCameraInterceptListener {


    void openCamera(Fragment fragment, int cameraMode, int requestCode);
}
