package com.qs.arm.di.module;

import android.app.Application;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qs.arm.config.GsonConfig;
import com.qs.arm.integration.IRepositoryManager;
import com.qs.arm.integration.RepositoryManager;
import com.qs.arm.integration.cache.Cache;
import com.qs.arm.integration.cache.CacheType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供一些框架必须的实例的 {@link Module}
 * @author 华清松
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfig.GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null) {
            configuration.configGson(application, builder);
        }
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

}
