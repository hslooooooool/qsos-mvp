package com.qs.gzb.db;

import android.content.Context;

import com.qs.gzb.mvp.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author 华清松
 * @see
 * @since TODO 描述
 */
public class DatabaseObservable {
    /**
     * 用户查询
     */
    private PublishSubject<List> userSubject;

    public DatabaseObservable(Context context) {
        userSubject = PublishSubject.create();
    }

    /**
     * 返回观察者
     *
     * @return Observable
     */
    public Observable<List> getObservable(Context context, int phone, String password) {
        Observable<List> firstObservable = Observable.fromCallable(this::getRepoList);
        return firstObservable.concatWith(userSubject);
    }

    private ArrayList<User> getRepoList() {
        return new ArrayList<>();
    }
}
