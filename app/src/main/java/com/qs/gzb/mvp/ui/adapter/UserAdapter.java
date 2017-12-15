package com.qs.gzb.mvp.ui.adapter;

import android.view.View;

import com.qs.arm.base.BaseAdapter;
import com.qs.arm.base.BaseHolder;
import com.qs.gzb.R;
import com.qs.gzb.mvp.model.entity.User;
import com.qs.gzb.mvp.ui.holder.UserItemHolder;

import java.util.List;

/**
 * 展示 {@link BaseAdapter} 的用法
 *
 * @author 华清松
 */
public class UserAdapter extends BaseAdapter<User> {

    public UserAdapter(List<User> users) {
        super(users);
    }

    @Override
    public BaseHolder<User> getHolder(View view, int viewType) {
        return new UserItemHolder(view);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }
}
