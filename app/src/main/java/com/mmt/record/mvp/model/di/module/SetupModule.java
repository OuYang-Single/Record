package com.mmt.record.mvp.model.di.module;

import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.mvp.contract.SetupContract;
import com.mmt.record.mvp.model.mvp.contract.StartContract;
import com.mmt.record.mvp.model.mvp.model.SetupModel;
import com.mmt.record.mvp.model.mvp.model.StartModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


@Module
public abstract class SetupModule {
    @Binds
    abstract SetupContract.Model bindSetupModel(SetupModel model);


    @Provides
    public static ManagerFactory getManagerFactory() {
        return ManagerFactory.getInstance();
    }


}
