package com.qs.arm.integration.lifecycle;

import android.app.Activity;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * 让 {@link Activity} 实现此接口,即可正常使用 {@link RxLifecycle}
 *
 * @author 华清松
 */
public interface ActivityLifecycle extends LifecycleAble<ActivityEvent> {

}
