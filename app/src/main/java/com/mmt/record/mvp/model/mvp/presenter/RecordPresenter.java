package com.mmt.record.mvp.model.mvp.presenter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.jess.arms.utils.PermissionUtil.requestPermission;
import static com.mmt.record.mvp.model.mvp.util.Utils.showNotification;

import android.Manifest;
import android.app.Application;
import android.graphics.Color;
import android.location.Location;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
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
import com.mmt.record.greendao.FolderEntityDao;
import com.mmt.record.greendao.FolderEntityManager;
import com.mmt.record.greendao.GpsEntityDao;
import com.mmt.record.greendao.GpsEntityManager;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.greendao.UserManager;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.FolderEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.GpsEntitys;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.model.RecordModel;
import com.mmt.record.mvp.model.mvp.util.FileUtils;
import com.mmt.record.mvp.model.mvp.util.MediaUtil;
import com.mmt.record.mvp.model.mvp.util.RecordManagerUtil;
import com.mmt.record.mvp.model.mvp.util.ResourcesUtils;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;
import com.mmt.record.mvp.model.mvp.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Waypoint;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;


@ActivityScope
public class RecordPresenter extends BasePresenter<RecordContract.Model, RecordContract.View> implements AMapLocationListener, AMap.OnMyLocationChangeListener {
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
    @Inject
    RxPermissions permissions;
    @Inject
    FileEntityManager mFileEntityManager;
    @Inject
    GpsEntityManager mGpsEntityManager;

    @Inject
    FolderEntityManager mFolderEntityManager;
    @Inject
    HashSet<Waypoint> waypoints;
    @Inject
    GPX gpx;

    AMap aMap;
    MyLocationStyle myLocationStyle;
    Location location;
    GpsEntity entity = null;
    @Inject
    UserManager mUserManager;
    @Inject
    public RecordPresenter(RecordContract.Model model, RecordContract.View rootView) {
        super(model, rootView);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
    public void isLogIn() {

        if (mUserManager.queryAll().size()>0) {
            mRootView.setUserName(mUserManager.queryAll().get(0).getUserName());
        }else {
            mRootView.setUserName("");
        }
    }

    public void Apply(int time) {

        requestPermission(new PermissionUtil.RequestPermission() {
                              @Override
                              public void onRequestPermissionSuccess() {

                              Observable.timer(time, TimeUnit.MILLISECONDS)
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                                          .subscribe(new ErrorHandleSubscriber<Long>(mErrorHandler) {
                                              @Override
                                              public void onNext(Long observable) {
                                                  mRootView.onRequestPermissionSuccess();
                                              }

                                              @Override
                                              public void onError(Throwable t) {
                                                  Timber.e("" + t.toString());
                                              }
                                          });

                              }

                              @Override
                              public void onRequestPermissionFailure(List<String> permissions) {
                                  // mRootView.onRequestPermissionSuccess();
                                  mRootView.showMessage("权限未开，无法录制");
                              }

                              @Override
                              public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                                  mRootView.showMessage("权限未开，无法录制");
                              }
                          }, permissions, mErrorHandler,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
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
                        RecordPresenter.this.onComplete(observable, fileEntities);
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

    public void onComplete(File parentFile, String targetName) {
        ToastUtils.makeText(mAppManager.getTopActivity(),"视频开始上传...");
        showNotification("上传通知","视频开始上传...",mAppManager.getTopActivity());
        mModel.onComplete(parentFile.getPath() + "/" + targetName)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<Observable<Request>>(mErrorHandler) {
                    @Override
                    public void onNext(Observable<Request> observable) {
                        Timber.e("onComplete");

                        RecordPresenter.this.onComplete(observable, targetName);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("" + t.toString());
                        showNotification("上传通知","视频上传失败，请检查网络...",mAppManager.getTopActivity());
                        CrashReport.postCatchedException(new Throwable("日志内容:  上传失败11"+t.toString()));
                    }
                });

    }

    public void onComplete(Observable<Request> observable, String targetName) {

        CrashReport.postCatchedException(new Throwable("日志内容:  开始上传"));
        observable.subscribeOn(Schedulers.io())
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
                    public void onNext(Request stringRequest) {
                        if ("1000".equals(stringRequest.getCode())){
                            ToastUtils.makeText(mAppManager.getTopActivity(),"视频上传成功...");
                            showNotification("上传通知","视频上传成功...",mAppManager.getTopActivity());
                        }else {
                            ToastUtils.makeText(mAppManager.getTopActivity(),"视频上传失败，"+stringRequest.getMsg());
                            showNotification("上传通知","视频上传失败，"+stringRequest.getMsg(),mAppManager.getTopActivity());
                        }

                        RecordManagerUtil.getInstance().deleteFiles();
                        try {
                            FileEntity fileEntity = mFileEntityManager.queryBuilder().where(FileEntityDao.Properties.FilePath.eq(targetName)).build().unique();
                            if(fileEntity!=null){
                                fileEntity.setUpload(true);
                                mFileEntityManager.update(fileEntity);
                            }
                        }catch (Exception e){

                        }
                        Timber.e("" + stringRequest.getData());
                        try {
                            new File(RecordModel.zipFileString).delete();
                        } catch (Exception e) {

                        }
                        CrashReport.postCatchedException(new Throwable("日志内容:  上传成功"));
                    }

                    @Override
                    public void onError(Throwable t) {
                        showNotification("上传通知","视频上传失败，请检查网络...",mAppManager.getTopActivity());
                        Timber.e("" + t.toString());
                        CrashReport.postCatchedException(t);
                        CrashReport.postCatchedException(new Throwable("日志内容:  上传失败"+t.toString()));
                        try {
                            RecordManagerUtil.getInstance().deleteFiles();
                            new File(RecordModel.zipFileString).delete();
                        } catch (Exception e) {

                        }
                        super.onError(t);
                    }
                });

    }


    public void Location(MapView mMapView) {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.interval(2000);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(this);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        this.location = location;
        Waypoint waypoint=new Waypoint();
        waypoint.setTime(new Date(location.getTime()));
        waypoint.setLatitude(location.getLatitude());
        waypoint.setLongitude(location.getLongitude());
       // dateTranfer("2022-12-04T10:26:54Z");
        gpx.addWaypoint(waypoint);
    }
    // str的格式为---"2022-08-24T02:54:46.970Z" 这个只是其中一种格式，也有可能是"2022-08-24T02:54:46.97Z" 少了一个数字的
// str的格式为---"2022-08-24T02:54:46.970Z" 这个只是其中一种格式，也有可能是"2022-08-24T02:54:46.97Z" 少了一个数字的
    private String dateTranfer(String str) {
        Date date = null;
        // ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss'Z'",
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // 2019-05-21T08:44:00Z 对应的时间格式 yyyy-MM-dd'T'HH:mm:ss'Z'
        try {
            // ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            // 2020-09-18T06:44:32.552Z 对应的时间格式 yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
            date = sdf.parse(str);

            Timber.i("UTC时间:" + date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = calendar.getTime();
            String date2 = simpleDateFormat.format(calendar.getTime());
            Timber.i("北京时间:" + date2);
            return date2;
        } catch (ParseException e) {
            return e.getLocalizedMessage();
        }
    }


    public void gpsUpload(File parentFile, String targetName,boolean is) {
        if (location != null) {
            try {
            FileEntity fileEntity = mFileEntityManager.queryBuilder().where(FileEntityDao.Properties.FilePath.eq(targetName)).build().unique();
            FolderEntity folderEntity = mFolderEntityManager.queryBuilder().where(FolderEntityDao.Properties.FolderPath.eq(parentFile)).build().unique();
            entity = new GpsEntity();
            entity.setGps_time(location.getTime());
            entity.setDevice_id(FileUtils.getDeviceId(mApplication));
            entity.setLatitude(location.getLatitude());
            entity.setLongitude(location.getLongitude());
            entity.setSpeed(location.getSpeed());
            entity.setHeading(location.getBearing());
            entity.setFileEntityId(fileEntity.getId());
            entity.setFolderEntityId(folderEntity.getId());
            List<GpsEntity> gpsEntities = mGpsEntityManager.queryBuilder().where(GpsEntityDao.Properties.Gps_time.eq(location.getTime())).build().list();
            if (gpsEntities.size()>0){
              return;
            }

            mGpsEntityManager.save(entity);
            if (is){
                return;
            }

            mModel.gpsUpload(entity).subscribeOn(Schedulers.io())
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
                            List<GpsEntity> entity = mGpsEntityManager.queryBuilder().where(GpsEntityDao.Properties.FileEntityId.eq(fileEntity.getId())).build().list();
                            for (GpsEntity gpsEntity : entity) {
                                gpsEntity.setUpload(true);
                            }
                            mGpsEntityManager.update(entity);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.w("", "");
                        }
                    });
            }catch (Exception ignored){

            }
        }
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
}