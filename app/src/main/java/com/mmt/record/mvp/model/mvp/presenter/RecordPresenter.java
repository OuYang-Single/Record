package com.mmt.record.mvp.model.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.mmt.record.R;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.util.ResourcesUtils;
import com.mmt.record.mvp.model.mvp.util.Utils;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class RecordPresenter extends BasePresenter<RecordContract.Model, RecordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    Gson mGson;

    @Inject
    public RecordPresenter(RecordContract.Model model, RecordContract.View rootView) {
        super(model, rootView);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }



}