package com.qs.gzb.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.arm.integration.IRepositoryManager;
import com.qs.arm.mvp.BaseModel;
import com.qs.gzb.mvp.contract.UserContract;
import com.qs.gzb.mvp.model.api.cache.CommonCache;
import com.qs.gzb.mvp.model.api.service.UserService;
import com.qs.gzb.mvp.model.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import timber.log.Timber;

/**
 * 展示 Model 的用法
 *
 * @author 华清松
 */
@ActivityScope
public class UserModel extends BaseModel implements UserContract.Model {
    private static final int USERS_PER_PAGE = 10;

    @Inject
    UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        // 使用rxCache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager.obtainRetrofitService(UserService.class).getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap(func ->
                        mRepositoryManager.obtainCacheService(CommonCache.class).getUsers(func, new DynamicKey(lastIdQueried), new EvictDynamicKey(update)));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }

}
