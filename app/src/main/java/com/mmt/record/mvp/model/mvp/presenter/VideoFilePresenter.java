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
import com.jess.arms.utils.RxLifecycleUtils;


import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.FileEntityManager;
import com.mmt.record.greendao.GpsEntityDao;
import com.mmt.record.greendao.GpsEntityManager;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.LocalMediaFolder;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.VideoInfo;
import com.mmt.record.mvp.model.mvp.config.SelectMimeType;
import com.mmt.record.mvp.model.mvp.contract.OnQueryAllAlbumListener;
import com.mmt.record.mvp.model.mvp.contract.OnQueryDataResultListener;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.loader.IBridgeMediaLoader;
import com.mmt.record.mvp.model.mvp.loader.PictureSelector;
import com.mmt.record.mvp.model.mvp.model.RecordModel;
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapter;
import com.mmt.record.mvp.model.mvp.util.ACache;
import com.mmt.record.mvp.model.mvp.util.MediaUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;


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
    List<LocalMedia> mLocalMedia;
    @Inject
    RxPermissions permissions;
    @Inject
    VideoFileAdapter mVideoFileAdapter;
    @Inject
    FileEntityManager mFileEntityManager;
    @Inject
    GpsEntityManager mGpsEntityManager;
    IBridgeMediaLoader loader;
    int mPosition = 1;

    List<LocalMediaFolder> localMediaFolders=new ArrayList<>();
    public int page=0;
    @Inject
    public VideoFilePresenter(VideoFileContract.Model model, VideoFileContract.View rootView) {
        super(model, rootView);
    }


    @Override
    public void onStart() {
        super.onStart();
        mFiles = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


    public void getVideos(Handler myHandler) {
        requestPermission(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                if (loader == null) {
                    loader = PictureSelector.create(mRootView.getActivity())
                            .dataSource(SelectMimeType.ofVideo()).isPageStrategy(true, 50).buildMediaLoader();
                }
                loader.loadAllAlbum(new OnQueryAllAlbumListener<LocalMediaFolder>() {
                    @Override
                    public void onComplete(List<LocalMediaFolder> result) {
                        Log.w("", "");

                        if (localMediaFolders.size()>0){
                            mPosition = result.get(0).getFolderTotalNum();
                        }
                        VideoFilePresenter.this.  localMediaFolders=result;
                        VideoFilePresenter.this.getLocalMedias();
                    }
                });

            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.hideLoading();
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.hideLoading();
            }
        }, permissions, mErrorHandler, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE);
        //开线程初始化数据
    }

    public void getLocalMedias() {
    
        if (localMediaFolders.size()<=0){
            getVideoInfoList();
            mRootView.hideLoading();
            return;
        }
        loader.loadPageMediaData(localMediaFolders.get(mPosition).getBucketId(), page,50,new OnQueryDataResultListener<LocalMedia>(){
            @Override
            public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                super.onComplete(result, isHasMore);
                mRootView.hideLoading();
                mLocalMedia.addAll(result);
                getVideoInfoList();
            }
        });
    }




    public void getVideoInfoList() {
        if (mLocalMedia.size() <= 0) {
            mRootView.nullData();
        }
        mVideoFileAdapter.notifyDataSetChanged();
    }

    public void onComplete() {
        List<FileEntity> fileEntities = mFileEntityManager.queryBuilder().where(FileEntityDao.Properties.Upload.eq(false)).build().list();
        mModel.onComplete(fileEntities)
                .subscribeOn(Schedulers.io())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<Observable<Request>>(mErrorHandler) {
                    @Override
                    public void onNext(Observable<Request> observable) {
                        Timber.e("onComplete");
                        VideoFilePresenter.this.onComplete(observable, fileEntities);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("" + t.toString());
                    }
                });

    }

    public void onComplete(Observable<Request> observable, List<FileEntity> fileEntities) {
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(0, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    // mRootView.showLoading();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    //  mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<Request>(mErrorHandler) {
                    @Override
                    public void onNext(Request stringRequest) {
                        for (FileEntity fileEntity : fileEntities) {
                            fileEntity.setUpload(true);
                        }
                        mFileEntityManager.update(fileEntities);
                        Timber.e("" + stringRequest.getData());
                        try {
                            new File(RecordModel.zipFileString).delete();
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("" + t.toString());
                        try {
                            new File(RecordModel.zipFileString).delete();
                        } catch (Exception e) {

                        }
                        super.onError(t);
                    }
                });

    }

    public void gpsUploads(final int i) {
        List<GpsEntity> list = mGpsEntityManager.queryBuilder().where(GpsEntityDao.Properties.Upload.eq(false)).build().list();
        if (list.size() > i) {
            mModel.gpsUpload(list.get(i)).subscribeOn(Schedulers.io())
                    .retryWhen(new RetryWithDelay(0, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(disposable -> {
                        // mRootView.showLoading();
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        //  mRootView.hideLoading();
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<Request>(mErrorHandler) {
                        @Override
                        public void onNext(Request isUserExists) {
                            list.get(i).setUpload(true);
                            mGpsEntityManager.update(list.get(i));
                            int d = i + 1;
                            gpsUploads(d);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.w("", "");
                        }
                    });
        }


    }

    public void loadMoreDate() {
        page++;
        getLocalMedias();
    }
}