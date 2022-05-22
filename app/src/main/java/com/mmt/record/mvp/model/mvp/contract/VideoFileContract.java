package com.mmt.record.mvp.model.mvp.contract;

import android.app.Activity;
import android.content.Context;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;
import com.mmt.record.mvp.model.entity.Request;

import java.util.List;

import io.reactivex.Observable;

public interface VideoFileContract {
    interface View extends IView {


        Activity getActivity();

        void nullData();
    }

    interface Model extends IModel {

        Observable<Observable<Request>> onComplete(List<FileEntity> fileEntities);

        Observable<Request> gpsUpload(GpsEntity gpsEntity);
    }
}
