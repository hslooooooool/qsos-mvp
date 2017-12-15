package com.qs.gzb.app;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qs.arm.base.BaseApplication;
import com.qs.gzb.BuildConfig;

/**
 * @author 华清松
 * @see BaseApplication
 * @since 跟踪宝App
 */
public class GzbApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);

    }
}
