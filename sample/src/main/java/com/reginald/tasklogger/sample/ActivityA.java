package com.reginald.tasklogger.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class ActivityA extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ActivityA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        Button testBtn1 = (Button) findViewById(R.id.btn1);
        testBtn1.setOnClickListener(this);
        Button testBtn2 = (Button) findViewById(R.id.btn2);
        testBtn2.setOnClickListener(this);

        testBtn1.setText("to Activity B");
        testBtn2.setText("to itself");

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            // A -> B
            Intent intent = new Intent(this, ActivityB.class);
            intent.setFlags(intentFlagsAB);
            startActivity(intent);
        } else if (view.getId() == R.id.btn2) {
            // A -> A
            Intent intent = new Intent(this, ActivityA.class);
            startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        ComponentName componentName = intent.getComponent();
        intent.getFlags();
        if (componentName != null) {
            Log.d(TAG, "startActivity " + this.getClass().getSimpleName() + " -> " + componentName.getClassName());
        }
        super.startActivity(intent);
    }


}
