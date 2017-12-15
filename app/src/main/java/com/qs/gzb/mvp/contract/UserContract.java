package com.qs.gzb.mvp.contract;

import android.app.Activity;

import com.qs.arm.mvp.IModel;
import com.qs.arm.mvp.IPresenter;
import com.qs.arm.mvp.IView;
import com.qs.gzb.mvp.model.entity.User;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.Observable;

/**
 * 展示 Contract 的用法
 *
 * @author 华清松
 */
public interface UserContract {
    interface View extends IView {
        /**
         * 加载更多
         */
        void startLoadMore();

        /**
         * 停止加载更多
         */
        void endLoadMore();

        /**
         * 获得当前Activity
         *
         * @return Activity
         */
        Activity getActivity();

        /**
         * 申请权限
         *
         * @return RxPermissions
         */
        RxPermissions getRxPermissions();

    }

    interface Model extends IModel {
        /**
         * 获取用户列表
         *
         * @param lastIdQueried 最后一个会员id
         * @param update        是否更新
         * @return Observable
         */
        Observable<List<User>> getUsers(int lastIdQueried, boolean update);

    }

    interface Presenter extends IPresenter {
        /**
         * 请求会员列表数据
         *
         * @param pullToRefresh 是否刷新数据
         */
        void requestUsers(final boolean pullToRefresh);

    }
}
