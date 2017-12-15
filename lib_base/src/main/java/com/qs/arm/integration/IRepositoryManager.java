package com.qs.arm.integration;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.qs.arm.mvp.IModel;

/**
 * 用来管理网络请求层,以及数据缓存层,添加数据库请求层
 * 提供给 {@link IModel} 必要的 Api 做数据处理
 *
 * @author 华清松
 */
public interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     * 网络服务
     *
     * @param service Class
     * @param <T>     T
     * @return T
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     * 缓存服务
     *
     * @param cache Class
     * @param <T>   T
     * @return T
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 根据传入的 Class 获取对应的 Room 数据库
     *
     * @param database 数据库实体Class
     * @param dbName   数据库名称
     * @return RoomDatabase
     */
    <DB extends RoomDatabase> DB obtainRoomDatabase(Class<DB> database, String dbName);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    /**
     * getContext
     *
     * @return Context
     */
    Context getContext();

}
