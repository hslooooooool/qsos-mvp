package com.qs.contact.mvp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * @author 华清松
 * @since 联系人分组
 */
@Entity(tableName = "contact_group")
public class ContactGroup {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gid")
    private long groupId;
    /**
     * 拥有者
     */
    @ColumnInfo(name = "cid")
    private String ownerId;
    /**
     * 组名
     */
    @ColumnInfo(name = "group_name")
    private String groupName;
    /**
     * 成员数
     */
    private String members;
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

    public ContactGroup() {
    }

    @Ignore
    public ContactGroup(@NonNull String name) {
        this.groupName = name;
        this.members = "0";
        this.gmtCreate = new Date(System.currentTimeMillis());
        this.gmtModified = new Date(System.currentTimeMillis());
    }

    @Ignore
    public ContactGroup(@NonNull String name, @NonNull Date date) {
        this.groupName = name;
        this.gmtCreate = date;
        this.gmtModified = date;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
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
}
