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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;


import com.mmt.record.R;
import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.FileEntityManager;
import com.mmt.record.greendao.GpsEntityDao;
import com.mmt.record.greendao.GpsEntityManager;
import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.LocalMedia;
import com.mmt.record.mvp.model.entity.LocalMediaFolder;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;
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
import com.mmt.record.mvp.model.mvp.ui.adapter.VideoFileAdapters;
import com.mmt.record.mvp.model.mvp.util.ACache;
import com.mmt.record.mvp.model.mvp.util.FileUtils;
import com.mmt.record.mvp.model.mvp.util.MediaUtil;
import com.mmt.record.mvp.model.mvp.util.ResourcesUtils;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;


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
    @Inject
    UserManager mUserManager;
    int mPosition = 1;
    @Inject
    List<LocalMediaFolder> localMediaFolderList;
    LocalMediaFolder localMediaFolder;
    List<LocalMediaFolder> localMediaFolders=new ArrayList<>();
    @Inject
    VideoFileAdapters mVideoFileAdapters;
    public int page=1;
    long bucketId;
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


    public void getVideos() {
        requestPermission(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                loader = PictureSelector.create(mRootView.getActivity())
                        .dataSource(SelectMimeType.ofVideo()).buildMediaLoader();
                loader.loadAllAlbum(new OnQueryAllAlbumListener<LocalMediaFolder>() {
                    @Override
                    public void onComplete(List<LocalMediaFolder> result) {
                        Log.w("", "");
                        for (LocalMediaFolder localMediaFolder:result){
                            Timber.e("getVideos  -----"+localMediaFolder.getFolderName());
                            if (localMediaFolder.getBucketId()!=-1){
                                try {
                                    if (localMediaFolder.getFide().getPath().contains("/normal_storage/") ){
                                        localMediaFolderList.add(localMediaFolder);
                                    }
                                }catch (Exception e){
                                    CrashReport.postCatchedException(e);
                                }

                            }
                        }

                        getVideoInfoList(false);
                        if (localMediaFolders.size()>0){
                            mPosition = result.get(0).getFolderTotalNum();
                        }
                        VideoFilePresenter.this.  localMediaFolders=result;
                     //   VideoFilePresenter.this.getLocalMedias();
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

    public void getLocalMedias(long bucketId,int i) {

        loader.loadPageMediaDatas(bucketId,new OnQueryDataResultListener<LocalMedia>(){
            @Override
            public void onComplete(ArrayList<LocalMedia> result, boolean isHasMore) {
                super.onComplete(result, isHasMore);
                mLocalMedia.addAll(result);
                getVideoInfoList(true);

            }
        });
    }
    public void getLocalMedias() {
        mRootView.setRecyclerViewUI(View.VISIBLE);
        mRootView.showLoading();
        mLocalMedia.clear();
        for (File file:mFiles){
            try {
                LocalMedia localMedia=new LocalMedia();
                localMedia.setPath(file.getParent());
                localMedia.setFileName(file.getName());
                localMedia.setRealPath(file.getPath());
                mLocalMedia.add(localMedia);
            }catch (Exception e){
                CrashReport.postCatchedException(e);
            }

        }
        getVideoInfoList(true);
    }




    public void getVideoInfoList(boolean b) {
        mRootView.hideLoading();
        if (b){
            if (mLocalMedia.size() <= 0) {
                mRootView.nullData(View.VISIBLE);
            }else {
                mRootView.nullData(View.GONE);
            }
            mVideoFileAdapter.notifyDataSetChanged();
        }else {
            if (localMediaFolderList.size() <= 0) {
                mRootView.nullData(View.VISIBLE);
            }else {
                mRootView.nullData(View.GONE);
            }
            mVideoFileAdapters.notifyDataSetChanged();
        }

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
                         //   new File(RecordModel.zipFileString).delete();
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

    public void isLogIn() {

        if (mUserManager.queryAll().size()>0) {
            mRootView.setLogInUi(View.GONE,mUserManager.queryAll().get(0).getUserName(),mUserManager.queryAll().get(0).getPassWord());
            mRootView.setUserName(mUserManager.queryAll().get(0).getUserName());
        }else {
            mRootView.setLogInUi(View.VISIBLE,"","");
            mRootView.setUserName("");
        }
    }
    public void login(String toString, String toString1) {
        if (!TextUtils.isEmpty(toString)) {
            if (TextUtils.isEmpty(toString1)){
                mRootView.showMessage(ResourcesUtils.getString(mApplication, R.string.code));
            }else {
                toString1 = FileUtils.  stringToBase64(toString1);
                String finalToString = toString1;
                mModel.login(toString,toString1).subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelay(0, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                        .doOnSubscribe(disposable -> {
                             mRootView.showLoading();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> {
                             mRootView.hideLoading();
                        })
                        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                        .subscribe(new ErrorHandleSubscriber<Request<User>>(mErrorHandler) {
                            @Override
                            public void onNext(Request<User> isUserExists) {
                                isUserExists.getData().setPassWord(finalToString);
                                isUserExists.getData().setUserName(toString);
                                mUserManager.saveOrUpdate(isUserExists.getData());
                                isLogIn();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.w("","");
                            }
                        });
                //
            }
        } else {
            mRootView.showMessage(ResourcesUtils.getString(mApplication, R.string.code_phono));
        }
    }

    public void logout() {
        mUserManager.deleteAll();
        isLogIn();
    }

    public  void getVideos(Handler myHandler,File file) {
        requestPermission(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        judge(file);
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

    private void judge(File file) {

        //  mVideoInfoList.addAll(MediaUtil.INSTANCE.setVideoList(mRootView.getActivity()));
        try {
            mPreferences = mRootView.getActivity().getSharedPreferences("table", Context.MODE_PRIVATE);
        } catch (Exception e) {
            //子线程未销毁可能时执行
        }
        mFiles.clear();
        mFiles.addAll( com.blankj.utilcode.utils.FileUtils.listFilesInDirWithFilter(file, ".mp4"));


    }


}