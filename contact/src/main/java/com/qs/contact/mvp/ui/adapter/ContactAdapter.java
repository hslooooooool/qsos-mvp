package com.qs.contact.mvp.ui.adapter;

import android.view.View;

import com.qs.arm.base.BaseAdapter;
import com.qs.arm.base.BaseHolder;
import com.qs.contact.R;
import com.qs.contact.mvp.model.entity.Contact;
import com.qs.contact.mvp.ui.holder.ContactItemHolder;

import java.util.List;

/**
 * @author 华清松
 * @see
 * @since TODO 描述
 */
public class ContactAdapter extends BaseAdapter<Contact> {

    public ContactAdapter(List<Contact> dataS) {
        super(dataS);
    }

    @Override
    public BaseHolder<Contact> getHolder(View view, int viewType) {
        return new ContactItemHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.contact_item_view_contact;
    }
}
