package com.mmt.record.mvp.model.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.mmt.record.mvp.model.mvp.contract.SetupContract;
import com.mmt.record.mvp.model.mvp.contract.StartContract;

import javax.inject.Inject;


@ActivityScope
public class SetupModel extends BaseModel implements SetupContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SetupModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


}