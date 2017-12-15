package com.qs.contact.mvp.ui.adapter;

import android.view.View;

import com.qs.arm.base.BaseAdapter;
import com.qs.arm.base.BaseHolder;
import com.qs.contact.R;
import com.qs.contact.mvp.model.entity.ContactGroup;
import com.qs.contact.mvp.ui.holder.ContactGroupItemHolder;

import java.util.List;

/**
 * @author 华清松
 * @see
 * @since TODO 描述
 */
public class ContactGroupAdapter extends BaseAdapter<ContactGroup> {

    public ContactGroupAdapter(List<ContactGroup> dataS) {
        super(dataS);
    }

    @Override
    public BaseHolder<ContactGroup> getHolder(View view, int viewType) {
        return  new ContactGroupItemHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.contact_recycle_list;
    }
}
