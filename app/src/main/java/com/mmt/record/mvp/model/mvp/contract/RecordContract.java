package com.mmt.record.mvp.model.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.Request;

import java.util.List;

import io.reactivex.Observable;

public interface RecordContract {
    interface View extends IView {


        void onRequestPermissionSuccess();

        Activity getActivity();

        void setUserName(String userName);
    }

    interface Model extends IModel {

        Observable<Observable<Request>> onComplete(String s);
        Observable<Observable<Request>> onComplete(List<FileEntity> fileEntities);
        Observable<Request> gpsUpload(GpsEntity entity);
    }
}
