package com.qs.gzb.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.widget.RecyclerView;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.arm.integration.AppManager;
import com.qs.arm.mvp.BasePresenter;
import com.qs.arm.utils.PermissionUtil;
import com.qs.arm.utils.RxLifecycleUtils;
import com.qs.gzb.mvp.contract.UserContract;
import com.qs.gzb.mvp.model.entity.User;

import java.util.List;

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
public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> implements UserContract.Presenter {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<User> mUsers;
    private RecyclerView.Adapter mAdapter;
    private int lastUserId = 1;
    private boolean isFirst = true;
    private int preEndIndex;

    @Inject
    UserPresenter(UserContract.Model model, UserContract.View view, RxErrorHandler handler
            , AppManager appManager, Application application, List<User> list
            , RecyclerView.Adapter adapter) {

        super(model, view);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
        this.mUsers = list;
        this.mAdapter = adapter;
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycle 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        // 加载列表
        requestUsers(true);
    }

    @Override
    public void requestUsers(final boolean pullToRefresh) {
        // 请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                // request permission success, do something.
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

        if (pullToRefresh) {
            // 下拉刷新默认只请求第一页
            lastUserId = 1;
        }

        // 是否使用缓存,为 true 即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存
        boolean isEvictCache = pullToRefresh;

        // 默认在第一次下拉刷新时使用缓存
        if (pullToRefresh && isFirst) {
            isFirst = false;
            isEvictCache = false;
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                // 遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .retryWhen(new RetryWithDelay(3, 2))
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                        // 显示下拉刷新的进度条
                        mRootView.showLoading();
                    } else {
                        // 显示上拉加载更多的进度条
                        mRootView.startLoadMore();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh) {
                        // 隐藏下拉刷新的进度条
                        mRootView.hideLoading();
                    } else {
                        // 隐藏上拉加载更多的进度条
                        mRootView.endLoadMore();
                    }
                })
                // 使用 RxLifecycle,使 Disposable 和 Activity 一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    @Override
                    public void onNext(List<User> users) {
                        // 记录最后一个id,用于下一次请求
                        lastUserId = users.get(users.size() - 1).getId();
                        if (pullToRefresh) {
                            // 如果是下拉刷新则清空列表
                            mUsers.clear();
                        }
                        // 更新之前列表总长度,用于确定加载更多的起始位置
                        preEndIndex = mUsers.size();
                        mUsers.addAll(users);
                        if (pullToRefresh) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mUsers = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }
}
