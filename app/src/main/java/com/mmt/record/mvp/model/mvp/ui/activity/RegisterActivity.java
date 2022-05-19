package com.mmt.record.mvp.model.mvp.ui.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;
import com.mmt.record.mvp.model.di.component.DaggerMainComponent;
import com.mmt.record.mvp.model.di.component.DaggerRegisterComponent;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.presenter.RegisterPresenter;
import com.mmt.record.mvp.model.mvp.util.RoutingUtils;

import butterknife.OnClick;

@Route(path = RoutingUtils.REGISTER_PATH)
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View  {


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle bundle) {
        return R.layout.activity_register;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public void showMessage(@NonNull String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.login_btn})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.login_btn:

                break;
        }
    }
}