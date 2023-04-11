package com.mmt.record.mvp.model.mvp.presenter;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.SetupContract;

import javax.inject.Inject;

@ActivityScope
public class SetupPresenter extends BasePresenter<SetupContract.Model, SetupContract.View> {
    @Inject
    public SetupPresenter(SetupContract.Model model, SetupContract.View rootView) {
        super(model, rootView);
    }


}
