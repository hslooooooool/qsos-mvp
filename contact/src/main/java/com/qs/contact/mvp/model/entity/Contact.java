package com.qs.contact.mvp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.qs.arm.utils.CharacterHandler;
import com.qs.contact.mvp.ui.adapter.type.ContactType;

import java.util.Date;
import java.util.UUID;

/**
 * @author 华清松
 * @since 联系人实体
 */
@Entity(tableName = "contact")
public class Contact extends ContactBean {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "cid")
    private String contactId;
    /**
     * 联系人姓名
     */
    @ColumnInfo(name = "contact_name")
    private String contactName;
    /**
     * 联系人性别
     */
    private String sex;
    /**
     * 头像地址
     */
    @ColumnInfo(name = "head_icon")
    private String headIcon;
    /**
     * 联系人手机号
     */
    private String phone;
    /**
     * 联系人密码
     */
    private String password;
    /**
     * 联系人邮箱号
     */
    private String email;
    /**
     * 创建时间
     */
    @ColumnInfo(name = "gmt_create")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @ColumnInfo(name = "gmt_modified")
    private Date gmtModified;

    public Contact() {
    }

    @Ignore
    public Contact(@NonNull String name) {
        this.contactId = UUID.randomUUID().toString();
        this.contactName = name;
        this.gmtCreate = new Date(System.currentTimeMillis());
        this.gmtModified = new Date(System.currentTimeMillis());
    }

    @Ignore
    public Contact(@NonNull String id, @NonNull String name, @NonNull String phone) {
        this.contactId = id;
        this.contactName = name;
        this.gmtCreate = new Date(System.currentTimeMillis());
        this.gmtModified = new Date(System.currentTimeMillis());
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public ContactType getType() {
        return ContactType.Contact;
    }

    public String getPinyin() {
        return CharacterHandler.getFirstLetter(contactName);
    }
}
