package com.mmt.record.mvp.model.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.navi.NaviSetting;
import com.hjq.shape.layout.ShapeLinearLayout;
import com.hjq.shape.view.ShapeTextView;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
import com.mmt.record.app.BaseActivity;

import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.di.component.DaggerVideoFileComponent;
import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.LocalMediaFolder;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapters;
import com.mmt.record.mvp.model.mvp.util.DateUtil;
import com.mmt.record.mvp.model.mvp.util.NetworkType;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

@Route(path = RoutingUtils.VIDEO_FILE_PATH)
public class VideoFileActivity extends BaseActivity<VideoFilePresenter> implements VideoFileContract.View{
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.recyclerViews)
    RecyclerView mRecyclerViews;
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
    @BindView(R.id.login_btns)
    ShapeTextView login_btns;
    @BindView(R.id.image_break)
    ImageView image_break;
    @BindView(R.id.user_name)
    TextView user_name;
    @Inject
    GridLayoutManager manager;

    @Inject
    VideoFileAdapter mVideoFileAdapter;
    @Inject
    VideoFileAdapters mVideoFileAdapters;
    @Inject
    PromptDialog promptDialog;
    @Inject
    List<LocalMediaFolder> localMediaFolderList;
    @Inject
    List<LocalMedia> mLocalMedia;
    Handler handlers=new Handler(){
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 1:
                 mPresenter.getLocalMedias();
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


        mPresenter.isLogIn();
        initTime();
        MapsInitializer.updatePrivacyShow(this,true,true);
        AMapLocationClient.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
        AMapLocationClient.updatePrivacyAgree(this,true);
        NaviSetting.updatePrivacyShow(this, true, true);
        NaviSetting.updatePrivacyAgree(this, true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerViews.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViews.setAdapter(mVideoFileAdapters);
        mVideoFileAdapters.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int i, @NonNull Object o, int i1) {
                // mPresenter.getLocalMedias( localMediaFolderList.get(i1).getBucketId(),i1);
                try {
                    String pus=localMediaFolderList.get(i1).getFirstImagePath().split(localMediaFolderList.get(i1).getFolderName())[0]+localMediaFolderList.get(i1).getFolderName();
                    mPresenter.getVideos(handlers, new File(pus));
                }catch (Exception e){

                }

            }
        });
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

    }


    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onComplete();
        mPresenter.gpsUploads(0);
        localMediaFolderList.clear();
        showLoading();
        mPresenter.getVideos();
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
    public void nullData(int visible) {
        ly_null_data.setVisibility(visible);
    }

    @Override
    public void setLogInUi(int visible,String userName,String password) {

        if (visible==View.GONE){
           login_btn.setClickable(false);
            login_btns.setClickable(true);
            input_phone_password.setText(userName);
            input_code_password.setText(password);
            input_code_password.setEnabled(false);
            input_phone_password.setEnabled(false);

        }else {
            input_code_password.setEnabled(true);
            input_phone_password.setEnabled(true);
            input_phone_password.setText("");
            input_code_password.setText("");
            login_btn.setClickable(true);
            login_btns.setClickable(false);
        }
    }

    @Override
    public void setRecyclerViewUI(int visible) {
        mRecyclerView.setVisibility(visible);
        image_break.setVisibility(visible);
        mRecyclerViews.setVisibility(visible==View.GONE?View.VISIBLE:View.GONE);
    }

    @Override
    public void setUserName(String userName) {
        user_name.setText(userName);
    }

    @OnClick({R.id.labeleds,R.id.login_btn,R.id.image_break,R.id.login_btns})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.labeleds:
                mLocalMedia.clear();
                mVideoFileAdapter.notifyDataSetChanged();
                setRecyclerViewUI(View.GONE);
                ARouter.getInstance().build(RoutingUtils. RECORD_PATH).navigation();
                break;
            case R.id.image_break:
                nullData(View.GONE);
                mLocalMedia.clear();
                mVideoFileAdapter.notifyDataSetChanged();
                setRecyclerViewUI(View.GONE);
                break;
            case R.id.login_btn:
                mPresenter.login(input_phone_password.getText().toString(),input_code_password.getText().toString());

                break;
            case R.id.login_btns:
                mPresenter.logout();

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
