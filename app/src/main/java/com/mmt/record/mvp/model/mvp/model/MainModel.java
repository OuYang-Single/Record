package com.mmt.record.mvp.model.mvp.model;


import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.mmt.record.mvp.model.api.Api;
import com.mmt.record.mvp.model.api.cache.CommonCache;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Url;


@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;


    @Inject
    public MainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<Request<User>> login(String name, String password) {

        User user=new User();
        user.setUserName(name);
        user.setPassWord(password);
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api.class)
                .login(user))
                .flatMap(new Function<Observable<Request<User>>, ObservableSource<Request<User>>>() {
                    @Override
                    public ObservableSource<Request<User>> apply(@NonNull Observable<Request<User>> listObservable) throws Exception {
                        return listObservable;
                    }
                });
    }
}