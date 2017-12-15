package com.qs.contact.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.qs.arm.di.scope.ActivityScope;
import com.qs.arm.integration.IRepositoryManager;
import com.qs.arm.mvp.BaseModel;
import com.qs.contact.mvp.contract.ContactContract;
import com.qs.contact.mvp.model.api.service.ContactService;
import com.qs.contact.mvp.model.db.ContactDatabase;
import com.qs.contact.mvp.model.entity.ContactGroup;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * @author 华清松
 * @since 联系人数据操作
 */
@ActivityScope
public class ContactModel extends BaseModel implements ContactContract.Model {

    String test = "1";

    @Inject
    ContactModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Completable addContactGroup(ContactGroup contactGroup) {
        return Completable.fromAction(() -> {
            mRepositoryManager.obtainRoomDatabase(ContactDatabase.class, ContactDatabase.dbName).contactGroupDao()
                    .insert(contactGroup);
        });
    }

    @Override
    public Observable<List<ContactGroup>> getContactGroups(boolean update) {
        if (update) {
            return mRepositoryManager.obtainRetrofitService(ContactService.class).getContactGroup();
        } else {
            return mRepositoryManager.obtainRoomDatabase(ContactDatabase.class, ContactDatabase.dbName).contactGroupDao()
                    .findAll()
                    .toObservable();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }

}
