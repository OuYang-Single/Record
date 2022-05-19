package com.mmt.record.mvp.model.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.mvp.model.di.module.StartModule;
import com.mmt.record.mvp.model.mvp.contract.StartContract;
import com.mmt.record.mvp.model.mvp.ui.activity.StartActivity;

import dagger.BindsInstance;
import dagger.Component;


@ActivityScope
@Component(modules = StartModule.class, dependencies = AppComponent.class)
public interface StartComponent {
    void inject(StartActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(StartContract.View view);

        Builder appComponent(AppComponent appComponent);

        StartComponent build();
    }
}