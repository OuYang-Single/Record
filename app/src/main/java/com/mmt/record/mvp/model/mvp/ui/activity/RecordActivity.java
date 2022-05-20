package com.mmt.record.mvp.model.mvp.ui.activity;

import static com.mmt.record.mvp.model.mvp.util.RoutingUtils.MAIN_PATH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deep.dpwork.util.DisplayUtil;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
import com.mmt.record.mvp.model.di.component.DaggerMainComponent;
import com.mmt.record.mvp.model.di.component.DaggerRecordComponent;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.presenter.RecordPresenter;
import com.mmt.record.mvp.model.mvp.presenter.RegisterPresenter;
import com.mmt.record.mvp.model.mvp.util.RecordManagerUtil;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;

import java.util.Timer;

import butterknife.BindView;

@Route(path = RoutingUtils. RECORD_PATH)
public class RecordActivity extends BaseActivity<RecordPresenter> implements RecordContract.View{
    @BindView(R.id.mainSurfaceView)
    SurfaceView mainSurfaceView;

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


       // ViewAnimator.animate(recordImg).alpha(0f, 1f, 0f).repeatCount(-1).duration(1000).start();
        //ViewAnimator.animate(fenTv).alpha(0f, 1f, 0f).repeatCount(-1).duration(2000).start();

        RecordManagerUtil.getInstance().init(this, mainSurfaceView);
    }

    @Override
    public void showMessage(@NonNull String s) {

    }
}
