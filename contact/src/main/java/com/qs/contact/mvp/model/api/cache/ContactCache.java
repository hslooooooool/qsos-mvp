package com.qs.contact.mvp.model.api.cache;

import com.qs.contact.mvp.model.entity.ContactResult;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;

/**
 * 联系人群组与联系人数据缓存
 *
 * @author 华清松
 */
public interface ContactCache {

    /**
     * 获取联系人与群组数据
     *
     * @param contacts          当前群组与联系人列表
     * @param idLastUserQueried 最后一个id缓存key
     * @param evictProvider     key对应的缓存器
     * @return Observable
     */
    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<ContactResult> getGroupAndContact(Observable<ContactResult> contacts, DynamicKey idLastUserQueried, EvictProvider evictProvider);

}
