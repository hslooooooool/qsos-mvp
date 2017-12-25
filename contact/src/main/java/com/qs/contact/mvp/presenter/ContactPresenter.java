package com.qs.contact.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.qs.arm.mvp.BasePresenter;
import com.qs.arm.utils.RxLifecycleUtils;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.model.entity.Contact;
import com.qs.contact.mvp.model.entity.ContactBean;
import com.qs.contact.mvp.model.entity.ContactGroup;
import com.qs.contact.mvp.ui.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * @author 华清松
 * @since 联系人数据获取
 */
public class ContactPresenter extends BasePresenter<ContactContract.Model, ContactContract.View>
        implements ContactContract.Presenter {

    private Application mApplication;
    private RxErrorHandler mErrorHandler;
    private ContactAdapter mAdapter;
    private List<ContactBean> mContacts;
    private List<ContactGroup> mGroups;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    ContactPresenter(ContactContract.Model model, ContactContract.View view, RxErrorHandler handler,
                     Application application, List<ContactBean> contacts, ContactAdapter adapter) {
        super(model, view);

        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mContacts = contacts;
        this.mAdapter = adapter;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

    }

    @Override
    public void addContact(Contact contact) {
        mDisposable.add(mModel.addContact(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }));
    }

    @Override
    public void addContactGroup(ContactGroup contactGroup) {
        mDisposable.add(mModel.addContactGroup(contactGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }));
    }

    @Override
    public void getGroupAndContact(boolean update) {
        mModel.getContactGroups(update)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<ContactGroup>>(mErrorHandler) {
                    @Override
                    public void onNext(List<ContactGroup> groups) {
                        if (groups != null) {
                            mContacts.clear();
                            mContacts.addAll(groups);

                            mGroups = new ArrayList<>();
                            mGroups.addAll(groups);

                            getContact();
                        }
                    }
                });
    }

    @Override
    public void getContact() {
        mModel.getContacts()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<Contact>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Contact> contacts) {
                        if (contacts != null) {
                            mContacts.clear();
                            mContacts.addAll(mGroups);
                            mContacts.addAll(contacts);

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mApplication = null;
        this.mContacts = null;
        this.mGroups = null;
    }
}
