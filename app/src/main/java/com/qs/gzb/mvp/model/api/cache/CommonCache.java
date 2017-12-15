package com.qs.gzb.mvp.model.api.cache;

import com.qs.gzb.mvp.model.entity.BaseJson;
import com.qs.gzb.mvp.model.entity.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;
import io.rx_cache2.internal.RxCache;

/**
 * 展示 {@link RxCache#using(Class)} 中需要传入的 Providers 的使用方式
 *
 * @author 华清松
 */
public interface CommonCache {

    /**
     * 获取用户列表
     *
     * @param users             当前用户列表
     * @param idLastUserQueried 最后一个id缓存key
     * @param evictProvider     key对应的缓存器
     * @return Observable
     */
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<List<User>> getUsers(Observable<List<User>> users, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    /**
     * 登录
     *
     * @param userObservable 当前用户
     * @param dynamicKey     当前用户ID
     * @param evictProvider  是否缓存器
     * @return Observable
     */
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<BaseJson<User>>> login(Observable<BaseJson<User>> userObservable, DynamicKey dynamicKey, EvictProvider evictProvider);
}
