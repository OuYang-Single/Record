package com.mmt.record.mvp.model.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.mvp.model.di.module.VideoFileModule;
import com.mmt.record.mvp.model.mvp.contract.VideoFileContract;
import com.mmt.record.mvp.model.mvp.ui.activity.VideoFileActivity;

import dagger.BindsInstance;
import dagger.Component;

@ActivityScope
@Component(modules =VideoFileModule.class, dependencies = AppComponent.class)
public interface VideoFileComponent {
    void inject(VideoFileActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(VideoFileContract.View view);

        Builder appComponent(AppComponent appComponent);

        VideoFileComponent build();
    }
}