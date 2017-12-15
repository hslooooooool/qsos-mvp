package com.qs.contact.mvp.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.qs.contact.mvp.model.entity.Contact;
import com.qs.contact.mvp.model.entity.ContactGroup;

/**
 * @author 华清松
 * @since 联系人数据库
 */
@Database(entities = {Contact.class, ContactGroup.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class ContactDatabase extends RoomDatabase {
    public static String dbName = "qs_os_contact";

    /**
     * 联系人 Dao 层
     *
     * @return ContactDao
     */
    public abstract ContactDao contactDao();

    /**
     * 联系人群组 Dao 层
     *
     * @return ContactGroupDao
     */
    public abstract ContactGroupDao contactGroupDao();

}
