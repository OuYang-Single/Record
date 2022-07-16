package com.mmt.record.mvp.model.di.module;



import androidx.fragment.app.FragmentActivity;

import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.greendao.FileEntityManager;
import com.mmt.record.greendao.FolderEntityManager;
import com.mmt.record.greendao.GpsEntityManager;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.model.RecordModel;
import com.mmt.record.mvp.model.mvp.model.RegisterModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RecordModule {

    @Binds
    abstract RecordContract.Model bindMainModel(RecordModel model);
    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(RecordContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }
    @Provides
    public static UserManager getUserManager(RecordContract.View view) {
        return ManagerFactory.getInstance().getStudentManager(view.getActivity());
    }
    @Provides
    public static FileEntityManager getManagerFactory(RecordContract.View view) {
        return ManagerFactory.getInstance().getFileEntityManager(view.getActivity());
    }
    @Provides
    public static GpsEntityManager getGpsEntityManager(RecordContract.View view) {
        return ManagerFactory.getInstance().getGpsEntityManager(view.getActivity());
    }
    @Provides
    public static FolderEntityManager getFolderEntityManager(RecordContract.View view) {
        return ManagerFactory.getInstance().getFolderEntityManager(view.getActivity());
    }

}