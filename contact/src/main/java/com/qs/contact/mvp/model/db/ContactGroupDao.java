package com.qs.contact.mvp.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.qs.contact.mvp.model.entity.ContactGroup;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author 华清松
 * @since 联系人群组 Dao 层
 */
@Dao
public interface ContactGroupDao {

    /**
     * 添加联系人群组
     *
     * @return List<ContactGroup>
     */
    @Insert
    void insert(ContactGroup contactGroup);

    /**
     * 获取所有联系人群组
     *
     * @return List<ContactGroup>
     */
    @Query("SELECT * FROM contact_group")
    Flowable<List<ContactGroup>> findAll();


}
