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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Provides;

@Route(path = RoutingUtils.VIDEO_FILE_PATH)
public class VideoFileActivity extends BaseActivity<VideoFilePresenter> implements VideoFileContract.View{
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.hourTv)
    TextView hourTv;
    @BindView(R.id.minuteTv)
    TextView minuteTv;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.tv_no_network)
    TextView tv_no_network;
    @BindView(R.id.ly_null_data)
    LinearLayout ly_null_data;
    @BindView(R.id.img_network_type)
    ImageView img_network_type;
    @Inject
    GridLayoutManager manager;
    @Inject
    VideoFileAdapter mVideoFileAdapter;

    Timer timerTime;
    Handler handler=new Handler(){
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    Date date =new  Date();
                    int hour;
                    if (date.getHours() > 12) {
                        hour =  date.getHours() - 12;
                    } else {
                        hour =  date.getHours();
                    }
                    if (hour < 10) {
                        hourTv.setText("0"+hour);
                    } else {
                        hourTv.setText(hour+"");
                    }
                    if (date.getMinutes() < 10) {
                        minuteTv.setText( "0" +date.getMinutes());
                    } else {
                        minuteTv.setText(date.getMinutes()+"");
                    }
                    mDate.setText(DateUtil.timeStamp2Date(System.currentTimeMillis()));
                    break;
                case 1:

                    break;
            }
        }
    };

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
        timerTime = new Timer();
        handler.sendEmptyMessage(0);
        timerTime.schedule(new  TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mVideoFileAdapter);
        mPresenter.getVideos(handler);
    }

    @Override
    public void showMessage(@NonNull String s) {

    }
    @Override
    public void onNetDisconnected() {
        img_network_type.setImageResource(R.mipmap.ic_no_network);
        tv_no_network.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        tv_no_network.setVisibility(View.GONE);
        if (networkType.equals(NetworkType.NETWORK_WIFI) ) {
            img_network_type.setImageResource(R.mipmap.ic_wifi);
        } else {
            img_network_type.setImageResource(R.mipmap.ic_move);
        }

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
