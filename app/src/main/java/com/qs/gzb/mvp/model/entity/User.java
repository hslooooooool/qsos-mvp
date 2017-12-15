package com.qs.gzb.mvp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * 用户实体
 *
 * @author 华清松
 */
@Entity(foreignKeys = @ForeignKey(entity = Money.class,
        parentColumns = "mid",
        childColumns = "uid"))
public class User {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    private int id;
    @ColumnInfo(name = "login")
    private String login;
    @ColumnInfo(name = "avatar_url")
    private String avatar_url;
    @Embedded
    private Address address;

    public User() {
    }

    public User(int id, String login, String avatarUrl) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatarUrl;
    }

    public String getAvatarUrl() {
        if (avatar_url.isEmpty()) {
            return avatar_url;
        }
        return avatar_url.split("\\?")[0];
    }

    @Override
    public String toString() {
        return "id -> " + id + " login -> " + login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
