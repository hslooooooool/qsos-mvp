package com.qs.gzb.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.arm.integration.AppManager;
import com.qs.arm.mvp.BasePresenter;
import com.qs.arm.utils.RxLifecycleUtils;
import com.qs.gzb.mvp.contract.LoginContract;
import com.qs.gzb.mvp.model.entity.BaseJson;
import com.qs.gzb.mvp.model.entity.User;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

/**
 * 展示 Presenter 的用法
 *
 * @author 华清松
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> implements LoginContract.Presenter {
    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private int preEndIndex;

    @Inject
    LoginPresenter(LoginContract.Model model, LoginContract.View view, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, view);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycle 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

    }

    @Override
    public void checkNullByPhone(int phone) {

    }

    @Override
    public void login(int phone, String password) {
        // 判断手机号是否为空
        // TODO: 2017/11/14

        // 判断密码是否为空
        if (password == null) {
            mRootView.formIsError(1);
            return;
        }

        mModel.login(phone, password)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    // 显示登录进度条
                    mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    // 隐藏登录的进度条
                    mRootView.hideLoading();
                })
                // 使用 RxLifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<User>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<User> user) {
                        mRootView.loginResult(user.getCode());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }
}
