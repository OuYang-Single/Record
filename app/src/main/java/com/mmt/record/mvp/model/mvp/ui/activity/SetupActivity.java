package com.mmt.record.mvp.model.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
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

       }

       @Override
       public int initView(@Nullable Bundle bundle) {
           return 0;
       }

       @Override
       public void initData(@Nullable Bundle bundle) {

       }

       @Override
       public Context getContent() {
           return null;
       }

       @Override
       public Activity getActivity() {
           return null;
       }

       @Override
       public void showMessage(@NonNull String s) {

       }
   }
