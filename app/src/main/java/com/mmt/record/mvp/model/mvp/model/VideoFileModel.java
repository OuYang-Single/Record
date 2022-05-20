package com.mmt.record.mvp.model.mvp.model;


import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;

import javax.inject.Inject;


@ActivityScope
public class VideoFileModel extends BaseModel implements VideoFileContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;


    @Inject
    public VideoFileModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }




}