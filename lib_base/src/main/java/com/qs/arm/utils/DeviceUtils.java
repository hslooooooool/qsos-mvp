
package com.qs.arm.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;

/**
 * ================================================
 * 获取设备常用信息和处理设备常用操作的工具类
 * <p>
 * Created by JessYan on 2016/3/15
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 *
 * @author 华清松
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class DeviceUtils {
    // 手机网络类型
    private static final int NET_TYPE_WIFI = 0x01;
    private static final int NET_TYPE_CMWAP = 0x02;
    private static final int NET_TYPE_CMNET = 0x03;

    private static boolean GTE_HC;
    private static boolean GTE_ICS;
    private static boolean PRE_HC;
    private static Boolean hasBigScreen = null;
    private static Boolean hasCamera = null;
    private static Boolean isTablet = null;
    private static Integer loadFactor = null;
    private static float displayDensity = 0.0F;

    static {
        GTE_ICS = Build.VERSION.SDK_INT >= 14;
        GTE_HC = Build.VERSION.SDK_INT >= 11;
        PRE_HC = Build.VERSION.SDK_INT < 11;
    }

    private DeviceUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dp      dp值
     * @return px值
     */
    public static float dpToPixel(Context context, float dp) {
        return dp * (getDisplayMetrics(context).densityDpi / 160F);
    }

    /**
     * px转dp
     *
     * @param context context
     * @param px      px值
     * @return dp值
     */
    public static float pixelsToDp(Context context, float px) {
        return px / (getDisplayMetrics(context).densityDpi / 160F);
    }

    public static int getDefaultLoadFactor(Context context) {
        if (loadFactor == null) {
            Integer integer = 0xf & context.getResources().getConfiguration().screenLayout;
            loadFactor = integer;
            loadFactor = Math.max(integer, 1);
        }
        return loadFactor;
    }

    private static float getDensity(Context context) {
        if (displayDensity == 0.0) {
            displayDensity = getDisplayMetrics(context).density;
        }
        return displayDensity;
    }

    private static DisplayMetrics getDisplayMetrics(@NonNull Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        }
        return displaymetrics;
    }

    /**
     * 屏幕高度
     *
     * @param context context
     * @return height
     */
    public static float getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 屏幕宽度
     *
     * @param context context
     * @return width
     */
    public static float getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取activity尺寸
     *
     * @param activity activity
     * @return size
     */
    public static int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        try {
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
            screenWidth = realSize.x;
            screenHeight = realSize.y;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return height
     */
    public static int getStatusBarHeight(Context context) {
        // 默认值
        int height = 38;
        try {
            @SuppressLint("PrivateApi") Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            height = context.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return height;
    }

    public static boolean hasBigScreen(Context context) {
        boolean flag = true;
        if (hasBigScreen == null) {
            boolean flag1;
            int f = 0xf;
            flag1 = (f & context.getResources().getConfiguration().screenLayout) >= 3;
            Boolean boolean1 = flag1;
            hasBigScreen = boolean1;
            if (!boolean1) {
                float density = 1.5F;
                if (getDensity(context) <= density) {
                    flag = false;
                }
                hasBigScreen = flag;
            }
        }
        return hasBigScreen;
    }

    /**
     * 设备是否有相机
     *
     * @param context context
     * @return boolean
     */
    public static boolean hasCamera(Context context) {
        if (hasCamera == null) {
            PackageManager pckMgr = context
                    .getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            flag2 = flag || flag1;
            hasCamera = flag2;
        }
        return hasCamera;
    }

    /**
     * 设备是否有实体菜单
     *
     * @param context context
     * @return boolean
     */
    public static boolean hasHardwareMenuKey(Context context) {
        return PRE_HC || GTE_ICS && ViewConfiguration.get(context).hasPermanentMenuKey();
    }

    /**
     * 当前是否有网
     *
     * @param context context
     * @return boolean
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager != null && manager.getActiveNetworkInfo() != null;
    }

    /**
     * 当前的包是否存在
     *
     * @param context context
     * @param pckName 包名
     * @return boolean
     */
    private static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager().getPackageInfo(pckName, 0);
            if (pckInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TDvice", e.getMessage());
        }
        return false;
    }

    /**
     * 隐藏视图动画
     *
     * @param view 视图
     */
    public static void hideAnimatedView(View view) {
        if (PRE_HC && view != null) {
            view.setPadding(view.getWidth(), 0, 0, 0);
        }
    }

    /**
     * 显示视图动画
     *
     * @param view 视图
     */
    public static void showAnimatedView(View view) {
        if (PRE_HC && view != null) {
            view.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context context
     * @param view    当前视图
     */
    public static void hideSoftKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 是否是横屏
     *
     * @param context context
     * @return boolean
     */
    public static boolean isLandscape(Context context) {
        boolean flag;
        if (context.getResources().getConfiguration().orientation == 2) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否是竖屏
     *
     * @param context context
     * @return boolean
     */
    public static boolean isPortrait(Context context) {
        boolean flag = true;
        if (context.getResources().getConfiguration().orientation != 1) {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否为写字板
     *
     * @param context context
     * @return boolean
     */
    public static boolean isTablet(Context context) {
        if (isTablet == null) {
            boolean flag;
            int f = 0xf;
            flag = (f & context.getResources().getConfiguration().screenLayout) >= 3;
            isTablet = flag;
        }
        return isTablet;
    }

    /**
     * 显示软键盘
     *
     * @param dialog dialog
     */
    public static void showSoftKeyboard(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(4);
        }
    }

    /**
     * 在视图上显示软键盘
     *
     * @param context context
     * @param view    view
     */
    public static void showSoftKeyboard(@NonNull Context context, View view) {
        InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static void googleSoftKeyboard(@NonNull Context context) {
        InputMethodManager inputMethodManager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * sd卡可用
     *
     * @return boolean
     */
    public static boolean isSdcardReady() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取语言
     *
     * @param context context
     * @return countryLanguage
     */
    public static String getCurCountryLanguage(@NonNull Context context) {
        return context.getResources().getConfiguration().locale.getLanguage()
                + "-"
                + context.getResources().getConfiguration().locale.getCountry();
    }

    public static boolean isZhCN(Context context) {
        String lang = context.getResources().getConfiguration().locale.getCountry();
        String china = "CN";
        return china.equalsIgnoreCase(lang);
    }

    public static String percent(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        str = nf.format(p3);
        return str;
    }

    public static String percent2(double p1, double p2) {
        String str;
        double p3 = p1 / p2;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
        str = nf.format(p3);
        return str;
    }


    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static PackageInfo getPackageInfo(Context context, String pckName) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(pckName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @param context context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 获取指定包名应用的版本号
     *
     * @param context     context
     * @param packageName
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 获取版本名
     *
     * @param context context
     * @return
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            name = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 安装应用
     *
     * @param context context
     * @param file
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static Intent getInstallApkIntent(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 拨打电话
     *
     * @param context context
     * @param number
     */
    public static void openDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 发送短信
     *
     * @param context context
     * @param smsBody
     * @param tel
     */
    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     * 打开度盘
     *
     * @param context context
     */
    public static void openDail(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开短信
     *
     * @param context context
     */
    public static void openSendMsg(Context context) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用照相机
     *
     * @param context context
     */
    @SuppressLint("WrongConstant")
    public static void openCamera(Context context) {
        int flagCode = 821034;
        Intent intent = new Intent();
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        intent.setFlags(flagCode);
        context.startActivity(intent);
    }

    /**
     * 获取手机IMEI
     *
     * @param context context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: 声明权限
            return null;
        }
        return tel.getSubscriberId();
    }

    public static String getPhoneType() {
        return Build.MODEL;
    }

    public static void openApp(Context context, String packageName) {
        Intent mainIntent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        if (mainIntent == null) {
            mainIntent = new Intent(packageName);
        } else {
            // TODO: 2017/11/10
        }
        context.startActivity(mainIntent);
    }

    /**
     * Activity跳转
     *
     * @param context      context
     * @param packageName  当前包名
     * @param activityName 跳转到的包名
     * @return
     */
    public static boolean openAppActivity(Context context, String packageName, String activityName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, activityName);
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查wifi是否开启
     *
     * @param context context
     * @return
     */
    public static boolean checkWifiIsOpen(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        // 新版方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            // 旧版本方法
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        Log.d("Network", "NET_WORK_NAME: " + anInfo.getTypeName());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 卸载软件
     *
     * @param context     context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 复制到粘贴板
     *
     * @param context context
     * @param content 内容
     */
    public static void copyTextToBoard(@NonNull Context context, String content) {
        if (!TextUtils.isEmpty(content)) {
            // 以下方法SDK11以上使用
            ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clip != null) {
                clip.setPrimaryClip(ClipData.newPlainText(null, content));
            }
        }
    }

    /**
     * 发送邮件
     *
     * @param context context
     * @param subject 主题
     * @param content 内容
     * @param emails  邮件地址
     */
    public static void sendEmail(Context context, String subject,
                                 String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            // 真机类型
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null && !extraInfo.isEmpty()) {
                if ("cmnet".equalsIgnoreCase(extraInfo)) {
                    netType = NET_TYPE_CMNET;
                } else {
                    netType = NET_TYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NET_TYPE_WIFI;
        }
        return netType;
    }

    public static boolean netIsConnected(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //手机网络连接状态
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //WIFI连接状态
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            //当前无可用的网络
            return false;
        }
        return true;
    }

    /**
     * 判断是否存在sd卡
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


}


