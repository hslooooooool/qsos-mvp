package com.qs.arm.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 基类 {@link RecyclerView.Adapter} ,如果需要实现非常复杂的 {@link RecyclerView} ,请尽量使用其他优秀的三方库
 *
 * @author 华清松
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    private List<T> dataS;
    private OnRecyclerViewItemClickListener<T> mOnItemClickListener = null;
    private BaseHolder<T> mHolder;

    public BaseAdapter(List<T> dataS) {
        super();
        this.dataS = dataS;
    }

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent   parent
     * @param viewType viewType
     * @return BaseHolder
     */
    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        mHolder = getHolder(view, viewType);
        // 设置Item点击事件
        mHolder.setOnItemClickListener((view1, position) -> {
            if (mOnItemClickListener != null && dataS.size() > 0) {
                mOnItemClickListener.onItemClick(view1, viewType, dataS.get(position), position);
            }
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder   holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        holder.setData(dataS.get(position), position);
    }

    /**
     * 返回数据个数
     *
     * @return int
     */
    @Override
    public int getItemCount() {
        return dataS.size();
    }

    public List<T> getInfo() {
        return dataS;
    }

    /**
     * 获得某个 {@code position} 上的 item 的数据
     *
     * @param position position
     * @return T
     */
    public T getItem(int position) {
        return dataS == null ? null : dataS.get(position);
    }

    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param view     view
     * @param viewType viewType
     * @return BaseHolder
     */
    public abstract BaseHolder<T> getHolder(View view, int viewType);

    /**
     * 提供用于 {@code item} 布局的 {@code layoutId}
     *
     * @param viewType viewType
     * @return int
     */
    public abstract int getLayoutId(int viewType);

    /**
     * 遍历所有{@link BaseHolder},释放他们需要释放的资源
     *
     * @param recyclerView recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder != null && viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }
    }

    /**
     * 设置点击监听
     *
     * @param listener listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener<T> {
        /**
         * 点击监听
         *
         * @param view     view
         * @param viewType viewType
         * @param data     data
         * @param position position
         */
        void onItemClick(View view, int viewType, T data, int position);
    }
}
