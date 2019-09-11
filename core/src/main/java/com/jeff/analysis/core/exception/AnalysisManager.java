package com.jeff.analysis.core.exception;

import android.content.Context;

import com.jeff.analysis.core.CrashHandler;
import com.jeff.analysis.core.util.ContextUtils;

/**
 * @author Jeff
 * @describe
 * @date 2019/9/11.
 */
public class AnalysisManager {
    private static AnalysisManager sInstance;

    private AnalysisManager(){

    }

    public static void init(Context context){
        ContextUtils.init(context);
        CrashHandler.getInstance().init(context);
        getInstance();
    }

    public static AnalysisManager getInstance() {
        if (sInstance == null) {
            synchronized (AnalysisManager.class) {
                if (sInstance == null) {
                    sInstance = new AnalysisManager();
                }
            }
        }
        return sInstance;
    }


}
