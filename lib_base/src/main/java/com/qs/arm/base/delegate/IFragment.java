
package com.qs.arm.base.delegate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qs.arm.base.BaseFragment;
import com.qs.arm.di.component.AppComponent;

import org.simple.eventbus.EventBus;

/**
 * 框架中的每个 {@link Fragment} 都需要实现此类,以满足规范
 *
 * @author 华清松
 * @see BaseFragment
 */
public interface IFragment {
    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类,进行 Component 依赖
     *
     * @param appComponent appComponent
     */
    void setupFragmentComponent(AppComponent appComponent);

    /**
     * 是否使用 {@link EventBus}
     *
     * @return
     */
    boolean useEventBus();

    /**
     * 初始化 View
     *
     * @param inflater           inflater
     * @param container          container
     * @param savedInstanceState savedInstanceState
     * @return View
     */
    View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data data
     */
    void setData(Object data);
}
