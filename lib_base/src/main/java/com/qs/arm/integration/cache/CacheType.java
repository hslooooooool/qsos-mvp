package com.qs.arm.integration.cache;

import android.app.ActivityManager;
import android.content.Context;

/**
 * 构建 {@link Cache} 时,使用 {@link CacheType} 中声明的类型,来区分不同的模块
 * 从而为不同的模块构建不同的缓存策略
 *
 * @see Cache.Factory#build(CacheType)
 */
public interface CacheType {
    int RETROFIT_SERVICE_CACHE_TYPE_ID = 0;
    int CACHE_SERVICE_CACHE_TYPE_ID = 1;
    int EXTRAS_TYPE_ID = 2;
    /**
     * Room 数据库类型
     */
    int ROOM_DATABASE_CACHE_TYPE_ID = 3;
    /**
     * RepositoryManager 中存储 Retrofit Service 的容器
     */
    CacheType RETROFIT_SERVICE_CACHE = new CacheType() {
        private static final int MAX_SIZE = 150;
        private static final float MAX_SIZE_MULTIPLIER = 0.002f;

        @Override
        public int getCacheTypeId() {
            return RETROFIT_SERVICE_CACHE_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    /**
     * RepositoryManager 中储存 Cache Service 的容器
     */
    CacheType CACHE_SERVICE_CACHE = new CacheType() {
        private static final int MAX_SIZE = 150;
        private static final float MAX_SIZE_MULTIPLIER = 0.002f;

        @Override
        public int getCacheTypeId() {
            return CACHE_SERVICE_CACHE_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    /**
     * AppComponent 中的 extras
     */
    CacheType EXTRAS = new CacheType() {
        private static final int MAX_SIZE = 500;
        private static final float MAX_SIZE_MULTIPLIER = 0.005f;

        @Override
        public int getCacheTypeId() {
            return EXTRAS_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    CacheType ROOM_DATABASE = new CacheType() {
        private static final int MAX_SIZE = 500;
        private static final float MAX_SIZE_MULTIPLIER = 0.005f;

        @Override
        public int getCacheTypeId() {
            return ROOM_DATABASE_CACHE_TYPE_ID;
        }

        @Override
        public int calculateCacheSize(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int targetMemoryCacheSize = (int) (activityManager.getMemoryClass() * MAX_SIZE_MULTIPLIER * 1024);
            if (targetMemoryCacheSize >= MAX_SIZE) {
                return MAX_SIZE;
            }
            return targetMemoryCacheSize;
        }
    };

    /**
     * 返回框架内需要缓存的模块对应的 {@code id}
     *
     * @return
     */
    int getCacheTypeId();

    /**
     * 计算对应模块需要的缓存大小
     *
     * @param context context
     * @return int
     */
    int calculateCacheSize(Context context);
}
