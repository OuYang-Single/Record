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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.deep.dpwork.util.CountDownTimeTextUtil;
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
import com.mmt.record.mvp.model.mvp.util.FileSaveUtils;
import com.mmt.record.mvp.model.mvp.util.RecordManagerUtil;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = RoutingUtils. RECORD_PATH)
public class RecordActivity extends BaseActivity<RecordPresenter> implements RecordContract.View, RecordManagerUtil.RecordEvent {
    @BindView(R.id.mainSurfaceView)
    SurfaceView mainSurfaceView;

    @BindView(R.id.map)
    MapView mMapView ;
    @BindView(R.id.recordImg)
    ImageView recordImg ;
    @BindView(R.id.labeleds)
    TextView labeleds ;
    @BindView(R.id.timeTv)
    TextView timeTv ;
    @BindView(R.id.user_name)
    TextView user_name;
    Timer   timer;
    public Handler handlers=new Handler(){
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 0:
                    if (RecordManagerUtil.getInstance().isRecording) {
                       // timeTv.text = "?????????"
                        timeTv.setText (CountDownTimeTextUtil.getTimerString(System.currentTimeMillis() - startTime).toString());
                    } else {
                        //luZhiTv.text = "???????????????"
                        timeTv.setText("00:00:00");
                    }
                    break;
            }
        }
    };
    private long   startTime= 0;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRecordComponent //??????????????????,?????????????????????
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
        mPresenter.isLogIn();

        RecordManagerUtil.getInstance().init(this, mainSurfaceView,this);
        labeleds.setText("????????????");
        mMapView.onCreate(bundle);

        ViewAnimator.animate(recordImg).alpha(0f, 1f, 0f).repeatCount(-1).duration(1000).start();
    }

    public void initTime() {
        timer = new Timer();
        handler.sendEmptyMessage(0);
        timer.schedule(new  TimerTask() {
            @Override
            public void run() {
                handlers.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    @Override
    public void showMessage(@NonNull String s) {
        ToastUtils.makeText(this,s);
    }

    @OnClick({R.id.labeleds,R.id.backs,R.id.back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.labeleds:
                if (RecordManagerUtil.getInstance().isRecording){
                    labeleds.setText("????????????");
                    RecordManagerUtil.getInstance().stopRecord();
                }else {
                    mPresenter.Apply(0);
                }
                break;
            case R.id.backs:
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionSuccess() {
        if (!RecordManagerUtil.getInstance().isRecording){
            startTime= System.currentTimeMillis();
            RecordManagerUtil.getInstance().startRecord(5);
            labeleds.setText("????????????");
        }

        mPresenter.Location(mMapView);
        mPresenter.onComplete();
        mPresenter.gpsUploads(0);
        anInt=0;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setUserName(String userName) {
        user_name.setText(userName);
    }


    @Override
    public void onComplete(File parentFile, String targetName) {
        mPresenter.onComplete(parentFile,targetName);
        mPresenter.gpsUpload(parentFile,targetName,false);
    }

    @Override
    public void onStop(File parentFile, String targetName) {
        if (targetName==null){
        return;
        }
        try {
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            FileSaveUtils.INSTANCE.saveVideo(this,new File(parentFile+"/"+targetName));
        }catch (Exception e){

        }

        if (anInt==0){
            mPresenter.onComplete(parentFile,targetName);
        }
        mPresenter.gpsUpload(parentFile,targetName, anInt != 0);
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        //???activity??????onDestroy?????????mMapView.onDestroy()???????????????
        if (mMapView!=null){
            mMapView.onDestroy();
        }
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView.onResume ()???????????????????????????
        if (mMapView!=null){
            mMapView.onResume();
        }
        mPresenter.Apply(500);
    }
    int anInt=0;
    @Override
    protected void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView.onPause ()????????????????????????
        if (mMapView!=null){
            mMapView.onPause();
        }
        anInt=1;
        RecordManagerUtil.getInstance().stopRecord();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //???activity??????onSaveInstanceState?????????mMapView.onSaveInstanceState (outState)??????????????????????????????
        if (mMapView!=null){
            mMapView.onSaveInstanceState(outState);
        }
       // RecordManagerUtil.getInstance().stopRecord();
    }

}
