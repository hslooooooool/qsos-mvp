package com.qs.gzb.mvp.contract;

import android.app.Activity;

import com.qs.arm.mvp.IModel;
import com.qs.arm.mvp.IPresenter;
import com.qs.arm.mvp.IView;
import com.qs.gzb.mvp.model.entity.BaseJson;
import com.qs.gzb.mvp.model.entity.User;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

/**
 * 登录
 *
 * @author 华清松
 */
public interface LoginContract {
    interface View extends IView {

        /**
         * 表单有误
         *
         * @param errorCode 错误代码
         *                  手机号格式错误：1
         */
        void formIsError(int errorCode);

        /**
         * 登录
         *
         * @param code 结果代码
         *             登录成功：0
         *             手机号不存在：1
         *             密码错误：2
         */
        void loginResult(String code);

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
         * @param phone    账号
         * @param password 密码
         * @return Observable
         */
        Observable<BaseJson<User>> login(int phone, String password);

    }

    interface Presenter extends IPresenter {
        /**
         * 通过 phone 检查账号是否存在
         *
         * @param phone 手机号
         */
        void checkNullByPhone(int phone);

        /**
         * 使用 phone 和 password 进行账号登录
         *
         * @param phone    手机号
         * @param password 密码
         */
        void login(int phone, String password);
    }
}
