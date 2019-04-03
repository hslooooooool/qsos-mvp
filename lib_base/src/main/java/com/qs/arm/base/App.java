package com.qs.arm.base;

import android.support.annotation.NonNull;

import com.qs.arm.di.component.AppComponent;

/**
 * @author : 华清松
 * @date : 2019/4/3
 * @description : App
 */
public interface App {

    @NonNull
    AppComponent getAppComponent();
}
