/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qs.arm.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;

import com.qs.arm.utils.ArmsUtils;
import com.qs.arm.utils.LogUtils;

import org.simple.eventbus.EventBus;

/**
 * {@link ActivityDelegate} 实现
 *
 * @author 华清松
 */
public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity activity;
    private IActivity iActivity;

    public ActivityDelegateImpl(Activity activity) {
        this.activity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.debugInfo(getClass().getSimpleName(), "onCreate");
        // 如果要使用EventBus请将此方法返回true
        if (iActivity.useEventBus()) {
            // 注册到事件主线
            EventBus.getDefault().register(activity);
        }
        // 依赖注入
        iActivity.setupActivityComponent(ArmsUtils.obtainAppComponentFromContext(activity));
    }

    @Override
    public void onStart() {
        LogUtils.debugInfo(getClass().getSimpleName(), "onStart");
    }

    @Override
    public void onResume() {
        LogUtils.debugInfo("onResume");
    }

    @Override
    public void onPause() {
        LogUtils.debugInfo(getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onStop() {
        LogUtils.debugInfo(getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.debugInfo(getClass().getSimpleName(), "onSaveInstanceState");
    }

    @Override
    public void onDestroy() {
        LogUtils.debugInfo(getClass().getSimpleName(), "onDestroy");
        // 如果要使用EventBus请将此方法返回true
        if (iActivity != null && iActivity.useEventBus()) {
            EventBus.getDefault().unregister(activity);
        }
        this.iActivity = null;
        this.activity = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private ActivityDelegateImpl(Parcel in) {
        this.activity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
    }

    public static final Creator<ActivityDelegateImpl> CREATOR = new Creator<ActivityDelegateImpl>() {

        @Override
        public ActivityDelegateImpl createFromParcel(Parcel source) {
            return new ActivityDelegateImpl(source);
        }

        @Override
        public ActivityDelegateImpl[] newArray(int size) {
            return new ActivityDelegateImpl[size];
        }
    };
}
