package com.mmt.record.mvp.model.di.module;



import androidx.fragment.app.FragmentActivity;

import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.model.MainModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.leefeng.promptlibrary.PromptDialog;

@Module
public abstract class MainModule {

    @Binds
    abstract MainContract.Model bindMainModel(MainModel model);
    @Provides
    public static ManagerFactory getManagerFactory() {
        return ManagerFactory.getInstance();
    }
    @ActivityScope
    @Provides
    public static PromptDialog getPromptDialog(MainContract.View view){
        return new PromptDialog(view.getActivity());
    }
}