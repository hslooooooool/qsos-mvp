package com.qs.arm.mvp;

import android.app.Activity;
import android.content.Intent;

/**
 * 框架中的每个 View 都需要实现此类,以满足规范
 *
 * @author 华清松
 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     *
     * @param message message
     */
    void showMessage(String message);

    /**
     * 跳转 {@link Activity}
     *
     * @param intent
     */
    void launchActivity(Intent intent);

    /**
     * 杀死自己
     */
    void killMyself();
}
