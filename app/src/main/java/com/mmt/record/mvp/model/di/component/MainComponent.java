package com.mmt.record.mvp.model.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.mmt.record.mvp.model.di.module.MainModule;
import com.mmt.record.mvp.model.mvp.contract.MainContract;
import com.mmt.record.mvp.model.mvp.ui.activity.MainActivity;

import dagger.BindsInstance;
import dagger.Component;

@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(MainContract.View view);

        Builder appComponent(AppComponent appComponent);

        MainComponent build();
    }
}