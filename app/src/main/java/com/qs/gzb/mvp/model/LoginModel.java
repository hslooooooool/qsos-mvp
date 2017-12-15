
package com.qs.gzb.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.arm.integration.IRepositoryManager;
import com.qs.arm.mvp.BaseModel;
import com.qs.gzb.mvp.contract.LoginContract;
import com.qs.gzb.mvp.model.api.cache.CommonCache;
import com.qs.gzb.mvp.model.api.service.UserService;
import com.qs.gzb.mvp.model.entity.BaseJson;
import com.qs.gzb.mvp.model.entity.User;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import timber.log.Timber;

/**
 * 登录
 *
 * @author 华清松
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {

    @Inject
    LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }

    @Override
    public Observable<BaseJson<User>> login(int phone, String password) {
        // 使用rxCache缓存
        return Observable.just(mRepositoryManager.obtainRetrofitService(UserService.class).login(phone, password))
                .flatMap(databaseObservable ->
                        mRepositoryManager.obtainCacheService(CommonCache.class).login(databaseObservable, new DynamicKey(phone), new EvictDynamicKey(true)).map(Reply::getData));
    }

}
