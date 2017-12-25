package com.qs.contact.mvp.model.entity;

import android.arch.persistence.room.Ignore;

import com.qs.contact.mvp.ui.adapter.type.ContactType;

/**
 * @author 华清松
 * @since 联系人与分组
 */
public class ContactBean {

    @Ignore
    private ContactType type;
    @Ignore
    private String pinyin;

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
