package com.reginald.tasklogger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.reginald.tasklogger.reflect.Reflect;

public class TaskLogger {

    public static final String TASK_TAG = "$tasklogger$";
    public static final String DEBUG_TAG = "$debug$TaskLogger";

    private Context mAppContext;
    private static TaskLogger sInstance;

    /**
     * must be called in {@link android.app.Application#attachBaseContext(Context)}
     */
    public static void init(Application application) {
        TaskLogger.getInstance(application).attachBaseContext();
    }


    private synchronized static TaskLogger getInstance(Context appContext) {
        if (sInstance == null) {
            sInstance = new TaskLogger(appContext);
        }
        return sInstance;
    }

    private TaskLogger(Context appContext) {
        mAppContext = appContext;
    }

    private void replaceIntrumentation(Context contextImpl) {

        Reflect contextImplRef = Reflect.on(contextImpl);
        Reflect activityThreadRef = contextImplRef.field("mMainThread");
        Reflect instrumentationRef = activityThreadRef.field("mInstrumentation");
        TaskLoggerInstrumentation newInstrumentation = new TaskLoggerInstrumentation((Instrumentation) instrumentationRef.get());
        activityThreadRef.set("mInstrumentation", newInstrumentation);
    }

    private void attachBaseContext() {
        Context contextImpl = getContextImpl(mAppContext);
        replaceIntrumentation(contextImpl);
    }

    private void connectService(TaskLoggerService.TaskActivity taskActivity, int flag) {
        Intent intent = new Intent(mAppContext, TaskLoggerService.class);
        intent.putExtra("TaskActivity", taskActivity);
        intent.putExtra("flag", flag);
        mAppContext.startService(intent);
    }

    public void launchActivity(Context context, Intent intent) {
        ResolveInfo info = context.getPackageManager().resolveActivity(intent, 0);
        connectService(new TaskLoggerService.TaskActivity(context.toString(), info.activityInfo, intent), TaskLoggerService.MSG_LAUNCH_ACTIVITY);
    }

    private class TaskLoggerInstrumentation extends Instrumentation {

        Instrumentation base;
        Reflect instrumentRef;

        public TaskLoggerInstrumentation(Instrumentation base) {
            this.base = base;
            instrumentRef = Reflect.on(base);
        }

        /**
         * @Override
         */
        public ActivityResult execStartActivity(
                Context who, IBinder contextThread, IBinder token, Activity target,
                Intent intent, int requestCode, Bundle options) {

            Log.i(DEBUG_TAG, "execStartActivity()");
            launchActivity(who, intent);
            return instrumentRef.call("execStartActivity", who, contextThread, token, target, intent, requestCode, options).get();
        }

        /**
         * @Override
         */
        public ActivityResult execStartActivity(
                Context who, IBinder contextThread, IBinder token, Activity target,
                Intent intent, int requestCode) {
            Log.i(DEBUG_TAG, "execStartActivity()");
            launchActivity(who, intent);
            return instrumentRef.call("execStartActivity", who, contextThread, token, target, intent, requestCode).get();

        }


        @Override
        public void callActivityOnCreate(Activity activity, Bundle bundle) {

            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_CREATE);
            super.callActivityOnCreate(activity, bundle);
        }

        @Override
        public void callActivityOnNewIntent(Activity activity, Intent intent) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_NEW_INTENT);
            super.callActivityOnNewIntent(activity, intent);
        }

        @Override
        public void callActivityOnRestart(Activity activity) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_RESTART);
            super.callActivityOnRestart(activity);
        }

        @Override
        public void callActivityOnStart(Activity activity) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_START);
            super.callActivityOnStart(activity);
        }

        @Override
        public void callActivityOnResume(Activity activity) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_RESUME);
            super.callActivityOnResume(activity);
        }

        @Override
        public void callActivityOnPause(Activity activity) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_PAUSE);
            super.callActivityOnPause(activity);
        }

        @Override
        public void callActivityOnStop(Activity activity) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_STOP);
            super.callActivityOnStop(activity);
        }

        @Override
        public void callActivityOnDestroy(Activity activity) {
            connectService(new TaskLoggerService.TaskActivity(activity), TaskLoggerService.MSG_ON_ACTIVITY_DESTROY);
            super.callActivityOnDestroy(activity);
        }

    }


    private Context getContextImpl(Context context) {
        Context nextContext;
        while ((context instanceof ContextWrapper) &&
                (nextContext = ((ContextWrapper) context).getBaseContext()) != null) {
            context = nextContext;
        }
        return context;
    }

    private String getSystemInfo() {
        String currentProcName = "unknown";
        int pid = android.os.Process.myPid();
        long tid = Thread.currentThread().getId();
        ActivityManager manager = (ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                currentProcName = processInfo.processName;
                break;
            }
        }
        return " in process " + currentProcName + "(" + pid + ") thread " + tid;
    }
}
