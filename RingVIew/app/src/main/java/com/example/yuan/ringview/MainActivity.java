package com.example.yuan.ringview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    RingView ringView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ringView= (RingView) findViewById(R.id.aa);
        ringView.start();
    }

    public void cancle(View v){
        ringView.finish();
    }
}
