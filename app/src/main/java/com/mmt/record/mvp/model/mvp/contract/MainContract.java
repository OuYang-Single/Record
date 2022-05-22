package com.mmt.record.mvp.model.mvp.contract;

import android.app.Activity;
import android.net.Uri;


import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.mmt.record.mvp.model.entity.Request;
import com.mmt.record.mvp.model.entity.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface MainContract {

    interface View extends IView {


        Activity getActivity();
    }

    interface Model extends IModel {
        Observable<Request<User>> login(String Neme, String Password);

    }
}
