package com.qs.arm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.qs.arm.base.delegate.ActivityDelegate;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * UI工具类
 *
 * @author 华清松
 */
public class ThirdViewUtil {

    /**
     * 0 说明 AndroidManifest 里面没有使用 AutoLayout 的Meta,即不使用 AutoLayout
     * 1 为有 Meta ,即需要使用
     */
    public static int USE_AUTO_LAYOUT = -1;

    private ThirdViewUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static Unbinder bindTarget(Object target, Object source) {
        if (source instanceof Activity) {
            return ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof View) {
            return ButterKnife.bind(target, (View) source);
        } else if (source instanceof Dialog) {
            return ButterKnife.bind(target, (Dialog) source);
        } else {
            return Unbinder.EMPTY;
        }
    }

    @Nullable
    public static View convertAutoView(String name, Context context, AttributeSet attrs) {
        // 本框架并不强制你使用 AutoLayout
        // 如果你不想使用 AutoLayout ,就不要在 AndroidManifest 中声明, AutoLayout 的 Meta属性(design_width,design_height)
        if (USE_AUTO_LAYOUT == -1) {
            USE_AUTO_LAYOUT = 1;
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo == null || applicationInfo.metaData == null
                        || !applicationInfo.metaData.containsKey("design_width")
                        || !applicationInfo.metaData.containsKey("design_height")) {
                    USE_AUTO_LAYOUT = 0;
                }
            } catch (PackageManager.NameNotFoundException e) {
                USE_AUTO_LAYOUT = 0;
            }
        }

        if (USE_AUTO_LAYOUT == 0) {
            return null;
        }

        switch (name) {
            case ActivityDelegate.LAYOUT_FRAME_LAYOUT:
                return new AutoFrameLayout(context, attrs);
            case ActivityDelegate.LAYOUT_LINEAR_LAYOUT:
                return new AutoLinearLayout(context, attrs);
            case ActivityDelegate.LAYOUT_RELATIVE_LAYOUT:
                return new AutoRelativeLayout(context, attrs);
            default:
                return null;
        }
    }
}
