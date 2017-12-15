package com.qs.gzb.app.utils;

import com.qs.arm.mvp.IView;
import com.qs.arm.utils.RxLifecycleUtils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 放置便于使用 RxJava 的一些工具类
 *
 * @author 华清松
 */
public class RxUtils {

    RxUtils() {
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        // 隐藏进度条
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    // 显示进度条
                    view.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(view::hideLoading).compose(RxLifecycleUtils.bindToLifecycle(view));
    }

}
