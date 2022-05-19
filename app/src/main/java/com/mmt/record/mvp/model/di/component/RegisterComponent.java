package com.mmt.record.mvp.model.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.mvp.model.di.module.MainModule;
import com.mmt.record.mvp.model.di.module.RegisterModule;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.ui.activity.MainActivity;
import com.mmt.record.mvp.model.mvp.ui.activity.RegisterActivity;

import dagger.BindsInstance;
import dagger.Component;

@ActivityScope
@Component(modules = RegisterModule.class, dependencies = AppComponent.class)
public interface RegisterComponent {
    void inject(RegisterActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(RegisterContract.View view);

        Builder appComponent(AppComponent appComponent);

        RegisterComponent build();
    }
}