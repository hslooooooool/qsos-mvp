package com.qs.arm.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qs.arm.utils.ThirdViewUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 基类 {@link RecyclerView.ViewHolder}
 *
 * @author 华清松
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected OnViewClickListener mOnViewClickListener = null;

    public BaseHolder(View itemView) {
        super(itemView);
        // 点击监听
        itemView.setOnClickListener(this);
        if (ThirdViewUtil.USE_AUTO_LAYOUT == 1) {
            // 适配
            AutoUtils.autoSize(itemView);
        }
        // ButterKnife绑定
        ThirdViewUtil.bindTarget(this, itemView);
    }

    /**
     * 设置数据
     *
     * @param data     data
     * @param position position
     */
    public abstract void setData(T data, int position);

    /**
     * 释放资源
     */
    protected void onRelease() {

    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getPosition());
        }
    }

    interface OnViewClickListener {
        /**
         * 点击监听
         *
         * @param view     view
         * @param position position
         */
        void onViewClick(View view, int position);
    }

    void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }
}
