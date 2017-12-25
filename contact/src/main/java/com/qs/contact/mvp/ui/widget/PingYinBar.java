package com.qs.contact.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.qs.arm.R;

/**
 * @author 华清松
 * @since 类说明：拼音导航栏
 */
public class PingYinBar extends View {

    private Paint textPaint = new Paint();
    private String[] pinyin = new String[]{"❤", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    /**
     * 鼠标点击、滑动时选择的字母下标
     */
    private int choose = -1;
    /**
     * 选择的字母显示
     */
    private TextView chosen;

    public PingYinBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PingYinBar(Context context) {
        super(context);
    }

    public PingYinBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawText(canvas);
    }

    /**
     * 画字母
     */
    private void drawText(Canvas canvas) {
        // 获取View的宽高
        int width = getWidth();
        int height = getHeight();
        // 获取每个字母的高度
        int singleHeight = height / pinyin.length;
        // 画字母
        for (int i = 0; i < pinyin.length; i++) {
            // 画笔默认颜色
            initPaint();
            // 高亮字母颜色
            if (choose == i) {
                textPaint.setColor(Color.RED);
            }
            // 计算每个字母的坐标
            float x = (width - textPaint.measureText(pinyin[i])) / 2;
            float y = (i + 1) * singleHeight;
            canvas.drawText(pinyin[i], x, y, textPaint);
            // 重置颜色
            textPaint.reset();
        }
    }

    private void initPaint() {
        textPaint.setTextSize(24);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 计算选中字母
        int index = (int) (event.getY() / getHeight() * pinyin.length);
        // 防止脚标越界
        if (index >= pinyin.length) {
            index = pinyin.length - 1;
        } else if (index < 0) {
            index = 0;
        }
        if (chosen == null) {
            chosen = new TextView(getContext());
            chosen.setWidth(50);
            chosen.setHeight(50);
            chosen.setTextSize(30);
            chosen.setTextColor(getResources().getColor(R.color.white));
            chosen.setBackgroundColor(getResources().getColor(R.color.black));
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setBackgroundColor(Color.GRAY);
                // 选中字母高亮
                choose = index;
                // 出现中间文字
                chosen.setVisibility(VISIBLE);
                chosen.setText(pinyin[choose]);
                // 调用 List 连动接口
                if (listener != null) {
                    listener.touchLetterListener(pinyin[choose]);
                }
                // 重绘
                invalidate();
                break;
            default:
                setBackgroundColor(Color.TRANSPARENT);
                // 取消选中字母高亮
                choose = -1;
                // 隐藏中间文字
                chosen.setVisibility(GONE);
                // 重绘
                invalidate();
                break;
        }
        return true;
    }

    public OnTouchLetterListener listener;

    public interface OnTouchLetterListener {
        void touchLetterListener(String letter);
    }

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        this.listener = listener;
    }

    /**
     * 传进来一个 TextView 用于显示被选字母
     *
     * @param chosen 被选择字母的显示控件
     */
    public void setTextView(TextView chosen) {
        this.chosen = chosen;
    }
}
