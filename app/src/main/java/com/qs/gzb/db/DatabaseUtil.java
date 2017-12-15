package com.qs.gzb.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.qs.gzb.mvp.model.entity.User;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 华清松
 * @since BaseUserDatabase单例工具类
 */
public class DatabaseUtil {

    private static BaseUserDatabase database = null;

    public static BaseUserDatabase getInstance(Context context) {
        if (database == null) {
            synchronized (BaseUserDatabase.class) {
                database = Room.databaseBuilder(context, BaseUserDatabase.class, "database-name").build();
            }
        }
        return database;
    }

    Observable<User> login(int phone, String password) {
        return Observable.just(
                database.userDao().login(phone, password))
                .subscribeOn(Schedulers.computation());
    }

}
