package com.qs.gzb.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.paginate.Paginate;
import com.qs.arm.base.BaseActivity;
import com.qs.arm.base.BaseAdapter;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.utils.ArmsUtils;
import com.qs.gzb.R;
import com.qs.gzb.di.component.DaggerUserComponent;
import com.qs.gzb.di.module.UserModule;
import com.qs.gzb.mvp.contract.UserContract;
import com.qs.gzb.mvp.presenter.UserPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

/**
 * @author 华清松
 */
@Route(path = "user/list")
public class UserActivity extends BaseActivity<UserPresenter>
        implements UserContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    /**
     * 分页管理
     */
    private Paginate mPaginate;
    /**
     * 是否为加载更多
     */
    private boolean isLoadingMore;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerUserComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_user;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRecycleView();
        mRecyclerView.setAdapter(mAdapter);
        initPaginate();
    }

    @Override
    public void onRefresh() {
        mPresenter.requestUsers(true);
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecycleView(mRecyclerView, mLayoutManager);
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestUsers(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return false;
                }
            };

            mPaginate = Paginate.with(mRecyclerView, callbacks).setLoadingTriggerThreshold(0).build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    protected void onDestroy() {
        BaseAdapter.releaseAllHolder(mRecyclerView);

        super.onDestroy();

        this.mRxPermissions = null;
        this.mPaginate = null;
    }
}
