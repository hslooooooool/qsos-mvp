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
import android.support.v4.app.FragmentManager;

import com.qs.arm.base.BaseActivity;
import com.qs.arm.base.BaseFragment;
import com.qs.arm.di.component.AppComponent;

import org.simple.eventbus.EventBus;

/**
 * 框架中的每个 {@link Activity} 都需要实现此类,以满足规范
 *
 * @author 华清松
 * @see BaseActivity
 */
public interface IActivity {

    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类,进行 Component 依赖
     *
     * @param appComponent appComponent
     */
    void setupActivityComponent(AppComponent appComponent);

    /**
     * 是否使用 {@link EventBus}
     *
     * @return boolean
     */
    boolean useEventBus();

    /**
     * 初始化 View,如果initView返回0,框架则不会调用{@link Activity#setContentView(int)}
     *
     * @param savedInstanceState savedInstanceState
     * @return int
     */
    int initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return boolean
     */
    boolean useFragment();
}
