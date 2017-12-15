package com.qs.arm.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qs.arm.base.delegate.IFragment;
import com.qs.arm.integration.lifecycle.FragmentLifecycleAble;
import com.qs.arm.mvp.IPresenter;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 因为 Java 只能单继承,所以如果要用到需要继承特定 @{@link Fragment} 的三方库,那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment},然后再按照 {@link BaseFragment} 的格式,将代码复制过去,记住一定要实现{@link IFragment}
 *
 * @author 华清松
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleAble {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    protected P mPresenter;


    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubscriber() {
        return mLifecycleSubject;
    }

    public BaseFragment() {
        // 必须确保在Fragment实例化时setArguments()
        setArguments(new Bundle());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
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

}
