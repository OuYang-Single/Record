package com.mmt.record.mvp.model.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.navi.NaviSetting;
import com.google.gson.Gson;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.R;
import com.mmt.record.app.BaseActivity;

import com.mmt.record.mvp.model.di.component.DaggerVideoFileComponent;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.util.ACache;
import com.mmt.record.mvp.model.mvp.util.DateUtil;
import com.mmt.record.mvp.model.mvp.util.NetworkType;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.Provides;
import me.leefeng.promptlibrary.PromptDialog;

@Route(path = RoutingUtils.VIDEO_FILE_PATH)
public class VideoFileActivity extends BaseActivity<VideoFilePresenter> implements VideoFileContract.View{
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ly_null_data)
    LinearLayout ly_null_data;
    @Inject
    GridLayoutManager manager;
    @Inject
    VideoFileAdapter mVideoFileAdapter;
    @Inject
    PromptDialog promptDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoFileComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle bundle) {
        return R.layout.activity_video_file;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        initTime();
        MapsInitializer.updatePrivacyShow(this,true,true);
        AMapLocationClient.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
        AMapLocationClient.updatePrivacyAgree(this,true);
        NaviSetting.updatePrivacyShow(this, true, true);
        NaviSetting.updatePrivacyAgree(this, true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mVideoFileAdapter);
        mPresenter.getVideos(handler);
        showLoading();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onComplete();
        mPresenter.gpsUploads(0);
    }

    @Override
    public void showMessage(@NonNull String s) {
        ToastUtils.makeText(this,s);
    }

    @Override
    public void showLoading() {
        promptDialog.showLoading(getString(R.string.loade));
    }

    @Override
    public void hideLoading() {
        promptDialog.dismiss();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void nullData() {
        ly_null_data.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.labeled})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.labeled:
                ARouter.getInstance().build(RoutingUtils. RECORD_PATH).navigation();
                break;
        }
    }
}
