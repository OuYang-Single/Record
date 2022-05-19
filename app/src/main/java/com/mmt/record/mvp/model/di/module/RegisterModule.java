package com.mmt.record.mvp.model.di.module;



import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.model.MainModel;
import com.mmt.record.mvp.model.mvp.model.RegisterModel;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RegisterModule {

    @Binds
    abstract RegisterContract.Model bindMainModel(RegisterModel model);


}