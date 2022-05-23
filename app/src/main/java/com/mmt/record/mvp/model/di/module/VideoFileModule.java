package com.mmt.record.mvp.model.di.module;



import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jess.arms.di.scope.ActivityScope;

import com.jess.arms.http.imageloader.ImageLoader;
import com.mmt.record.greendao.FileEntityManager;
import com.mmt.record.greendao.GpsEntityManager;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.VideoEntity;
import com.mmt.record.mvp.model.entity.VideoInfo;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.model.VideoFileModel;
import com.mmt.record.mvp.model.mvp.ui.activity.StartActivity;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.util.ACache;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.leefeng.promptlibrary.PromptDialog;

@Module
public abstract class VideoFileModule {

    @Binds
    abstract VideoFileContract.Model bindMainModel(VideoFileModel model);


    @ActivityScope
    @Provides
    static GridLayoutManager getGridLayoutManager(VideoFileContract.View view) {
        return new GridLayoutManager(view.getActivity(), 3);
    }

    @ActivityScope
    @Provides
    static VideoFileAdapter getVideoFileAdapter(List<LocalMedia> list,ImageLoader mImageLoader) {
        return new VideoFileAdapter(list,mImageLoader);
    }
    @ActivityScope
    @Provides
    static List<LocalMedia> getVideoEntityList() {

        return new ArrayList<LocalMedia>();
    }

    @ActivityScope
    @Provides
    static ACache getACache(VideoFileContract.View view) {
        return  ACache.get(view.getActivity());
    }
    @ActivityScope
    @Provides
    static RxPermissions provideRxPermissions(VideoFileContract.View view) {
        return new RxPermissions((FragmentActivity) view.getActivity());
    }

    @Provides
    public static UserManager getUserManager(VideoFileContract.View view) {
        return ManagerFactory.getInstance().getStudentManager(view.getActivity());
    }
    @Provides
    public static FileEntityManager getManagerFactory(VideoFileContract.View view) {
        return ManagerFactory.getInstance().getFileEntityManager(view.getActivity());
    }
    @Provides
    public static GpsEntityManager getGpsEntityManager(VideoFileContract.View view) {
        return ManagerFactory.getInstance().getGpsEntityManager(view.getActivity());
    }
    @ActivityScope
    @Provides
    public static PromptDialog getPromptDialog(VideoFileContract.View view){
        return new PromptDialog(view.getActivity());
    }
}