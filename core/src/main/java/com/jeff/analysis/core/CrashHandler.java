package com.jeff.analysis.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @describe : TODO: handle exception to sdCard and server
 * @author : Jeff
 * @date : 2017/11/8.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    public static final boolean DEBUG = true;

    public static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/CrashAnalysis/log/";
    public static final String FILENAME = "crash";
    public static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler instance;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context context;

    private CrashHandler() {

    }

    /**
     * get instance of CrashHandler
     *
     * @return CrashHandler instance
     */
    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    /**
     * init CrashHandler,usually in application
     *
     * @param context
     */
    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            //dump exception
            dumpExceptionToSDCard(e);
            uploadExceptionToServer();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        e.printStackTrace();

        //end our application if system has provided default CrashHandler,or kill ourselves
        if (mDefaultCrashHandler!=null){
            mDefaultCrashHandler.uncaughtException(t,e);
        }else{
            Process.killProcess(Process.myPid());
        }
    }

    /**
     * dump exception to sdCard
     *
     * @param throwable
     * @throws IOException
     */
    private void dumpExceptionToSDCard(Throwable throwable) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "dumpExceptionToSDCard: dump failed caused by sdCard unmounted!");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            long current = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
            File file = new File(PATH + FILENAME + time + FILE_NAME_SUFFIX);

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            throwable.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dumpExceptionToSDCard: dump failed caused by Exception: " + e.getMessage());
        }

    }

    /**
     * dump phone info
     *
     * @param writer PrintWriter
     * @throws PackageManager.NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter writer) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        //application version
        writer.print("App Version: ");
        writer.print(info.versionName);
        writer.print("_");
        writer.println(info.versionCode);

        //android version
        writer.print("OS Version: ");
        writer.print(Build.VERSION.RELEASE);
        writer.print("_");
        writer.println(Build.VERSION.SDK_INT);

        //phone company
        writer.print("Vendor: ");
        writer.println(Build.MANUFACTURER);

        //phone model
        writer.print("Model: ");
        writer.println(Build.MODEL);

        //cpu abi
        writer.print("CPU ABI: ");
        writer.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer() {
        // TODO: 2017/11/8 upload exception to server
    }
}
