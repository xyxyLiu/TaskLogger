package com.reginald.tasklogger.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class BaseActivity extends Activity {

    public static final String TAG = "BaseActivity";
    public static int intentFlagsAB;
    public static int intentFlagsBA;
    AlertDialog menuDialog;
    View menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuDialog = new AlertDialog.Builder(this).create();
        menuView = View.inflate(this, R.layout.menu, null);
        menuDialog.setView(menuView);

        Log.d(TAG, "intentFlagsAB = " + intentFlagsAB + ", intentFlagsBA = " + intentFlagsBA);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("menu");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menuDialog != null) {
            final CheckBox a1 = (CheckBox) menuView.findViewById(R.id.new_task_checkbox_a);
            final CheckBox a2 = (CheckBox) menuView.findViewById(R.id.multi_task_checkbox_a);
            final CheckBox a3 = (CheckBox) menuView.findViewById(R.id.clear_top_checkbox_a);
            final CheckBox a4 = (CheckBox) menuView.findViewById(R.id.clear_task_checkbox_a);
            final CheckBox a5 = (CheckBox) menuView.findViewById(R.id.single_top_checkbox_a);

            final CheckBox b1 = (CheckBox) menuView.findViewById(R.id.new_task_checkbox_b);
            final CheckBox b2 = (CheckBox) menuView.findViewById(R.id.multi_task_checkbox_b);
            final CheckBox b3 = (CheckBox) menuView.findViewById(R.id.clear_top_checkbox_b);
            final CheckBox b4 = (CheckBox) menuView.findViewById(R.id.clear_task_checkbox_b);
            final CheckBox b5 = (CheckBox) menuView.findViewById(R.id.single_top_checkbox_b);

            refreshCheckBoxes(intentFlagsAB, a1, a2, a3, a4, a5);
            refreshCheckBoxes(intentFlagsBA, b1, b2, b3, b4, b5);

            menuDialog.show();

            menuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    intentFlagsAB = refreshflags(a1.isChecked(), a2.isChecked(), a3.isChecked(), a4.isChecked(), a5.isChecked());
                    intentFlagsBA = refreshflags(b1.isChecked(), b2.isChecked(), b3.isChecked(), b4.isChecked(), b5.isChecked());
                }
            });
        }
        return false;
    }

    public int refreshflags(boolean isNewTask, boolean isMultiTask, boolean isClearTop, boolean isClearTask, boolean isSingleTop) {
        int flags = 0;
        if (isNewTask)
            flags |= Intent.FLAG_ACTIVITY_NEW_TASK;
        if (isMultiTask)
            flags |= Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (isClearTop)
            flags |= Intent.FLAG_ACTIVITY_CLEAR_TOP;
        if (isClearTask)
            flags |= Intent.FLAG_ACTIVITY_CLEAR_TASK;
        if (isSingleTop)
            flags |= Intent.FLAG_ACTIVITY_SINGLE_TOP;

        return flags;
    }

    public void refreshCheckBoxes(int flags, CheckBox cb1, CheckBox cb2, CheckBox cb3, CheckBox cb4, CheckBox cb5) {
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        cb4.setChecked(false);
        cb5.setChecked(false);

        if ((flags & Intent.FLAG_ACTIVITY_NEW_TASK) != 0)
            cb1.setChecked(true);
        if ((flags & Intent.FLAG_ACTIVITY_MULTIPLE_TASK) != 0)
            cb2.setChecked(true);
        if ((flags & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0)
            cb3.setChecked(true);
        if ((flags & Intent.FLAG_ACTIVITY_CLEAR_TASK) != 0)
            cb4.setChecked(true);
        if ((flags & Intent.FLAG_ACTIVITY_SINGLE_TOP) != 0)
            cb5.setChecked(true);
    }

}
