package com.mmt.record.mvp.model.mvp.model;


import android.app.Application;
import android.os.Environment;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.mmt.record.mvp.model.api.Api;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.util.FileUtils;
import com.mmt.record.mvp.model.mvp.util.RetrofitUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;


@ActivityScope
public class RecordModel extends BaseModel implements RecordContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;
    public static final  String zipFileString= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/normal_storage/complete.zip";

    @Inject
    public RecordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<Observable<Request>> onComplete(String s) {

        return  Observable.just(s).flatMap(new Function<String, ObservableSource<? extends Observable<Request>>>() {
            @Override
            public ObservableSource<? extends Observable<Request>> apply(String aLong) throws Exception {
                FileUtils.ZipFolder(s,zipFileString);

                RequestBody filePart = RetrofitUtils.createPartFromString(FileUtils.getDeviceId(mApplication));
                return  Observable.just(mRepositoryManager
                        .obtainRetrofitService(Api.class)
                        .upload(filePart,    RetrofitUtils.createFilePart("file",new File(zipFileString))));
            }
        });
    }

    @Override
    public Observable<Observable<Request>> onComplete(List<FileEntity> fileEntities) {
        return  Observable.just("").flatMap(new Function<String, ObservableSource<? extends Observable<Request>>>() {
            @Override
            public ObservableSource<? extends Observable<Request>> apply(String aLong) throws Exception {
                FileUtils.ZipFolder(fileEntities,RecordModel.zipFileString);
                if (!FileUtils.isFile){
                    throw new Exception("Stub!");
                }
                RequestBody filePart = RetrofitUtils.createPartFromString(FileUtils.getDeviceId(mApplication));
                return  Observable.just(mRepositoryManager
                        .obtainRetrofitService(Api.class)
                        .upload(filePart,    RetrofitUtils.createFilePart("file",new File(zipFileString))));
            }
        });
    }

    @Override
    public Observable<Request> gpsUpload(GpsEntity entity) {

        return Observable.just(mRepositoryManager
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