package com.mmt.record.app;

import android.os.Bundle;

import com.jess.arms.mvp.IPresenter;
import com.mmt.record.mvp.model.mvp.ui.Receiver.NetStateChangeObserver;
import com.mmt.record.mvp.model.mvp.ui.Receiver.NetStateChangeReceiver;
import com.mmt.record.mvp.model.mvp.util.NetworkType;

public abstract class BaseActivity<P extends IPresenter> extends com.jess.arms.base.BaseActivity<P> implements NetStateChangeObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetStateChangeReceiver.registerObserver(this);
        NetStateChangeReceiver. registerReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateChangeReceiver.unRegisterObserver(this);
        NetStateChangeReceiver. unRegisterReceiver(this);
    }
    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }
}
