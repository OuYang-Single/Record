package com.mmt.record.mvp.model.mvp.ui.Service;

import static com.mmt.record.mvp.model.mvp.model.RecordModel.zipFileString;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jess.arms.base.BaseService;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.mmt.record.app.AppLifecyclesImpl;
import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.FileEntityManager;
import com.mmt.record.greendao.GpsEntityDao;
import com.mmt.record.greendao.GpsEntityManager;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.api.Api;
import com.mmt.record.mvp.model.entity.EventMessage;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.mvp.model.RecordModel;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.util.FileUtils;
import com.mmt.record.mvp.model.mvp.util.RetrofitUtils;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import okhttp3.RequestBody;
import timber.log.Timber;

public class MyService extends BaseService {
    FileEntityManager mFileEntityManager;
    GpsEntityManager mGpsEntityManager;
    @Override
    public void init() {
        mFileEntityManager= ManagerFactory.getInstance().getFileEntityManager(this);
        mGpsEntityManager= ManagerFactory.getInstance().getGpsEntityManager(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void eventMessage(EventMessage eventMessage){
        if ("onComplete".equals(eventMessage.getName())){
            onComplete();
        }else {
            gpsUploads(0);
        }
    }


    public void onComplete() {
        List<FileEntity> fileEntities = mFileEntityManager.queryBuilder().where(FileEntityDao.Properties.Upload.eq(false)).build().list();
        onComplete(fileEntities,this)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(0, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<Observable<Request>>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNext(Observable<Request> requestObservable) {
                        Timber.e("onComplete");
                        onCompletes(requestObservable, fileEntities);
                    }
                });
    }
    public Observable<Observable<Request>> onComplete(List<FileEntity> fileEntities, Context context) {
        return  Observable.just("").flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                FileUtils.ZipFolder(fileEntities, RecordModel.zipFileString);
                return Observable.just("");
            }
        }).flatMap(new Function<String, ObservableSource<? extends Observable<Request>>>() {
            @Override
            public ObservableSource<? extends Observable<Request>> apply(String aLong) throws Exception {

                if (!FileUtils.isFile){
                    throw new RuntimeException("Stub!");
                }
                RequestBody filePart = RetrofitUtils.createPartFromString(FileUtils.getDeviceId(AppLifecyclesImpl.application));
                return  Observable.just( ArmsUtils.obtainAppComponentFromContext(context).repositoryManager()
                        .obtainRetrofitService(Api.class)
                        .upload(filePart, RetrofitUtils.createFilePart("file",new File(zipFileString))));
            }
        });
    }


    public void onCompletes(Observable<Request> observable, List<FileEntity> fileEntities) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new ErrorHandleSubscriber<Request>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                    @Override
                    public void onNext(Request request) {
                        for (FileEntity fileEntity : fileEntities) {
                            fileEntity.setUpload(true);
                        }
                        mFileEntityManager.update(fileEntities);
                    }

                    @Override
                    public void onError(Throwable t) {
                        try {
                            new File(RecordModel.zipFileString).delete();
                        } catch (Exception se) {

                        }
                    }
                });

    }


    public void gpsUploads(final int i) {
        try {
            List<GpsEntity> list = mGpsEntityManager.queryBuilder().where(GpsEntityDao.Properties.Upload.eq(false)).build().list();
            if (list.size() > i) {
                gpsUpload(list.get(i),this).subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelay(0, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandleSubscriber<Request>(ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler()) {
                            @Override
                            public void onNext(Request isUserExists) {
                                if ( "0".equals(isUserExists.getCode())){
                                    list.get(i).setUpload(true);
                                    mGpsEntityManager.update(list.get(i));
                                    int d = i + 1;
                                    gpsUploads(d);
                                }
                            }
                        });
            }
        }catch (Exception e){

        }

    }


    public Observable<Request> gpsUpload(GpsEntity entity,Context context) {
        return Observable.just( ArmsUtils.obtainAppComponentFromContext(context).repositoryManager()
                        .obtainRetrofitService(Api.class)
                        .gpsUpload(entity))
                .flatMap(new Function<Observable<Request>, ObservableSource<Request>>() {
                    @Override
                    public ObservableSource<Request> apply(@NonNull Observable<Request> listObservable) throws Exception {
                        return listObservable;
                    }
                });
    }


}