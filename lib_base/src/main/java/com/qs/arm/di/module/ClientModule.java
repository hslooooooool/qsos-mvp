package com.qs.arm.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.qs.arm.http.GlobalHttpHandler;
import com.qs.arm.http.RequestInterceptor;
import com.qs.arm.utils.DataHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供一些三方库客户端实例的 {@link Module}
 */
@Module
public class ClientModule {
    private static final int TIME_OUT = 10;

    /**
     * 提供 {@link Retrofit}
     *
     * @param application
     * @param configuration
     * @param builder
     * @param client
     * @param httpUrl
     * @param gson
     * @return
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration, Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl, Gson gson) {
        builder
                // 域名
                .baseUrl(httpUrl)
                // 设置okHttp
                .client(client);

        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }

        builder
                // 使用 RxJava
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用 Gson
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    /**
     * 提供 {@link OkHttpClient}
     *
     * @param builder
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(Application application, @Nullable OkHttpConfiguration configuration, OkHttpClient.Builder builder, Interceptor intercept
            , @Nullable List<Interceptor> interceptors, @Nullable GlobalHttpHandler handler) {
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);

        if (handler != null) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                }
            });
        }

        // 如果外部提供了interceptor的集合则遍历添加
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (configuration != null) {
            configuration.configOkHttp(application, builder);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    Interceptor provideInterceptor(RequestInterceptor intercept) {
        return intercept;//打印请求信息的拦截器
    }

    /**
     * 提供 {@link RxCache}
     *
     * @param cacheDirectory RxCache缓存路径
     * @return
     */
    @Singleton
    @Provides
    RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration, @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder);
        }
        if (rxCache != null) {
            return rxCache;
        }
        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }

    /**
     * 需要单独给 {@link RxCache} 提供缓存路径
     *
     * @param cacheDir
     * @return
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return DataHelper.makeDirs(cacheDirectory);
    }

    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @return
     */
    @Singleton
    @Provides
    RxErrorHandler proRxErrorHandler(Application application, ResponseErrorListener listener) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build();
    }

    public interface RetrofitConfiguration {
        /**
         * 配置Retrofit
         *
         * @param context context
         * @param builder builder
         */
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkHttpConfiguration {
        /**
         * 配置OkHttp
         *
         * @param context context
         * @param builder builder
         */
        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }

    public interface RxCacheConfiguration {
        /**
         * 配置RxCache
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastJson
         * 请 {@code return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());}
         * 否则请 {@code return null;}
         *
         * @param context context
         * @param builder builder
         * @return RxCache
         */
        RxCache configRxCache(Context context, RxCache.Builder builder);
    }
}
