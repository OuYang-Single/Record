package com.mmt.record.mvp.model.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.hjq.shape.layout.ShapeLinearLayout;
import com.hjq.shape.view.ShapeTextView;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
import com.mmt.record.app.BaseActivity;

import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.di.component.DaggerVideoFileComponent;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.util.NetworkType;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

@Route(path = RoutingUtils.VIDEO_FILE_PATH)
public class VideoFileActivity extends BaseActivity<VideoFilePresenter> implements VideoFileContract.View{
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ly_null_data)
    LinearLayout ly_null_data;
    @BindView(R.id.network)
    TextView network;
    @BindView(R.id.phone_password)
    ShapeLinearLayout phone_password;
    @BindView(R.id.code_password)
    ShapeLinearLayout code_password;
    @BindView(R.id.input_phone_password)
    EditText input_phone_password;
    @BindView(R.id.input_code_password)
    EditText input_code_password;
    @BindView(R.id.login_btn)
    ShapeTextView login_btn;
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
        mPresenter.isLogIn();
        initTime();
        MapsInitializer.updatePrivacyShow(this,true,true);
        AMapLocationClient.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
        AMapLocationClient.updatePrivacyAgree(this,true);
        NaviSetting.updatePrivacyShow(this, true, true);
        NaviSetting.updatePrivacyAgree(this, true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mVideoFileAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){

                   mPresenter. loadMoreDate();
                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });

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

    @Override
    public void setLogInUi(int visible) {
        phone_password.setVisibility(visible);
        code_password.setVisibility(visible);
        if (visible==View.GONE){
            login_btn.setText("注销");
        }else {
            login_btn.setText("登录");
        }
    }

    @OnClick({R.id.labeleds,R.id.login_btn})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.labeleds:
                ARouter.getInstance().build(RoutingUtils. RECORD_PATH).navigation();
                break;
            case R.id.login_btn:
                if ("注销".equals(login_btn.getText().toString())){
                    mPresenter.logout();
                }else {
                    mPresenter.login(input_phone_password.getText().toString(),input_code_password.getText().toString());
                }

                break;
        }
    }

    @Override
    public void onNetDisconnected() {
        super.onNetDisconnected();
        network.setText("当前网络状态：无网络");
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        super.onNetConnected(networkType);

        network.setText("当前网络状态："+networkType.name());

    }
}
