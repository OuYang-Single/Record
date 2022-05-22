package com.mmt.record.mvp.model.mvp.ui.activity;

import static com.mmt.record.mvp.model.mvp.util.RoutingUtils.MAIN_PATH;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.deep.dpwork.util.DisplayUtil;
import com.github.florent37.viewanimator.ViewAnimator;
import com.hjq.shape.view.ShapeTextView;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
import com.mmt.record.app.BaseActivity;
import com.mmt.record.mvp.model.di.component.DaggerMainComponent;
import com.mmt.record.mvp.model.di.component.DaggerRecordComponent;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.presenter.RecordPresenter;
import com.mmt.record.mvp.model.mvp.presenter.RegisterPresenter;
import com.mmt.record.mvp.model.mvp.util.DateUtil;
import com.mmt.record.mvp.model.mvp.util.RecordManagerUtil;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Date;
import java.util.Timer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = RoutingUtils. RECORD_PATH)
public class RecordActivity extends BaseActivity<RecordPresenter> implements RecordContract.View, RecordManagerUtil.RecordEvent {
    @BindView(R.id.mainSurfaceView)
    SurfaceView mainSurfaceView;
    @BindView(R.id.labeled)
    ShapeTextView labeled;
    @BindView(R.id.map)
    MapView mMapView ;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle bundle) {
        return R.layout.activity_record;
    }
    private BroadcastReceiver broadcastReceiver  = new  BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    public void initData(@Nullable Bundle bundle) {
        initTime();
        RecordManagerUtil.getInstance().init(this, mainSurfaceView,this);

        mMapView.onCreate(bundle);
    }



    @Override
    public void showMessage(@NonNull String s) {
        ToastUtils.makeText(this,s);
    }

    @OnClick({R.id.labeled})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.labeled:
                if (RecordManagerUtil.getInstance().isRecording){
                    labeled.setText(" 开始录制");
                    RecordManagerUtil.getInstance().stopRecord();
                }else {
                    mPresenter.Apply();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionSuccess() {
        if (!RecordManagerUtil.getInstance().isRecording){
            RecordManagerUtil.getInstance().startRecord(1);
            labeled.setText("停止录制");
            labeled.setVisibility(View.VISIBLE);
        }
        mPresenter.Location(mMapView);
        mPresenter.onComplete();
        mPresenter.gpsUploads(0);
    }

    @Override
    public Activity getActivity() {
        return this;
    }



    @Override
    public void onComplete(File parentFile, String targetName) {
        mPresenter.onComplete(parentFile,targetName);
        mPresenter.gpsUpload(parentFile,targetName);
    }

    @Override
    public void onStop(File parentFile, String targetName) {
        mPresenter.gpsUpload(parentFile,targetName);
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView!=null){
            mMapView.onDestroy();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (mMapView!=null){
            mMapView.onResume();
        }
        mPresenter.Apply();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView!=null){
            mMapView.onPause();
        }
        RecordManagerUtil.getInstance().stopRecord();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mMapView!=null){
            mMapView.onSaveInstanceState(outState);
        }
       // RecordManagerUtil.getInstance().stopRecord();
    }

}
