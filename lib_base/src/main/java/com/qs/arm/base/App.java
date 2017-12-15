
package com.qs.arm.base;

import android.support.annotation.NonNull;

import com.qs.arm.base.delegate.AppLifecycle;
import com.qs.arm.di.component.AppComponent;

/**
 * 框架中的每个 {@link android.app.Application} 都需要实现此类,以满足规范
 *
 * @see BaseApplication
 */
public interface App {
    /**
     * 获取Lifecycle{@link AppLifecycle}的Component
     *
     * @return AppComponent
     */
    @NonNull
    AppComponent getAppComponent();
}
