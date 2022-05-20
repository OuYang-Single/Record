package com.mmt.record.mvp.model.mvp.ui.activity;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.mmt.record.R;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.di.component.DaggerStartComponent;
import com.mmt.record.mvp.model.mvp.contract.StartContract;
import com.mmt.record.mvp.model.mvp.presenter.StartPresenter;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;

import javax.inject.Inject;


@Route(path = RoutingUtils.START_PATH)
public class StartActivity extends BaseActivity<StartPresenter> implements StartContract.View{

    @Inject
    ManagerFactory mManagerFactory;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerStartComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_start;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mManagerFactory.getStudentManager(this).queryAll().size()>0) {

            goActivity( RoutingUtils.VIDEO_FILE_PATH);
        }else {
            goActivity( RoutingUtils.MAIN_PATH);
        }

    }

    private void goActivity(String path) {
        ARouter.getInstance().build(path).navigation();
        finish();
    }


    @Override
    public Context getContent() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showMessage(@NonNull String s) {

    }
}
