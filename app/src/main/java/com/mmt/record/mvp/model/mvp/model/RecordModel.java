package com.mmt.record.mvp.model.mvp.model;


import android.app.Application;
import android.os.Environment;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.mmt.record.app.AppLifecyclesImpl;
import com.mmt.record.mvp.model.api.Api;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.FolderEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.util.FileUtils;
import com.mmt.record.mvp.model.mvp.util.RetrofitUtils;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
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
    GPX gpx;
    @Inject
    Application mApplication;
    public static final  String zipFileString= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/complete.zip";
    public static final  String gpxFileString= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)+"/outFile.gpx";

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
    public Observable<Observable<Request>> onComplete(String mp3) {

        return  Observable.just(mp3).flatMap(new Function<String, ObservableSource<? extends Observable<Request>>>() {
            @Override
            public ObservableSource<? extends Observable<Request>> apply(String aLong) throws Exception {
                GPXParser p = new GPXParser();
                File mp3File=new File(mp3);
                String gpxFileString=mp3File.getParent()+"/"+mp3File.getName().replace(".mp4",".gpx");
                FileOutputStream out = new FileOutputStream(gpxFileString);
                p.writeGPX(gpx, out);
                out.close();
                List<File> fileEntities=new ArrayList<>();
                File gpxFile=new File(gpxFileString);
                fileEntities.add(gpxFile);
                fileEntities.add(mp3File);
                FileUtils.ZipFolders(fileEntities,RecordModel.zipFileString);
                try {
                    gpx.getWaypoints().clear();
                } catch (Exception ignored) {

                }
                RequestBody filePart = RetrofitUtils.createPartFromString(FileUtils.getDeviceId(AppLifecyclesImpl.application));
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
                RequestBody filePart = RetrofitUtils.createPartFromString(FileUtils.getDeviceId(AppLifecyclesImpl.application));
                try {
                    return  Observable.just(mRepositoryManager
                            .obtainRetrofitService(Api.class)
                            .upload(filePart,    RetrofitUtils.createFilePart("file",new File(zipFileString))));
                }catch (Exception e)
                {
                    return  Observable.just(Observable.just(new Request()) );

                }

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