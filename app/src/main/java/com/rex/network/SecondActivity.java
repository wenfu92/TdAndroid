package com.rex.network;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rex.tdbus.TdBus;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.tv_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TdBus.getInstance().post("1","1","4");
            }
        });
    }
}
