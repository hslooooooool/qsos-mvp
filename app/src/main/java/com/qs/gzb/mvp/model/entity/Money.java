package com.qs.gzb.mvp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * 金钱实体
 *
 * @author 华清松
 */
@Entity
public class Money {
    @PrimaryKey
    private final int id;
    @ColumnInfo(name = "login")
    private final String login;

    public Money(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "id -> " + id + " login -> " + login;
    }
}
