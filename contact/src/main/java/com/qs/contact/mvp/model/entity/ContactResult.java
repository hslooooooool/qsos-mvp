package com.qs.contact.mvp.model.entity;

import java.util.List;

/**
 * @author 华清松
 * @since 联系人与分组
 */
public class ContactResult {

    private List<ContactGroup> groups;

    private List<Contact> contacts;

    public List<ContactGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ContactGroup> groups) {
        this.groups = groups;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
