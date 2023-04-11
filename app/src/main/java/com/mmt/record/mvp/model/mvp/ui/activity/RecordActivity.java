package com.mmt.record.mvp.model.mvp.ui.activity;

import static com.mmt.record.mvp.model.mvp.model.RecordModel.gpxFileString;
import static com.mmt.record.mvp.model.mvp.util.RoutingUtils.MAIN_PATH;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import com.amap.api.maps.MapView;
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
import com.mmt.record.mvp.model.wigth.Camera2View;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = RoutingUtils.RECORD_PATH)
public class RecordActivity extends BaseActivity<RecordPresenter> implements RecordContract.View, RecordManagerUtil.RecordEvent {
    @BindView(R.id.mainSurfaceView)
    SurfaceView mainSurfaceView;

    @BindView(R.id.map)
    MapView mMapView;

    @BindView(R.id.recordImg)
    ImageView recordImg;
    @BindView(R.id.labeleds)
    TextView labeleds;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.user_name)
    TextView user_name;
    Timer timer;
    @Inject
    GPX gpx;
    public Handler handlers = new Handler() {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    if (timeTv != null) {
                        if (RecordManagerUtil.getInstance().isRecording) {
                            // timeTv.text = "录制中"
                            timeTv.setText(CountDownTimeTextUtil.getTimerString(System.currentTimeMillis() - startTime).toString());
                        } else {
                            //luZhiTv.text = "录制已停止"
                            timeTv.setText("00:00:00");
                        }
                    }
                    break;
            }
        }
    };
    private long startTime = 0;

    boolean isBack=false;

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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        Log.w("RecordActivity","initData");
        RecordManagerUtil.getInstance().init(this, mainSurfaceView, this);
        initTime();
        mPresenter.isLogIn();
        labeleds.setText("停止录制");
        mMapView.onCreate(bundle);
       // Camera2View.show(this);
        ViewAnimator.animate(recordImg).alpha(0f, 1f, 0f).repeatCount(-1).duration(1000).start();

       /* if (!getIntent().getBooleanExtra("aBoolean",true)){
            ARouter.getInstance().build(RoutingUtils. RECORD_PATH).withBoolean("aBoolean",true).navigation();
            finish();
        }*/

    }

    public void initTime() {
        timer = new Timer();
        handler.sendEmptyMessage(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handlers.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    @Override
    public void showMessage(@NonNull String s) {
        ToastUtils.makeText(this, s);
    }

    @OnClick({R.id.labeleds, R.id.backs, R.id.back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.labeleds:
                if (RecordManagerUtil.getInstance().isRecording) {
                    labeleds.setText("开始录制");
                    RecordManagerUtil.getInstance().stopRecord();
                } else {
                    mPresenter.Apply(0);
                }
                break;
            case R.id.backs:
            case R.id.back:
                isBack=true;
                RecordManagerUtil.getInstance().stopRecord();
                break;
        }
    }


    @Override
    public void onRequestPermissionSuccess() {
        if (!RecordManagerUtil.getInstance().isRecording) {
            startTime = System.currentTimeMillis();
            RecordManagerUtil.getInstance().startRecord(5);
            labeleds.setText("停止录制");
        }

        mPresenter.Location(mMapView);

        anInt = 0;
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
        mPresenter.onComplete(parentFile, targetName);
        mPresenter.gpsUpload(parentFile, targetName, false);
    }

    @Override
    public void onStop(File parentFile, String targetName) {
      if (isBack){
          finish();
      }
    }

    @Override
    public void onStarts() {
        if (mPresenter != null) {
            mPresenter.Apply(100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        RecordManagerUtil.getInstance().stopRecord();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null) {
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

        if (mMapView != null) {
            mMapView.onResume();
        }

    }

    int anInt = 0;

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView != null) {
            mMapView.onPause();
        }
        anInt = 1;
        RecordManagerUtil.getInstance().stopRecord();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
        // RecordManagerUtil.getInstance().stopRecord();
    }


}
