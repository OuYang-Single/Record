package com.mmt.record.mvp.model.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.mvp.model.di.module.RecordModule;
import com.mmt.record.mvp.model.di.module.RegisterModule;
import com.mmt.record.mvp.model.mvp.contract.RecordContract;
import com.mmt.record.mvp.model.mvp.contract.RegisterContract;
import com.mmt.record.mvp.model.mvp.ui.activity.RecordActivity;
import com.mmt.record.mvp.model.mvp.ui.activity.RegisterActivity;

import dagger.BindsInstance;
import dagger.Component;

@ActivityScope
@Component(modules =RecordModule.class, dependencies = AppComponent.class)
public interface RecordComponent {
    void inject(RecordActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(RecordContract.View view);

        Builder appComponent(AppComponent appComponent);

        RecordComponent build();
    }
}