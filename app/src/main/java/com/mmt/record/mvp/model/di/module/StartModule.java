package com.mmt.record.mvp.model.di.module;


import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.mvp.contract.StartContract;
import com.mmt.record.mvp.model.mvp.model.StartModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class StartModule {
    @Binds
    abstract StartContract.Model bindStartModel(StartModel model);


    @Provides
    public static ManagerFactory getManagerFactory() {
        return ManagerFactory.getInstance();
    }


}
