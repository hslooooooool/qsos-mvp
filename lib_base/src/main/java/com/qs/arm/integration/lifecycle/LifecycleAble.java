
package com.qs.arm.integration.lifecycle;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.qs.arm.utils.RxLifecycleUtils;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.subjects.Subject;

/**
 * ================================================
 * 让 {@link Activity}/{@link Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 * 无需再继承 {@link RxLifecycle} 提供的 Activity/Fragment ,扩展性极强
 *
 * @author 华清松
 * @see RxLifecycleUtils 详细用法请查看此类
 * Created by JessYan on 25/08/2017 18:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface LifecycleAble<E> {
    /**
     * 提供生命周期监听者
     *
     * @return
     */
    @NonNull
    Subject<E> provideLifecycleSubscriber();
}
