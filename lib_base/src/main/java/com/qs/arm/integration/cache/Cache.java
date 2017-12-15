package com.qs.arm.integration.cache;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qs.arm.di.module.ConfigModule;

/**
 * 用于缓存框架中所必需的组件,开发者可通过 {@link ConfigModule.Builder#cacheFactory(Factory)} 为框架提供缓存策略
 * 开发者也可以用于自己日常中的使用
 *
 * @author 华清松
 * @see ConfigModule#provideCacheFactory(Application)
 * @see LruCache
 */
public interface Cache<K, V> {
    interface Factory {

        /**
         * Returns a new cache
         *
         * @param type 框架中需要缓存的模块类型
         * @return Cache
         */
        @NonNull
        Cache build(CacheType type);
    }

    /**
     * 返回当前缓存已占用的总 size
     *
     * @return size
     */
    int size();

    /**
     * 返回当前缓存所能允许的最大 size
     *
     * @return size
     */
    int getMaxSize();

    /**
     * 返回这个 {@code key} 在缓存中对应的 {@code value}, 如果返回 {@code null} 说明这个 {@code key} 没有对应的 {@code value}
     *
     * @param key key
     * @return V
     */
    @Nullable
    V get(K key);

    /**
     * 将 {@code key} 和 {@code value} 以条目的形式加入缓存,如果这个 {@code key} 在缓存中已经有对应的 {@code value}
     * 则此 {@code value} 被新的 {@code value} 替换并返回,如果为 {@code null} 说明是一个新条目
     *
     * @param key   key
     * @param value value
     * @return V
     */
    @Nullable
    V put(K key, V value);

    /**
     * 移除缓存中这个 {@code key} 所对应的条目,并返回所移除条目的 value
     * 如果返回为 {@code null} 则有可能时因为这个 {@code key} 对应的 value 为 {@code null} 或条目不存在
     *
     * @param key key
     * @return V
     */
    @Nullable
    V remove(K key);

    /**
     * 如果这个 {@code key} 在缓存中有对应的 value 并且不为 {@code null}, 则返回 {@code true}
     *
     * @param key key
     * @return boolean
     */
    boolean containsKey(K key);

    /**
     * 清除缓存中所有的内容
     */
    void clear();
}
