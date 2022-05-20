package com.mmt.record.mvp.model.di.module;



import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.model.RecordModel;
import com.mmt.record.mvp.model.mvp.model.RegisterModel;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RecordModule {

    @Binds
    abstract RecordContract.Model bindMainModel(RecordModel model);


}