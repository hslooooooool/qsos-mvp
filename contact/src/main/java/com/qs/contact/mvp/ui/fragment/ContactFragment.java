package com.qs.contact.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qs.arm.base.BaseAdapter;
import com.qs.arm.base.BaseFragment;
import com.qs.arm.di.component.AppComponent;
import com.qs.arm.utils.ArmsUtils;
import com.qs.contact.R;
import com.qs.contact.di.component.DaggerContactComponent;
import com.qs.contact.di.module.ContactModule;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.model.entity.Contact;
import com.qs.contact.mvp.model.entity.ContactGroup;
import com.qs.contact.mvp.presenter.ContactPresenter;
import com.qs.contact.mvp.ui.adapter.ContactAdapter;
import com.qs.contact.mvp.ui.widget.PingYinBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 华清松
 * @email 821034742@qq.com
 * @weixin hslooooooool
 * @doc 类说明：
 */
public class ContactFragment extends BaseFragment<ContactPresenter> implements ContactContract.View {
    public static BaseFragment newInstance(Bundle bundle) {
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.contact_search_input)
    TextInputEditText mSearchInput;
    @BindView(R.id.contact_list)
    RecyclerView mContactList;
    @BindView(R.id.contact_pinyin)
    PingYinBar mPinyinBar;
    @BindView(R.id.contact_chosen)
    TextView mChosen;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    ContactAdapter mAdapter;

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void onDestroy() {
        BaseAdapter.releaseAllHolder(mContactList);
        super.onDestroy();
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerContactComponent.builder()
                .appComponent(appComponent)
                .contactModule(new ContactModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_fragment_main, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        ArmsUtils.configRecycleView(mContactList, mLayoutManager);
        mContactList.setAdapter(mAdapter);
        mPinyinBar.setTextView(mChosen);
        mPinyinBar.setOnTouchLetterListener(letter -> {
            int position = mAdapter.getSelectPosition(letter);
            if (position != -1) {
                mLayoutManager.scrollToPosition(position);
            }
        });
        mPresenter.getGroupAndContact(false);
    }

    @Override
    public void setData(Object data) {

    }

    @OnClick(R.id.contact_add)
    void AddContact(View contactAdd) {
        mPresenter.addContactGroup(new ContactGroup(mSearchInput.getText().toString()));
        mPresenter.addContact(new Contact(mSearchInput.getText().toString()));
    }

}
