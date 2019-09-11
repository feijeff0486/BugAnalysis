package com.jeff.analysis.core.util;

import com.jeff.analysis.core.exception.CannotCreateException;

/**
 * @author Jeff
 * @describe
 * @date 2019/3/26.
 */
public final class ConvertUtils {

    private ConvertUtils() {
        throw new CannotCreateException(getClass());
    }

    public static float getSystemDensity(){
        return ContextUtils.getDisplayMetrics().density;
    }

    public static float getSystemScaledDensity(){
        return ContextUtils.getDisplayMetrics().scaledDensity;
    }

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(final float dpValue) {
        final float scale = getSystemDensity();
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = getSystemDensity();
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Value of sp to value of px.
     *
     * @param spValue The value of sp.
     * @return value of px
     */
    public static int sp2px(final float spValue) {
        final float fontScale = getSystemScaledDensity();
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Value of px to value of sp.
     *
     * @param pxValue The value of px.
     * @return value of sp
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = getSystemScaledDensity();
        return (int) (pxValue / fontScale + 0.5f);
    }
}
