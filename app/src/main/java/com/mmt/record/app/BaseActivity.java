package com.mmt.record.app;

import static com.mmt.record.mvp.model.mvp.util.Utils.showNotification;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.mvp.IPresenter;
import com.mmt.record.R;
import com.mmt.record.mvp.model.entity.EventMessage;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.ui.Receiver.NetStateChangeObserver;
import com.mmt.record.mvp.model.mvp.ui.Receiver.NetStateChangeReceiver;
import com.mmt.record.mvp.model.mvp.util.DateUtil;
import com.mmt.record.mvp.model.mvp.util.NetworkType;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public abstract class BaseActivity<P extends IPresenter> extends com.jess.arms.base.BaseActivity<P> implements NetStateChangeObserver {
    @BindView(R.id.hourTv)
    TextView hourTv;
    @BindView(R.id.minuteTv)
    TextView minuteTv;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.tv_no_network)
    TextView tv_no_network;
    @BindView(R.id.img_network_type)
    ImageView img_network_type;
    @BindView(R.id.date_)
    TextView date_;
    Timer timerTime;
   public Handler handler=new Handler(){
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
                    date_.setText(DateUtil.getWeekDay(System.currentTimeMillis()));
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetStateChangeReceiver.registerObserver(this);
        NetStateChangeReceiver. registerReceiver(this);
    }


    public void initTime() {
        timerTime = new Timer();
        handler.sendEmptyMessage(0);
        timerTime.schedule(new  TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerTime!=null){
            timerTime.cancel();
        }
        handler.removeMessages(0);
        NetStateChangeReceiver.unRegisterObserver(this);
        NetStateChangeReceiver. unRegisterReceiver(this);
    }
    @Override
    public void onNetDisconnected() {
        img_network_type.setImageResource(R.mipmap.ic_no_network);
        tv_no_network.setVisibility(View.VISIBLE);
        showNotification("网络异常","当前网络异常",this);
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        tv_no_network.setVisibility(View.GONE);
        EventBus.getDefault().postSticky(new EventMessage("onComplete"));
        EventBus.getDefault().postSticky(new EventMessage("gpsUploads"));
        if (networkType.equals(NetworkType.NETWORK_WIFI) ) {
            img_network_type.setImageResource(R.mipmap.ic_wifi);
        } else {
            img_network_type.setImageResource(R.mipmap.ic_move);
        }

    }
}
