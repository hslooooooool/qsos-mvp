package com.qs.arm.di.module;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.qs.arm.config.DatabaseConfig;
import com.qs.arm.config.GsonConfig;
import com.qs.arm.http.BaseUrl;
import com.qs.arm.http.GlobalHttpHandler;
import com.qs.arm.http.RequestInterceptor;
import com.qs.arm.http.imageloader.BaseImageLoaderStrategy;
import com.qs.arm.http.imageloader.glide.GlideImageLoaderStrategy;
import com.qs.arm.integration.cache.Cache;
import com.qs.arm.integration.cache.LruCache;
import com.qs.arm.utils.DataHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * 框架独创的建造者模式 {@link Module},可向框架中注入外部配置的自定义参数
 *
 * @author 华清松
 */
@Module
public class ConfigModule {
    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private BaseImageLoaderStrategy mLoaderStrategy;
    private GlobalHttpHandler mHandler;
    private List<Interceptor> mInterceptors;
    private ResponseErrorListener mErrorListener;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkHttpConfiguration mOkHttpConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    private GsonConfig.GsonConfiguration mGsonConfiguration;
    private DatabaseConfig.RoomConfiguration mRoomConfiguration;
    private RequestInterceptor.Level mPrintHttpLogLevel;
    private Cache.Factory mCacheFactory;

    private ConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mHandler = builder.handler;
        this.mInterceptors = builder.interceptors;
        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkHttpConfiguration = builder.okHttpConfiguration;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        this.mCacheFactory = builder.cacheFactory;
        this.mRoomConfiguration = builder.roomConfiguration;
    }

    public static Builder builder() {
        return new Builder();
    }


    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }

    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl == null ? HttpUrl.parse("https://api.github.com/") : mApiUrl;
    }

    /**
     * 提供图片加载框架,默认使用 {@link Glide}
     *
     * @return
     */
    @Singleton
    @Provides
    BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return mLoaderStrategy == null ? new GlideImageLoaderStrategy() : mLoaderStrategy;
    }

    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? DataHelper.getCacheFile(application) : mCacheFile;
    }


    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkHttpConfiguration provideOkHttpConfiguration() {
        return mOkHttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    GsonConfig.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    DatabaseConfig.RoomConfiguration provideRoomConfiguration() {
        return mRoomConfiguration == null ? DatabaseConfig.RoomConfiguration.EMPTY : mRoomConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    RequestInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application) {
        return mCacheFactory != null ? mCacheFactory : type -> {
            //若想自定义 LruCache 的 size, 或者不想使用 LruCache , 想使用自己自定义的策略
            //并使用 ConfigModule.Builder#cacheFactory() 扩展
            return new LruCache(type.calculateCacheSize(application));
        };
    }

    public static final class Builder {
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors;
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkHttpConfiguration okHttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;
        private GsonConfig.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;
        private Cache.Factory cacheFactory;
        private DatabaseConfig.RoomConfiguration roomConfiguration;

        private Builder() {
        }

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseUrl(BaseUrl baseUrl) {
            if (baseUrl == null) {
                throw new NullPointerException("BaseUrl can not be null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 用来请求网络图片
         *
         * @param loaderStrategy loaderStrategy
         * @return Builder
         */
        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        /**
         * 用来处理http响应结果
         *
         * @param handler handler
         * @return Builder
         */
        public Builder globalHttpHandler(GlobalHttpHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 动态添加任意个interceptor
         *
         * @param interceptor interceptor
         * @return Builder
         */
        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }

        /**
         * 处理所有RxJava的onError逻辑
         *
         * @param listener listener
         * @return Builder
         */
        public Builder responseErrorListener(ResponseErrorListener listener) {
            this.responseErrorListener = listener;
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okHttpConfiguration(ClientModule.OkHttpConfiguration okHttpConfiguration) {
            this.okHttpConfiguration = okHttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(GsonConfig.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder roomConfiguration(DatabaseConfig.RoomConfiguration roomConfiguration) {
            this.roomConfiguration = roomConfiguration;
            return this;
        }

        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) { //是否让框架打印 Http 的请求和响应信息
            if (printHttpLogLevel == null) {
                throw new NullPointerException("printHttpLogLevel == null. Use RequestInterceptor.Level.NONE instead.");
            }
            this.printHttpLogLevel = printHttpLogLevel;
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public ConfigModule build() {
            return new ConfigModule(this);
        }

    }

}
