package com.qs.arm.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * 代理{@link Activity} ,用于框架内部在每个 {@link Activity} 的对应生命周期中插入需要的逻辑
 *
 * @author 华清松
 * @see ActivityDelegateImpl
 */
public interface ActivityDelegate extends Parcelable {

    String LAYOUT_LINEAR_LAYOUT = "LinearLayout";
    String LAYOUT_FRAME_LAYOUT = "FrameLayout";
    String LAYOUT_RELATIVE_LAYOUT = "RelativeLayout";
    String ACTIVITY_DELEGATE = "activity_delegate";

    /**
     * onCreate
     *
     * @param savedInstanceState savedInstanceState
     */
    void onCreate(Bundle savedInstanceState);

    /**
     * onStart
     */
    void onStart();

    /**
     * onResume
     */
    void onResume();

    /**
     * onPause
     */
    void onPause();

    /**
     * onStop
     */
    void onStop();

    /**
     * onSaveInstanceState
     *
     * @param outState outState
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * onDestroy
     */
    void onDestroy();
}
