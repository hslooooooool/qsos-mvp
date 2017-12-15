package com.qs.arm.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * {@link Fragment} 代理类,用于框架内部在每个 {@link Fragment} 的对应生命周期中插入需要的逻辑
 *
 * @author 华清松
 * @see FragmentDelegateImpl
 */
public interface FragmentDelegate extends Parcelable {

    String FRAGMENT_DELEGATE = "fragment_delegate";

    /**
     * onAttach
     *
     * @param context context
     */
    void onAttach(Context context);

    /**
     * onCreate
     *
     * @param savedInstanceState savedInstanceState
     */
    void onCreate(Bundle savedInstanceState);

    /**
     * onCreateView
     *
     * @param view               view
     * @param savedInstanceState savedInstanceState
     */
    void onCreateView(View view, Bundle savedInstanceState);

    /**
     * onActivityCreate
     *
     * @param savedInstanceState savedInstanceState
     */
    void onActivityCreate(Bundle savedInstanceState);

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
     * onStart
     *
     * @param outState outState
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * onDestroyView
     */
    void onDestroyView();

    /**
     * onDestroy
     */
    void onDestroy();

    /**
     * onDetach
     */
    void onDetach();


    /**
     * fragment是否被add到activity
     *
     * @return boolean
     */
    boolean isAdded();
}
