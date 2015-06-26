package com.reginald.tasklogger.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class ActivityB extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ActivityB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        Button testBtn1 = (Button) findViewById(R.id.btn1);
        testBtn1.setOnClickListener(this);
        Button testBtn2 = (Button) findViewById(R.id.btn2);
        testBtn2.setOnClickListener(this);
        testBtn1.setText("to Activity A");
        testBtn2.setText("to itself");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            // B -> A
            Intent intent = new Intent(this, ActivityA.class);
            intent.setFlags(intentFlagsBA);
            startActivity(intent);
        } else if (view.getId() == R.id.btn2) {
            // B -> B
            Intent intent = new Intent(this, ActivityB.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


}
