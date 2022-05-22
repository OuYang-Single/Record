package com.mmt.record.mvp.model.mvp.contract;

import androidx.fragment.app.Fragment;



/**
 * @author：luck
 * @date：2022/3/18 2:55 下午
 * @describe：OnRecordAudioInterceptListener
 */
public interface OnRecordAudioInterceptListener {

    void onRecordAudio(Fragment fragment, int requestCode);
}
