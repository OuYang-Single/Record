package com.mmt.record.mvp.model.mvp.presenter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.jess.arms.utils.PermissionUtil.requestPermission;
import static com.mmt.record.mvp.model.mvp.util.FileUtil.getSDPath;

import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import com.blankj.utilcode.utils.FileUtils;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.mmt.record.mvp.model.entity.VideoInfo;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.util.ACache;
import com.mmt.record.mvp.model.mvp.util.MediaUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class VideoFilePresenter extends BasePresenter<VideoFileContract.Model, VideoFileContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    Gson mGson;
    private SharedPreferences mPreferences;

    List<File> mFiles;
    @Inject
    ACache mCatch;
    @Inject
    List<VideoInfo> mVideoInfoList;
    @Inject
    RxPermissions permissions;
    @Inject
    public VideoFilePresenter(VideoFileContract.Model model, VideoFileContract.View rootView) {
        super(model, rootView);
    }


    @Override
    public void onStart() {
        super.onStart();
        mFiles=new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }



    public  void getVideos(Handler myHandler) {
        requestPermission(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        judge();
                        Message message = new Message();
                        message.what = 1;
                        myHandler.sendMessage(message);
                    }
                }).start();
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {

            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

            }
        }, permissions, mErrorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE);
        //开线程初始化数据

    }

    private void judge( ) {

      //  mVideoInfoList.addAll(MediaUtil.INSTANCE.setVideoList(mRootView.getActivity()));
        try {
            mPreferences = mRootView.getActivity().getSharedPreferences("table", Context.MODE_PRIVATE);
        } catch (Exception e) {
            //子线程未销毁可能时执行
        }

        boolean first = mPreferences.getBoolean("firstVideo", true);
        int num = mPreferences.getInt("numVideo", 0);

        long time = mPreferences.getLong("VideoTime", 0);
        long cha = System.currentTimeMillis() - time;
        //判断缓存时间是否过期
        Log.d("aaa", "aaa: " +cha);
       if (!first && time != 0 & cha < 86400000) {
            for (int i = 0; i < num; i++) {
                Log.d("aaa", "ccc: " +num);
                String s = String.valueOf(i);
                String string = mCatch.getAsString(s + "video");
                if (string!=null) {
                    Log.d("aaa", "judge: " +string);
                    File file = mGson.fromJson(string, File.class);
                    mFiles.add(file);
                }
            }
        } else {
           mFiles = FileUtils.listFilesInDirWithFilter(Environment.getExternalStorageDirectory(), ".mp4");
           //  mFiles.addAll(FileUtils.listFilesInDirWithFilter( Environment.getDataDirectory().getAbsolutePath(), ".mp4")) ;
           Log.d("aaa", "ccc: " +mFiles.size());
           addCatch();
        }


    }


    private void addCatch() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < mFiles.size(); i++) {
            String s = mGson.toJson(mFiles.get(i));
            strings.add(s);
        }
        for (int i = 0; i < strings.size(); i++) {
            String s = String.valueOf(i);
            mCatch.put(s + "video", strings.get(i), ACache.TIME_DAY);
        }


        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean("firstVideo", false);
        edit.putInt("numVideo", strings.size());
        edit.putLong("VideoTime", System.currentTimeMillis());
        edit.commit();
    }
}