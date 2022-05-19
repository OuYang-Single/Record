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
import android.widget.Toast;
import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.mmt.record.R;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.entity.User;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.util.ResourcesUtils;
import com.mmt.record.mvp.model.mvp.util.Utils;


import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;



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

            if (Utils.isMobileNO(toString)) {
                if (TextUtils.isEmpty(toString1)){
                    mRootView.showMessage(ResourcesUtils.getString(mApplication, R.string.code));
                }else {
                    User user=new User();
                    user.setPassword(toString1);
                    user.setPhone(toString);
                    mManagerFactory.getStudentManager(mApplication).saveOrUpdate(user);
                }
            } else {
                mRootView.showMessage(ResourcesUtils.getString(mApplication, R.string.code_phono));
            }
    }
}