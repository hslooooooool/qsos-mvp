package com.qs.arm.integration;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.qs.arm.config.DatabaseConfig;
import com.qs.arm.integration.cache.Cache;
import com.qs.arm.integration.cache.CacheType;
import com.qs.arm.mvp.IModel;
import com.qs.arm.utils.Preconditions;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {@link IModel} 层必要的 Api 做数据处理
 *
 * @author 华清松
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {
    private Lazy<Retrofit> mRetrofit;
    private Lazy<RxCache> mRxCache;
    private Application mApplication;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;
    private Cache<String, Object> mDatabaseServiceCache;
    private Cache.Factory mCacheFactory;
    private DatabaseConfig.RoomConfiguration mRoomConfiguration;

    @Inject
    RepositoryManager(Lazy<Retrofit> retrofit, Lazy<RxCache> rxCache, Application application
            , Cache.Factory cacheFactory, DatabaseConfig.RoomConfiguration roomConfiguration) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
        this.mApplication = application;
        this.mCacheFactory = cacheFactory;
        this.mRoomConfiguration = roomConfiguration;
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service service
     * @param <T>     T
     * @return T
     */
    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCacheFactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.get().create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param service service
     * @param <T>     T
     * @return T
     */
    @Override
    public <T> T obtainCacheService(Class<T> service) {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCacheFactory.build(CacheType.CACHE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mCacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(service.getName());
            if (cacheService == null) {
                cacheService = mRxCache.get().using(service);
                mCacheServiceCache.put(service.getName(), cacheService);
            }
        }
        return cacheService;
    }

    @Override
    public <DB extends RoomDatabase> DB obtainRoomDatabase(Class<DB> database, String dbName) {
        if (mDatabaseServiceCache == null) {
            mDatabaseServiceCache = mCacheFactory.build(CacheType.ROOM_DATABASE);
        }
        Preconditions.checkNotNull(mDatabaseServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        DB roomDatabase;
        synchronized (mDatabaseServiceCache) {
            roomDatabase = (DB) mDatabaseServiceCache.get(database.getName());
            if (roomDatabase == null) {
                RoomDatabase.Builder builder = Room.databaseBuilder(mApplication, database, dbName);
                // 自定义 Room 配置
                if (mRoomConfiguration != null) {
                    mRoomConfiguration.configRoom(mApplication, builder);
                }
                roomDatabase = (DB) builder.build();
                mDatabaseServiceCache.put(database.getName(), roomDatabase);
            }
        }
        return roomDatabase;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return mApplication;
    }
}
