package com.mmt.record.mvp.model.mvp.ui.activity;

import static com.mmt.record.mvp.model.mvp.util.Utils.showNotification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
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

import com.mmt.record.greendao.FolderBeanDao;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.di.component.DaggerVideoFileComponent;
import com.mmt.record.mvp.model.entity.EventMessage;
import com.mmt.record.mvp.model.entity.FolderBean;
import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.LocalMediaFolder;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.ui.Service.MyService;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapters;
import com.mmt.record.mvp.model.mvp.util.DateUtil;
import com.mmt.record.mvp.model.mvp.util.NetworkType;
import com.mmt.record.mvp.model.mvp.util.RecordManagerUtil;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.SPManager;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;
import com.zlylib.fileselectorlib.FileSelector;
import com.zlylib.fileselectorlib.utils.Const;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;
import timber.log.Timber;

@Route(path = RoutingUtils.VIDEO_FILE_PATH)
public class VideoFileActivity extends BaseActivity<VideoFilePresenter> implements VideoFileContract.View, RecordManagerUtil.RecordEvent {
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
    @BindView(R.id.mainSurfaceView)
    SurfaceView mainSurfaceView;
    @BindView(R.id.set_video)
    ImageView set_video;
    @BindView(R.id.set_text)
    TextView set_text;
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
    boolean aBoolean= false;
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
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        mPresenter.isLogIn();
        initTime();
        File pathFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if(SPManager. getInstance().getFile()!=null){
            pathFile  =new  File(SPManager. getInstance().getFile());
        }
        if (!pathFile.exists()){
            pathFile  =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        }


        set_text.setText("视频存储目录:"+pathFile.getPath());
        mainSurfaceView.setVisibility(View.GONE);
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
                mPresenter.getVideos(i1);
                /*// mPresenter.getLocalMedias( localMediaFolderList.get(i1).getBucketId(),i1);
                try {
                   File file= new File( localMediaFolderList.get(i1).getFide().getParent());
                    mPresenter.getVideos(handlers, file);
                }catch (Exception e){
                    try {
                        String pus=localMediaFolderList.get(i1).getFirstImagePath().split(localMediaFolderList.get(i1).getFolderName())[0]+localMediaFolderList.get(i1).getFolderName();
                        mPresenter.getVideos(handlers, new File(pus));
                    }catch (Exception exception){

                    }

                }*/

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
    private String getTimeString(long timeLong) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(timeLong);
        return format.format(d1);
    }



    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().postSticky(new EventMessage("onComplete"));
        EventBus.getDefault().postSticky(new EventMessage("gpsUploads"));
        localMediaFolderList.clear();
        showLoading();
        mPresenter. getVideos();
        File pathFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if(SPManager. getInstance().getFile()!=null){
            pathFile  =new  File(SPManager. getInstance().getFile());
        }
        if (!pathFile.exists()){
            pathFile  =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            SPManager. getInstance().setFile(pathFile.getPath());
        }

        set_text.setText("视频存储目录:"+pathFile.getPath());
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

    @OnClick({R.id.labeleds,R.id.login_btn,R.id.image_break,R.id.login_btns,R.id.set_video})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.labeleds:
                showLoading();
                mLocalMedia.clear();
                mVideoFileAdapter.notifyDataSetChanged();
                setRecyclerViewUI(View.GONE);
                ARouter.getInstance().build(RoutingUtils. RECORD_PATH).withBoolean("aBoolean",aBoolean).navigation();
                aBoolean=true;
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
             case R.id.set_video:

                 FileSelector.from(this)
                         .setTilteBg(R.color.txt_color) //不填写默认是： ?attr/colorPrimary
                         .onlyShowFolder()  //只能选择文件夹
                         .requestCode(1) //设置返回码
                         .start();

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            ArrayList<String> essFileList = data.getStringArrayListExtra(Const.EXTRA_RESULT_SELECTION);
            StringBuilder builder = new StringBuilder();
            for (String file : essFileList) {
                builder.append(file);
            }

            if (builder.toString().contains("emulated/0")){
                   SPManager. getInstance().setFile(builder.toString());
                showNotification("视频存储","视频存储目录改为:"+builder.toString(),this);
                ToastUtils.makeTexts(this,"视频存储目录改为:"+builder.toString());
            }else {
                ToastUtils.makeTexts(this,"因在SD卡中只有指定的文件目录才能创建文件，因此SD卡视频存储目录只能为改为:"+getExternalFilesDir(null).getPath());
                set_text.setText("视频存储目录:"+getExternalFilesDir(null).getPath());
                SPManager. getInstance().setFile(getExternalFilesDir(null).getPath());
                showNotification("视频存储","因在SD卡中只有指定的文件目录才能创建文件，因此SD卡视频存储目录只能为改为:"+getExternalFilesDir(null).getPath(),this);
            }


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

    @Override
    public void onComplete(File parentFile, String targetName) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop(File parentFile, String targetName) {

    }

    @Override
    public void onStarts() {

    }

    @Override
    public void onException() {

    }


}
