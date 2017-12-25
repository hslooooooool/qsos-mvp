package com.qs.contact.mvp.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.qs.contact.mvp.model.entity.Contact;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author 华清松
 * @since 联系人 Dao 层
 */
@Dao
public interface ContactDao {
    /**
     * 添加一个联系人
     *
     * @param contact contact
     */
    @Insert
    void insert(Contact contact);

    /**
     * 更新联系人
     *
     * @param contact contact
     */
    @Update
    void updata(@NonNull Contact contact);

    /**
     * 添加或更新一组联系人
     *
     * @param contacts Contact...
     */
    @Insert
    void insertAll(Contact... contacts);

    /**
     * 删除联系人
     *
     * @param contact contact
     */
    @Delete
    void delete(Contact contact);

    /**
     * 获取所有联系人
     *
     * @return List<User>
     */
    @Query("SELECT * FROM contact ORDER BY contact_name , phone ASC")
    Flowable<List<Contact>> findAll();

    /**
     * 通过 id 查询联系人
     *
     * @param cid cid
     * @return User
     */
    @Query("SELECT * FROM contact WHERE cid = :cid LIMIT 1")
    Flowable<Contact> findById(String cid);

    /**
     * 通过手机号或姓名匹配查询联系人
     *
     * @param key 搜索关键字
     * @return User
     */
    @Query("SELECT * FROM contact WHERE contact_name LIKE :key OR phone LIKE :key")
    Flowable<List<Contact>> findByKey(String key);

    /**
     * 通过姓名查询联系人
     *
     * @param name 姓
     * @return User
     */
    @Query("SELECT * FROM contact WHERE contact_name = :name LIMIT 1")
    Flowable<Contact> findByName(String name);

    /**
     * 通过电话查询联系人
     *
     * @param phone 电话
     * @return User
     */
    @Query("SELECT * FROM contact WHERE phone = :phone LIMIT 1")
    Flowable<Contact> findByPhone(String phone);

    /**
     * 通过电话(账号)和密码查询联系人
     *
     * @param phone    电话
     * @param password 密码
     * @return User
     */
    @Query("SELECT * FROM contact WHERE phone = :phone AND password = :password LIMIT 1")
    Flowable<Contact> login(String phone, String password);

}
