package com.jeff.analysis.core.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.jeff.analysis.core.exception.CannotCreateException;


/**
 * Toast工具类
 * @describe
 *
 * @author Jeff
 * @date 2018/12/06.
 */
public final class ToastUtils {
    private static Toast mToast;

    private ToastUtils(){
        throw new CannotCreateException(getClass());
    }

    public static void showToast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(ContextUtils.getContext(), msg, duration);
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void shortToast(@NonNull String msg){
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void shortToast(@StringRes int resId){
        String msg=ContextUtils.getString(resId);
        shortToast(msg);
    }

    public static void longToast(@NonNull String msg){
        showToast(msg, Toast.LENGTH_LONG);
    }

    public static void longToast(@StringRes int resId){
        String msg=ContextUtils.getString(resId);
        longToast(msg);
    }

}
