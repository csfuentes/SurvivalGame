package com.example.yo_pc.compasssurvival;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        lanzarThread();
    }

    private void lanzarThread(){
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    Intent intent = new Intent(splashActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        timer.start();
    }
}
