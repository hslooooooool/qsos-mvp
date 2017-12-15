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

import android.app.Application;
import android.content.Context;

/**
 * 代理 {@link Application} 生命周期
 *
 * @author 华清松
 * @see AppDelegate
 */
public interface AppLifecycle {
    /**
     * 代理Application的attachBaseContext
     *
     * @param context context
     */
    void attachBaseContext(Context context);

    /**
     * 代理Application的onCreate
     *
     * @param application application
     */
    void onCreate(Application application);

    /**
     * 代理Application的onTerminate
     *
     * @param application application
     */
    void onTerminate(Application application);
}
