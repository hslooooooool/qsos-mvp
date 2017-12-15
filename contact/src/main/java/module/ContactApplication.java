package module;

import com.alibaba.android.arouter.launcher.ARouter;
import com.qs.arm.base.BaseApplication;
import com.qs.contact.BuildConfig;

/**
 * @author 华清松
 * @see BaseApplication
 * @since 联系人App
 */
public class ContactApplication extends BaseApplication {
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
