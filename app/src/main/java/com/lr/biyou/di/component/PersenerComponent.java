package com.lr.biyou.di.component;


import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.di.module.PersenerModule;
import com.lr.biyou.di.scope.ActivityScope;

import javax.inject.Singleton;

import dagger.Component;


@ActivityScope
@Singleton
@Component(modules = PersenerModule.class)
public interface PersenerComponent {
    //RequestPresenterImp
    void injectTo(BasicActivity activity);
    void injectTo(BasicFragment fragment);

}
