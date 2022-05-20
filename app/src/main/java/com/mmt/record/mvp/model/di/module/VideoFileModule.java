package com.mmt.record.mvp.model.di.module;



import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jess.arms.di.scope.ActivityScope;

import com.mmt.record.mvp.model.entity.VideoEntity;
import com.mmt.record.mvp.model.entity.VideoInfo;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.model.VideoFileModel;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.util.ACache;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class VideoFileModule {

    @Binds
    abstract VideoFileContract.Model bindMainModel(VideoFileModel model);


    @ActivityScope
    @Provides
    static GridLayoutManager getGridLayoutManager(VideoFileContract.View view) {
        return new GridLayoutManager(view.getActivity(), 4);
    }

    @ActivityScope
    @Provides
    static VideoFileAdapter getVideoFileAdapter(List<VideoInfo> list) {
        return new VideoFileAdapter(list);
    }
    @ActivityScope
    @Provides
    static List<VideoInfo> getVideoEntityList() {

        return new ArrayList<VideoInfo>();
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

}