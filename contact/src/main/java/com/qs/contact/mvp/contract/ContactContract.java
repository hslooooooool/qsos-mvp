package com.qs.contact.mvp.contract;

import android.app.Activity;

import com.qs.arm.mvp.IModel;
import com.qs.arm.mvp.IPresenter;
import com.qs.arm.mvp.IView;
import com.qs.contact.mvp.model.entity.Contact;
import com.qs.contact.mvp.model.entity.ContactGroup;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author 华清松
 * @since 联系人群组与联系人列表
 */
public interface ContactContract {

    interface View extends IView {
        /**
         * 获得当前Activity
         *
         * @return Activity
         */
        Activity getActivity();

    }

    interface Model extends IModel {
        /**
         * 添加联系人
         *
         * @param contact 联系人
         * @return Completable
         */
        Completable addContact(Contact contact);

        /**
         * 添加群组列表
         *
         * @param contactGroup 联系人群组
         * @return Completable
         */
        Completable addContactGroup(ContactGroup contactGroup);

        /**
         * 获取群组列表
         *
         * @param update 是否更新数据
         * @return Observable
         */
        Observable<List<ContactGroup>> getContactGroups(boolean update);

        /**
         * 获取群组列表
         *
         * @return Observable
         */
        Observable<List<Contact>> getContacts();

    }

    interface Presenter extends IPresenter {
        /**
         * 添加联系人
         *
         * @param contact 联系人
         */
        void addContact(Contact contact);

        /**
         * 添加群组
         *
         * @param contactGroup 是否从缓存获取数据
         */
        void addContactGroup(ContactGroup contactGroup);

        /**
         * 获取群组与联系人列表
         *
         * @param update 是否从缓存获取数据
         */
        void getGroupAndContact(boolean update);

        /**
         * 获取用户列表
         */
        void getContact();

    }

}
