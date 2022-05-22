package com.mmt.record.mvp.model.mvp.presenter;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.jess.arms.utils.PermissionUtil.requestPermission;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.icu.text.IDNA;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.RxLifecycleUtils;
import com.mmt.record.R;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.util.FileUtils;
import com.mmt.record.mvp.model.mvp.util.ResourcesUtils;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;
import com.mmt.record.mvp.model.mvp.util.Utils;


import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import me.leefeng.promptlibrary.PromptDialog;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
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
    ManagerFactory mManagerFactory;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
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
                                // mRootView.showLoading();
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally(() -> {
                                //  mRootView.hideLoading();
                            })
                            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                            .subscribe(new ErrorHandleSubscriber<Request<User>>(mErrorHandler) {
                                @Override
                                public void onNext(Request<User> isUserExists) {
                                    isUserExists.getData().setPassWord(finalToString);
                                    isUserExists.getData().setUserName(toString);
                                    mManagerFactory.getStudentManager(mApplication).saveOrUpdate(isUserExists.getData());
                                    ARouter.getInstance().build(RoutingUtils.VIDEO_FILE_PATH).navigation();
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
}