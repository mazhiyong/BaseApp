package com.lr.biyou.di.component;

import com.lr.biyou.di.scope.ActivityScope;

import javax.inject.Singleton;

import dagger.Component;

@ActivityScope
@Singleton
@Component(modules = com.lr.biyou.di.module.ObjectModule.class)
public interface ObjectComponent {
   // void injectTo(PayHistoryAdapter adapter);
}
