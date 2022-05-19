package com.mmt.record.mvp.model.mvp.ui.Receiver;

import com.mmt.record.mvp.model.utils.NetworkType;

public interface NetStateChangeObserver {
     void onNetDisconnected() ;

    void onNetConnected(NetworkType networkType);
}
