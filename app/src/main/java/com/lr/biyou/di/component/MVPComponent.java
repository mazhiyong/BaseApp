package com.lr.biyou.di.component;
import com.lr.biyou.di.module.MVPModule;
import com.lr.biyou.mvp.base.BaseModel;
import com.lr.biyou.mvp.model.RequestModelImp;

import javax.inject.Singleton;

import dagger.Component;

/**
 * MVP的DaggeR的注射器
 */

@Singleton
@Component(modules = MVPModule.class)
public interface MVPComponent {

    //ApiManagerService
    void InjectinTo(BaseModel baseModel);
    //CompositeDisposable
    void InjectinTo(RequestModelImp requestModel);

}
