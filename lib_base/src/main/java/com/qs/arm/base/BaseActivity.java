package com.qs.arm.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.qs.arm.base.delegate.IActivity;
import com.qs.arm.integration.lifecycle.ActivityLifecycle;
import com.qs.arm.mvp.IPresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.qs.arm.utils.ThirdViewUtil.convertAutoView;

/**
 * 因为 Java 只能单继承,所以如果要用到需要继承特定 {@link Activity} 的三方库,那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity},然后再按照 {@link BaseActivity} 的格式,将代码复制过去,记住一定要实现{@link IActivity}
 *
 * @author 华清松
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity
        implements IActivity, ActivityLifecycle {

    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder unbinder;
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    protected P mPresenter;

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubscriber() {
        return mLifecycleSubject;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = convertAutoView(name, context, attrs);
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            // 如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                // 绑定到ButterKnife
                unbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    /**
     * super.onDestroy()之后会unbind,所有view被置为null,所以关于view的所有操作必须在super之前调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
        unbinder = null;
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return boolean
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link com.qs.arm.base.BaseFragment} 的Fragment将不起任何作用
     *
     * @return boolean
     */
    @Override
    public boolean useFragment() {
        return true;
    }
}
