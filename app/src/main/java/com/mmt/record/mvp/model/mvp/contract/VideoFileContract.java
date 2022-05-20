package com.mmt.record.mvp.model.mvp.contract;

import android.app.Activity;
import android.content.Context;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

public interface VideoFileContract {
    interface View extends IView {


        Activity getActivity();

    }

    interface Model extends IModel {

    }
}
