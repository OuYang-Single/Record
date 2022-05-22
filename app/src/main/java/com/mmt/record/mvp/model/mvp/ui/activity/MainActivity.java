package com.mmt.record.mvp.model.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.mmt.record.R;

import com.mmt.record.mvp.model.di.component.DaggerMainComponent;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.presenter.MainPresenter;
import com.mmt.record.mvp.model.mvp.util.HtmlUtil;
import com.mmt.record.mvp.model.mvp.util.ToastUtils;


import butterknife.BindView;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;


import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.mmt.record.mvp.model.mvp.util.RoutingUtils.MAIN_PATH;


import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

@Route(path = MAIN_PATH)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.customer)
    TextView customer;
    @BindView(R.id.input_phone_password)
    EditText input_phone_password;
    @BindView(R.id.input_code_password)
    EditText input_code_password;
    @Inject
    PromptDialog promptDialog;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        customer.setText(Html.fromHtml("<font color='#ffbcbdbb' size='12'>还没有帐号? </font>" + "<a href='3'>马上注册</a>"));
        HtmlUtil.handleHtmlClickAndStyle(this, customer, Color.parseColor("#0F73EE"));

    }



    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ToastUtils.makeText(this,message);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void showLoading() {
        promptDialog.showLoading(getString(R.string.load));
    }

    @Override
    public void hideLoading() {
        promptDialog.dismiss();
    }

    @OnClick({R.id.login_btn})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.login_btn:
                mPresenter.login(input_phone_password.getText().toString(),input_code_password.getText().toString());
                break;
        }
    }


    @Override
    public Activity getActivity() {
        return this;
    }
}