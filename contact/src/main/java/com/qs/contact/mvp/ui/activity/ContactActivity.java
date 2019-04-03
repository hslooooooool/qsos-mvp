package com.qs.contact.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qs.arm.base.BaseActivity;
import com.qs.arm.base.BaseFragment;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.integration.lifecycle.ActivityLifecycle;
import com.qs.arm.utils.ArmsUtils;
import com.qs.contact.R;
import com.qs.contact.constant.ConstantRouter;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.presenter.ContactPresenter;
import com.qs.contact.mvp.ui.fragment.ContactFragment;

/**
 * @author 华清松
 * @see com.qs.arm.base.BaseActivity
 * @since 联系人列表页
 */
@Route(group = ConstantRouter.GROUP, path = ConstantRouter.MAIN)
public class ContactActivity extends BaseActivity<ContactPresenter>
        implements ContactContract.View, ActivityLifecycle {

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.contact_activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        BaseFragment fragment = ContactFragment.newInstance(new Bundle());
        FragmentManager manager = this.getSupportFragmentManager();
        if (manager != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_enter, R.anim.right_exit)
                    .replace(R.id.contact_main, fragment);
            transaction.commit();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
