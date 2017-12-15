package com.qs.arm.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.qs.arm.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * {@link FragmentDelegate} 实现类
 *
 * @author 华清松
 */
public class FragmentDelegateImpl implements FragmentDelegate {
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;
    private Unbinder unbinder;

    public FragmentDelegateImpl(android.support.v4.app.FragmentManager fragmentManager, android.support.v4.app.Fragment fragment) {
        this.mFragmentManager = fragmentManager;
        this.mFragment = fragment;
        this.iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 如果要使用EventBus请将此方法返回true
        if (iFragment.useEventBus()) {
            // 注册到事件主线
            EventBus.getDefault().register(mFragment);
        }
        iFragment.setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(mFragment.getActivity()));
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        // 绑定到ButterKnife
        if (view != null) {
            unbinder = ButterKnife.bind(mFragment, view);
        }
    }

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        iFragment.initData(savedInstanceState);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroyView() {
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            try {
                unbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Timber.w("onDestroyView: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        // 如果要使用EventBus请将此方法返回true
        if (iFragment != null && iFragment.useEventBus()) {
            // 注册到事件主线
            EventBus.getDefault().unregister(mFragment);
        }
        this.unbinder = null;
        this.mFragmentManager = null;
        this.mFragment = null;
        this.iFragment = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private FragmentDelegateImpl(Parcel in) {
        this.mFragmentManager = in.readParcelable(FragmentManager.class.getClassLoader());
        this.mFragment = in.readParcelable(Fragment.class.getClassLoader());
        this.iFragment = in.readParcelable(IFragment.class.getClassLoader());
        this.unbinder = in.readParcelable(Unbinder.class.getClassLoader());
    }

    public static final Creator<FragmentDelegateImpl> CREATOR = new Creator<FragmentDelegateImpl>() {
        @Override
        public FragmentDelegateImpl createFromParcel(Parcel source) {
            return new FragmentDelegateImpl(source);
        }

        @Override
        public FragmentDelegateImpl[] newArray(int size) {
            return new FragmentDelegateImpl[size];
        }
    };
}
