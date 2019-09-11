package com.jeff.analysis.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

import com.jeff.analysis.core.exception.CannotCreateException;

import java.io.File;


/**
 * @author Jeff
 * @describe
 * @date 2018/11/7.
 */
public final class ContextUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    private ContextUtils() {
        throw new CannotCreateException(getClass());
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        ContextUtils.sContext = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (sContext != null) {
            return sContext;
        }
        throw new NullPointerException("should be initialized in application");
    }

    public static String getString(@StringRes int resId){
        return getContext().getString(resId);
    }

    public static int getColor(int resId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getColor(resId);
        }else {
            return getResources().getColor(resId);
        }
    }

    public static float getDimen(@DimenRes int id){
        return getResources().getDimension(id);
    }

    public static Drawable getDrawable(int resId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getContext().getDrawable(resId);
        }else {
            return getResources().getDrawable(resId);
        }
    }

    public static Resources getResources(){
        return getContext().getResources();
    }

    public static File getCacheDir(){
        return getContext().getCacheDir();
    }

    public static AssetManager getAssets(){
        return getContext().getAssets();
    }

    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static DisplayMetrics getDisplayMetrics(){
        return getResources().getDisplayMetrics();
    }
}
