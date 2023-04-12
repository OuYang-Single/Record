package com.mmt.record.mvp.model.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
import com.mmt.record.mvp.model.di.component.DaggerSetupComponent;
import com.mmt.record.mvp.model.di.component.DaggerStartComponent;
import com.mmt.record.mvp.model.mvp.contract.SetupContract;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.presenter.SetupPresenter;
import com.mmt.record.mvp.model.mvp.presenter.VideoFilePresenter;
import com.mmt.record.mvp.model.mvp.util.RecordManagerUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SetupActivity extends BaseActivity<SetupPresenter> implements SetupContract.View {



    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSetupComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle bundle) {
        return R.layout.activity_setup;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

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
