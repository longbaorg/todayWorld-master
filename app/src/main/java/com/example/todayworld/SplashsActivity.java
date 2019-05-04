package com.example.todayworld;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashs);

        if (getActionBar()!=null){
            getActionBar().hide();
        }

        //启动线程闪屏页到主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashsActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
