package com.qs.gzb.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.qs.gzb.mvp.model.MoneyDao;
import com.qs.gzb.mvp.model.UserDao;
import com.qs.gzb.mvp.model.entity.Money;
import com.qs.gzb.mvp.model.entity.User;

/**
 * @author 华清松
 * @see RoomDatabase
 * @since 数据库
 */
@Database(entities = {User.class, Money.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class BaseUserDatabase extends RoomDatabase {
    /**
     * 用户Dao层
     *
     * @return UserDao
     */
    public abstract UserDao userDao();

    /**
     * 金钱Dao层
     *
     * @return MoneyDao
     */
    public abstract MoneyDao moneyDao();
}
