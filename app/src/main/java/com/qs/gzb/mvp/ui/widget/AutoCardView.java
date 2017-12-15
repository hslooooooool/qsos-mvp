package com.qs.gzb.mvp.ui.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * 实现 AndroidAutoLayout 规范的 {@link CardView}
 * 可使用 MVP_generator_solution 中的 AutoView 模版生成各种符合 AndroidAutoLayout 规范的 {@link View}
 *
 * @author 华清松
 */
public class AutoCardView extends CardView {
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoCardView(Context context) {
        super(context);
    }

    public AutoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public AutoFrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoFrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}