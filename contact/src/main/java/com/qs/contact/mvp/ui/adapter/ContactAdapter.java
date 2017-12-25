package com.qs.contact.mvp.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.qs.arm.base.BaseAdapter;
import com.qs.arm.base.BaseHolder;
import com.qs.contact.R;
import com.qs.contact.mvp.model.entity.ContactBean;
import com.qs.contact.mvp.ui.adapter.type.ContactType;
import com.qs.contact.mvp.ui.holder.ContactItemHolder;
import com.qs.contact.mvp.ui.holder.GroupItemHolder;

import java.util.List;

/**
 * @author 华清松
 * @see BaseAdapter
 * @since 联系人容器
 */
public class ContactAdapter extends BaseAdapter<ContactBean> {

    private int CONTACT = R.layout.contact_item_contact;
    private int GROUP = R.layout.contact_item_group;

    public ContactAdapter(List<ContactBean> data) {
        super(data);
    }

    @Override
    public BaseHolder<ContactBean> getHolder(View view, int viewType) {
        if (viewType == CONTACT) {
            return new ContactItemHolder(view);
        } else if (viewType == GROUP) {
            return new GroupItemHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (ContactType.Contact.equals(getData().get(position).getType())) {
            return CONTACT;
        } else if (ContactType.Group.equals(getData().get(position).getType())) {
            return GROUP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getLayoutId(int viewType) {
        return viewType;
    }

    /**
     * 获取字母代表的位置
     *
     * @param letter 字母
     * @return 列表对应的字母位置
     */
    public int getSelectPosition(@NonNull String letter) {
        for (int i = 0; i < getItemCount(); i++) {
            if (letter.equals(getItem(i).getPinyin())) {
                return i;
            }
        }
        return -1;
    }
}
