package com.mmt.record.mvp.model.mvp.contract;

/**
 * @author：luck
 * @date：2020/4/24 11:48 AM
 * @describe：OnKeyValueResultCallbackListener
 */
public interface OnKeyValueResultCallbackListener {
    /**
     * @param srcPath
     * @param resultPath
     */
    void onCallback(String srcPath, String resultPath);
}
