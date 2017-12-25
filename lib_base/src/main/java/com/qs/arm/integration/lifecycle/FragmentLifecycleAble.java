package com.qs.arm.integration.lifecycle;

import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * 让 {@link Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 */
public interface FragmentLifecycleAble extends LifecycleAble<FragmentEvent> {
}
