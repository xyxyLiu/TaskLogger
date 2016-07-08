package com.reginald.tasklogger;

import android.app.Application;
import android.content.Context;

public class TaskLoggerApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        TaskLogger.init(this);
    }
}
