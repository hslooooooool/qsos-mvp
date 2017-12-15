package com.qs.gzb.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.qs.arm.base.delegate.AppLifecycle;
import com.qs.arm.di.module.ConfigModule;
import com.qs.arm.http.RequestInterceptor;
import com.qs.arm.utils.ArmsUtils;
import com.qs.gzb.BuildConfig;
import com.qs.gzb.mvp.model.api.Api;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * app 的全局配置信息在此配置,需要将此实现类声明到 AndroidManifest 中
 *
 * @author 华清松
 */
public final class GlobalConfiguration implements com.qs.arm.integration.ConfigModule {
//    public static String sDomain = Api.APP_DOMAIN;

    @Override
    public void applyOptions(Context context, ConfigModule.Builder builder) {
        // Release 时,让框架不再打印 Http 请求和响应的信息
        if (!BuildConfig.LOG_DEBUG) {
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }

        builder.baseUrl(Api.APP_DOMAIN)
                // 强烈建议自己自定义图片加载逻辑,因为默认提供的 GlideImageLoaderStrategy 并不能满足复杂的需求
                // 请参考 https://github.com/JessYanCoding/MVPArms/wiki#3.4
//                .imageLoaderStrategy(new CustomLoaderStrategy())

                // 想支持多 BaseUrl,以及运行时动态切换任意一个 BaseUrl,请使用 https://github.com/JessYanCoding/RetrofitUrlManager
                // 如果 BaseUrl 在 App 启动时不能确定,需要请求服务器接口动态获取,请使用以下代码
                // 以下代码只是配置,还要使用 Okhttp (AppComponent中提供) 请求服务器获取到正确的 BaseUrl 后赋值给 GlobalConfiguration.sDomain
                // 切记整个过程必须在第一次调用 Retrofit 接口之前完成,如果已经调用过 Retrofit 接口,将不能动态切换 BaseUrl
//                .baseUrl(new BaseUrl() {
//                    @Override
//                    public HttpUrl url() {
//                        return HttpUrl.parse(sDomain);
//                    }
//                })

                // 可根据当前项目的情况以及环境为框架某些部件提供自定义的缓存策略, 具有强大的扩展性
//                .cacheFactory(new Cache.Factory() {
//                    @NonNull
//                    @Override
//                    public Cache build(CacheType type) {
//                        switch (type.getCacheTypeId()){
//                            case CacheType.EXTRAS_TYPE_ID:
//                                return new LruCache(1000);
//                            case CacheType.CACHE_SERVICE_CACHE_TYPE_ID:
//                                return new Cache(type.calculateCacheSize(context));//自定义 Cache
//                            default:
//                                return new LruCache(200);
//                        }
//                    }
//                })

                // 这里提供一个全局处理 Http 请求和响应结果的处理类,可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                // 用来处理 RxJava 中发生的所有错误,RxJava 中发生的每个错误都会回调此接口
                // RxJava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                .responseErrorListener(new ResponseErrorListenerImpl())
                .gsonConfiguration((context1, gsonBuilder) -> {
                    // 这里可以自己自定义配置Gson的参数
                    gsonBuilder.serializeNulls().enableComplexMapKeySerialization();
                })
                .roomConfiguration((context12, roomBuilder) -> {

                })
                .retrofitConfiguration((context1, retrofitBuilder) -> {
                    // 这里可以自己自定义配置Retrofit的参数,比如使用FastJson替代gson,甚至你可以替换系统配置好的OkHttp对象,
//                    retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());
                })
                .okHttpConfiguration((context1, okHttpBuilder) -> {
                    // 这里可以自己自定义配置OkHttp的参数
                    // 支持 Https,详情请百度
//                    okhttpBuilder.sslSocketFactory();
                    okHttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
                    // 使用一行代码监听 Retrofit／OkHttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                    ProgressManager.getInstance().with(okHttpBuilder);
                    // 让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                    RetrofitUrlManager.getInstance().with(okHttpBuilder);
                })
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {
                    // 这里可以自己自定义配置 RxCache 的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    /**
                     * 想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 FastJson,
                     * 请
                     * return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                     */
                    return null;
                });
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycle> lifecycles) {
        // AppLifecycle 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecycleImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        // ActivityLifecycleCallbacks 的所有方法都会在 Activity (包括三方库) 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) ArmsUtils
                        .obtainAppComponentFromContext(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }

}
