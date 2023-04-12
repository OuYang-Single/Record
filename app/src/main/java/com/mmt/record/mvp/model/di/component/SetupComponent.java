package com.mmt.record.mvp.model.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.mvp.model.di.module.SetupModule;
import com.mmt.record.mvp.model.di.module.StartModule;
import com.mmt.record.mvp.model.mvp.contract.SetupContract;
import com.mmt.record.mvp.model.mvp.contract.StartContract;
import com.mmt.record.mvp.model.mvp.ui.activity.SetupActivity;
import com.mmt.record.mvp.model.mvp.ui.activity.StartActivity;

import dagger.BindsInstance;
import dagger.Component;


@ActivityScope
@Component(modules = SetupModule.class, dependencies = AppComponent.class)
public interface SetupComponent {
    void inject(SetupActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(SetupContract.View view);

        Builder appComponent(AppComponent appComponent);

        SetupComponent build();
    }
}