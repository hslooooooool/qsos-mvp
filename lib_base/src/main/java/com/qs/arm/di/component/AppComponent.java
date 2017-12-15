package com.qs.arm.di.component;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.qs.arm.base.delegate.AppDelegate;
import com.qs.arm.di.module.AppModule;
import com.qs.arm.di.module.ConfigModule;
import com.qs.arm.di.module.ClientModule;
import com.qs.arm.http.imageloader.ImageLoader;
import com.qs.arm.integration.AppManager;
import com.qs.arm.integration.IRepositoryManager;
import com.qs.arm.integration.cache.Cache;
import com.qs.arm.utils.ArmsUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * 可通过 {@link ArmsUtils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 *
 * @author 华清松
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, ConfigModule.class})
public interface AppComponent {
    /**
     * Application
     *
     * @return Application
     */
    Application application();

    /**
     * 所有activity的管理器
     *
     * @return AppManager
     */
    AppManager appManager();

    /**
     * 用于管理网络请求层,以及数据缓存层
     *
     * @return IRepositoryManager
     */
    IRepositoryManager repositoryManager();

    /**
     * RxJava 错误处理管理类
     *
     * @return RxErrorHandler
     */
    RxErrorHandler rxErrorHandler();

    /**
     * 图片管理器,用于加载图片的管理类,默认使用 Glide ,使用策略模式,可在运行时替换框架
     *
     * @return ImageLoader
     */
    ImageLoader imageLoader();

    /**
     * OkHttpClient
     *
     * @return OkHttpClient
     */
    OkHttpClient okHttpClient();

    /**
     * gson
     *
     * @return Gson
     */
    Gson gson();

    /**
     * 缓存文件根目录(RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下)
     * 应该将所有缓存都放到这个根目录下,便于管理和清理,可在 ConfigModule 里配置
     *
     * @return File
     */
    File cacheFile();

    /**
     * 用来存取一些整个App公用的数据,切勿大量存放大容量数据
     *
     * @return Cache
     */
    Cache<String, Object> extras();

    /**
     * 注入Application生命周期代理
     *
     * @param delegate
     */
    void inject(AppDelegate delegate);
}
