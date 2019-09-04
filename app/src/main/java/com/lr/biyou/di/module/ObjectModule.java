package com.lr.biyou.di.module;

import com.lr.biyou.utils.tool.TextViewUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *  各个Object 类的实例化
 */
@Module
public class ObjectModule {

    //实例化TextViewUtils
    @Provides
    @Singleton
    TextViewUtils providerTextViewUtils(){
        return new TextViewUtils();
    }


}
